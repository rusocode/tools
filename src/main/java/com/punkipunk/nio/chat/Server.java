package com.punkipunk.nio.chat;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.UIManager;

import net.miginfocom.swing.MigLayout;

import static com.punkipunk.utils.Global.*;

/**
 * ¿Por que usar NIO para hacer una conexion de sockets cuando tambien se puede usar el flujo I/O clasico?
 * <p>
 * Porque debido a que NIO es sin bloqueo, no requiere subprocesos adicionales. Un chat basado en sockets requeriria tantos
 * subprocesos como usuarios, lo que agregaria una sobrecarga significativa, mientras que un chat NIO siempre necesitaria un solo
 * subproceso, lo que lo haria mucho mas escalable, ya que la sobrecarga de subprocesos puede volverse realmente significativa.
 * <p>
 * No necesariamente. En medidas reales de Java en Linux, los multiprocesos de los diseños clasicos de I/O superan a NIO en un 30%
 * aproximadamente (ver "Java Network Programming Book").
 * <p>
 * Java NIO supera a un diseño clasico de I/O solo si:
 * <ol>
 * <li>Tienes una enorme cantidad de clientes > 20.000.
 * <li>El intervalo entre paquetes de datos para enviar es muy corto.
 * </ol>
 * Conclusion: Necesita un servidor para > 20.000 clientes con comunicacion de alta frecuencia.
 * <p>
 * El Selector es un *multiplexor de objetos SelectableChannel encargado de seleccionar canales sin bloqueo y determinar
 * qué canales están listos para, por ejemplo, leer o escribir. De esta manera, un solo hilo puede administrar múltiples
 * canales y, por lo tanto, múltiples conexiones de red.
 * <p>
 * ¿Por que utilizar un selector?
 * La ventaja de usar un solo hilo para manejar múltiples canales es que necesita menos hilos para manejar los canales.
 * En realidad, puede usar un solo hilo para manejar todos sus canales. Cambiar entre subprocesos es costoso para un
 * sistema operativo, y cada subproceso también consume algunos recursos (memoria) en el sistema operativo. Por lo
 * tanto, cuantos menos subprocesos uses, mejor.
 * <p>
 * Un canal seleccionable puede estar en modo de bloqueo o en modo sin bloqueo. En el modo de bloqueo, cada operacion de
 * I/O invocada en el canal se bloqueara hasta que se complete. En el modo sin bloqueo, una operacion de I/O nunca se
 * bloqueara y puede transferir menos bytes de los solicitados o posiblemente ningun byte. El modo de bloqueo de un
 * canal seleccionable puede determinarse invocando su metodo isBlocking(). Los canales seleccionables recien creados
 * estan siempre en modo de bloqueo. El modo sin bloqueo es mas util junto con la multiplexacion basada en selectores.
 * Un canal debe colocarse en modo de no bloqueo antes de ser registrado con un selector, y no puede volver al modo de
 * bloqueo hasta que se haya cancelado su registro. En caso de registrar el servidor con un selector que fue configurado
 * con bloqueo, lanzara un IllegalBlockingModeException.
 * <p>
 * El Selector incluye los siguientes canales seleccionables:
 * -ServerSocketChannel
 * -SocketChannel
 * -DatagramChannel
 * -Pipe.SinkChannel
 * -Pipe.SourceChannel
 * FileChannel no se puede usar con un Selector ya que no se puede cambiar al modo sin bloqueo.
 * <p>
 * Se dice que un canal que "dispara un evento" esta "listo" para ese evento. Por lo tanto, un canal que se ha
 * conectado correctamente a otro servidor esta "listo para conectarse". Un canal de socket de servidor que acepta una
 * conexion entrante esta listo para "aceptar". Un canal que tiene datos listos para ser leidos esta listo para "leer".
 * Un canal que esta listo para escribir datos en el, esta listo para "escribir". Estos eventos determinan de qué
 * canales se pueden leer y escribir examinando las keys seleccionadas del selector.
 * <p>
 * Un canal puede registrarse como maximo una vez con cualquier selector en particular. Una vez que el canal este
 * registrado, el selector puede verificar y asegurarse de que las operaciones de I/O, como listo para leer o listo
 * para escribir, se realicen en consecuencia. En realidad, el selector funciona con canales directamente, pero usa
 * objetos SelectionKey en su lugar.
 * <a href="https://github.com/rusocode/utilidades/blob/master/src/main/resources/recortes/IO-NIO-Netty-Buffer/Datagrama%20NIO%20cliente-servidor.PNG">...</a>
 * <p>
 * Cada registro del canal esta identificado por una clave (SelectionKey) que es utilizada por el selector.
 * Existen tres tipos de conjuntos de claves:
 * -"Key set" contiene las claves de los canales registrados y se obtienen usando el metodo keys().
 * -"Selected-key set" contiene las claves listas para al menos una de las operaciones y se obtienen usando el metodo
 * selectedKeys().
 * -"Cancelled-key set" contiene las claves canceladas cuyos canales aun no se han cancelado.
 * Los tres conjuntos están vacíos en un selector recién creado.
 * <p>
 * Cuando registras un canal con un selector, el metodo register() devuelve un objeto de tipo SelectionKey que
 * representa el canal registrado con ese selector. Este objeto contiene algunas propiedades interesantes:
 * -El canal
 * -El selector
 * -El interest set
 * -El ready set
 * -Y un objeto adjunto (opcional)
 * <p>
 * *Los multiplexores son circuitos combinacionales con varias entradas y una unica salida de datos. Estan dotados de
 * entradas de control capaces de seleccionar una, y solo una, de las entradas de datos para permitir su transmision
 * desde la entrada seleccionada hacia dicha salida.
 * <p>
 * Links:
 * <a href="https://gist.github.com/Botffy/3860641">NiochatServer.java</a>
 * <a href="http://tutorials.jenkov.com/java-nio/selectors.html">...</a>
 * <a href="https://docs.oracle.com/javase/8/docs/api/java/nio/channels/SelectableChannel.html">...</a>
 *
 * @author Juan Debenedetti
 */

