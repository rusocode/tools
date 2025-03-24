package com.punkipunk.io;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static com.punkipunk.utils.Global.*;

/**
 * <p>
 * RandomAccessFile es una clase en Java que permite leer y escribir en archivos de forma no secuencial, tratando el archivo como
 * una gran matriz de bytes. Sus caracteristicas principales son:
 * <ol>
 * <li>Utiliza un puntero de archivo para navegar y realizar operaciones de lectura y escritura en cualquier posicion.
 * <li>Permite tanto la lectura como la escritura en un mismo objeto, a diferencia de FileInputStream o FileOutputStream que estan
 * limitados a una sola operacion.
 * <li>Ofrece la capacidad de reemplazar partes existentes de un archivo, algo no posible con flujos estandar.
 * <li>Trata el archivo como una matriz de bytes, proporcionando un acceso mas flexible que FileInputStream, que solo lee el flujo
 * de datos secuencialmente.
 * <li>Es ideal para manipular datos estructurados, mientras que FileInputStream es mas adecuado para leer datos sin procesar,
 * como imagenes.
 * </ol>
 * <p>
 * En cuanto al rendimiento, se sugiere que FileInputStream podria ser mas rapido debido a su estructura mas simple, aunque esto
 * no esta confirmado. La eleccion entre RandomAccessFile y las clases InputStream/OutputStream dependera de las necesidades
 * especificas de acceso y manipulacion de datos en el archivo.
 * <p>
 * Links:
 * <a href="http://tutorials.jenkov.com/java-io/randomaccessfile.html">Java RandomAccessFile</a>
 *
 * @author Juan Debenedetti
 */

public class RandomAccessFile {

    public static void main(String[] args) {

        List<Product> products = new ArrayList<>();

        products.add(new Product(1, "Product 1", 10.5, true));
        products.add(new Product(2, "Product 2", 20, false));
        products.add(new Product(3, "Product 3", 15.3, true));
        products.add(new Product(4, "Product 4", 10.5, false));

        try (java.io.RandomAccessFile raf = new java.io.RandomAccessFile(RAF, "rw")) { // r = read, w = write

            for (Product product : products) {

                raf.writeInt(product.getId());

                /* El metodo writeUTF() puede generar problemas de bytes y no son precisos, encambio el metodo writeChars()
                 * escribe caracter por caracter (2 bytes). */
                StringBuilder sb = new StringBuilder(product.getName());
                sb.setLength(10); // Estbalece un limite de 10 caracteres (?
                raf.writeChars(sb.toString());

                raf.writeDouble(product.getPrice());
                raf.writeBoolean(product.isSold());

            }

            System.out.println("File size: " + raf.length());
            System.out.println("Current position of file pointer after writing = " + raf.getChannel().position());

            /* Hasta ahora se escribieron 4 registros de 33 bytes cada uno, ya que el int para el id ocupa 4 bytes, cada caracter
             * de la cadena ocupa 2 bytes por lo tanto son 2x10 = 20 bytes, el double 8 bytes y el boolean 1 byte. Esto suma un
             * total de 132 bytes. */

            /* Posiciona el puntero del archivo en el byte 0, es decir en el primer registro, ya que la posicion del registro
             * quedo en el ultimo byte escrito. Si el puntero se posiciona en un byte que no sea el indice incial de un registro,
             * entonces lee datos erroneos. */
            raf.seek(0);

            System.out.println("id = " + raf.readInt());
            StringBuilder nombre = new StringBuilder();
            for (int i = 0; i < 10; i++)
                nombre.append(raf.readChar());
            System.out.println("name = " + nombre);
            System.out.println("price = " + raf.readDouble());
            System.out.println("sold = " + raf.readBoolean());

            /* Si nos fijamos en la carpeta dat de resources, podemos ver que el archivo raf.dat ocupa 140 bytes que son los 4
             * registros por los 35 bytes de los datos. */

        } catch (IOException e) {
            System.err.println("I/O error: " + e.getMessage());
        }

    }

    private static class Product {

        int id;
        String name;
        double price;
        boolean sold;

        public Product(int id, String name, double price, boolean sold) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.sold = sold;
        }

        public int getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public double getPrice() {
            return price;
        }

        public boolean isSold() {
            return sold;
        }

    }

}
