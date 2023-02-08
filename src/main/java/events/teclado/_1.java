/* https://www.youtube.com/watch?v=hgHvmjyHxpU&list=PLU8oAlHdN5BktAXdEVCLUYzvDyqRQJ2lk&index=71 */

package events.teclado;

import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

/**
 * ¿Que sucede realmente con los eventos?
 * <p>
 * Pulsacion de tecla = evento (suceso)
 * Nosotros consideramos a este suceso como algo intangible, pero java lo considera como un OBJETO.
 * Java lo que hace es empaquetar al suceso en un objeto (suceso = objeto).
 * <p>
 * A ese paquete se le añaden una serie de caracteristicas, por ejemplo en el caso de una tecla pulsada, el suceso (objeto) seria:
 * tipo de objeto: KeyEvent
 * tipo de evento: Pressed
 * <p>
 * Lo que ocurre despues de empaquetar ese evento, es que viaja y se almacena en el parametro del metodo keyPressed(KeyEvent objeto)
 * del objeto Oyente (KeyListener).
 */

public class _1 {

	public static void main(String[] args) {
		new Marco().setVisible(true);
	}
}

class Marco extends JFrame {


	public Marco() {
		setTitle("Ventana");
		setSize(Toolkit.getDefaultToolkit().getScreenSize().width / 2, Toolkit.getDefaultToolkit().getScreenSize().height / 2);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addKeyListener(new Oyente());
	}

	private static class Oyente implements KeyListener {

		private int lastKey = -1;

		public void keyPressed(KeyEvent e) {
			// Si la ultima tecla presionada es distinta a la actual, entonces ejecuta la accion
			if (lastKey != e.getKeyCode()) {
				System.out.println("Tecla presionada");
				lastKey = e.getKeyCode();
			}
		}

		public void keyReleased(KeyEvent e) {
			lastKey = -1; // Asigna de nuevo -1 para que se pueda volver a condicionar como una tecla distinta a la actual
			System.out.println("Tecla soltada");
		}

		// Combinacion de tecla pulsada y soltada
		public void keyTyped(KeyEvent e) {
			// System.out.println("Pulsada y soltada");
		}

	}

}