public class Server extends JFrame implements Runnable {

    private static final long serialVersionUID = 1L;

    private JTextArea console;

    private Selector selector;
    private ServerSocketChannel server;

    private static final int BUFFER_SIZE = 1024;

    public Server() {

        super("Servidor");
        setResizable(false);
        setSize(700, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initialize();

        new Thread(this).start();
    }

    private void initialize() {

        JPanel panel = new JPanel();
        panel.setLayout(new MigLayout("fill", "", "[grow][]")); // columna/fila

        console = new JTextArea();
        console.setEditable(false);
        panel.add(console, "grow, wrap");

        add(panel);
        setLocationRelativeTo(null);

    }

    @Override
    public void run() {

        try {

            // Abre el selector
            selector = Selector.open();
            if (selector.isOpen()) console.append("[Server] Se abrio el selector\n");

            /* Abre el servidor. Ahora mismo el socket del servidor no esta vinculado; debe estar vinculado a una direccion
             * especifica a traves de uno de los metodos de vinculacion de su socket antes de que se puedan aceptar las
             * conexiones. */
            server = ServerSocketChannel.open();
            if (server.isOpen()) console.append("[Server] Se abrio el servidor\n");

            // Vincula el servidor a la diereccion local y puerto TCP
            server.bind(new InetSocketAddress(SERVER_PORT));

            // Configura el canal del servidor en modo sin bloqueo
            server.configureBlocking(false);
            if (!server.isBlocking()) console.append("[Server] Se configuro el servidor en modo sin bloqueo\n");

            /* Registra el canal del servidor con el selector y le asigna un interest set.
             * Para registrar interes en mas de un evento: | SelectionKey.OP_READ */
            server.register(selector, SelectionKey.OP_ACCEPT /* server.validOps() */);

            if (server.isRegistered())
                console.append("[Server] Se registro el servidor con el selector para aceptar conexiones!\n");

            while (true) {

                console.append("[Server] Esperando una conexion en el puerto " + SERVER_PORT + "...\n");

                // Bloquea el servidor hasta que notifique un evento
                selector.select(); // blocking selection

                // Bloquea el servidor hasta que notifique un evento o expire el periodo de tiempo de espera dado
                // selector.select(23); // blocking selection

                Set<SelectionKey> selectedKeys = selector.selectedKeys();
                Iterator<SelectionKey> keys = selectedKeys.iterator();

                while (keys.hasNext()) {

                    SelectionKey key = keys.next();

                    /* El Selector no elimina las instancias de SelectionKey del propio conjunto de keys seleccionadas. Tienes que hacer
                     * esto cuando hayas terminado de procesar el canal. La proxima vez que el canal este "listo", el selector la agregara
                     * al conjunto de keys seleccionadas nuevamente. */
                    keys.remove();

                    // Si la clave no es valida
                    if (!key.isValid()) continue;

                    /* Si un cliente solicito una nueva conexion. Solo se espera que el canal ServerSocketChannel registrado para
                     * OP_ACCEPT reciba una clave "aceptable". */
                    if (key.isAcceptable()) {

                        // El servidor acepta la conexion del cliente
                        SocketChannel client = server.accept();

                        /* En el modo sin bloqueo, el metodo accept() regresa inmediatamente y puede devolver un valor nulo si no hubiera
                         * llegado una conexion entrante. Por lo tanto, verifica si el SocketChannel devuelto es nulo. */
                        if (client != null && client.isConnected()) {

                            // La IP es link-local?
                            console.append("[Server] " + client.getRemoteAddress() + " se conecto!\n");

                            client.configureBlocking(false);

                            /* Registra el canal del cliente con el selector en donde le asigna un interest set y un buffer.
                             * En este punto, la clave de seleccion ahora es de lectura, ya que logicamente despues de aceptar una conexion, se leen
                             * los datos de esta. */
                            client.register(selector, SelectionKey.OP_READ, ByteBuffer.allocate(BUFFER_SIZE));

                        } else console.append("[Server] El cliente no se pudo conectar!\n");

                    }

                    // Si el cliente tiene datos para leer
                    else if (key.isReadable()) {

                        // Obtiene el canal y el buffer del cliente registrado usando la clave
                        SocketChannel client = (SocketChannel) key.channel();
                        ByteBuffer buf = (ByteBuffer) key.attachment();

                        console.append("[Server] " + client.getRemoteAddress() + ": ");

                        // Mientras no se haya leido todo el buffer
                        while (client.read(buf) > 0) {

                            // Voltea el buf para poder leerlo
                            buf.flip();

                            // Mientras haya bytes entre la posicion y el limite del buffer, muestra la representacion del byte en caracter
                            while (buf.hasRemaining())
                                console.append("" + (char) buf.get());

                            console.append("\n");

                            // "Limpia" el buffer para rellenarlo con los bytes sobrantes del canal
                            buf.clear();

                        }

                        /* TODO Cuando y donde cerrar el cliente?
                         * Cerrarlo cuando se hayan terminado de leer todos los bytes */
                        key.channel().close(); // client.close();

                    }

                }

                selectedKeys.clear();

            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Error de I/O\n" + e, "Error en el servidor", JOptionPane.ERROR_MESSAGE);
        } catch (IllegalArgumentException e) {
            JOptionPane.showMessageDialog(null, e.getMessage(), "Error en el servidor", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        } finally {
            try {
                server.close();
                selector.close(); // Cierra el Selector e invalida todas las instancias de SelectionKey registradas con este Selector
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error al cerrar la conexion\n" + e, "Error en el servidor",
                        JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    public static void main(String[] args) throws IOException {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        new Server().setVisible(true);
    }

}
