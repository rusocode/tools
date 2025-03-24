package com.punkipunk.concurrency;

/**
 * <p>
 * El codigo que es seguro para que varios subprocesos lo llamen simultaneamente se denomina <i>thread safe</i> (seguro para
 * subprocesos). Si un fragmento de codigo es seguro para subprocesos, entonces no contiene condiciones de carrera. La condicion
 * de carrera solo ocurre cuando varios subprocesos actualizan recursos compartidos. Por lo tanto, es importante saber que
 * recursos comparten los subprocesos de Java durante la ejecucion.
 * <h3>Variables locales</h3>
 * <p>
 * Las variables locales se almacenan en la propia stack (pila) de cada hilo. Eso significa que las variables locales nunca se
 * comparten entre subprocesos. Eso tambien significa que todas las variables primitivas locales son seguras para subprocesos. A
 * continuacion se muestra un ejemplo de una variable primitiva local segura para subprocesos:
 * <pre>{@code
 * public void someMethod() {
 *
 *     int threadSafeInt = 0;
 *
 *     threadSafeInt++;
 *
 * }
 * }</pre>
 * <h3>Referencias de objetos locales</h3>
 * <p>
 * Las referencias locales a objetos son un poco diferentes. La referencia en si no se comparte. Sin embargo, el objeto al que se
 * hace referencia no se almacena en la stack local de cada subproceso. Todos los objetos se almacenan en el heap compartido.
 * <p>
 * Si un objeto creado localmente nunca escapa del metodo en el que se creo, es seguro para subprocesos. De hecho, tambien puedes
 * pasarlo a otros metodos y objetos siempre que ninguno de estos metodos u objetos haga que el objeto pasado este disponible para
 * otros subprocesos.
 * <p>
 * A continuacion se muestra un ejemplo de un objeto local seguro para subprocesos:
 * <pre>{@code
 * public void someMethod() {
 *
 *     LocalObject localObject = new LocalObject();
 *
 *     localObject.callMethod();
 *     method2(localObject);
 *
 * }
 *
 * public void method2(LocalObject localObject) {
 *     localObject.setValue("value");
 * }
 * }</pre>
 * <p>
 * La instancia de {@code LocalObject} en este ejemplo no se devuelve desde el metodo ni se pasa a ningun otro objeto al que se
 * pueda acceder desde fuera del metodo {@code someMethod()}. Cada hilo que ejecute el metodo someMethod() creara su propia
 * instancia de LocalObject y la asignara a la referencia de {@code localObject}. Por lo tanto, el uso de LocalObject aqui es
 * seguro para subprocesos.
 * <p>
 * De hecho, todo el metodo someMethod() es seguro para subprocesos. Incluso si la instancia de LocalObject se pasa como parametro
 * a otros metodos en la misma clase o en otras clases, su uso es seguro para subprocesos.
 * <p>
 * La unica excepcion es, por supuesto, si uno de los metodos llamados con LocalObject como parametro almacena la instancia de
 * LocalObject de una manera que permite el acceso a ella desde otros subprocesos.
 * <h3>Variables miembro de objeto</h3>
 * <p>
 * Las variables miembro del objeto (campos) se almacenan en el heap junto con el objeto. Por lo tanto, si dos subprocesos llaman
 * a un metodo en la misma instancia de objeto y este metodo actualiza las variables miembro del objeto, el metodo no es seguro
 * para subprocesos. A continuacion se muestra un ejemplo de un metodo que no es seguro para subprocesos:
 * <pre>{@code
 * public class NotThreadSafe {
 *
 *     StringBuilder builder = new StringBuilder();
 *
 *     public add(String text) {
 *         this.builder.append(text);
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Si dos subprocesos llaman al metodo {@code add()} simultaneamente <b>en la misma instancia de NotThreadSafe</b>, se generan
 * condiciones de carrera. Por ejemplo:
 * <pre>{@code
 * ThreadSafetyAndSharedResources sharedInstance = new ThreadSafetyAndSharedResources();
 *
 * new Thread(new MyRunnable(sharedInstance)).start();
 * new Thread(new MyRunnable(sharedInstance)).start();
 *
 * public class MyRunnable implements Runnable {
 *     ThreadSafetyAndSharedResources instance = null;
 *
 *     public MyRunnable(ThreadSafetyAndSharedResources instance) {
 *         this.instance = instance;
 *     }
 *
 *     public void run() {
 *         this.instance.add("some text");
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe como las dos instancias {@code MyRunnable} comparten la misma instancia {@code NotThreadSafe}. Por lo tanto, cuando
 * llaman al metodo add() en la instancia de NotThreadSafe, se genera una condicion de carrera.
 * <p>
 * Sin embargo, si dos subprocesos llaman al metodo add() simultaneamente <b>en diferentes instancias</b>, no se produce una
 * condicion de carrera. Aqui esta el ejemplo anterior, pero ligeramente modificado:
 * <pre>{@code
 * new Thread(new MyRunnable(new NotThreadSafe())).start();
 * new Thread(new MyRunnable(new NotThreadSafe())).start();
 * }</pre>
 * <p>
 * Ahora los dos subprocesos tienen cada uno su propia instancia de NotThreadSafe, por lo que sus llamadas al metodo add no
 * interfieren entre si. El codigo ya no tiene condicion de carrera. Por lo tanto, incluso si un objeto no es seguro para
 * subprocesos, aun se puede usar de una manera que no conduzca a una condicion de carrera.
 * <h3>La regla de escape del control de subprocesos</h3>
 * <p>
 * Al intentar determinar si el acceso de su codigo a un determinado recurso es seguro para subprocesos, puede utilizar la regla
 * de escape de control de subprocesos:
 * <pre>{@code
 * Si un recurso se crea, utiliza y elimina dentro de
 * el control del mismo hilo,
 * y nunca escapa al control de este hilo,
 * el uso de ese recurso es seguro para subprocesos.
 * }</pre>
 * <p>
 * Los recursos pueden ser cualquier recurso compartido como un objeto, matriz, archivo, conexion de base de datos, socket, etc.
 * En Java no siempre se eliminan explicitamente los objetos, por lo que "eliminar" significa perder o anular la referencia al
 * objeto.
 * <p>
 * Incluso si el uso de un objeto es seguro para subprocesos, si ese objeto apunta a un recurso compartido como un archivo o una
 * base de datos, es posible que su aplicacion en su conjunto no sea segura para subprocesos. Por ejemplo, si el subproceso 1 y el
 * subproceso 2 crean cada uno sus propias conexiones de base de datos, la conexion 1 y la conexion 2, el uso de cada conexion en
 * si es seguro para subprocesos. Pero el uso de la base de datos a la que apuntan las conexiones puede no ser seguro para
 * subprocesos. Por ejemplo, si ambos subprocesos ejecutan codigo como este:
 * <pre>{@code
 * comprobar si el registro X existe
 * si no, inserte el registro X
 * }</pre>
 * <p>
 * Si dos subprocesos ejecutan esto simultaneamente y el registro X que estan verificando resulta ser el mismo registro, existe el
 * riesgo de que ambos subprocesos terminen insertandolo. Asi es como:
 * <pre>{@code
 * Thread 1 comprueba si existe el registro X. Resultado = no
 * Thread 2 comprueba si existe el registro X. Resultado = no
 * Thread 1 inserta el registro X
 * Thread 2 inserta el registro X
 * }</pre>
 * <p>
 * Esto tambien podria suceder con subprocesos que operan en archivos u otros recursos compartidos. Por lo tanto, es importante
 * distinguir entre si un objeto controlado por un hilo <b>es</b> el recurso o si simplemente hace <b>referencia</b> al recurso
 * (como lo hace una conexion de base de datos).
 * <p>
 * Fuente: <a href="https://jenkov.com/tutorials/java-concurrency/thread-safety.html">Thread Safety and Shared Resources</a>
 */

