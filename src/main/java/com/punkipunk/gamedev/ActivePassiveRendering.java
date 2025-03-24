package com.punkipunk.gamedev;

/**
 * <b>Renderizado Pasivo:</b>
 * <p>
 * En el renderizado pasivo, la aplicacion se basa en eventos generados por el sistema operativo para determinar cuando
 * debe realizarse el dibujo en la pantalla. En aplicaciones de ventana tradicionales, como las de GUI, es el sistema
 * operativo el que decide cuando ejecutar el metodo de pintura (paint) en respuesta a eventos como mover, redimensionar,
 * mostrar u ocultar una ventana. En Java, este metodo de pintura (paint) se activa automaticamente por el sistema
 * operativo y se le pasa un objeto Graphics para realizar el dibujo.
 * <p>
 * <b>Renderizado Activo:</b>
 * <p>
 * En el renderizado activo, en lugar de depender de eventos del sistema operativo, la aplicacion asume el control total
 * del proceso de dibujo. En el modo de pantalla completa exclusiva, el contenido se dibuja directamente en la pantalla
 * sin tener que esperar eventos del sistema. En lugar de usar el metodo paint, se utiliza un bucle de renderizado en el
 * que la aplicacion constantemente dibuja el contenido en el lienzo (Canvas). Esto elimina la necesidad de preocuparse
 * por eventos de pintura entregados por el sistema operativo.
 * <p>
 * Consejos para el Renderizado Activo:
 * <ul>
 * <li>Evitar poner codigo de dibujo en el metodo de pintura (paint). En su lugar, se recomienda usar un metodo separado
 * para el dibujo, como render(Graphics g), que se puede llamar desde el metodo de pintura en modo de ventana o
 * directamente desde el bucle de renderizado en modo de pantalla completa.
 * <li>Utilizar el metodo setIgnoreRepaint en la ventana de la aplicacion y en los componentes para desactivar los
 * eventos de pintura del sistema operativo, evitando posibles problemas.
 * <li>Mantener separado el codigo de dibujo del bucle de renderizado para adaptarse tanto al modo de pantalla completa
 * como al de ventana.
 * <li>Optimizar el dibujo para evitar dibujar todo en la pantalla todo el tiempo.
 * <li>Evitar el uso de componentes pesados, ya que implican sobrecarga del sistema.
 * <li>Si se usan componentes ligeros (como los de Swing), se pueden llamar a metodos como paintComponents,
 * paintComponent, paintBorder y paintChildren directamente desde el bucle de renderizado.
 * </ul>
 * En resumen, el renderizado pasivo se basa en eventos del sistema operativo para el dibujo, mientras que el
 * renderizado activo involucra un bucle de renderizado controlado por la aplicacion, lo que es especialmente útil en
 * aplicaciones de pantalla completa donde el control del dibujo es esencial.
 */

public class ActivePassiveRendering {
}
