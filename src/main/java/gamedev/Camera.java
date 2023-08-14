package gamedev;

/**
 * <h1>Camara</h1>
 * La camara es el punto de vista del jugador en el juego, su ojo hacia el mundo.
 * <p>
 * Las camaras de punto fijo y de desplazamiento a menudo se agrupan como 2D. Los juegos de plataformas de mundo
 * abierto, usan una camara que sigue directamente al jugador, intentando mantenerlo en el centro de la pantalla (o
 * cerca del centro) siempre que sea posible.
 * <p>
 * Para el juego Argentum Online se utiliza el sistema de camara <a href="https://es-academic.com/dic.nsf/eswiki/924763"><i>Top-Down</i></a>,
 * tambien llamado "vista de pajaro" y se juega como si estuvieras mirando desde arriba hacia abajo. Este sistema es
 * parecido al <b>3/4</b>.
 * <br><br>
 * <h2>¿Que son los desplazamientos?</h2>
 * Los <i>desplazamientos</i> (offsets), son las coordenadas <b>x</b> e <b>y</b> de la esquina superior izquierda de
 * la vista de la camara, que nos dicen que tan lejos (restando o sumando) dibujamos algo (tile o entidad) de su
 * posicion original. Para el primer ejemplo, la vista tiene origen en {@code (0, 0)} de la pantalla. Ahora si el
 * desplazamiento es de x en 1, se estaria dibujando la matriz (todos los tiles) hacia la izquierda, dando el efecto de
 * que la camara se "mueve" hacia la derecha.
 * <p>
 * Los tiles se dibujan individualmente de izquierda a derecha (forma tradicional) usando un doble for. Esto se hace
 * tan rapido que no se nota. Cada iteracion completa de renderizado, "mueve" el mundo hacia una determinada direccion.
 * <p>
 * Si la camara se "mueve" hacia la derecha (+x), los tiles se desplazan hacia la izquierda restando la posicion
 * original. Si la camara se "mueve" hacia la izquierda (-x), los tiles se desplazan hacia la derecha sumando la
 * posicion original. Esto mismo se aplica para el eje y. Si la camara se "mueve" hacia abajo (+y), los tiles
 * se desplazan hacia arriba restando la posicion original. Si la camara se "mueve" hacia arriba (-y), los tiles se
 * desplazan hacia abajo sumando la posicion original.
 * <p>
 * Es importante tener en cuenta que la multiplicacion de signos negativos da como resultado un signo positivo. Esto
 * quiere decir que si la camara se "mueve" hacia la izquierda (-x) en -1, entonces estoy sumando la posicion original
 * de los tiles en 1, ya que la instruccion {@code - (-1)} es igual a 1.
 * <br><br>
 * <h2>¿Como centrar la camara en la entidad?</h2>
 * Primero se calcula el centro de la pantalla restando la mitad del ancho y altura de la ventana y entidad. Ahora, el
 * centro de la pantalla es la vista de la camara. Luego, a ese valor (centro de la pantalla) se le resta (o suma, ver
 * multiplicacion se signos) la posicion de la entidad, obteniendo asi, un "seguimiento" central de la camara en la
 * entidad.
 * <p>
 * Este desplazamiento es necesario aplicarlo tanto para los tiles como para la entidad.
 * <p>
 * <i>Nota: No confundir el centro de la pantalla con el centro del mundo.</i>
 * <br><br>
 * Recursos:
 * <a href="http://game-wisdom.com/critical/camera-systems-game-design">Tipos de camara</a>
 * <a href="https://www.youtube.com/watch?v=vf87f1gH-fw">Tipos de camara (video)</a>
 * <a href="http://www.imake-games.com/cameras-in-2d-platformers/">Camaras en plataformas 2D (detallado)</a>
 * <a href="https://soherearemyideas.blogspot.com/2012/06/my-view-on-2d-platformer-cameras.html">Analisis en camaras de plataformas 2D</a>
 * <a href="https://www.samjhhu.com/2d-platformer-camera/">Movimientos de camara para juegos de plataformas en 2D: ¿Como se cual elegir?</a>
 * <a href="https://www.gamedeveloper.com/design/the-differences-between-2d-and-3d-camera-systems-in-game-design">Diferencias entre los sistemas de camara 2D y 3D</a>
 * <a href="https://www.youtube.com/watch?v=TCIMPYM0AQg">Logica de la camara de Super Mario World</a>
 *
 * @author Juan Debenedetti
 */

public class Camera {
}
