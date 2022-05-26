package concurrency;

/**
 * El False Sharing en Java ocurre cuando dos subprocesos que se ejecutan en dos CPU diferentes escriben en dos
 * variables diferentes que estan almacenadas dentro de la misma linea de cache de la CPU. Cuando el primer subproceso
 * modifica una de las variables, toda la linea de cache de la CPU se invalida en los caches de la CPU de la otra CPU
 * donde se esta ejecutando el otro subproceso. Esto significa que las otras CPU necesitan recargar el contenido de la
 * linea de cache invalidada, incluso si realmente no necesitan la variable que se modifico dentro de esa linea de
 * cache.
 * <br><br>
 * <h3>Lineas de cache</h3>
 * Cuando las caches de la CPU estan leyendo datos de caches de nivel inferior o de la RAM principal (por ejemplo, L1 de
 * L2, L2 de L3 y L3 de la RAM principal), no solo leen un solo byte a la vez. Eso seria ineficaz. En su lugar, leen una
 * linea de cache. Una linea de cache generalmente consta de 64 bytes. Por lo tanto, los caches leen 64 bytes a la vez
 * desde caches de nivel inferior o RAM principal.
 * <p>
 * Debido a que una linea de cache consta de varios bytes, una sola linea de cache a menudo almacenara mas de una
 * variable. Si la misma CPU necesita acceder a mas variables almacenadas dentro de la misma linea de cache, esto es una
 * ventaja. Si varias CPU necesitan acceder a las variables almacenadas dentro de la misma linea de cache, puede ocurrir
 * un intercambio falso.
 * <br><br>
 * <h3>Invalidacion de linea de cache</h3>
 * Cuando una CPU escribe en la direccion de memoria en una linea de cache, normalmente porque la CPU esta escribiendo
 * en una variable, la linea de cache se ensucia. Luego, la linea de cache debe sincronizarse con otras CPU que tambien
 * tienen esa linea de cache en sus caches de CPU. La misma linea de cache almacenada en las otras caches de la CPU deja
 * de ser valida; en otras palabras, deben actualizarse.
 * <p>
 * La actualizacion de la memoria cache despues de la invalidacion de la memoria cache puede ocurrir mediante mecanismos
 * de coherencia de la memoria cache o recargando la linea de memoria cache desde la RAM principal.
 * <p>
 * La CPU no puede acceder a esa linea de cache hasta que se haya actualizado.
 * <br><br>
 * <h3>El uso compartido falso da como resultado una penalizacion por desempeño</h3>
 * Cuando una linea de cache se invalida porque los datos dentro de esa linea de cache han sido cambiados por otra CPU,
 * entonces la linea de cache invalidada debe actualizarse, ya sea desde la cache L3 o mediante mecanismos de coherencia
 * de cache. Por lo tanto, si la CPU necesita leer la linea de cache invalidada, debe esperar hasta que se actualice la
 * linea de cache. Esto da como resultado una degradacion del rendimiento. El tiempo de la CPU se pierde esperando la
 * actualizacion de la linea de cache, lo que significa que la CPU puede ejecutar menos instrucciones durante ese
 * tiempo.
 * <p>
 * El uso compartido falso significa que dos (o mas) CPU escriben en variables almacenadas dentro de la misma linea de
 * cache, pero cada CPU realmente no depende del valor escrito por la otra CPU. Sin embargo, ambos siguen ensuciando
 * continuamente la linea de cache, lo que hace que se invalide para la otra CPU, lo que obliga a la otra CPU a
 * actualizarla, antes de que la otra CPU tambien ensucie la linea de cache, lo que hace que la primera CPU tenga que
 * actualizarla. etc.
 * <p>
 * La solucion al uso compartido falso es cambiar sus estructuras de datos para que las variables independientes
 * utilizadas por las cpu ya no se almacenen dentro de la misma linea de cache.
 * <br><br>
 * <h3>Solucionar problemas de uso compartido falso</h3>
 * La forma de solucionar un problema de uso compartido falso es diseñar su codigo para que las diferentes variables
 * utilizadas por diferentes subprocesos no terminen almacenadas dentro de la misma linea de cache de la CPU.
 * Exactamente como lo haga depende de su codigo concreto, pero almacenar las variables en diferentes objetos es una
 * forma de hacerlo, como se mostro en el ejemplo de la seccion anterior.
 * <br>
 * Fuentes:
 * <a href="http://tutorials.jenkov.com/java-concurrency/false-sharing.html">jenkov.com</a>
 *
 * @author Ruso
 */

public class FalseSharing {

	public static void main(String[] args) {

		/* Usando la misma instancia del objeto Counter se tarda mas de 1 minuto en ejecutarse la aplicacion. Esto se debe al
		 * uso compartido falso en la misma linea de cache por parte de los campos count1 y count2 que usan la misma instancia
		 * del objeto Counter.
		 *
		 * Usando un objeto distinto para cada instancia se tarda casi 12 segundos. Es muy probable que esta diferencia de
		 * velocidad se deba al uso compartido falso en el primer ejemplo, donde los campos count1 y count2 en la instancia de
		 * Counter compartida se encuentran dentro de la misma linea de cache en tiempo de ejecucion. En el segundo ejemplo (a
		 * continuacion), los dos subprocesos usan cada uno su propia instancia de Contador, que ya no almacena sus campos
		 * dentro de la misma linea de cache. Por lo tanto, no se produce un intercambio falso y el codigo se ejecuta mas
		 * rapido. */

		Counter counter1 = new Counter();
		Counter counter2 = new Counter();
		// Usando una instancia diferente de la clase Counter, no se produce un intercambio falso y el codigo se ejecuta mas rapido
		// Counter counter = new Counter();

		long iterations = 1_000_000_000;

		// Expresion lambda
		Thread thread1 = new Thread(() -> {
			long startTime = System.currentTimeMillis();
			for (long i = 0; i < iterations; i++)
				counter1.count1++;
			long endTime = System.currentTimeMillis();
			System.out.println("Total time: " + (endTime - startTime) / 1000 + " seg");
		});

		Thread thread2 = new Thread(() -> {
			long startTime = System.currentTimeMillis();
			for (long i = 0; i < iterations; i++)
				counter2.count2++;
			long endTime = System.currentTimeMillis();
			System.out.println("Total time: " + (endTime - startTime) / 1000 + " seg");
		});

		thread1.start();
		thread2.start();
	}

}

class Counter {

	public volatile long count1;
	public volatile long count2;

	/* -Evitar el uso compartido falso con la anotación @Contented
	 * Desde Java 8 y 9, Java tiene la anotación @Contended que puede rellenar los campos dentro de las clases con bytes
	 * vacíos (después del campo, cuando se almacenan en la RAM), de modo que los campos dentro de un objeto de esa clase no
	 * se almacenan dentro de la misma caché de la CPU. línea. A continuación se muestra la clase Counter del ejemplo
	 * anterior con la anotación @Contended agregada a uno de los campos. Al agregar esta anotación, el tiempo de ejecución
	 * se redujo aproximadamente al mismo tiempo que cuando los dos subprocesos usaban dos instancias de Contador
	 * diferentes. Aquí está la clase Counter modificada:
	 *
	 * @jdk.internal.vm.annotation.Contended
	 * public volatile long count1 = 0;
	 * public volatile long count2 = 0; */

}
