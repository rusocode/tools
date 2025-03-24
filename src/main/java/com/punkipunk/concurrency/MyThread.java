package com.punkipunk.concurrency;

/**
 * <p>
 * Un <i>Java Thread</i> es como una CPU virtual que puede ejecutar codigo Java, dentro de la aplicacion Java. Cuando se inicia
 * una aplicacion Java, el metodo {@code main()} es ejecutado por el <i>main thread</i>, un hilo especial creado por la JVM para
 * ejecutar la aplicacion. Desde dentro de la aplicacion se puede crear e iniciar mas subprocesos que pueden ejecutar partes del
 * codigo de la aplicacion en paralelo con el subproceso principal.
 * <p>
 * Los threads de Java son objetos como cualquier otro objeto de Java. Los subprocesos son instancias de la clase
 * {@code java.lang.Thread} o instancias de subclases de esta clase. Ademas de ser objetos, los subprocesos de Java tambien pueden
 * ejecutar codigo.
 * <h3>Crear e iniciar Threads</h3>
 * <p>
 * Para crear:
 * <p>
 * {@code Thread thread = new Thread();}
 * <p>
 * Para iniciar:
 * <p>
 * {@code thread.start();}
 * <p>
 * Hay dos formas de especificar que codigo debe ejecutar el hilo. La primera es crear una subclase de Thread y sobreescribir
 * (@Override) el metodo {@code run()}. El segundo metodo consiste en pasar un objeto que implemente {@code Runnable} al
 * constructor Thread.
 * <h3>Subclase Thread</h3>
 * <pre>{@code
 * public class MyThread extends Thread {
 *     public void run() {
 *         System.out.println("MyThread running");
 *     }
 * }
 * }</pre>
 * Para crear e iniciar el hilo anterior:
 * <pre>{@code
 * MyThread thread = new MyThread();
 * thread.start();
 * }</pre>
 * <p>
 * La llamada {@code start()} volvera tan pronto como se inicie el hilo. No esperara hasta que finalice el metodo run(). El metodo
 * run() se ejecutara como si lo ejecutara una CPU diferente. Cuando se ejecute el metodo run(), imprimira el texto "MyThread
 * running".
 * <p>
 * Tambien puedes crear una subclase anonima de Thread como esta:
 * <pre>{@code
 * Thread thread = new Thread() {
 *     public void run() {
 *         System.out.println("Thread running");
 *     }
 * }
 * thread.start();
 * }</pre>
 * <h3>Interfaz Runnable</h3>
 * <p>
 * La segunda forma de especificar que codigo debe ejecutar un hilo es creando una clase que implemente la interfaz
 * {@code java.lang.Runnable}. Un objeto que implementa la interfaz Runnable puede ser ejecutado mediante un Thread.
 * <p>
 * La interfaz Runnable es una interfaz estandar que viene con la plataforma Java y solo tiene un unico metodo run().
 * <p>
 * Cualquier cosa que se supone que debe hacer el hilo cuando se ejecuta debe incluirse en la implementacion del metodo run(). Hay
 * tres formas de implementar la interfaz Runnable:
 * <ol>
 * <li>Desde una clase que implemente la interfaz Runnable:
 * <pre>{@code
 * public class MyRunnable implements Runnable {
 *     @Override
 *     public void run() {
 *         System.out.println("MyRunnable running");
 *     }
 * }
 * }</pre>
 * Todo lo que hace esta implementacion de Runnable es imprimir el texto "MyRunnable running". Despues de imprimir ese texto, el
 * metodo run() sale y el hilo que ejecuta el metodo run() se detendra.
 * <li>Desde una clase anonima que implemente la interfaz Runnable:
 * <pre>{@code
 * Runnable myRunnable =
 *      new Runnable() {
 *          public void run() {
 *              System.out.println("Runnable running");
 *          }
 *      }
 * }</pre>
 * <li>Desde un Lambda que implemente la interfaz Runnable:
 * <pre>{@code
 * Runnable runnable = () -> { System.out.println("Lambda Runnable running"); };
 * }</pre>
 * La tercera forma de implementar la interfaz Runnable es creando una implementacion Lambda de la interfaz Runnable. Esto es
 * posible porque la interfaz Runnable solo tiene un unico metodo no implementado y, por lo tanto, es practicamente (aunque
 * posiblemente sin querer) una interfaz funcional.
 * </ol>
 * <h4>Iniciar un hilo con un Runnable</h4>
 * <p>
 * Para que un subproceso ejecute el metodo run(), se puede pasar una instancia de una clase, clase anonima o expresion lambda que
 * implemente la interfaz Runnable a un subproceso en su constructor. Asi es como se hace:
 * <pre>{@code
 * Runnable runnable = new MyRunnable(); // O una clase anonima, o lambda...
 * Thread thread = new Thread(runnable);
 * thread.start();
 * }</pre>
 * <h3>¿Subclase o Runnable?</h3>
 * <p>
 * No existen reglas sobre cual de los dos metodos es el mejor. Ambos metodos funcionan. Sin embargo, personalmente prefiero
 * implementar Runnable y entregar una instancia de la implementacion a una instancia de Thread. Cuando un thread pool ejecuta
 * Runnable, es facil poner en cola las instancias de Runnable hasta que un subproceso del grupo este inactivo. Esto es un poco
 * mas dificil de hacer con las subclases de Thread.
 * <h3>Pausar un Thread</h3>
 * <p>
 * Un hilo puede pausarse llamando al metodo estatico {@code Thread.sleep()} tomando una cantidad de milisegundos como parametro.
 * El metodo sleep() intentara dormir esa cantidad de milisegundos antes de reanudar la ejecucion. Thread sleep() no es 100%
 * preciso, pero aun asi es bastante bueno. Ejemplo de como pausar un hilo durante 3 segundos (3000 milisegundos):
 * <pre>{@code
 * try {
 *     Thread.sleep(3000);
 * } catch (InterruptedException e) {
 *     e.printStackTrace();
 * }
 * }</pre>
 * <h3>Detener un Thread</h3>
 * <p>
 * Detener un subproceso requiere cierta preparacion del codigo de implementacion del subproceso. La clase Thread contiene un
 * metodo stop(), pero esta en desuso. El metodo stop() original no ofreceria ninguna garantia sobre en que estado se detuvo el
 * hilo. Eso significa que todos los objetos a los que el hilo tuvo acceso durante la ejecucion quedarian en un estado desconocido.
 * Si otros subprocesos de la aplicacion tambien tienen acceso a los mismos objetos, la aplicacion podria fallar de forma
 * inesperada e impredecible.
 * <p>
 * En lugar de llamar al metodo stop(), tendras que implementar el codigo de tu hilo para poder detenerlo. Aqui hay un ejemplo
 * de una clase que implementa Runnable que contiene un metodo adicional llamado {@code stop()} que indica a Runnable que se
 * detenga. El Runnable comprobara esta señal y se detendra cuando este listo para hacerlo.
 * <pre>{@code
 * private class MyRunnable implements Runnable {
 *
 *     private boolean stopped;
 *
 *     public synchronized void stop() {
 *         stopped = true;
 *     }
 *
 *     private synchronized boolean isStopped() {
 *         return stopped;
 *     }
 *
 *     @Override
 *     public void run() {
 *         // Mientras el hilo no este detenido
 *         while (!isStopped()) {
 *             System.out.println("MyRunnable running");
 *             try {
 *                 Thread.sleep(3L * 1000L);
 *             } catch (InterruptedException e) {
 *                 e.printStackTrace();
 *             }
 *         }
 *    }
 *
 * }
 * }</pre>
 * <p>
 * El metodo stop() esta pensado para ser llamado desde otro subproceso distinto del subproceso que ejecuta el metodo run() de
 * MyRunnable. El metodo isStopped() es llamado internamente por el hilo que ejecuta el metodo run() de MyRunnable. Mientras no se
 * haya llamado a stop(), el metodo isStopped() devolvera false, lo que significa que el subproceso que ejecuta el metodo run()
 * seguira ejecutandose.
 * <p>
 * A continuacion se muestra un ejemplo de como iniciar un subproceso que ejecuta una instancia de la clase MyRunnable y lo
 * detiene despues de una pausa:
 * <pre>{@code
 * public class MyRunnableMain {
 *
 *     public static void main(String[] args) {
 *         MyRunnable myRunnable = new MyRunnable();
 *
 *         Thread thread = new Thread(myRunnable);
 *
 *         thread.start();
 *
 *         try {
 *             Thread.sleep(10L * 1000L);
 *         } catch (InterruptedException e) {
 *             e.printStackTrace();
 *         }
 *
 *         myRunnable.stop();
 *     }
 * }
 * }</pre>
 * <p>
 * Este ejemplo primero crea una instancia MyRunnable, luego pasa esa instancia a un subproceso e inicia el subproceso. Luego, el
 * hilo que ejecuta el metodo main() (el hilo principal) duerme durante 10 segundos y luego llama al metodo stop() de la
 * instancia MyRunnable. Esto hara que el hilo que ejecuta el metodo MyRunnable se detenga, porque isStopped() devolvera true
 * despues de que se haya llamado a stop().
 * <p>
 * Tenga en cuenta que si su implementacion de Runnable necesita algo mas que el metodo run() (por ejemplo, un metodo stop() o
 * pause() tambien), entonces ya no podra crear su implementacion de Runnable con una expresion lambda. Una lambda solo puede
 * implementar un unico metodo. En su lugar, debe utilizar una clase personalizada o una interfaz personalizada que extienda
 * Runnable, que tenga metodos adicionales y que se implemente mediante una clase anonima.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html">Creating and Starting Java Threads</a>
 * <a href="https://www.youtube.com/watch?v=eQk5AWcTS8w">Creating and Starting Java Threads (video)</a>
 */

