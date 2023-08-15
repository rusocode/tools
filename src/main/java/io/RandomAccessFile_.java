package io;

import java.io.*;
import java.util.ArrayList;

import static utils.Global.*;

/**
 * Esta clase se utiliza para leer y escribir en archivos de acceso aleatorio. Un archivo de acceso aleatorio se
 * comporta como una gran variedad de bytes. Hay un cursor implicito en la matriz llamado file pointer, al mover
 * el cursor hacemos las operaciones de lectura y escritura. Si se llega al final del archivo antes de que se haya leido
 * el numero deseado de bytes, se lanza EOFException.
 * 
 * La clase RandomAccessFile del paquete IO le permite moverse, navegar por un archivo y leerlo o escribir en el como
 * desee. Tambien puede reemplazar partes existentes de un archivo. Esto no es posible con FileInputStream o
 * FileOutputStream.
 * 
 * La ventaja de RandomAccessFile es que puede hacer entradas y salidas de bytes en la misma clase, en cambio, un flujo
 * estandar como FileOuputStream no puede leer datos del archivo, para eso se crearia una instancia de la clase
 * FileInputStream. Es decir que nos estariamos ahorrando crear dos clases.
 * 
 * ¿Cual es la diferencia con respecto a las clases InputStream/OutputStream?
 * RandomAccessFile trata el archivo como una matriz de bytes donde tiene el puntero interno. El hecho de que lo trate
 * como una gran variedad de bytes es lo único de esta clase. FileInputStream, sin embargo, solo lee el flujo y devuelve
 * los datos. Es más adecuado para leer datos sin procesar como imágenes, etc. No trata el archivo como una matriz
 * grande, solo mantiene pestañas de en qué parte del archivo se ha leído hasta ahora. Con FileInputStream, en realidad
 * tendría que leer los datos y colocarlos en una matriz para obtener el mismo estilo de acceso que RandomAccessFile.
 * 
 * En cuanto al rendimiento, no estoy muy seguro de si hay mucha diferencia, pero tendría que asumir que FileInputStream
 * sería más rápido, ya que no tiene que preocuparse por la estructura de una matriz y solo devuelve los datos sin
 * procesar que lee. Menos estructura tiende a ser más rápida cuando se trata de cosas como esta. Probablemente por qué
 * se recomienda para datos de imágenes.
 * 
 * Recursos
 * youtube.com/watch?v=gcPau_67V8s
 * http://tutorials.jenkov.com/java-io/randomaccessfile.html
 * https://www.dreamincode.net/forums/topic/149024-fileinputstream-vs-randomaccessfile-for-reading-files/
 * 
 * @author Ru$o
 * 
 */

public class RandomAccessFile_ {

	public static void main(String[] args) {

		ArrayList<Producto> productos = new ArrayList<>();

		productos.add(new Producto(1, "producto 1", 'Z', 10.5, true));
		productos.add(new Producto(2, "producto 2", 'C', 20, false));
		productos.add(new Producto(3, "producto 3", 'X', 15.3, true));
		productos.add(new Producto(4, "producto 4", 'Z', 10.5, false));

		/* Crea un obj de RandomAccessFile para poder leer y escribir (r = read, w = write) desde un try-catch-resources sin la
		 * necesidad de cerrar el flujo del archivo. */
		try (RandomAccessFile raf = new RandomAccessFile(RAF, "rw")) {

			// Escribe los 4 registros en el archivo
			for (Producto producto : productos) {

				raf.writeInt(producto.getId());

				/* No usa writeUTF ya que puede generar problemas de bytes y no son precisos, encambio el metodo writeChars() escribe
				 * caracter x caracter (2 bytes).
				 * Crea un StringBuffer para que la cadena sea mutable y no se creen nuevos objetos cuando se modifique. */
				StringBuffer sb = new StringBuffer(producto.getNombre());
				sb.setLength(10); // Estbalece un limite a 10 caracteres (?
				raf.writeChars(sb.toString());

				raf.writeChar(producto.getTipo());
				raf.writeDouble(producto.getPrecio());
				raf.writeBoolean(producto.isVendido());

				/* Al igual que con el metodo read(), el metodo write() avanza el puntero del archivo despues de ser llamado. De esa
				 * manera, no tiene que mover constantemente el puntero del archivo para escribir datos en una nueva ubicacion. */

			}

			System.out.println("Despues de escribir los 4 registros, el archivo ocupa " + raf.length() + " Kbs");

			/* Hasta ahora se escribieron 4 registros de 35 bytes cada uno, ya que el int para el id ocupa 4 bytes, cada caracter de
			 * la cadena ocupa 2 bytes por lo tanto son 2x10 = 20 bytes, el char para el tipo 2 bytes, el double 8 bytes y el
			 * boolean 1 byte. Esto suma un total de 35 bytes para cada registro escrito desde el objeto RandomAccessFile. */

			/* Establece el desplazamiento del file pointer medido desde el principio de este archivo (0), en el que se produce
			 * la siguiente lectura o escritura. El desplazamiento puede establecerse mas alla del final del archivo. Establecer el
			 * desplazamiento mas alla del final del archivo no cambia la longitud del archivo. La longitud del archivo cambiara
			 * solo si se escribe despues de que el desplazamiento se haya establecido mas alla del final del archivo.
			 * 
			 * En este caso posiciona el file pointer en el byte 35, es decir en el segundo registro ya que el file pointer del
			 * primer registro comienza en 0 (0-34 primer reg, 35-69 segundo reg). Si posicionamos el puntero en un byte que no sea
			 * el indice incial de un registro, estaria leyendo datos erroneos. */
			raf.seek(35);

			System.out.println(raf.readInt());

			String nombre = "";
			for (int i = 0; i < 10; i++)
				nombre += raf.readChar(); // El metodo readUTF() para leer los bytes de una cadena puede fallar
			System.out.println(nombre);

			System.out.println(raf.readChar());
			System.out.println(raf.readDouble());
			System.out.println(raf.readBoolean());

			/* Si nos fijamos en la carpeta dat de resources, podemos ver que el archivo raf.dat ocupa 140 bytes que son los 4
			 * registros x los 35 bytes de los datos. */

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private static class Producto {

		int id;
		String nombre;
		char tipo;
		double precio;
		boolean vendido;

		public Producto(int id, String nombre, char tipo, double precio, boolean vendido) {
			this.id = id;
			this.nombre = nombre;
			this.tipo = tipo;
			this.precio = precio;
			this.vendido = vendido;
		}

		public int getId() {
			return id;
		}

		public String getNombre() {
			return nombre;
		}

		public char getTipo() {
			return tipo;
		}

		public double getPrecio() {
			return precio;
		}

		public boolean isVendido() {
			return vendido;
		}

	}

}
