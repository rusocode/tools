package com.punkipunk.concurrency.cpu_cache_coherence;

/**
 * <h3>Coherencia de cache de CPU en concurrencia de Java</h3>
 * <p>
 * En la concurrencia de Java, la coherencia de cache de CPU juega un papel crucial en la gestion de variables compartidas entre
 * hilos. Cuando un hilo modifica variables en los registros de la CPU, estas se descargan eventualmente a la RAM principal,
 * pasando potencialmente por la cache de la CPU. El hardware utiliza mecanismos de coherencia de cache para asegurar que todas
 * las caches de CPU puedan ver las variables actualizadas. Es importante notar que las variables no siempre se escriben
 * inmediatamente en la RAM; pueden permanecer en la cache de la CPU hasta que sea necesario. Desde la perspectiva del codigo en
 * ejecucion, esto es transparente, ya que siempre obtiene los datos mas recientes independientemente de su ubicacion fisica.
 * Aunque este proceso de coherencia de cache tiene un ligero impacto en el rendimiento, es mas eficiente que escribir
 * constantemente en la RAM principal. Como desarrollador, no es necesario preocuparse por los detalles de este mecanismo, ya que
 * Java y el hardware se encargan de mantener la consistencia de los datos entre hilos.
 * <p>
 * A continuacion se muestra un diagrama que ilustra lo dicho anteriormente. La flecha roja discontinua a la izquierda representa
 * mi afirmacion falsa de otros tutoriales: que las variables se vaciaron del cache de la CPU a la RAM principal. La flecha de la
 * derecha representa lo que realmente sucede: que las variables se vacian de los registros de la CPU al cache de la CPU.
 * <p>
 * <img src="coherencia de cache de cpu.png">
 * <p>
 * Fuente: <a href="https://jenkov.com/tutorials/java-concurrency/cache-coherence-in-java-concurrency.html">CPU Cache Coherence in
 * Java Concurrency</a>
 */

public class CPUCacheCoherence {
}
