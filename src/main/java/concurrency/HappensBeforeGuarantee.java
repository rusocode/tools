package concurrency;

/**
 * <p>
 * <i>Java happens before guarantee</i> es un conjunto de reglas que rigen como la JVM y la CPU pueden reordenar las instrucciones
 * para mejorar el rendimiento. La happens before guarantee (sucede antes de la garantia) hace posible que los subprocesos dependan
 * de cuando se sincroniza un valor de variable hacia o desde la memoria principal, y que otras variables se han sincronizado al
 * mismo tiempo. La happens before guarantee se centra en el acceso a variables volatiles y a variables a las que se accede desde
 * bloques sincronizados.
 * <h3>Reordenamiento de instrucciones</h3>
 * <p>
 * Las CPU modernas tienen la capacidad de ejecutar instrucciones en paralelo si las instrucciones no dependen unas de otras. Por
 * ejemplo, las dos instrucciones siguientes no dependen entre si y, por lo tanto, pueden ejecutarse en paralelo:
 * <pre>{@code
 * a = b + c
 * d = e + f
 * }</pre>
 * <p>
 * Sin embargo, las dos instrucciones siguientes no se pueden ejecutar facilmente en paralelo, porque la segunda instruccion
 * depende del resultado de la primera:
 * <pre>{@code
 * a = b + c
 * d = a + e
 * }</pre>
 * Imagine que las dos instrucciones anteriores fueran parte de un conjunto mas grande de instrucciones, como el siguiente:
 * <pre>{@code
 * a = b + c
 * d = a + e
 *
 * l = m + n
 * y = x + z
 * }</pre>
 * <p>
 * Las instrucciones se pueden reordenar como se muestra a continuacion. Luego, la CPU puede ejecutar al menos las primeras 3
 * instrucciones en paralelo y, tan pronto como finalicen las primeras instrucciones, puede comenzar a ejecutar la cuarta
 * instruccion.
 * <pre>{@code
 * a = b + c
 *
 * l = m + n
 * y = x + z
 *
 * d = a + e
 * }</pre>
 * <p>
 * Como puede ver, reordenar las instrucciones puede aumentar la ejecucion paralela de instrucciones en la CPU. Una mayor
 * paralelizacion significa un mayor rendimiento.
 * <p>
 * Se permite la reordenacion de instrucciones para la JVM y la CPU siempre que la semantica del programa no cambie. El resultado
 * final debe ser el mismo que si las instrucciones se ejecutaran en el orden exacto en que aparecen en el codigo fuente.
 * <h3>Problemas de reordenamiento de instrucciones en computadoras con multiples CPU</h3>
 * <p>
 * El reordenamiento de instrucciones en sistemas con multiples CPU y subprocesos puede causar problemas en la sincronizacion y
 * comunicacion entre hilos. El siguiente ejemplo presenta dos hilos: uno que produce frames y otro que los dibuja, utilizando una
 * clase FrameExchanger para comunicarse.
 * <pre>{@code
 * public class FrameExchanger  {
 *
 *     private long framesStoredCount;
 *     private long framesTakenCount;
 *
 *     private boolean hasNewFrame;
 *
 *     private Frame frame;
 *
 *     // Llamado por el frame producing thread
 *     public void storeFrame(Frame frame) {
 *         this.frame = frame;
 *         this.framesStoredCount++;
 *         this.hasNewFrame = true;
 *     }
 *
 *     // Llamado por el frame drawing thread
 *     public Frame takeFrame() {
 *         while(!hasNewFrame) {
 *             // Espera hasta que llegue el nuevo frame
 *         }
 *
 *         Frame newFrame = this.frame;
 *         this.framesTakenCount++;
 *         this.hasNewFrame = false;
 *         return newFrame;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * El problema surge cuando las instrucciones dentro del metodo {@code storeFrame()} se reordenan. Si {@code hasNewFrame} se
 * establece como true antes de que el nuevo frame se asigne, el hilo de dibujo podria tomar y dibujar un frame antiguo. Aunque en
 * este caso especifico solo resultaria en un desperdicio de recursos, en otras situaciones, tal reordenamiento podria causar mal
 * funcionamiento de la aplicacion.
 * <pre>{@code
 * public void storeFrame(Frame frame) {
 *     this.hasNewFrame = true;
 *     this.framesStoredCount++;
 *     this.frame = frame;
 * }
 * }</pre>
 * <p>
 * Este ejemplo ilustra como la reordenacion de instrucciones, que puede ocurrir a nivel de JVM o CPU para optimizacion, puede
 * llevar a comportamientos inesperados en programas multihilo, destacando la importancia de considerar estos aspectos en el diseño
 * de sistemas concurrentes.
 * <h3>La garantia de visibilidad volatil de Java</h3>
 * <p>
 * La palabra clave {@code volatile} proporciona algunas garantias de visibilidad cuando las escrituras y lecturas de variables
 * volatiles dan como resultado la sincronizacion del valor de la variable hacia y desde la memoria principal. Esta sincronizacion
 * hacia y desde la memoria principal es lo que hace que el valor sea <i>visible</i> para otros subprocesos. De ahi el termino
 * <i>garantia de visibilidad</i>.
 * <p>
 * En esta seccion cubrire brevemente la garantia de visibilidad volatil y explicare como el reordenamiento de instrucciones
 * puede romper la garantia de visibilidad volatil. Es por eso que tambien tenemos la volatil de <i>happens before guarantee</i>,
 * para imponer algunas restricciones en el reordenamiento de instrucciones para que la garantia de visibilidad volatil no se rompa
 * con el reordenamiento de instrucciones.
 * <h4>La garantia de visibilidad de escritura volatil de Java</h4>
 * <p>
 * La garantia de visibilidad de escritura volatil en Java asegura que:
 * <ol>
 * <li>Al escribir en una variable volatil, el valor se guarda directamente en la memoria principal.
 * <li>Todas las variables visibles para el hilo que escribe en la variable volatil tambien se sincronizan con la memoria principal.
 * </ol>
 * En el ejemplo dado:
 * <pre>{@code
 * nonVolatileVarA = 34;
 * nonVolatileVarB = new String("Text");
 * volatileVarC    = 300;
 * }</pre>
 * <p>
 * Cuando se escribe en {@code volatileVarC} (declarada como volatil), no solo su valor se guarda en la memoria principal, sino que
 * tambien los valores de nonVolatileVarA y nonVolatileVarB se sincronizan con la memoria principal. Esto garantiza que todos los
 * cambios realizados antes de la escritura volatil sean visibles para otros hilos que lean la variable volatil posteriormente.
 * <h4>La garantia de visibilidad de lectura volatil de Java</h4>
 * <p>
 * Cuando lee el valor de un volatil, se garantiza que el valor se leera directamente desde la memoria. Ademas, todas las variables
 * visibles para el hilo que lee la variable volatil tambien tendran sus valores actualizados desde la memoria principal.
 * <p>
 * Para ilustrar la garantia de visibilidad de lectura volatil, mire este ejemplo:
 * <pre>{@code
 * c = volatileVarC;
 * b = nonVolatileB;
 * a = nonVolatileA;
 * }</pre>
 * <p>
 * Observe que la primera instruccion es una lectura de una variable volatil (volatileVarC). Cuando se lee volatileVarC desde la
 * memoria principal, nonVolatileB y nonVolatileA tambien se leen desde la memoria principal.
 * <h3>La volatilidad de Java happens before guarantee</h3>
 * <p>
 * La happens before guarantee con variables volatiles establece restricciones en el reordenamiento de instrucciones alrededor de
 * estas variables. En el ejemplo modificado de FrameExchanger, donde hasNewFrame se declara volatil:
 * <ol>
 * <li>Cuando hasNewFrame se establece como true, {@code frame} y {@code framesStoredCount} se sincronizan con la memoria principal.
 * <li>Cada lectura de hasNewFrame en {@code takeFrame()} actualiza frame, framesStoredCount y {@code framesTakenCount} desde la
 * memoria principal.
 * </ol>
 * Sin embargo, si las instrucciones en storeFrame() se reordenaran, poniendo la asignacion de hasNewFrame primero, podria ocurrir
 * que:
 * <ol>
 * <li>Ahora los campos framesStoredCount y frame se sincronizaran con la memoria principal cuando se ejecute la primera
 * instruccion (porque hasNewFrame es volatil), ¡lo cual es <i>antes</i> de que se les asignen sus nuevos valores!
 * <li>El hilo de dibujo podria salir del bucle while antes de que frame tenga su nuevo valor asignado.
 * <li>No habria garantia de que el nuevo valor de frame sea visible para el hilo de dibujo.
 * </ol>
 * <p>
 * Este escenario ilustra la importancia de happens before guarantee para prevenir problemas de sincronizacion en programacion
 * concurrente con variables volatiles.
 * <h4>Happens Before Guarantee para escrituras de variables volatiles</h4>
 * <p>
 * Como puede ver, reordenar las instrucciones dentro del metodo storeFrame() puede provocar que la aplicacion no funcione
 * correctamente. Aqui es donde ocurre la escritura volatil antes de que entre en vigencia la garantia, para imponer restricciones
 * sobre que tipo de reordenamiento de instrucciones se permite en torno a las escrituras en variables volatiles:
 * <p>
 * Se garantiza que una escritura en una variable volatil o no volatil que ocurre antes de una escritura en una variable volatil
 * ocurrira antes de la escritura en esa variable volatil.
 * <p>
 * En el caso del metodo storeFrame(), eso significa que las dos primeras instrucciones de escritura no se pueden reordenar para
 * que sucedan despues de la ultima escritura en hasNewFrame, ya que hasNewFrame es una variable volatil.
 * <pre>{@code
 * public void storeFrame(Frame frame) {
 *     this.frame = frame;
 *     this.framesStoredCount++;
 *     this.hasNewFrame = true; // hasNewFrame es volatil
 * }
 * }</pre>
 * <p>
 * Las dos primeras instrucciones no escriben en variables volatiles, por lo que la JVM puede reordenarlas libremente. Asi, se
 * permite esta reordenacion:
 * <pre>{@code
 * public void storeFrame(Frame frame) {
 *     this.framesStoredCount++;
 *     this.frame = frame;
 *     this.hasNewFrame = true
 * }
 * }</pre>
 * <p>
 * Este reordenamiento no rompe el codigo en el metodo takeFrame(), ya que la variable frame todavia se escribe antes de escribir
 * la variable hasNewFrame. El programa total todavia funciona segun lo previsto.
 * <h4>Happens Before Guarantee para lecturas de variables volatiles</h4>
 * <p>
 * Las variables volatiles en Java tienen una garantia similar antes de las lecturas de variables volatiles. Solo que la direccion
 * es opuesta:
 * <p>
 * Se producira una lectura de una variable volatil antes de cualquier lectura posterior de variables volatiles y no volatiles.
 * <p>
 * Cuando digo que la direccion es diferente a la de las escrituras, quiero decir que para las escrituras volatiles, todas las
 * instrucciones <i>antes</i> a la escritura permaneceran antes de la escritura volatil. Para lecturas volatiles, todas las
 * lecturas <i>despues</i> a la lectura volatil permaneceran despues de la lectura volatil.
 * <p>
 * Mira el siguiente ejemplo:
 * <pre>{@code
 * int a = this.volatileVarA;
 * int b = this.nonVolatileVarB;
 * int c = this.nonVolatileVarC;
 * }</pre>
 * <p>
 * Tanto la instruccion 2 como la 3 deben permanecer despues de la primera instruccion, porque la primera instruccion lee una
 * variable volatil. En otras palabras, se garantiza que la lectura de la variable volatil se realizara antes de las dos lecturas
 * posteriores de las variables no volatiles.
 * <p>
 * Las dos ultimas instrucciones podrian reordenarse libremente entre si, sin violar la garantia de ocurre antes del volatil leido
 * en la primera instruccion. Asi, se permite esta reordenacion:
 * <pre>{@code
 * int a = this.volatileVarA;
 * int c = this.nonVolatileVarC;
 * int b = this.nonVolatileVarB;
 * }</pre>
 * <p>
 * Debido a la garantia de visibilidad de lectura volatil, cuando volatileVarA se lee desde la memoria principal, todas las demas
 * variables tambien son visibles para el hilo en ese momento. Por lo tanto, nonVolatileVarB y nonVolatileVarC tambien se leen
 * desde la memoria principal al mismo tiempo. Esto significa que el hilo que lee volatileVarA puede confiar en nonVolatileVarB y
 * nonVolatileVarC para estar actualizado tambien con la memoria principal.
 * <p>
 * Si alguna de las dos ultimas instrucciones se reordenara por encima de la primera instruccion de lectura volatil, la garantia
 * de esa instruccion en el momento de su ejecucion no se mantendria. Es por eso que las lecturas posteriores no se pueden reordenar
 * para que aparezcan encima de una lectura de una variable volatil.
 * <p>
 * Con respecto al metodo takeFrame(), la primera lectura de una variable volatil es la lectura del campo hasNewFrame dentro del
 * bucle while. Eso significa que no se pueden reordenar instrucciones de lectura para que se ubiquen por encima de eso. En este
 * caso particular, mover cualquiera de las otras operaciones de lectura por encima del bucle while tambien romperia la semantica
 * del codigo, por lo que esos reordenamientos no se permitirian de todos modos.
 * <h3>La garantia de visibilidad sincronizada de Java</h3>
 * <p>
 * Los bloques {@code synchronized} proporcionan garantias de visibilidad similares a las de las variables volatiles.
 * <h4>Garantia de visibilidad de entrada sincronizada de Java</h4>
 * <p>
 * Cuando un hilo ingresa a un bloque sincronizado, todas las variables visibles para el hilo se actualizan desde la memoria
 * principal.
 * <h4>Garantia de visibilidad de salida sincronizada de Java</h4>
 * <p>
 * Cuando un hilo sale de un bloque sincronizado, todas las variables visibles para el hilo se vuelven a escribir en la memoria
 * principal.
 * <h4>Ejemplo de visibilidad sincronizada de Java</h4>
 * <pre>{@code
 * public class ValueExchanger {
 *
 *     private int valA;
 *     private int valB;
 *     private int valC;
 *
 *     public void set(Values v) {
 *         this.valA = v.valA;
 *         this.valB = v.valB;
 *
 *         synchronized(this) {
 *             this.valC = v.valC;
 *         }
 *     }
 *
 *     public void get(Values v) {
 *         synchronized(this) {
 *             v.valC = this.valC;
 *         }
 *         v.valB = this.valB;
 *         v.valA = this.valA;
 *     }
 *
 * }
 * }</pre>
 * <p>
 * Observe los dos bloques sincronizados dentro del metodo {@code set()} y {@code get()}. Observe como los bloques se colocan al
 * final y al primero en los dos metodos.
 * <p>
 * En el metodo set(), el bloque sincronizado al final del metodo forzara que todas las variables se sincronicen con la memoria
 * principal despues de actualizarse. Este vaciado de los valores de las variables a la memoria principal ocurre cuando el hilo
 * sale del bloque sincronizado. Esa es la razon por la que se ha colocado en ultimo lugar en el metodo: para garantizar que todos
 * los valores de las variables actualizados se vacien a la memoria principal.
 * <p>
 * En el metodo get() el bloque sincronizado se coloca al principio del metodo. Cuando el hilo que llama a get() ingresa al bloque
 * sincronizado, todas las variables se vuelven a leer desde la memoria principal. Es por eso que este bloque sincronizado se
 * coloca al comienzo del metodo, para garantizar que todas las variables se actualicen desde la memoria principal antes de leerlas.
 * <h3>La sincronizacion de Java ocurre antes de la garantia</h3>
 * <p>
 * Los bloques sincronizados de Java proporcionan dos garantias de que sucede antes: una garantia relacionada con el comienzo de
 * un bloque sincronizado y otra garantia relacionada con el final de un bloque sincronizado.
 * <h4>El inicio del bloque sincronizado de Java ocurre antes de la garantia</h4>
 * <p>
 * El comienzo de un bloque sincronizado de Java proporciona la garantia de visibilidad, de que cuando un hilo ingresa a un bloque
 * sincronizado, todas las variables visibles para el hilo se leeran (actualizaran desde) la memoria principal.
 * <p>
 * Para poder mantener esa garantia, es necesario un conjunto de restricciones al reordenamiento de instrucciones. Para ilustrar
 * por que, usare el metodo get() del ValueExchanger que se mostro anteriormente:
 * <pre>{@code
 *     public void get(Values v) {
 *         synchronized(this) {
 *             v.valC = this.valC;
 *         }
 *         v.valB = this.valB;
 *         v.valA = this.valA;
 *     }
 * }</pre>
 * Como puede ver, el bloque sincronizado al comienzo del metodo garantizara que todas las variables valC, valB y valA se
 * actualicen (lean) desde la memoria principal. Las siguientes lecturas de estas variables utilizaran el ultimo valor.
 * <p>
 * Para que esto funcione, ninguna de las lecturas de las variables se puede reordenar para que aparezca antes del comienzo del
 * bloque sincronizado. Si se reordenara la lectura de una variable para que apareciera antes del comienzo del bloque sincronizado,
 * se perderia la garantia de que los valores de las variables se actualicen desde la memoria principal. Este seria el caso en el
 * caso de la siguiente reordenacion no permitida de las instrucciones:
 * <pre>{@code
 *     public void get(Values v) {
 *         v.valB = this.valB;
 *         v.valA = this.valA;
 *         synchronized(this) {
 *             v.valC = this.valC;
 *         }
 *     }
 * }</pre>
 * <h4>El final del bloque sincronizado con Java ocurre antes de la garantia</h4>
 * <p>
 * El final de un bloque sincronizado proporciona la garantia de visibilidad de que todas las variables modificadas se volveran a
 * escribir en la memoria principal cuando el hilo salga del bloque sincronizado.
 * <p>
 * Para poder mantener esa garantia, es necesario un conjunto de restricciones al reordenamiento de instrucciones. Para ilustrar
 * por que, usare el metodo set() del ValueExchanger mostrado anteriormente:
 * <pre>{@code
 *     public void set(Values v) {
 *         this.valA = v.valA;
 *         this.valB = v.valB;
 *
 *         synchronized(this) {
 *             this.valC = v.valC;
 *         }
 *     }
 * }</pre>
 * <p>
 * Como puede ver, el bloque sincronizado al final del metodo garantizara que todas las variables cambiadas valA, valB y valC se
 * volveran a escribir (vaciar) en la memoria principal cuando el hilo llame a set( ) sale de los bloques sincronizados.
 * <p>
 * Para que esto funcione, ninguna de las escrituras en las variables se puede reordenar para que aparezca despues del final del
 * bloque sincronizado. Si las escrituras en las variables se reordenaran para que aparecieran despues del final del bloque
 * sincronizado, perderia la garantia de que los valores de las variables se vuelvan a escribir en la memoria principal. este
 * seria el caso en la siguiente reordenacion no permitida de las instrucciones:
 * <pre>{@code
 *     public void set(Values v) {
 *         synchronized(this) {
 *             this.valC = v.valC;
 *         }
 *         this.valA = v.valA;
 *         this.valB = v.valB;
 *     }
 * }</pre>
 * <p>
 * Links:
 * <a href="https://jenkov.com/tutorials/java-concurrency/java-happens-before-guarantee.html">Java Happens Before Guarantee</a>
 */

public class HappensBeforeGuarantee {


}
