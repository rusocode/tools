package nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * El Selector es un objeto utilizado para seleccionar un canal listo para comunicarse (para realizar una operacion).
 * Es decir que se consulta al Selector si hay algun canal que este listo para operar sin bloqueo.
 * Tiene la posibilidad de elegir un canal seleccionable que este listo para algunas operaciones de red, por ejemplo,
 * conectar, aceptar, leer y escribir.
 * 
 * El canal debe estar en modo sin bloqueo para ser utilizado con un selector. Esto significa que no puede usar
 * FileChannel con un Selector ya que FileChannel no se puede cambiar al modo sin bloqueo. Sin embargo, los canales
 * de socket si.
 * 
 * Se dice que un canal que "dispara un evento" esta "listo" para ese evento. Por lo tanto, un canal que se ha
 * conectado correctamente a otro servidor esta "listo para conectarse". Un canal de socket de servidor que acepta una
 * conexion entrante esta listo para "aceptar". Un canal que tiene datos listos para ser leidos esta listo para "leer".
 * Un canal que esta listo para escribir datos en el, esta listo para "escribir".
 * 
 * El Selector incluye canales seleccionables:
 * - DatagramChannel
 * - Pipe.SinkChannel
 * - Pipe.SourceChannel
 * - ServerSocketChannel
 * - SocketChannel
 * 
 * Cada registro del canal esta representado por un SelectionKey.
 * Un selector funciona con un conjunto (set) de SelectionKey.
 * SelectionKey es un token que representa el registro de un canal con un selector.
 * El selector mantiene tres set de keys:
 * - "Key set" contiene las keys con los canales registrados;
 * - "Selected-key set" contiene las keys con canales listos para al menos una de las operaciones;
 * - "Cancelled-key set" contiene keys canceladas cuyos canales aun no se han cancelado.
 * 
 * Cuando registras un canal con un selector, el metodo register() devuelve un objeto de tipo SelectionKey que
 * representa el registro de los canales con ese selector. Este objeto contiene algunas propiedades interesantes:
 * El interest set
 * El ready set
 * El Channel
 * El Selector
 * Y un objeto adjunto (opcional)
 *
 * -Interest Set
 * El interest set es el conjunto de eventos que esta interesado en "seleccionar". Puede leer y escribir ese interest
 * set a traves de SelectionKey de esta manera:
 * int interestSet = selectionKey.interestOps();
 * boolean isInterestedInAccept = SelectionKey.OP_ACCEPT == (interests & SelectionKey.OP_ACCEPT);
 * boolean isInterestedInConnect = SelectionKey.OP_CONNECT == (interests & SelectionKey.OP_CONNECT);
 * boolean isInterestedInRead = SelectionKey.OP_READ == (interests & SelectionKey.OP_READ);
 * boolean isInterestedInWrite = SelectionKey.OP_WRITE == (interests & SelectionKey.OP_WRITE); *
 * 
 * -Ready Set
 * El ready set es el conjunto de operaciones para las que esta listo el canal. Principalmente accedera al ready set
 * despues de una seleccion. Accede al ready set de esta manera:
 * int readySet = selectionKey.readyOps();
 * Puede probar, de la misma manera que con el interest set, para que eventos/operaciones esta listo el
 * canal. Pero, tambien puede usar estos cuatro metodos en su lugar, que vuelven a generar un booleano:
 * selectionKey.isAcceptable();
 * selectionKey.isConnectable();
 * selectionKey.isReadable();
 * selectionKey.isWritable();
 * 
 * -Channel + Selector
 * Acceder al canal + selector desde SelectionKey es facil:
 * Channel channel = selectionKey.channel();
 * Selector selector = selectionKey.selector();
 * 
 * Seleccionar canales a traves de un selector
 * Una vez que haya registrado uno o mas canales con un selector, puede llamar a uno de los metodos select(). Estos
 * metodos devuelven los canales que estan "listos" para los eventos que le interesan (conectar, aceptar, leer o
 * escribir). En otras palabras, si esta interesado en canales que estan listos para leer, recibira los canales que
 * estan listos para leer de los metodos select().
 * 
 * select()
 * Realiza una operacion de seleccion de bloqueo.
 * Se bloquea hasta que al menos un canal este listo para los eventos para los que se registro.
 * 
 * select(long timeout)
 * Hace lo mismo que select() excepto que se bloquea durante un maximo de milisegundos de tiempo de espera.
 * 
 * selectNow()
 * Realiza una operacion de seleccion sin bloqueo.
 * 
 * El int devuelto por los metodos select() indica cuantos canales estan listos. Es decir, cuantos canales estuvieron
 * listos desde la ultima vez que llamo a select(). Si llama a select() y devuelve 1 porque un canal esta listo, y
 * llama a select() una vez mas, y un canal mas esta listo, devolvera 1 nuevamente. Si no ha hecho nada con el primer
 * canal que estaba listo, ahora tiene 2 canales listos, pero solo un canal estaba listo entre cada llamada select().
 * 
 * Una vez que haya llamado a uno de los metodos select() y su valor de retorno haya indicado que uno o mas canales
 * estan listos, puede acceder a los canales listos a traves del "selected-key set" usando el metodo selectedKeys():
 * Set<SelectionKey> selectedKeys = selector.selectedKeys();
 *
 * Fuente: http://tutorials.jenkov.com/java-nio/selectors.html
 * 
 * @author Juan Debenedetti aka Ru$o
 * 
 */