public class ThreadSafetyAndSharedResources {

    // Variable miembro del objeto que se almacenan en el heap junto con el objeto
    private final StringBuilder builder = new StringBuilder();

    public static void main(String[] args) throws InterruptedException {

        ThreadSafetyAndSharedResources sharedInstance = new ThreadSafetyAndSharedResources();

        new Thread(new MyRunnable(sharedInstance)).start();
        new Thread(new MyRunnable(sharedInstance)).start();

        // new Thread(new MyRunnable(new ThreadSafetyAndSharedResources())).start();
        // new Thread(new MyRunnable(new ThreadSafetyAndSharedResources())).start();

        // Espera hasta que todos los hilos terminen
        Thread.sleep(10);
        System.out.println(sharedInstance.builder);

    }

    private void add(String text) {
        builder.append(text);
    }

    public void someMethod() {
        // Variable local almacenada en el stack segura para subprocesos
        int threadSafeInt = 0;
        threadSafeInt++;
    }

    public void someMethod2() {
        // La referencia del objeto se alamacena en el stack pero el objeto en el heap
        LocalObject localObject = new LocalObject();
        localObject.callMethod();
        method2(localObject);
    }

    public void method2(LocalObject localObject) {
        localObject.setValue("value");
    }

    /**
     * El uso de la clase interna estatica es apropiado aqui porque {@code MyRunnable} no necestia acceder a ningun miembro no
     * estatico de la clase externa {@code ThreadSafetyAndSharedResources}.
     * <p>
     * Una clase interna estatica no mantiene una referencia implicita a la instancia de la clase externa, lo que es mas eficiente
     * en terminos de memoria.
     */
    private static class MyRunnable implements Runnable {

        ThreadSafetyAndSharedResources instance;

        public MyRunnable(ThreadSafetyAndSharedResources instance) {
            this.instance = instance;
        }

        @Override
        public void run() {
            instance.add("text");
        }

    }

    private static class LocalObject {

        String value;

        public void callMethod() {
        }

        public void setValue(String value) {
            this.value = value;
        }

    }

}
