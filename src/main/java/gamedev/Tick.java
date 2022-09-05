package gamedev;

/**
 * <h2>¿Que es un tick?</h2>
 * Un tick es una unidad de medida de tiempo, especificamente se refiere a una sola instancia de una accion repetida
 * (generalmente una accion amplia) en un juego, o el periodo de tiempo que consume esa accion.
 *
 * <p>Los ticks se repiten y (en su mayoria) son regulares; el termino proviene de uno de los sonidos que hace un reloj
 * ("tick" y "tock").
 *
 * <p>Normalmente, un tick es una iteracion de algun bucle, como el bucle logico del juego principal. Se puede decir,
 * por ejemplo, que la logica del juego "tick (marca)" una vez por cuadro, o que "durante el tictac, las posiciones de
 * los personajes se actualizan".
 *
 * <p><b>FPS</b> es una medida de cuantos cuadros (generalmente render frames) procesa un juego en un segundo. Los ticks
 * y los FPS no estan necesariamente relacionados, sin embargo, a veces la gente llamara a una sola ejecucion del bucle
 * logico del juego un "logic frame", en cuyo caso la tasa de FPS (logica) seria la cantidad de ticks procesados por
 * segundo. Esto es particularmente comun para los juegos que imponen velocidades de fotogramas fijas para la logica o
 * la fisica del juego por cualquier motivo.
 *
 * <p>El metodo {@code tick()} actualiza tiempos especificos por segundo (por ejemplo, 20, 60, etc.). Llamo a ese numero
 * <i>UPS</i> (actualizaciones por segundo). Consideremos el movimiento del jugador. Su velocidad es de 20 pixeles por
 * segundo. Entonces, si lo queremos mover a esta velocidad, necesitamos saber los UPS (por ejemplo, 20). Por lo tanto,
 * nuestro jugador se movera 20 pixeles por tick. Y eso esta mal. Si es entonces con nuestros 20 ticks, durante 1
 * segundo nuestro jugador se movera 20*20 pixeles (400 pixeles por segundo). Entonces, la velocidad de nuestro de la
 * entidad sera de 20 * (1 / UPS) por tick. Podemos cambiar nuestro UPS y tambien se cambiara la cantidad de pixeles que
 * mueve el jugaddor por tick. Pero todavia se movera 20 pixeles por segundo. Para que quede claro, supongamos que
 * nuestro UPS es 1. Entonces la velocidad sera 20 * (1/1) = 20 pixeles por tick (en un segundo es un tic) = 30 pixeles
 * por segundo. Lo mismo que queremos.
 *
 * <p>El {@code render()} solo dibuja lo que actualizamos. Puede hacerlo con la misma velocidad que actualizamos y
 * tambien puede hacerlo mas rapido. Pero da igual, porque solo veremos lo que actualicemos. UPS puede ser 1 pero FPS es
 * 160. Veras que todo se mueve cada segundo. Pero eso no significa que rendericemos 1 vez por segundo, significa que
 * durante un segundo renderizamos la misma imagen. Despues de 1 segundo, actualizamos nuestro juego y veremos otra
 * imagen.
 *
 * <br><br>
 *
 * <h2>¿Cual es la relacion entre FPS y Game Loop?</h2>
 * Por lo general, se dibuja un cuadro cada vez que se completa el ciclo del juego. Por lo tanto, el FPS te dice que tan
 * rapido se repite el juego.
 *
 * <p>Dicho esto, las arquitecturas de renderizado mas sofisticadas a menudo desacoplan el renderizado del bucle
 * principal del juego. En ese caso, los dos estan vagamente relacionados.
 *
 * <p>El FPS generalmente esta relacionado con graficos de tal manera que es una medida del tiempo de reproduccion de
 * video: cuantos frames (de video) por segundo esta produciendo el juego, mientras que el Game Loop (o tick) esta
 * relacionado con calculos de estados de juego como: fisica, lectura y aplicacion de entradas de usuario,
 * actualizaciones de entidades de juego, manejo de I/O de red, etc.
 *
 * <p>La razon por la que a menudo se mezcla es que muchos motores de juegos usan el mismo bucle unico para calcular su
 * estado y luego reproducir el video.
 *
 * <p>Recursos:
 * <a href="https://gamedev.stackexchange.com/questions/81608/what-is-a-tick-in-the-context-of-game-development#:~:text=A%20tick%20is%20a%20unit,%22%20and%20%22tock%22">¿Que es un "tick" en el contexto del desarrollo de juegos?</a>
 * <a href="https://gamedev.stackexchange.com/questions/96758/what-is-the-relationship-between-frames-per-second-and-a-game-loop">¿Cual es la relacion entre FPS y Game Loop?</a>
 */

public class Tick {
}