public class MyThread extends Thread {

    public static void main(String[] args) {
        MyThread thread = new MyThread(); // Subclase de Thread
        Thread thread2 = new Thread(new MyRunnable()); // Pasa un objeto Runnable al constructor de Thread
        // thread.start();
        // example();
    }

    public void run() {
        System.out.println("MyThread running");
    }

    private static class MyRunnable implements Runnable {

        private boolean stopped;

        public synchronized void stop() {
            stopped = true;
        }

        private synchronized boolean isStopped() {
            return stopped;
        }

        @Override
        public void run() {
            // Mientras el hilo no este detenido
            while (!isStopped()) {
                System.out.println("MyRunnable running");
                try {
                    Thread.sleep(3L * 1000L);
                } catch (InterruptedException e) {
                    System.err.println("Error: " + e.getMessage());
                }
            }
        }

    }

    /**
     * He aqui un pequeño ejemplo de un Thread. Primero imprime el nombre del hilo que ejecuta el metodo main(). Este hilo lo
     * asigna la JVM. Luego inicia 10 subprocesos y les da a todos un numero como nombre ("" + i). Luego, cada hilo imprime su
     * nombre y luego deja de ejecutarse.
     * <p>
     * Tenga en cuenta que incluso si los subprocesos se inician en secuencia (1, 2, 3, etc.), es posible que no se ejecuten
     * secuencialmente, lo que significa que el subproceso 1 puede no ser el primer subproceso en escribir su nombre en
     * System.out. Esto se debe a que, en principio, los subprocesos se ejecutan en paralelo y no de forma secuencial. La JVM y/o
     * el sistema operativo determinan el orden en el que se ejecutan los subprocesos. Este orden no tiene que ser el mismo orden
     * en el que se iniciaron.
     */
    private static void example() {
        System.out.println(Thread.currentThread().getName());
        for (int i = 0; i < 10; i++) {
            new Thread("" + i) {
                public void run() {
                    System.out.println("Thread " + getName() + " running");
                }
            }.start();
        }
    }

}
