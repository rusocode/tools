package com.punkipunk.concurrency;

import java.lang.Thread;

/**
 * <h3>Proceso y Subproceso</h3>
 * Un <b><i>proceso</i></b> es un programa activo que proporciona los recursos necesarios para ejecutarse. Incluye un
 * espacio de direcciones virtuales, un codigo ejecutable, identificadores abiertos para los objetos del sistema, un
 * contexto de seguridad, un identificador de proceso unico, variables de entorno, una clase de prioridad, tamaños
 * minimos y maximos de conjuntos de trabajo y al menos un subproceso de ejecucion. Cada proceso se inicia con un solo
 * subproceso, denominado <i>subproceso principal</i>, en donde a partir de este, se pueden crear subprocesos
 * adicionales.
 *
 * <p>Un <b><i>subproceso</i></b> es como una CPU virtual que puede ejecutar codigo dentro de una aplicacion. Cuando se
 * inicia una aplicacion java, el metodo main() es ejecutado por el <i>subproceso principal</i>, un subproceso especial
 * creado por la JVM para ejecutar la aplicacion. Desde dentro de la aplicacion, puede crear e iniciar mas subprocesos
 * que pueden ejecutar partes del codigo de la aplicacion en paralelo con el principal. Los subprocesos de java son
 * objetos como cualquier otro, siendo instancias de la clase java.lang.Thread.
 *
 * <p>Tanto los procesos como los subprocesos son secuencias independientes de ejecucion. La diferencia tipica es que
 * los subprocesos (del mismo proceso) se ejecutan en un espacio de memoria compartido, mientras que los procesos se
 * ejecutan en espacios de memoria separados.
 *
 * <p>Los subprocesos son una funcion del entorno operativo, en lugar de una funcion de la CPU (aunque la CPU
 * normalmente tiene operaciones que hacen que los subprocesos sean eficientes).
 *
 * <br><br>
 *
 * <h3>Concurrencia</h3>
 * El subproceso multiple significa que tiene varios subprocesos de ejecucion dentro de la misma aplicacion. Por lo
 * tanto, una aplicacion multiproceso es como una aplicacion que tiene varias CPU que ejecutan diferentes partes del
 * codigo al mismo tiempo.
 *
 * <p>Sin embargo, un subproceso no es igual a una CPU. Por lo general, una sola CPU compartira su tiempo de ejecucion
 * entre varios subprocesos, cambiando entre la ejecucion de cada uno de los subprocesos durante un periodo de tiempo
 * determinado.
 *
 * <p>En principio, los subprocesos se ejecutan en paralelo y no de forma secuencial. La JVM y/o el sistema operativo
 * determina el orden en el que se ejecutan los subprocesos. Este orden no tiene que ser el mismo orden en el que se
 * iniciaron.
 *
 * <p>Los terminos <i>proceso/aplicacion/programa</i> indican lo mismo.
 *
 * <p>Los terminos <i>subproceso/thread/hilo</i> indican lo mismo.
 *
 * <p>Recursos:
 * <a href="https://www.youtube.com/watch?v=exbKr6fnoUw">Introduccion a procesos y subprocesos (muy bien explicado)</a>
 * <a href="https://jenkov.com/tutorials/java-concurrency/creating-and-starting-threads.html">Crear e iniciar subprocesos</a>
 * <a href="https://www.youtube.com/watch?v=Dhf-DYO1K78">Proceso vs Subproceso (video)</a>
 * <a href="https://stackoverflow.com/questions/200469/what-is-the-difference-between-a-process-and-a-thread">Proceso vs Subproceso (stackoverflow)</a>
 * <a href="https://www.tutorialspoint.com/difference-between-process-and-thread">Proceso vs Subproceso (tutorialspoint)</a>
 * <a href="https://www.youtube.com/watch?v=7ENFeb-J75k">Multithreading (computerphile)</a>
 *
 * @author Juan Debenedetti
 */

public class ProcessSubprocess {
}
