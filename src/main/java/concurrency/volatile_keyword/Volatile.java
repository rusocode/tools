package concurrency.volatile_keyword;

/**
 * <p>
 * La palabra clave {@code volatil} de Java se utiliza para marcar una variable de Java como "almacenada en la memoria principal".
 * Mas precisamente, eso significa que cada lectura de una variable volatil se leera desde la memoria principal de la computadora,
 * y no desde el cache de la CPU, y que cada escritura en una variable volatil se escribira en la memoria principal, y no solo en
 * el cache de la CPU.
 * <p>
 * En realidad, desde Java 5, la palabra clave volatil garantiza mas que solo que las variables volatiles se escriban y lean desde
 * la memoria principal.
 * <h3>Problemas de visibilidad variable</h3>
 * <p>
 * La palabra clave volatil garantiza la visibilidad de los cambios en las variables entre subprocesos. Esto puede sonar un poco
 * abstracto, asi que permitanme explicarlo mas.
 * <p>
 * En una aplicacion multiproceso donde los subprocesos operan con variables no volatiles, cada subproceso puede copiar variables
 * de la memoria principal a los registros de la CPU mientras trabaja en ellas, por razones de rendimiento. Si su computadora
 * contiene mas de una CPU, cada subproceso puede ejecutarse en una CPU diferente. Eso significa que cada hilo puede copiar las
 * variables en los registros de CPU de diferentes CPU. Esto se ilustra aqui:
 * <p>
 * <img src="problemas de visibilidad variable.PNG">
 * <p>
 * Con las variables no volatiles, no hay garantias sobre cuando la JVM lee datos de la memoria principal en las memorias cache de
 * la CPU o escribe datos de las memorias cache de la CPU en la memoria principal.
 * <p>
 * Imagine una situacion en la que dos o mas subprocesos tienen acceso a un objeto compartido que contiene una variable de counter
 * declarada de esta manera:
 * <pre>{@code
 * public class SharedObject {
 *
 *     public int counter;
 *
 * }
 * }</pre>
 * <p>
 * Imaginese tambien que solo el subproceso 1 incrementa la variable counter, pero tanto el subproceso 1 como el subproceso 2
 * pueden leer la variable counter de vez en cuando.
 * <p>
 * Si la variable counter no se declara volatil, no hay garantia de cuando se escribe el valor de la variable counter
 * desde los registros de la CPU a la memoria principal. Esto significa que el valor de la variable counter en el registro de
 * la CPU puede no ser el mismo que en la memoria principal. Esta situacion se ilustra aqui:
 * <p>
 * <img src="problema de recurso compartido.PNG">
 * <p>
 * El problema con los subprocesos que no ven el ultimo valor de una variable porque otro subproceso aun no lo ha escrito en la
 * memoria principal se denomina problema de "visibilidad". Las actualizaciones de un hilo no son visibles para otros hilos.
 * <h3>La garantia de visibilidad volatil de Java</h3>
 * <p>
 * La palabra clave volatil esta destinada a abordar problemas de visibilidad de variables. Al declarar volatil la variable counter,
 * todas las escrituras en la variable counter se volveran a escribir en la memoria principal inmediatamente. Ademas, todas las
 * lecturas de la variable counter se leeran directamente desde la memoria principal.
 * <p>
 * Asi es como se ve la declaracion volatil de la variable contador:
 * <pre>{@code
 * public class SharedObject {
 *
 *     public volatile int counter;
 *
 * }
 * }</pre>
 * <p>
 * Declarar una variable volatil <i>garantiza asi la visibilidad</i> de otros subprocesos de escritura en esa variable.
 * <p>
 * En el escenario descrito anteriormente, donde un subproceso (T1) modifica el contador y otro subproceso (T2) lee el contador
 * (pero nunca lo modifica), declarar volatil la variable counter es suficiente para garantizar la visibilidad para T2 de las
 * escrituras en la variable counter.
 * <p>
 * Sin embargo, si tanto T1 como T2 estuvieran incrementando la variable counter, entonces declararla volatil no habria sido
 * suficiente.
 * <p>
 * <h3>Garantia de visibilidad totalmente volatil</h3>
 * <p>
 * En realidad, la garantia de visibilidad de volatil va mas alla de la propia variable volatil. La garantia de visibilidad es la
 * siguiente:
 * <ul>
 * <li>Si el subproceso A escribe en una variable volatil y el subproceso B lee posteriormente la misma variable volatil, todas las
 * variables visibles para el subproceso A antes de escribir la variable volatil tambien seran visibles para el subproceso B
 * despues de haber leido la variable volatil.
 * <li>Si el subproceso A lee una variable volatil, todas las variables visibles para el subproceso A al leer la variable volatil
 * tambien se volveran a leer desde la memoria principal.
 * </ul>
 * Permitanme ilustrarlo con un ejemplo de codigo:
 * <pre>{@code
 * public class MyClass {
 *
 *     private int years;
 *     private int months
 *     private volatile int days;
 *
 *     public void update(int years, int months, int days) {
 *         this.years = years;
 *         this.months = months;
 *         this.days = days;
 *     }
 *
 * }
 * }</pre>
 * El metodo {@code udpate()} escribe tres variables, de las cuales solo los {@code days} son volatiles.
 * <p>
 * La garantia de visibilidad volatil total significa que cuando un valor se escribe en days, todas las variables visibles para el
 * hilo tambien se escriben en la memoria principal. Es decir, cuando un valor se escribe en days, los valores de {@code years} y
 * {@code months} tambien se escriben en la memoria principal.
 * <p>
 * Al leer los valores de years, months y days podrias hacerlo asi:
 * <pre>{@code
 * public class MyClass {
 *
 *     private int years;
 *     private int months;
 *     private volatile int days;
 *
 *     public int totalDays() {
 *         int total = this.days;
 *         total += months * 30;
 *         total += years * 365;
 *         return total;
 *     }
 *
 *     public void update(int years, int months, int days) {
 *         this.years = years;
 *         this.months = months;
 *         this.days = days;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe que el metodo {@code totalDays()} comienza leyendo el valor de los days en la variable {@code total}. Al leer el valor
 * de days, los valores de months y years tambien se leen en la memoria principal. Por lo tanto, tiene la garantia de ver los
 * ultimos valores de days, months y years con la secuencia de lectura anterior.
 * <h3>Desafios de reordenamiento de instrucciones</h3>
 * <p>
 * La JVM y la CPU pueden reordenar las instrucciones del programa por motivos de rendimiento, siempre que el  significado
 * semantico de las instrucciones siga siendo el mismo. Por ejemplo, mire las siguientes instrucciones:
 * <pre>{@code
 * int a = 1;
 * int b = 2;
 *
 * a++;
 * b++;
 * }</pre>
 * <p>
 * Estas instrucciones podrian reordenarse en la siguiente secuencia sin perder el significado semantico del programa:
 * <pre>{@code
 * int a = 1;
 * a++;
 *
 * int b = 2;
 * b++;
 * }</pre>
 * <p>
 * Sin embargo, el reordenamiento de instrucciones presenta un desafio cuando una de las variables es volatil. Tomando de ejemplo
 * la clase {@code MyClass} del ejemplo anterior. Una vez que el metodo update() escribe un valor en days, los valores recien
 * escritos en years y months tambien se escriben en la memoria principal. Pero, que pasaria si la JVM reordenara las instrucciones,
 * asi:
 * <pre>{@code
 * public void update(int years, int months, int days) {
 *     this.days = days;
 *     this.months = months;
 *     this.years = years;
 * }
 * }</pre>
 * <p>
 * Los valores de months y years todavia se escriben en la memoria principal cuando se modifica la variable days, pero esta vez
 * sucede antes de que los nuevos valores se hayan escrito en months y years. Por lo tanto, los nuevos valores no se hacen visibles
 * correctamente para otros hilos. El significado semantico de las instrucciones reordenadas ha cambiado.
 * <p>
 * Java tiene una solucion para este problema, como veremos en la siguiente seccion.
 * <h3>La garantia volatil de Java sucede antes</h3>
 * <p>
 * Para abordar el desafio de reordenar las instrucciones, la palabra clave volatil ofrece una garantia de "sucede antes", ademas
 * de la garantia de visibilidad. La garantia sucede antes garantiza que:
 * <ul>
 * <li>Las lecturas y escrituras en otras variables no se pueden reordenar para que ocurran despues de una escritura en una variable
 * volatil, si las lecturas/escrituras ocurrieron originalmente antes de la escritura en la variable volatil. Se garantiza que las
 * lecturas/escrituras antes de escribir en una variable volatil "sucederan antes" de escribir en la variable volatil. Tenga en
 * cuenta que todavia es posible, por ejemplo, lecturas/escrituras de otras variables ubicadas despues de una escritura en un
 * volatil que se reordenaran para que ocurran antes de esa escritura en el volatil. Simplemente no al reves. Se permite de despues
 * a antes, pero no de antes a despues.
 * <li>Las lecturas y escrituras en otras variables no se pueden reordenar para que ocurran antes de una lectura de una variable
 * volatil, si las lecturas/escrituras ocurrieron originalmente despues de la lectura de la variable volatil. Tenga en cuenta que
 * es posible que las lecturas de otras variables que ocurren antes de la lectura de una variable volatil se puedan reordenar para
 * que ocurran despues de la lectura de la variable volatil. Pero no al reves. Se permite de antes a despues, pero no de despues a
 * antes.
 * </ul>
 * <p>
 * La garantia anterior de suceder antes garantiza que se este aplicando la garantia de visibilidad de la palabra clave volatil.
 * <h3>Volatil no siempre es suficiente</h3>
 * <p>
 * Aunque la palabra clave volatile garantiza que las lecturas y escrituras de una variable se realicen directamente desde y hacia
 * la memoria principal, existen situaciones donde esta declaracion no es suficiente para asegurar la correcta sincronizacion entre
 * hilos. Volatile es adecuado cuando solo un hilo escribe en la variable compartida o cuando multiples hilos escriben valores que
 * no dependen del valor anterior de la variable. Sin embargo, cuando un hilo necesita leer el valor actual de la variable volatil
 * para calcular su nuevo valor, surge una condicion de carrera. En estos casos, como en el incremento de un contador compartido por
 * multiples hilos, volatile no puede garantizar la consistencia. Por ejemplo, si dos hilos leen simultaneamente un contador con
 * valor 0, lo incrementan a 1 en sus registros CPU, y luego escriben el resultado, se perderia una de las actualizaciones. En tales
 * situaciones, se requieren mecanismos de sincronizacion mas robustos para asegurar la integridad de los datos compartidos. Esta
 * situacion se ilustra en el siguiente diagrama:
 * <p>
 * <img src="volatil no siempre es suficiente.PNG">
 * <p>
 * El hilo 1 y el hilo 2 ahora practicamente no estan sincronizados. El valor real de la variable counter compartido deberia
 * haber sido 2, pero cada uno de los subprocesos tiene el valor 1 para la variable en sus registros de CPU, y en la memoria
 * principal el valor sigue siendo 0. ¡Es un desastre! Incluso si los subprocesos finalmente escriben su valor para la variable de
 * contador compartido en la memoria principal, el valor sera incorrecto.
 * <h3>¿Cuando es suficiente volatil?</h3>
 * <p>
 * Como mencione anteriormente, si dos subprocesos leen y escriben en una variable compartida, entonces usar la palabra clave
 * volatil para eso no es suficiente. Es necesario utilizar synchronized en ese caso para garantizar que la lectura y escritura
 * de la variable sea atomica. Leer o escribir una variable volatil no bloquea la lectura o escritura de subprocesos. Para que esto
 * suceda, debe utilizar la palabra clave synchronized en las secciones criticas.
 * <p>
 * Como alternativa a un bloque sincronizado, tambien puede utilizar uno de los muchos tipos de datos atomicos que se encuentran
 * en el paquete {@code java.util.concurrent}. Por ejemplo, {@code AtomicLong} o {@code AtomicReference} o uno de los otros.
 * <p>
 * En caso de que solo un subproceso lea y escriba el valor de una variable volatil y otros subprocesos solo lean la variable, se
 * garantiza que los subprocesos de lectura veran el ultimo valor escrito en la variable volatil. Sin hacer volatil la variable,
 * esto no estaria garantizado.
 * <p>
 * Se garantiza que la palabra clave volatil funcionara en 32 bits y 64 variables.
 * <h3>Consideraciones de rendimiento de volatiles</h3>
 * <p>
 * La lectura y escritura de variables volatiles hace que la variable se lea o escriba en la memoria principal. Leer y escribir en
 * la memoria principal es mas costoso que acceder al registro de la CPU. El acceso a variables volatiles tambien evita el
 * reordenamiento de instrucciones, que es una tecnica normal de mejora del rendimiento. Por lo tanto, solo debe utilizar variables
 * volatiles cuando realmente necesite imponer la visibilidad de las variables.
 * <p>
 * En la practica, los valores de registro de la CPU normalmente simplemente se escribiran en la cache L1 de la CPU, que es
 * razonablemente rapida. No es tan rapido como escribir en un registro de la CPU, pero sigue siendo rapido. La sincronizacion desde
 * la cache L1 hasta la cache L2 y L3 y de regreso a la memoria principal (RAM) se realiza mediante chips separados de la CPU (hasta
 * donde tengo entendido), por lo que la CPU no esta cargada con eso.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/volatile.html">Java Volatile Keyword</a>
 */

public class Volatile {

    private volatile int counter;

}
