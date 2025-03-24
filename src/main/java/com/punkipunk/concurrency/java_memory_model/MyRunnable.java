package com.punkipunk.concurrency.java_memory_model;

/**
 * <h3>Java Memory Model</h3>
 * <p>
 * El modelo de memoria Java (JMM) especifica como funciona la maquina virtual Java con la memoria de la computadora (RAM). La
 * maquina virtual Java es un modelo de una computadora completa, por lo que este modelo incluye naturalmente un modelo de
 * memoria, tambien conocido como modelo de memoria Java.
 * <p>
 * Es muy importante comprender el modelo de memoria de Java si desea diseñar programas concurrentes que se comporten
 * correctamente. El modelo de memoria Java especifica como y cuando diferentes subprocesos pueden ver los valores escritos en
 * variables compartidas por otros subprocesos y como sincronizar el acceso a variables compartidas cuando sea necesario.
 * <p>
 * El modelo de memoria Java original era insuficiente, por lo que se reviso en Java 1.5. Esta version del modelo de memoria Java
 * todavia se utiliza en Java hoy (Java 14+).
 * <h3>El modelo de memoria interna de Java</h3>
 * <p>
 * El modelo de memoria Java utilizado internamente en la JVM divide la memoria entre thread stacks (pilas de subprocesos) y el
 * heap (monton). Este diagrama ilustra el modelo de memoria Java desde una perspectiva logica:
 * <p>
 * <img src="jmm.PNG">
 * <p>
 * Cada subproceso que se ejecuta en la maquina virtual Java tiene su propia pila de subprocesos. La pila de subprocesos contiene
 * informacion sobre que metodos ha llamado el subproceso para llegar al punto de ejecucion actual. Me referire a esto como "call
 * stack (pila de llamada)". A medida que el hilo ejecuta su codigo, la pila de llamadas cambia.
 * <p>
 * La pila de subprocesos tambien contiene todas las variables locales para cada metodo que se ejecuta (todos los metodos en la
 * pila de llamadas). Un hilo solo puede acceder a su propia pila de hilos. Las variables locales creadas por un hilo son
 * invisibles para todos los demas hilos excepto el hilo que las creo. Incluso si dos subprocesos ejecutan exactamente el mismo
 * codigo, los dos subprocesos seguiran creando las variables locales de ese codigo en cada uno de su propia pila de subprocesos.
 * Asi, cada hilo tiene su propia version de cada variable local.
 * <p>
 * Todas las variables locales de tipos primitivos ({@code boolean, byte, short, char, int, long, float, double}) estan
 * completamente almacenadas en la pila de subprocesos y, por lo tanto, no son visibles para otros subprocesos. Un hilo puede
 * pasar una copia de una variable primitiva a otro hilo, pero no puede compartir la variable local primitiva en si.
 * <p>
 * El heap contiene todos los objetos creados en su aplicacion Java, independientemente del hilo que creo el objeto. Esto incluye
 * las versiones de objetos de los tipos primitivos (por ejemplo, {@code Byte, Integer, Long}, etc.). No importa si un objeto se
 * creo y se asigno a una variable local, o se creo como una variable miembro de otro objeto, el objeto aún se almacena en el
 * heap.
 * <p>
 * A continuacion se muestra un diagrama que ilustra la pila de llamadas y las variables locales almacenadas en las pilas de
 * subprocesos y los objetos almacenados en el heap:
 * <p>
 * <img src="call stack y variables locales almacenadas en el thread stack y objetos en el heap.PNG">
 * <p>
 * Una variable local puede ser de tipo primitivo, en cuyo caso se mantiene totalmente en la pila de subprocesos.
 * <p>
 * Una variable local tambien puede ser una referencia a un objeto. En ese caso, la referencia (la variable local) se almacena en
 * la pila de subprocesos, pero el objeto en si se almacena en el heap.
 * <p>
 * Un objeto puede contener metodos y estos metodos pueden contener variables locales. Estas variables locales tambien se
 * almacenan en la pila de subprocesos, incluso si el objeto al que pertenece el metodo esta almacenado en el heap.
 * <p>
 * Las variables miembro de un objeto se almacenan en el heap junto con el objeto mismo. Esto es cierto tanto cuando la variable
 * miembro es de tipo primitivo como si es una referencia a un objeto.
 * <p>
 * Las variables de clase estaticas tambien se almacenan en el heap junto con la definicion de clase.
 * <p>
 * Todos los subprocesos que tienen una referencia al objeto pueden acceder a los objetos del heap. Cuando un hilo tiene acceso a
 * un objeto, tambien puede obtener acceso a las variables miembro de ese objeto. Si dos subprocesos llaman a un metodo en el
 * mismo objeto al mismo tiempo, ambos tendran acceso a las variables miembro del objeto, pero cada subproceso tendra su propia
 * copia de las variables locales.
 * <p>
 * Aqui hay un diagrama que ilustra los puntos anteriores:
 * <p>
 * <img src="diagrama.PNG">
 * <p>
 * Dos subprocesos tienen un conjunto de variables locales. Una de las variables locales ({@code Local variable 2}) apunta a un
 * objeto compartido en el heap (Objeto 3). Cada uno de los dos hilos tiene una referencia diferente al mismo objeto. Sus
 * referencias son variables locales y, por lo tanto, se almacenan en la pila de subprocesos de cada subproceso (en cada uno). Sin
 * embargo, las dos referencias diferentes apuntan al mismo objeto en el heap.
 * <p>
 * Observe como el objeto compartido (Objeto 3) tiene una referencia al Objeto 2 y al Objeto 4 como variables miembro (ilustradas
 * por las flechas del Objeto 3 al Objeto 2 y al Objeto 4). A traves de estas referencias de variables miembro en el Objeto 3, los
 * dos subprocesos pueden acceder al Objeto 2 y al Objeto 4.
 * <p>
 * El diagrama tambien muestra una variable local que apunta a dos objetos diferentes en el heap. En este caso las referencias
 * apuntan a dos objetos diferentes (Objeto 1 y Objeto 5), no al mismo objeto. En teoria, ambos subprocesos podrian acceder tanto
 * al Objeto 1 como al Objeto 5, si ambos subprocesos tuvieran referencias a ambos objetos. Pero en el diagrama de arriba cada
 * hilo solo tiene una referencia a uno de los dos objetos.
 * <p>
 * Entonces, ¿que tipo de codigo Java podria conducir al grafico de memoria anterior? Bueno, codigo tan simple como el siguiente:
 * <pre>{@code
 * public class MyRunnable implements Runnable() {
 *
 *     public void run() {
 *         methodOne();
 *     }
 *
 *     public void methodOne() {
 *         int localVariable1 = 45;
 *
 *         MySharedObject localVariable2 = MySharedObject.sharedInstance;
 *
 *         // Hacer mas con variables locales...
 *
 *         methodTwo();
 *     }
 *
 *     public void methodTwo() {
 *         Integer localVariable1 = new Integer(99);
 *
 *         // Hacer mas con variables locales...
 *     }
 *
 * }
 * }</pre>
 * <pre>{@code
 * public class MySharedObject {
 *
 *     // Variable estatica que apunta a la instancia de MySharedObject
 *     public static final MySharedObject sharedInstance =
 *         new MySharedObject();
 *
 *     // Variables miembro que apuntan a dos objetos en el heap
 *     public Integer object2 = new Integer(22);
 *     public Integer object4 = new Integer(44);
 *
 *     public long member1 = 12345;
 *     public long member2 = 67890;
 *
 * }
 * }</pre>
 * <p>
 * Si dos subprocesos estuvieran ejecutando el metodo {@code run()}, el resultado seria el diagrama mostrado anteriormente. El
 * metodo run() llama al {@code methodOne()} y el methodOne() llama al {@code methodTwo()}.
 * <p>
 * methodOne() declara una variable local primitiva ({@code localVariable1} de tipo {@code int}) y una variable local que es una
 * referencia de objeto ({@code localVariable2}).
 * <p>
 * Cada subproceso que ejecute methodOne() creara su propia copia de localVariable1 y localVariable2 en sus respectivas pilas de
 * subprocesos. Las variables localVariable1 estaran completamente separadas entre si y solo viviran en la pila de subprocesos de
 * cada subproceso. Un hilo no puede ver que cambios realiza otro hilo en su copia de localVariable1.
 * <p>
 * Cada hilo que ejecute methodOne() tambien creara su propia copia de localVariable2. Sin embargo, las dos copias diferentes de
 * localVariable2 terminan apuntando al mismo objeto en el heap. El codigo configura localVariable2 para que apunte a un objeto al
 * que hace referencia una variable estatica. Solo hay una copia de una variable estatica y esta copia se almacena en el heap. Por
 * lo tanto, las dos copias de localVariable2 terminan apuntando a la misma instancia de {@code MySharedObject} a la que apunta la
 * variable estatica. La instancia de MySharedObject tambien se almacena en el heap. Corresponde al Objeto 3 en el diagrama de
 * arriba.
 * <p>
 * Observe como la clase MySharedObject tambien contiene dos variables miembro. Las propias variables miembro se almacenan en el
 * heap junto con el objeto. Las dos variables miembro apuntan a otros dos objetos {@code Integer}. Estos objetos enteros
 * corresponden al Objeto 2 y al Objeto 4 en el diagrama anterior.
 * <p>
 * Observe tambien como metodoTwo() crea una variable local llamada localVariable1. Esta variable local es una referencia de
 * objeto a un objeto Integer. El metodo establece la referencia localVariable1 para que apunte a una nueva instancia de Integer.
 * La referencia localVariable1 se almacenara en una copia por hilo que ejecute el methodTwo(). Los dos objetos Integer
 * instanciados se almacenaran en el heap, pero dado que el metodo crea un nuevo objeto Integer cada vez que se ejecuta el metodo,
 * dos subprocesos que ejecuten este metodo crearan instancias de Integer separadas. Los objetos Integer creados dentro de
 * metodoTwo() corresponden al Objeto 1 y al Objeto 5 en el diagrama anterior.
 * <p>
 * Observe tambien las dos variables miembro en la clase MySharedObject de tipo long, que es un tipo primitivo. Dado que estas
 * variables son variables miembro, todavia se almacenan en el heap junto con el objeto. Solo las variables locales se almacenan
 * en la pila de subprocesos.
 * <h3>Arquitectura de memoria de hardware</h3>
 * <p>
 * <img src="arquitectura de memoria de hardware.PNG">
 * <p>
 * La arquitectura de memoria de hardware moderna difiere del modelo de memoria interna de Java y es crucial entenderla para
 * comprender como funciona el modelo de memoria Java. Los puntos clave son:
 * <ol>
 * <li>Múltiples CPUs: Las computadoras modernas tienen 2 o mas CPUs, permitiendo la ejecucion simultanea de múltiples hilos.
 * <li>Registros de CPU: Cada CPU tiene registros internos de acceso rapido.
 * <li>Cache de CPU: Las CPUs modernas tienen una o mas capas de memoria cache, mas rapida que la memoria principal pero mas
 * lenta que los registros.
 * <li>Memoria principal (RAM): Accesible por todas las CPUs, es mas grande pero mas lenta que la cache.
 * <li>Proceso de acceso a memoria:
 * <ul>
 * <li>La CPU lee datos de la memoria principal a su cache.
 * <li>Luego, puede cargar datos del cache a sus registros para operar.
 * <li>Al escribir, el proceso se invierte: registros -> cache -> memoria principal.
 * </ul>
 * <li>Actualizacion de cache: Se realiza por "lineas de cache", no necesariamente toda la cache a la vez.
 * </ol>
 * <h3>Cerrando la brecha entre el modelo de memoria Java y la arquitectura de memoria de hardware</h3>
 * <p>
 * El modelo de memoria de Java distingue entre pilas de subprocesos y heap, pero el hardware no hace esta distincion. En la
 * arquitectura fisica, ambos se almacenan en la memoria principal, con partes potencialmente presentes en las caches y registros
 * de la CPU. Esta diferencia entre el modelo logico de Java y la implementacion real del hardware es fundamental para entender el
 * funcionamiento de la memoria en aplicaciones Java. Esto se ilustra en este diagrama:
 * <p>
 * <img src="diferencia entre la arquitectura de jmm y la memoria del hardware.PNG">
 * <p>
 * Cuando los objetos y variables se pueden almacenar en varias areas de memoria diferentes de la computadora, pueden ocurrir
 * ciertos problemas. Los dos problemas principales son:
 * <ul>
 * <li>Visibilidad de las actualizaciones de subprocesos (escrituras) en variables compartidas.
 * <li>Condiciones de carrera al leer, verificar y escribir variables compartidas.
 * </ul>
 * <h4>Visibilidad de objetos compartidos</h4>
 * <p>
 * En sistemas multihilo que comparten objetos, sin el uso adecuado de declaraciones volatiles o sincronizacion, las
 * actualizaciones realizadas por un hilo pueden no ser visibles para otros. Esto ocurre porque cada CPU puede tener su propia
 * copia del objeto en su cache. Cuando un hilo modifica el objeto en la cache de su CPU, estos cambios no se reflejan
 * inmediatamente en la memoria principal. Como resultado, otros hilos ejecutandose en diferentes CPUs pueden estar trabajando con
 * versiones desactualizadas del objeto, lo que lleva a inconsistencias y potenciales errores en la ejecucion del programa.
 * <p>
 * El siguiente diagrama ilustra la situacion esbozada. Un subproceso que se ejecuta en la CPU izquierda copia el objeto
 * compartido en la cache de su CPU y cambia su variable {@code count} a 2. Este cambio no es visible para otros subprocesos que
 * se ejecutan en la CPU derecha, porque la actualizacion de count no se ha devuelto a principal memoria todavia.
 * <p>
 * <img src="visibilidad de objetos compartidos.PNG">
 * <p>
 * Para resolver este problema, puede utilizar la palabra clave <b>volatile</b>. La palabra clave volatile puede garantizar que
 * una variable determinada se lea directamente desde la memoria principal y siempre se vuelva a escribir en la memoria principal
 * cuando se actualice.
 * <h4>Condiciones de carrera</h4>
 * <p>
 * Si dos o mas subprocesos comparten un objeto y mas de un subproceso actualiza variables en ese objeto compartido, pueden
 * ocurrir condiciones de carrera.
 * <p>
 * Imagine que dos subprocesos, A y B, leen la variable {@code count} de un objeto compartido en sus respectivas caches de CPU.
 * Ambos incrementan count en 1 simultaneamente. Sin sincronizacion adecuada, cuando escriben de vuelta a la memoria principal,
 * solo se registra un incremento en lugar de dos. Asi, aunque count deberia haber aumentado en 2, solo aumenta en 1, resultando
 * en una condicion de carrera donde se pierde una actualizacion.
 * <p>
 * Este diagrama ilustra una ocurrencia del problema con las condiciones de carrera como se describe anteriormente:
 * <p>
 * <img src="condicion de carrera.PNG">
 * <p>
 * Para resolver las condiciones de carrera, Java ofrece <b>synchronized block</b>. Estos garantizan que solo un hilo a la vez
 * pueda ejecutar una seccion critica del codigo. Ademas, aseguran que todas las variables dentro del bloque se lean de la memoria
 * principal al entrar y se escriban de vuelta al salir, independientemente de si son volatiles o no. Esto proporciona
 * consistencia y visibilidad de las actualizaciones entre hilos, evitando problemas de concurrencia.
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/java-memory-model.html">Java Memory Model</a>
 * <a href="https://en.wikipedia.org/wiki/Java_memory_model">Wikipedia</a>
 * <a href="https://www.youtube.com/watch?v=Z4hMFBvCDV4&t=2s">Java Memory Model in 10 minutes</a>
 * <a href="https://www.cs.umd.edu/~pugh/java/memoryModel/jsr-133-faq.html">JSR 133 (Java Memory Model) FAQ</a>
 */

public class MyRunnable implements Runnable {

    @Override
    public void run() {
        methodOne();
    }

    private void methodOne() {
        // Variable local que se almacena en el thread stack, cada subproceso tiene una copia de esta
        int localVariable1 = 45;
        // Referencia para cada subprocesos que se almacena en el stack y el objeto compartido en el heap
        MySharedObject localVariable2 = MySharedObject.sharedInstance;
        methodTwo();
    }

    private void methodTwo() {
        // Para cada subproceso la referencia del objeto de tipo Integer se almacenada en el stack y el objeto en si en el heap
        Integer localVariable1 = 99;
    }

    public static void main(String[] args) {
        // Crea e inicia dos subprocesos usando la clase que implementa la interfaz Runnable
        new Thread(new MyRunnable(), "Thread-1").start();
        new Thread(new MyRunnable(), "Thread-2").start();
    }
}
