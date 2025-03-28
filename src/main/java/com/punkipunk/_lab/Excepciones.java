package com.punkipunk._lab;

import java.awt.Image;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Excepciones {

    public static void main(String[] args) {

        Integer[] n = {4, 3, 2, 1};

        print(n);
        excepcionNoControlada(n);
        excepcionControlada();
        try {
            usoThrows();
        } catch (IOException /* o Exception (clase generica) */ e) {
            System.out.println("\nLa imagen no se encuentra.");
        }
    }

    private static void print(Integer[] n) {
        System.out.print("Array: ");
        for (Integer integer : n) System.out.print(integer + " ");
    }

    // Mala practica de programacion
    private static void excepcionNoControlada(Integer[] n) {
        int menor = n[0];

        try {
            for (int i = 0; i <= n.length; i++)
                if (menor > n[i]) menor = n[i];
            System.out.println("\nEl menor es: " + menor);
        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("\nEl array se paso del limite.");
        }
    }

    private static void excepcionControlada() {
        try {
            Image imagenBola = ImageIO.read(new File("src/img/bola.png"));
            System.out.println("\nImagen encontrada.");
        } catch (IOException e) {
            System.out.println("\nLa imagen no se encuentra.");
        }
    }

    private static void usoThrows()
            throws IOException { /* La clausula throws indica que el metodo va a contener una excepcion de tipo IOException. Si fuera una excepcion no
     * comproba (RuntimeException), entonces no estariamos obligados a implementar el bloque try{...}catch(){}. */
        Image imagenBola = ImageIO.read(new File("src/img/bola.png"));
        System.out.println("\nImagen encontrada.");

    }

}
