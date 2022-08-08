package _lab;

/**
 * Una secuencia mutable de caracteres. Esta clase proporciona una API compatible con StringBuffer, pero sin garantia de
 * sincronizacion. Esta clase esta diseñada para usarse como un reemplazo directo para StringBuffer en lugares donde el
 * buffer de cadena estaba siendo utilizado por un solo subproceso (como suele ser el caso). Siempre que sea posible, se
 * recomienda utilizar esta clase con preferencia a StringBuffer, ya que sera mas rapida en la mayoria de las
 * implementaciones.
 * <ul>
 * <li>Es mutable.
 * <li>Se almacena en el heap, ya que es un objeto.
 * <li>Se crea con una capacidad inicial de 16 caracteres por defecto.
 * <li>No es thread-Safe (problemas de interferencia)
 * <li>Es mucho mas rapida que StringBuffer.
 * </ul>
 * ¿Cuando usarla? Si la cadena va a ser modificada desde un hilo.
 */

public class StringBuilder_ {

}
