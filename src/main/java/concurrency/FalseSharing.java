package concurrency;

/**
 * El False Sharing en Java ocurre cuando dos subprocesos que se ejecutan en dos CPU diferentes escriben en dos
 * variables diferentes que están almacenadas dentro de la misma línea de caché de la CPU. Cuando el primer subproceso
 * modifica una de las variables, toda la línea de caché de la CPU se invalida en los cachés de la CPU de la otra CPU
 * donde se está ejecutando el otro subproceso. Esto significa que las otras CPU necesitan recargar el contenido de la
 * línea de caché invalidada, incluso si realmente no necesitan la variable que se modificó dentro de esa línea de
 * caché.
 * 
 * -Líneas de caché
 * Cuando las cachés de la CPU están leyendo datos de cachés de nivel inferior o de la RAM principal (por ejemplo, L1 de
 * L2, L2 de L3 y L3 de la RAM principal), no solo leen un solo byte a la vez. Eso sería ineficaz. En su lugar, leen una
 * línea de caché. Una línea de caché generalmente consta de 64 bytes. Por lo tanto, los cachés leen 64 bytes a la vez
 * desde cachés de nivel inferior o RAM principal.
 * 
 * Debido a que una línea de caché consta de varios bytes, una sola línea de caché a menudo almacenará más de una
 * variable. Si la misma CPU necesita acceder a más variables almacenadas dentro de la misma línea de caché, esto es una
 * ventaja. Si varias CPU necesitan acceder a las variables almacenadas dentro de la misma línea de caché, puede ocurrir
 * un intercambio falso.
 * 
 * -Invalidación de línea de caché
 * Cuando una CPU escribe en la dirección de memoria en una línea de caché, normalmente porque la CPU está escribiendo
 * en una variable, la línea de caché se ensucia. Luego, la línea de caché debe sincronizarse con otras CPU que también
 * tienen esa línea de caché en sus cachés de CPU. La misma línea de caché almacenada en las otras cachés de la CPU deja
 * de ser válida; en otras palabras, deben actualizarse.
 * 
 * La actualización de la memoria caché después de la invalidación de la memoria caché puede ocurrir mediante mecanismos
 * de coherencia de la memoria caché o recargando la línea de memoria caché desde la RAM principal.
 * 
 * La CPU no puede acceder a esa línea de caché hasta que se haya actualizado.
 * 
 * -El uso compartido falso da como resultado una penalización por desempeño
 * Cuando una línea de caché se invalida porque los datos dentro de esa línea de caché han sido cambiados por otra CPU,
 * entonces la línea de caché invalidada debe actualizarse, ya sea desde la caché L3 o mediante mecanismos de coherencia
 * de caché. Por lo tanto, si la CPU necesita leer la línea de caché invalidada, debe esperar hasta que se actualice la
 * línea de caché. Esto da como resultado una degradación del rendimiento. El tiempo de la CPU se pierde esperando la
 * actualización de la línea de caché, lo que significa que la CPU puede ejecutar menos instrucciones durante ese
 * tiempo.
 * 
 * El uso compartido falso significa que dos (o más) CPU escriben en variables almacenadas dentro de la misma línea de
 * caché, pero cada CPU realmente no depende del valor escrito por la otra CPU. Sin embargo, ambos siguen ensuciando
 * continuamente la línea de caché, lo que hace que se invalide para la otra CPU, lo que obliga a la otra CPU a
 * actualizarla, antes de que la otra CPU también ensucie la línea de caché, lo que hace que la primera CPU tenga que
 * actualizarla. etc.
 * 
 * La solución al uso compartido falso es cambiar sus estructuras de datos para que las variables independientes
 * utilizadas por las cpu ya no se almacenen dentro de la misma línea de caché.
 * 
 * -Solucionar problemas de uso compartido falso
 * La forma de solucionar un problema de uso compartido falso es diseñar su código para que las diferentes variables
 * utilizadas por diferentes subprocesos no terminen almacenadas dentro de la misma línea de caché de la CPU.
 * Exactamente cómo lo haga depende de su código concreto, pero almacenar las variables en diferentes objetos es una
 * forma de hacerlo, como se mostró en el ejemplo de la sección anterior.
 * 
 * Fuente:
 * http://tutorials.jenkov.com/java-concurrency/false-sharing.html
 * 
 * @author Ru$o
 * 
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

		long iterations = 1_000_000_000;

		Thread thread1 = new Thread(() -> {

			long startTime = System.currentTimeMillis();
			for (long i = 0; i < iterations; i++)
				counter1.count1++;
			long endTime = System.currentTimeMillis();
			System.out.println("total time: " + (endTime - startTime) / 1000 + " seg");

		});

		Thread thread2 = new Thread(() -> {

			long startTime = System.currentTimeMillis();
			for (long i = 0; i < iterations; i++)
				counter2.count2++;
			long endTime = System.currentTimeMillis();
			System.out.println("total time: " + (endTime - startTime) / 1000 + " seg");

		});

		thread1.start();
		thread2.start();
	}

}

class Counter {

	public volatile long count1 = 0;
	public volatile long count2 = 0;

	/**
	 * - Evitar el uso compartido falso con la anotación @Contented
	 * 
	 * Desde Java 8 y 9, Java tiene la anotación @Contended que puede rellenar los campos dentro de las clases con bytes
	 * vacíos (después del campo, cuando se almacenan en la RAM), de modo que los campos dentro de un objeto de esa clase no
	 * se almacenan dentro de la misma caché de la CPU. línea. A continuación se muestra la clase Counter del ejemplo
	 * anterior con la anotación @Contended agregada a uno de los campos. Al agregar esta anotación, el tiempo de ejecución
	 * se redujo aproximadamente al mismo tiempo que cuando los dos subprocesos usaban dos instancias de Contador
	 * diferentes. Aquí está la clase Counter modificada:
	 * 
	 * {@code
	 * 
	 * @jdk.internal.vm.annotation.Contended
	 * public volatile long count1 = 0;
	 * public volatile long count2 = 0;
	 * 
	 * }
	 * 
	 */

}
