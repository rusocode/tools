package com.punkipunk.concurrency.false_sharing;

/**
 * <p>
 * <i>False Sharing</i> en Java ocurre cuando dos subprocesos que se ejecutan en dos CPU diferentes escriben en dos variables
 * diferentes que estan almacenadas dentro de la misma linea de cache de la CPU. Cuando el primer subproceso modifica una de las
 * variables, toda la linea de cache de la CPU se invalida en los caches de la CPU de la otra CPU donde se esta ejecutando el otro
 * subproceso. Esto significa que las otras CPU necesitan recargar el contenido de la linea de cache invalidada, incluso si
 * realmente no necesitan la variable que se modifico dentro de esa linea de cache.
 * <p>
 * Aqui hay un diagrama que ilustra el uso compartido falso en Java:
 * <p>
 * <img src="false sharing.png">
 * <p>
 * El diagrama muestra dos subprocesos que se ejecutan en diferentes CPU y que escriben en diferentes variables (las variables se
 * almacenan en la misma linea de cache de la CPU), lo que provoca un intercambio falso.
 * <h3>Lineas de cache</h3>
 * <p>
 * Cuando las caches de la CPU estan leyendo datos de caches de nivel inferior o de la RAM (por ejemplo, L1 de L2, L2 de L3 y L3
 * de la RAM), no solo leen un solo byte a la vez. Eso seria ineficaz. En su lugar, leen una <i>linea de cache</i>. Una linea de
 * cache generalmente consta de 64 bytes. Por lo tanto, los caches leen 64 bytes a la vez desde caches de nivel inferior o RAM.
 * <p>
 * Debido a que una linea de cache consta de varios bytes, una sola linea de cache a menudo almacenara mas de una variable. Si la
 * misma CPU necesita acceder a mas variables almacenadas dentro de la misma linea de cache, esto es una ventaja. Si varias CPU
 * necesitan acceder a las variables almacenadas dentro de la misma linea de cache, puede ocurrir un intercambio falso.
 * <h3>Invalidacion de linea de cache</h3>
 * <p>
 * Cuando una CPU escribe en la direccion de memoria en una linea de cache, normalmente porque la CPU esta escribiendo en una
 * variable, la linea de cache se <i>ensucia</i>. Luego, la linea de cache debe sincronizarse con otras CPU que tambien tienen esa
 * linea de cache en sus caches de CPU. La misma linea de cache almacenada en las otras caches de la CPU pasa a ser invalida; en
 * otras palabras, deben actualizarse.
 * <p>
 * En el diagrama anterior, ensuciar una linea de cache esta representada por las lineas azules y la invalidacion de la linea de
 * cache esta representada por las flechas rojas.
 * <p>
 * La actualizacion de la memoria cache despues de la invalidacion de la memoria cache puede ocurrir mediante mecanismos de
 * coherencia de la memoria cache o recargando la linea de memoria cache desde la RAM.
 * <p>
 * La CPU no puede acceder a esa linea de cache hasta que se haya actualizado.
 * <h3>El uso compartido falso da como resultado una penalizacion por desempeño</h3>
 * <p>
 * Cuando una linea de cache se invalida porque los datos dentro de esa linea de cache han sido cambiados por otra CPU, entonces
 * la linea de cache invalidada debe actualizarse, ya sea desde la cache L3 o mediante mecanismos de coherencia de cache. Por lo
 * tanto, si la CPU necesita leer la linea de cache invalidada, debe esperar hasta que se actualice la linea de cache. Esto da
 * como resultado una degradacion del rendimiento. El tiempo de la CPU se pierde esperando la actualizacion de la linea de cache,
 * lo que significa que la CPU puede ejecutar menos instrucciones durante ese tiempo.
 * <p>
 * El uso compartido falso significa que dos (o mas) CPU escriben en variables almacenadas dentro de la misma linea de cache, pero
 * cada CPU realmente no depende del valor escrito por la otra CPU. Sin embargo, ambos siguen ensuciando continuamente la linea de
 * cache, lo que hace que se invalide para la otra CPU, lo que obliga a la otra CPU a actualizarla, antes de que la otra CPU
 * tambien ensucie la linea de cache, lo que hace que la primera CPU tenga que actualizarla, etc.
 * <p>
 * La solucion al uso compartido falso es cambiar sus estructuras de datos para que las variables independientes utilizadas por
 * las cpu ya no se almacenen dentro de la misma linea de cache.
 * <p>
 * Nota: Incluso si las CPU a veces usan la variable escrita por la otra CPU, aun puede beneficiarse al asegurarse de que las
 * variables compartidas no se almacenen en la misma linea de cache. Exactamente cuanto tendras que experimentar para descubrirlo,
 * en tu caso concreto.
 * <h3>Ejemplo de codigo Java con false sharing</h3>
 * <p>
 * Las dos clases siguientes ilustran como puede ocurrir un intercambio falso en una aplicacion Java.
 * <p>
 * La primera clase es una clase Counter que es utilizada por dos subprocesos. El primer hilo incrementara el campo {@code count1}
 * y el segundo hilo incrementara el campo {@code count2}.
 * <pre>{@code
 * public class Counter {
 *
 *     public volatile long count1;
 *     public volatile long count2;
 *
 * }
 * }</pre>
 * <p>
 * Aqui hay un ejemplo de codigo que inicia 2 subprocesos que incrementan los dos campos de contador dentro de la misma instancia
 * de Counter:
 * <pre>{@code
 * public class FalseSharingExample {
 *
 *     public static void main(String[] args) {
 *
 *         Counter counter1 = new Counter();
 *         Counter counter2 = counter1;
 *
 *         long iterations = 1_000_000_000;
 *
 *         Thread thread1 = new Thread(() -> {
 *             long startTime = System.currentTimeMillis();
 *             for (long i = 0; i < iterations; i++) {
 *                 counter1.count1++;
 *             }
 *             long endTime = System.currentTimeMillis();
 *             System.out.println("Time: " + (endTime - startTime));
 *         });
 *         Thread thread2 = new Thread(() -> {
 *             long startTime = System.currentTimeMillis();
 *             for (long i = 0; i < iterations; i++) {
 *                 counter2.count2++;
 *             }
 *             long endTime = System.currentTimeMillis();
 *             System.out.println("Time: " + (endTime - startTime));
 *         });
 *
 *         thread1.start();
 *         thread2.start();
 *     }
 * }
 * }</pre>
 * <p>
 * En mi computadora portatil, ejecutar este ejemplo lleva alrededor de 36 segundos.
 * <p>
 * Si, en cambio, se cambia el codigo para que cada hilo incremente los campos de dos instancias de contador diferentes (que se
 * muestran a continuacion), entonces el codigo solo tardara unos 9 segundos en ejecutarse. Ese es un factor de 4 en la diferencia
 * entre codigo con y sin intercambio falso. ¡Eso es bastante!
 * <p>
 * Esta diferencia de velocidad probablemente se debe a un uso compartido falso en el primer ejemplo, donde los campos count1 y
 * count2 en la instancia de Counter compartida se encuentran dentro de la misma linea de cache en tiempo de ejecucion. En el
 * segundo ejemplo (a continuacion), los dos subprocesos utilizan cada uno su propia instancia de Counter, que ya no almacena sus
 * campos dentro de la misma linea de cache. Por lo tanto, no se produce un intercambio falso y el codigo se ejecuta mas rapido.
 * <pre>{@code
 * public class FalseSharingExample {
 *
 *     public static void main(String[] args) {
 *
 *         Counter counter1 = new Counter();
 *         Counter counter2 = new Counter();
 *
 *         long iterations = 1_000_000_000;
 *
 *         Thread thread1 = new Thread(() -> {
 *             long startTime = System.currentTimeMillis();
 *             for (long i = 0; i < iterations; i++) {
 *                 counter1.count1++;
 *             }
 *             long endTime = System.currentTimeMillis();
 *             System.out.println("Time: " + (endTime - startTime));
 *         });
 *         Thread thread2 = new Thread(() -> {
 *             long startTime = System.currentTimeMillis();
 *             for (long i = 0; i < iterations; i++) {
 *                 counter2.count2++;
 *             }
 *             long endTime = System.currentTimeMillis();
 *             System.out.println("Time: " + (endTime - startTime));
 *         });
 *
 *         thread1.start();
 *         thread2.start();
 *     }
 * }
 * }</pre>
 * <h3>Solucionar problemas de intercambio falso</h3>
 * <p>
 * La forma de solucionar un problema de intercambio falso es diseñar su codigo de modo que las diferentes variables utilizadas
 * por diferentes subprocesos no terminen almacenandose dentro de la misma linea de cache de la CPU. Exactamente como hacerlo
 * depende de su codigo concreto, pero almacenar las variables en diferentes objetos es una forma de hacerlo, como lo mostro el
 * ejemplo de la seccion anterior.
 * <h3>Evitar el intercambio falso con la anotacion @Contented</h3>
 * <p>
 * Desde Java 8 y 9, Java tiene la anotacion <i>@Contended</i> que puede rellenar campos dentro de clases con bytes vacios
 * (despues del campo, cuando se almacena en RAM), de modo que los campos dentro de un objeto de esa clase no se almacenen dentro
 * de la misma linea de cache de la CPU. A continuacion se muestra la clase Counter del ejemplo anterior con la anotacion
 * {@code @Contended} agregada a uno de los campos. Agregar esta anotacion hizo que el tiempo de ejecucion se redujera
 * aproximadamente al mismo tiempo que cuando los dos subprocesos usaban dos instancias de Counter diferentes. Aqui esta la clase
 * Counter modificada:
 * <pre>{@code
 * public class Counter1 {
 *     @jdk.internal.vm.annotation.Contended
 *     public volatile long count1;
 *     public volatile long count2;
 * }
 * }</pre>
 * <h3>Anotar clases con @Contended</h3>
 * <p>
 * Puede usar @Contended encima de una clase para hacer que todos los campos se completen entre si. Sin embargo, hacerlo en mi
 * ejemplo no redujo el tiempo de ejecucion. Anotar el primer campo lo hizo. Asegurese de medir el rendimiento de las diferentes
 * opciones antes de elegir una. A continuacion se muestra un ejemplo de como anotar la clase Counter con @Contended:
 * <pre>{@code
 * @jdk.internal.vm.annotation.Contended
 * public class Counter1 {
 *     public volatile long count1;
 *     public volatile long count2;
 * }
 * }</pre>
 * <h3>Anotar campos con @Contented</h3>
 * <p>
 * Puede usar @Contended encima de un campo para rellenar ese campo con respecto a otros campos de la clase. Asi es como se ve
 * anotar un campo con @Contended:
 * <pre>{@code
 * public class Counter1 {
 *     @jdk.internal.vm.annotation.Contended
 *     public volatile long count1;
 *     @jdk.internal.vm.annotation.Contended
 *     public volatile long count2;
 * }
 * }</pre>
 * <p>
 * <h3>Campos de agrupacion</h3>
 * <p>
 * La anotacion @Contended hace posible agrupar campos de modo que los campos agrupados se mantengan juntos en la RAM, pero tengan
 * relleno entre ellos y otros campos de la clase. A continuacion se muestra un ejemplo de agrupacion de campos con la anotacion
 * <pre>{@code
 * public class Counter1 {
 *     @jdk.internal.vm.annotation.Contended("group1")
 *     public volatile long count1;
 *     @jdk.internal.vm.annotation.Contended("group1");
 *     public volatile long count2;
 *     @jdk.internal.vm.annotation.Contended("group2");
 *     public volatile long count3;
 * }
 * }</pre>
 * <p>
 * En este ejemplo, los campos count1 y count2 estan agrupados en el mismo grupo llamado grupo1 y el campo count3 se encuentra en
 * su propio grupo. Por lo tanto, count1 y count2 se mantendran juntos en la clase, pero tendran un relleno entre ellos y el campo
 * count3.
 * <p>
 * Los nombres de los grupos no importan excepto para hacer coincidir los campos con los grupos.
 * <h3>Configurar el tamaño del relleno</h3>
 * <p>
 * De forma predeterminada, las anotaciones @Contended agregan 128 bytes de relleno despues de un campo anotado con @Contended.
 * Sin embargo, puede indicarle a la JVM cuantos bytes usar como relleno mediante el argumento de la linea de comando de la JVM.
 * Asi es como se ve el argumento de la linea de comando:
 * <p>
 * {@code -XX:ContendedPaddingWidth=64}
 * <p>
 * Este argumento configura la JVM para utilizar 64 bytes para rellenar con la anotacion @Contended en lugar de 128 bytes.
 * <p>
 * La cantidad de bytes de relleno necesarios para evitar el intercambio falso en Java depende de la arquitectura de hardware
 * subyacente, es decir, cuantos bytes contiene cada linea de cache de la CPU. Si lo sabe, puede optimizar el relleno de
 * prevencion de intercambio falso para que coincida con el tamaño de la linea de cache. Por lo tanto, si las lineas de cache
 * tienen solo 64 bytes, no hay razon para rellenar con 128 bytes. Ademas, si las lineas de cache son de 256 bytes, rellenar con
 * solo 128 bytes no sera suficiente para evitar el intercambio falso.
 * <p>
 * Fuente: <a href="http://tutorials.jenkov.com/java-concurrency/false-sharing.html">False Sharing in Java</a>
 */

public class FalseSharing {

    public static void main(String[] args) {

        Counter counter1 = new Counter();
        Counter counter2 = counter1;

        long iterations = 500_000_000;

        Thread t1 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            for (long i = 0; i < iterations; i++)
                counter1.count1++;
            long endTime = System.currentTimeMillis();
            System.out.println("Time: " + (endTime - startTime) / 1000 + " seg");
        });

        Thread t2 = new Thread(() -> {
            long startTime = System.currentTimeMillis();
            for (long i = 0; i < iterations; i++)
                counter2.count2++;
            long endTime = System.currentTimeMillis();
            System.out.println("Time: " + (endTime - startTime) / 1000 + " seg");
        });

        t1.start();
        t2.start();
    }

    private static class Counter {
        public volatile long count1;
        public volatile long count2;
    }

}