public class Selector_ {

	public static void main(String[] args) throws IOException {

		// Abre el selector
		Selector selector = Selector.open();

		ServerSocketChannel serverChannel = ServerSocketChannel.open();

		// Megasimplificacion para vincular el socket
		serverChannel.socket().bind(new InetSocketAddress(7666));

		// Configura el canal en el modo sin bloqueo
		serverChannel.configureBlocking(false);

		/* Registra el canal con un selector y un interest set que indica el tipo de evento que le interesa escuchar al
		 * canal. */
		SelectionKey key = serverChannel.register(selector, SelectionKey.OP_ACCEPT);

		/* Este bucle itera las keys en el selected-key set. Para cada clave, prueba la key para determinar para que esta
		 * listo el canal al que hace referencia la clave.
		 * 
		 * Observe la llamada keyIterator.remove() al final de cada iteracion. El Selector no elimina las instancias de
		 * SelectionKey del propio conjunto de keys seleccionadas. Tienes que hacer esto cuando hayas terminado de procesar el
		 * canal. La proxima vez que el canal este "listo", el selector lo agregara nuevamente al conjunto de claves
		 * seleccionadas.
		 * 
		 * El canal devuelto por el metodo SelectionKey.channel() debe transmitirse al canal con el que necesita trabajar, por
		 * ejemplo, ServerSocketChannel o SocketChannel, etc. */

		// while (true) {

		int readyChannels = selector.selectNow();
		System.out.println(readyChannels);
		// if (readyChannels == 0) continue;

		// Devuelve el selected-key set del selector y se lo asigna al conjunto selectedKeys del tipo SelectionKey
		Set<SelectionKey> selectedKeys = selector.selectedKeys();

		// Devuelve un iterador sobre los elementos del conjunto SelectionKey
		Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

		// Mientras haya elementos
		while (keyIterator.hasNext()) {

			// Devuelve el key del iterador
			key = keyIterator.next();

			if (key.isAcceptable()) System.out.println("Una conexion fue aceptada por ServerSocketChannel");
			else if (key.isConnectable()) System.out.println("Se establecio una conexion con un servidor remoto");
			else if (key.isReadable()) System.out.println("Un canal esta listo para leer");
			else if (key.isWritable()) System.out.println("Un canal esta listo para escribir");

			keyIterator.remove();
		}

		// }

	}

}
