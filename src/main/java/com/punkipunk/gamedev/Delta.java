package com.punkipunk.gamedev;

/**
 * <h1>Delta</h1>
 * El <b><i>Delta</i></b> (Δt, dt o delta time) se refiere al intervalo de tiempo entre dos actualizaciones consecutivas
 * del motor de juego o del bucle de renderizacion. En esencia, <i>es el tiempo que ha transcurrido desde el ultimo
 * fotograma o ciclo de actualizacion hasta el fotograma o ciclo de actualizacion actual</i>. El concepto de tiempo
 * delta es esencial para garantizar un rendimiento uniforme y consistente en los videojuegos.
 * <p>
 * El tiempo delta se utiliza para ajustar la velocidad de los elementos en el juego en funcion del rendimiento del
 * sistema. Esto es especialmente importante porque diferentes sistemas pueden tener diferentes capacidades de
 * procesamiento y rendimiento. Si un juego no tuviera en cuenta el tiempo delta, los elementos del juego (como la
 * velocidad de movimiento de un personaje) podrian verse afectados por la velocidad del hardware y variarian en
 * diferentes sistemas.
 * <p>
 * En un juego con 24 FPS en una computadora lenta, el delta es de 0,041 segundos. Esto lleva a una acumulacion de
 * deltas que alcanzan 1 segundo, ejecutando 24 FPS. A menudo, esto provoca que la entidad salte (teletransporte) para
 * ajustar la distancia y completar la cantidad de FPS. En un hardware mas potente que ejecuta el juego a 60 FPS, el
 * delta es de 0,016 segundos, lo que permite movimientos mas fluidos. Por lo tanto, este enfoque se refiere a juegos
 * con independencia de fotogramas, en donde mantienen la misma velocidad en diferentes FPS. Para lograr esa
 * independencia de FPS, se emplea un <i>delta fijo</i> o <i>timestep fijo</i>, que se aplica tanto para los ciclos de
 * actualizaciones como para los de renderizado (AVERIGUAR BIEN!).
 */

public class Delta {
}
