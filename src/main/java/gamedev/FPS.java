package gamedev;

/**
 * <h1>¿Que son los FPS?</h1>
 * FPS son las siglas de <b><a href="https://es.wikipedia.org/wiki/Cuadro_(v%C3%ADdeo)">Frames</a> Per Second</b>
 * (cuadros por segundo), aunque tambien podemos referirnos a ellos como <i>fotogramas por segundo</i> o <i>imagenes por
 * segundo</i>. A la hora de hablar de estas cifras, nos referimos a ellos como el framerate de un videojuego. Y como su
 * nombre indica, es la cantidad de imagenes consecutivas que se muestran en pantalla por cada segundo mientras jugamos.
 * <p>
 * Cuando estas viendo un video, lo que ves en realidad es una secuencia de fotogramas que pasan a gran velocidad para
 * dar la sensacion de movimiento. Lo que realmente ves son imagenes fijas mostrandose de forma consecutiva, aunque
 * pasan tan rapido que lo captas como un movimiento constante. Esta velocidad a la que pasan las imagenes esta
 * determinada por los FPS.
 * <p>
 * Cuando estas jugando a un videojuego, los fotogramas de la imagen son <a href="https://videojuegos.fandom.com/es/wiki/Renderizaci%C3%B3n">renderizados</a>
 * en tiempo real por la GPU y se muestran instantaneamente en pantalla.
 * <p>
 * Cuando hay muchos FPS en un juego, <b>los movimientos que suceden en pantalla se ven mas fluidos</b> y tienes mas
 * informacion de todo lo que esta pasando en ese momento. Por lo tanto, cuanto mayor sea el framerate mejor se vera el
 * juego en movimiento.
 * <p>
 * Independientemente de los FPS que este siendo capaz de mostrar tu PC o consola, siempre veras exactamente lo mismo en
 * pantalla. <b>La diferencia esta en la fluidez</b>, que sera mayor con mayores FPS, y te permitira reaccionar antes.
 * <br><br>
 * <h2>FPS y frecuencia de refresco: dos factores a tener cuenta a la hora de optimizar el uso de un monitor</h2>
 * Aunque los hercios de pantalla tengan una relacion casi obligada con los FPS, son una cifra que no tienen nada que
 * ver con estos.
 * <p>
 * No hay que confundir los frames por segundo con la frecuencia de refresco en hercios de una pantalla, que hace
 * referencia a la velocidad, o numero de veces por segundo que la pantalla se actualiza. Por ejemplo, <b>un TV o
 * monitor a 60Hz podra mostrar 60 cuadros en un segundo</b>, una de 120Hz podra mostrar 120 cuadros en un segundo.
 * <p>
 * Si tienes un ordenador monstruoso, usar un monitor de 60Hz hara que no puedas beneficiarte de alcanzar 90, 120, 144 o
 * mas fotogramas por segundo. Si, tu grafica puede ejecutar el juego a 144fps, pero tu monitor solo te mostrara 60.
 * <br><br>
 * <h3>Combinando hercios y FPS</h3>
 * En teoria contar con una tasa de refresco mas alta es siempre mas interesante. De ahi que vemos monitores gaming
 * preparados para trabajar a 144Hz e incluso algunos tope de gama se atreven y llegan a los 240Hz. Pero hay que tener
 * en cuenta que <b>un rendimiento optimo pone en juego tambien los FPS y la relacion con los hercios</b>.
 * <p>
 * Si por ejemplo usamos un monitor que funciona a 120Hz con un juego que corre a 60fps. La consola o el PC esta
 * enviando una señal que ofrece 60 cuadros por segundo al monitor, el cual, recordamos que cuenta con una frecuencia de
 * refresco de 120Hz. Esto se traduce en el doble de velocidad, por lo que para paliar esa diferencia, la pantalla
 * tirara de un recurso como es la <a href="https://hardzone.es/tutoriales/rendimiento/interpolacion-fotogramas/">interpolacion</a>
 * (repite el mismo cuadro en este caso dos veces o cuatro si el monitor funcionara a 240Hz).
 * <p>
 * <b>Esta solucion lo que hace es que puede generar <a href="https://hardzone.es/tutoriales/reparacion/solucionar-stuttering-pc/"><i>stuttering</i></b></a>,
 * una especie de parpadeo en la pantalla que puede resultar bastante molesto. Esto puede darse si el monitor no cuenta
 * con la capacidad de sincronizacion con la grafica que envia la señal del juego.
 * <p>
 * Luego podria darse el caso inverso, que es que contemos con una <b>pantalla con una menor tasa de refresco que los
 * frames del juego</b>. Un titulo que funciona a 120fps para una pantalla a 60Hz. ¿Cual seria el resultado? Que nunca
 * podriamos apreciar esos 120fps dados los limites del panel, el cual no tendria mas remedio que subdividir los cuadros
 * y mostrar solo 60fps.
 * <p>
 * Ademas, esta asincronicidad <b>provoca el siempre molesto efecto <a href="https://hardzone.es/reportajes/que-es/tearing-imagen-monitor/"><i>tearing</i></b></a>
 * o lo que se traduce en la sensacion de imagen dividida en varias partes, algo que se aprecia al realizar cambios
 * bruscos de camara. Un defecto que algunas marcas intentan corregir con sistemas añadidos al firmware que usan en sus
 * paneles como son los que permiten sincronizar las imagenes entre grafica y pantalla (FreeSync de AMD o G-Sync de
 * Nvidia).
 * <p>
 * En este sentido lo ideal es que un monitor <b>funcione siempre a la misma velocidad de refresco que los FPS</b> a
 * los que envia la señal. De esta forma si jugamos a 60fps y el monitor se refresca a 60Hz estaremos viendo una imagen
 * mas fluida en la que cada vez que se refresca la pantalla estara mostrandose una imagen distinta.
 * <br><br>
 * <h2>¿Cual es la relacion entre FPS y el Game Loop?</h2>
 * Por lo general, se dibuja un frame cada vez que se completa el ciclo del juego. Por lo tanto, el FPS te dice que tan
 * rapido se repite el juego.
 * <p>
 * Dicho esto, las arquitecturas de renderizado mas sofisticadas a menudo desacoplan el renderizado del bucle principal
 * del juego. En ese caso, los dos estan vagamente relacionados.
 * <p>
 * El FPS generalmente esta relacionado con graficos de tal manera que es una medida del tiempo de reproduccion de
 * video: cuantos frames (de video) por segundo esta produciendo el juego, mientras que el Game Loop (o tick) esta
 * relacionado con calculos de estados de juego como: fisica, lectura y aplicacion de entradas de usuario,
 * actualizaciones de entidades de juego, manejo de I/O de red, etc.
 * <p>
 * La razon por la que a menudo se mezcla es que muchos motores de juegos usan el mismo bucle unico para calcular su
 * estado y luego reproducir el video.
 * <br><br>
 * Recursos:
 * <a href="https://www.youtube.com/watch?v=YC_hTkZzVjU">¿Que son los FPS?</a>
 * <a href="https://www.xataka.com/basics/que-fps-fotogramas-segundo-sirven-videojuegos">¿Que son los FPS y para que sirven en los videojuegos?</a>
 * <a href="https://www.xatakahome.com/curiosidades/fps-frecuencia-refresco-dos-factores-a-tener-cuenta-a-hora-optimizar-uso-monitor">FPS y frecuencia de refresco: dos factores a tener cuenta a la hora de optimizar el uso de un monitor</a>
 * <a href="https://gamedev.stackexchange.com/questions/96758/what-is-the-relationship-between-frames-per-second-and-a-game-loop">¿Cual es la relacion entre FPS y el Game Loop?</a>
 * <a href="https://www.geeknetic.es/Guia/2262/A-Cuantos-FPS-Puede-Ver-el-Ojo-Humano.html#:~:text=As%C3%AD%20que%2C%20como%20respuesta%20final,la%20fluidez%20a%20m%C3%A1s%20fotogramas">¿A cuantos FPS puede ver el ojo humano?</a>
 * <a href="https://hardzone.es/tutoriales/rendimiento/fps-ojo-humano/">¿Cuantos FPS puede ver realmente el ojo del ser humano?</a>
 */

public class FPS {
}
