package com.punkipunk.concurrency;

/**
 * <p>
 * La clase <i>ThreadLocal</i> le permite crear variables que solo pueden ser leidas y escritas por el mismo hilo. Por lo tanto,
 * incluso si dos subprocesos ejecutan el mismo codigo y el codigo tiene una referencia a la misma variable {@code ThreadLocal},
 * los dos subprocesos no pueden ver las variables ThreadLocal del otro. Por lo tanto, la clase ThreadLocal proporciona una forma
 * sencilla de hacer que el codigo sea seguro para subprocesos que de otro modo no lo seria.
 * <h3>Creando un ThreadLocal</h3>
 * <p>
 * {@code private ThreadLocal threadLocal = new ThreadLocal();}
 * <p>
 * Esto solo debe hacerse una vez por hilo. Ahora varios subprocesos pueden obtener y establecer valores dentro de este ThreadLocal,
 * y cada subproceso solo vera el valor que establecio.
 * <h3>Establecer, obtener y eliminar el valor de ThreadLocal</h3>
 * <pre>{@code
 * threadLocal.set("A thread local value");
 * String threadLocalValue = (String) threadLocal.get();
 * threadLocal.remove();
 * }</pre>
 * <h3>ThreadLocal generico</h3>
 * <p>
 * Puede crear un ThreadLocal con un tipo generico. Usando un tipo generico, solo los objetos del tipo generico se pueden establecer
 * como valor en ThreadLocal. Ademas, no es necesario encasillar el valor devuelto por get().
 * <p>
 * {@code private ThreadLocal<String> myThreadLocal = new ThreadLocal<>();}
 * <p>
 * Ahora solo puedes almacenar cadenas en la instancia de ThreadLocal. Ademas, no es necesario encasillar (castear) el valor
 * obtenido de ThreadLocal:
 * <pre>{@code
 * myThreadLocal.set("Hello ThreadLocal");
 * String threadLocalValue = myThreadLocal.get();
 * }</pre>
 * <h3>Valor inicial de ThreadLocal</h3>
 * <p>
 * Es posible establecer un valor inicial para ThreadLocal que se utilizara la primera vez que se llame a get(), antes de que se
 * llame a set() con un nuevo valor. Tiene dos opciones para especificar un valor inicial para ThreadLocal:
 * <ul>
 * <li>Cree una subclase ThreadLocal que anule el metodo {@code initialValue()}.
 * <li>Cree un ThreadLocal con una implementacion de la interfaz {@code Supplier}.
 * </ul>
 * <h4>Anular initialValue()</h4>
 * <p>
 * La primera forma de especificar un valor inicial para una variable de tipo ThreadLocal es crear una subclase de ThreadLocal que
 * anula su metodo initialValue(). La forma mas sencilla de crear una subclase de ThreadLocal es simplemente crear una subclase
 * anonima, justo donde crea la variable ThreadLocal.
 * <pre>{@code
 * private ThreadLocal myThreadLocal = new ThreadLocal<String>() {
 *     @Override
 *     protected String initialValue() {
 *         return String.valueOf(System.currentTimeMillis());
 *     }
 * };
 * }</pre>
 * <p>
 * Tenga en cuenta que diferentes subprocesos seguiran viendo diferentes valores iniciales. Cada hilo creara su propio valor
 * inicial. Solo si devuelve exactamente el mismo objeto del metodo initialValue(), todos los hilos veran el mismo objeto. Sin
 * embargo, el objetivo de utilizar ThreadLocal en primer lugar es evitar que los diferentes subprocesos vean la misma instancia.
 * <h4>Proporcionar la implementacion Supplier</h4>
 * <p>
 * El segundo metodo para especificar un valor inicial para una variable de ThreadLocal es utilizar su metodo de fabrica estatico
 * {@code withInitial(Supplier)} pasando una implementacion de la interfaz Supplier como parametro. Esta implementacion proporciona
 * el valor inicial para ThreadLocal.
 * <pre>{@code
 * ThreadLocal<String> threadLocal = ThreadLocal.withInitial(new Supplier<String>() {
 *     @Override
 *     public String get() {
 *         return String.valueOf(System.currentTimeMillis());
 *     }
 * });
 * }</pre>
 * <p>
 * Dado que Supplier es una interfaz funcional, se puede implementar mediante una expresion lambda.
 * <pre>{@code
 * ThreadLocal threadLocal = ThreadLocal.withInitial(
 *         () -> { return String.valueOf(System.currentTimeMillis()); } );
 * }</pre>
 * <p>
 * Como puedes ver, este es algo mas corto que el ejemplo anterior. Pero se puede acortar aun mas, utilizando la sintaxis mas densa
 * de expresiones lambda:
 * <pre>{@code
 * ThreadLocal threadLocal = ThreadLocal.withInitial(
 *         () -> String.valueOf(System.currentTimeMillis()) );
 * }</pre>
 * <h3>Configuracion lazy del valor ThreadLocal</h3>
 * <p>
 * En algunas situaciones no se pueden utilizar las formas estandar de establecer un valor inicial. Por ejemplo, quizas necesite
 * alguna informacion de configuracion que no este disponible en el momento de crear la variable ThreadLocal. En ese caso, puede
 * establecer el valor inicial de forma perezosa.
 * <pre>{@code
 * public class MyDateFormatter {
 *
 *     private ThreadLocal<SimpleDateFormat> simpleDateFormatThreadLocal = new ThreadLocal<>();
 *
 *     public String format(Date date) {
 *         SimpleDateFormat simpleDateFormat = getThreadLocalSimpleDateFormat();
 *         return simpleDateFormat.format(date);
 *     }
 *
 *     private SimpleDateFormat getThreadLocalSimpleDateFormat() {
 *         SimpleDateFormat simpleDateFormat = simpleDateFormatThreadLocal.get();
 *         if (simpleDateFormat == null) {
 *             simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
 *             simpleDateFormatThreadLocal.set(simpleDateFormat);
 *         }
 *         return simpleDateFormat;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como el metodo {@code format()} llama al metodo {@code getThreadLocalSimpleDateFormat()} para obtener una instancia de
 * {@code SimpleDatFormat}. Si no se ha configurado una instancia de SimpleDateFormat en ThreadLocal, se crea un nuevo SimpleDateFormat y
 * se configura en la variable ThreadLocal. Una vez que un subproceso ha establecido su propio SimpleDateFormat en la variable
 * ThreadLocal, se utiliza el mismo objeto SimpleDateFormat para ese subproceso en el futuro. Pero solo para ese hilo. Cada hilo
 * crea su propia instancia de SimpleDateFormat, ya que no pueden ver las instancias de los demas configuradas en la variable
 * ThreadLocal.
 * <p>
 * La clase SimpleDateFormat no es segura para subprocesos, por lo que varios subprocesos no pueden usarla al mismo tiempo. Para
 * resolver este problema, la clase {@code MyDateFormatter} anterior crea un SimpleDateFormat por subproceso, por lo que cada
 * subproceso que llame al metodo format() utilizara su propia instancia de SimpleDateFormat.
 * <h3>Usando un ThreadLocal con un Thread Pool o ExecutorService</h3>
 * <p>
 * Si planea utilizar ThreadLocal desde dentro de una tarea pasada a un Thread Pool o ExecutorService, tenga en cuenta que no tiene
 * ninguna garantia de que subproceso ejecutara su tarea. Sin embargo, si todo lo que necesita es asegurarse de que cada hilo use
 * su propia instancia de algun objeto, esto no es un problema. Luego puede usar ThreadLocal con un Thread Pool o ExecutorService
 * sin problemas.
 * <h3>InheritableThreadLocal</h3>
 * <p>
 * La clase {@code InheritableThreadLocal} es una subclase de ThreadLocal. En lugar de que cada hilo tenga su propio valor dentro de
 * ThreadLocal, InheritableThreadLocal otorga acceso a los valores de un hilo y de todos los hilos secundarios creados por ese hilo.
 * <pre>{@code
 * public class InheritableThreadLocalBasicExample {
 *
 *     public static void main(String[] args) {
 *
 *         ThreadLocal<String> threadLocal = new ThreadLocal<>();
 *         InheritableThreadLocal<String> inheritableThreadLocal = new InheritableThreadLocal<>();
 *
 *         Thread thread1 = new Thread(() -> {
 *             System.out.println("===== Thread 1 =====");
 *             threadLocal.set("Thread 1 - ThreadLocal");
 *             inheritableThreadLocal.set("Thread 1 - InheritableThreadLocal");
 *
 *             System.out.println(threadLocal.get());
 *             System.out.println(inheritableThreadLocal.get());
 *
 *             Thread childThread = new Thread( () -> {
 *                 System.out.println("===== ChildThread =====");
 *                 System.out.println(threadLocal.get());
 *                 System.out.println(inheritableThreadLocal.get());
 *             });
 *             childThread.start();
 *         });
 *
 *         thread1.start();
 *
 *         Thread thread2 = new Thread(() -> {
 *             try {
 *                 Thread.sleep(3000);
 *             } catch (InterruptedException e) {
 *                 e.printStackTrace();
 *             }
 *
 *             System.out.println("===== Thread2 =====");
 *             System.out.println(threadLocal.get());
 *             System.out.println(inheritableThreadLocal.get());
 *         });
 *         thread2.start();
 *     }
 * }
 * }</pre>
 * <p>
 * Este ejemplo crea un ThreadLocal normal y un InheritableThreadLocal. Luego, el ejemplo crea un subproceso que establece el valor
 * de ThreadLocal y InheritableThreadLocal, y luego crea un subproceso secundario que accede a los valores de ThreadLocal y
 * InheritableThreadLocal. Solo el valor de InheritableThreadLocal es visible para el subproceso secundario.
 * <p>
 * Finalmente, el ejemplo crea un tercer hilo que tambien intenta acceder tanto a ThreadLocal como a InheritableThreadLocal, pero
 * que no ve ninguno de los valores almacenados por el primer hilo.
 * <p>
 * El resultado impreso al ejecutar este ejemplo se veria asi:
 * <pre>{@code
 * ===== Thread 1 =====
 * Thread 1 - ThreadLocal
 * Thread 1 - InheritableThreadLocal
 * ===== ChildThread =====
 * null
 * Thread 1 - InheritableThreadLocal
 * ===== Thread2 =====
 * null
 * null
 * }</pre>
 * <p>
 * Fuente: <a href="https://jenkov.com/tutorials/java-concurrency/threadlocal.html">Java ThreadLocal</a>
 */

public class _ThreadLocal {

    /**
     * <p>
     * El punto clave aqui es que ThreadLocal permite que cada hilo tenga su propia copia independiente de la variable, incluso
     * cuando comparten la misma instancia de Runnable. Esto es util para mantener datos especificos de cada hilo sin necesidad de
     * sincronizacion explicita.
     */
    public static void main(String[] args) throws InterruptedException {

        MyRunnable sharedRunnableInstance = new MyRunnable();

        Thread thread1 = new Thread(sharedRunnableInstance);
        Thread thread2 = new Thread(sharedRunnableInstance);

        thread1.start();
        thread2.start();

        thread1.join(); // Espera a que termine el hilo 1
        thread2.join(); // Espera a que termine el hilo 2

    }

    private static class MyRunnable implements Runnable {

        private final ThreadLocal<Integer> threadLocal = new ThreadLocal<>();

        @Override
        public void run() {
            threadLocal.set((int) (Math.random() * 100));
            System.out.println(threadLocal.get());
        }

    }

}

