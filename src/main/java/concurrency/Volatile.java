package concurrency;

/**
 * La palabra clave volatil de Java se utiliza para marcar una variable de Java como "almacenada en la memoria
 * principal". Mas precisamente, eso significa que cada lectura de una variable volatil se leera desde la memoria
 * principal de la computadora, y no desde el cache de la CPU, y que cada escritura en una variable volatil se
 * escribira en la memoria principal, y no solo en el cache de la CPU.
 * <p>
 * En una aplicacion de subprocesos multiples donde los subprocesos operan en variables no volatiles, cada subproceso
 * puede copiar variables de la memoria principal a una memoria cache de la CPU mientras trabaja en ellas, por motivos
 * de rendimiento. Si su computadora contiene mas de una CPU, cada subproceso puede ejecutarse en una CPU diferente.
 * Eso significa que cada subproceso puede copiar las variables en la memoria cache de la CPU de diferentes CPU.
 * <p>
 * Con las variables no volatiles, no hay garantias sobre cuando la maquina virtual de Java (JVM) lee datos de la memoria
 * principal en las memorias cache de la CPU o escribe datos de las memorias cache de la CPU en la memoria principal.
 * <br><br>
 * <h3>Garantia de visibilidad volatil completa</h3>
 * En realidad, la garantia de visibilidad de Java volatil va mas alla de la propia variable volatil. La garantia de visibilidad es la siguiente:
 * <li>Si el subproceso A escribe en una variable volatil y el subproceso B lee posteriormente la misma variable volatil, todas las variables
 * visibles para el subproceso A antes de escribir la variable volatil tambien seran visibles para el subproceso B despues de haber
 * leido la variable volatil.</li>
 * <li>Si el subproceso A lee una variable volatil, todas las variables visibles para el subproceso A al leer la variable volatil tambien
 * se volveran a leer desde la memoria principal.</li>
 * <h3>Volatil no siempre es suficiente</h3>
 * Tan pronto como un hilo necesite leer primero el valor de una variable volatile y, en funcion de ese valor, generar un nuevo valor para la variable
 * volatile compartida, una variable volatile ya no es suficiente para garantizar una visibilidad correcta. El breve intervalo de tiempo entre la lectura
 * de la variable volatile y la escritura de su nuevo valor crea una condicion de carrera en la que varios subprocesos pueden leer el mismo valor de la
 * variable volatile, generar un nuevo valor para la variable y, al volver a escribir el valor en Memoria principal: sobrescribe los valores de cada uno.
 * <p>
 * Si dos subprocesos leen y escriben en una variable compartida, entonces usar la palabra clave volatile no es suficiente. Debe usar un sincronizado
 * en ese caso para garantizar que la lectura y escritura de la variable sea atomica. Leer o escribir una variable volatil no bloquea la lectura o escritura
 * de subprocesos. Para que esto suceda, debe usar la synchronized palabra clave alrededor de las secciones criticas.
 * <br><br>
 * <h3>Consideraciones de rendimiento de volatiles</h3>
 * La lectura y escritura de variables volatiles hace que la variable se lea o escriba en la memoria principal. Leer y escribir en la memoria
 * principal es mas costoso que acceder a la memoria cache de la CPU. El acceso a variables volatiles también evita el reordenamiento de instrucciones,
 * que es una tecnica normal de mejora del rendimiento. Por lo tanto, solo debe usar variables volatiles cuando realmente necesite hacer cumplir la
 * visibilidad de las variables.
 * <br><br>
 * Fuentes:
 * <a href="https://jenkov.com/tutorials/java-concurrency/volatile.html">jenkov.com</a>
 */

public class Volatile {

	// Asi es como se ve la declaracion volatil de la variable de contador
	private volatile int contador;
	/* Si un subproceso (T1) modifica el contador y otro subproceso (T2) lee el contador (pero nunca lo modifica), declarar la
	 * variable de contador volatil es suficiente para garantizar la visibilidad para T2 de las escrituras en la variable de contador.
	 * Sin embargo, si tanto T1 como T2 estuvieran incrementando la variable de contador, entonces declarar la variable de contador
	 * volatil no habria sido suficiente. */

}
