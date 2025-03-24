package com.punkipunk._lab;

import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * <p>
 * Crea un array de enteros aleatorios entre 1 y 100, con un tamaño especificado entre 1 y 50 (ambos incluidos). Si el tamaño es
 * menor o igual a 0 o mayor a 50, entonces por defecto se inicializa en 4.
 * <p>
 * Esta clase cuenta con diferentes funciones para poder jugar con el array.
 *
 * @author Juan Debenedetti
 */

public class Arr {

    private static final Scanner input = new Scanner(System.in);

    public static void main(String[] args) {

        int size = 0;

        do {

            try {
                System.out.print("Size or -1 to exit: ");
                size = input.nextInt();
            } catch (InputMismatchException e) {
                System.err.println("La entrada recuperada no coincide con el patron para el tipo esperado!");
                System.exit(1);
            }

            if (size != -1) {
                int[] arr = init(size <= 0 || size > 50 ? 4 : size);
                System.out.println(toString(arr, 0));
                // sortAsc(arr);
                // sortDesc(arr);
                mayor(arr);
                // menor(arr);
                // promedio(arr);
                // pares(arr);
                // impares(arr);
                // desordenar(arr);
                // contador(arr);
            }

        } while (size != -1);

    }

    private static int[] init(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < arr.length; i++)
            arr[i] = (int) (Math.random() * 100 + 1);
        return arr;
    }

    /**
     * Ordena el array de forma ascendente.
     * <p>
     * El primer for controla las pasadas y el segundo el intercambio. Si arr es el array a ordenar, se realizan arr.length-1
     * pasadas. Si la variable i es la que cuenta el numero de pasadas, en cada pasada i se comprueban los elementos adyacentes
     * desde el primero hasta arr.length-i-1 ya que el resto hasta el final del array estan ordenados. Si los elementos adyacentes
     * estan desordenados se intercambian. El tiempo de ejecucion del algoritmo de la burbuja es del orden O(n2). Es uno de los
     * peores algoritmos de ordenacion en cuanto a tiempo de ejecucion, solamente es recomendable su uso para ordenar listas con
     * un numero pequeño de elementos.
     *
     * @param arr array.
     */
    private static void sortAsc(int[] arr) {
        System.out.println("ordenando de manera ascendente...");
        int aux;

        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] > arr[j + 1]) {
                    aux = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = aux;
                }
            }
        }

        // Otro forma de ordenar el vector
        // Arrays.sort(n);

        System.out.println(toString(arr, 0));

    }

    private static void sortDesc(int[] arr) {
        System.out.println("ordenando de manera descendente...");
        int aux;
        for (int i = 0; i < arr.length - 1; i++) { // Se itera hasta arr.length - 1 para evitar que el array se salga de
            // los limites
            for (int j = 0; j < arr.length - 1 - i; j++) {
                if (arr[j] < arr[j + 1]) {
                    aux = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = aux;
                }
            }
        }

        System.out.println(toString(arr, 0));
    }

    private static void mayor(int[] arr) {
        int mayor = 0;
        for (int j : arr)
            if (mayor < j) mayor = j;
        System.out.println("El mayor es: " + mayor);
    }

    private static void menor(int[] arr) {
        int menor = arr[0];

        for (int j : arr)
            if (menor > j) menor = j;

        System.out.println("El menor es: " + menor);
    }

    private static void promedio(int[] arr) {
        int suma = 0;
        for (int j : arr) suma += j;

        System.out.println("Promedio: " + (suma / arr.length));
    }

    private static void pares(int[] arr) {
        System.out.print("Pares:");

        for (int j : arr)
            if (j % 2 == 0) System.out.print(" " + j);

        System.out.println();
    }

    private static void impares(int[] arr) {
        System.out.print("Impares:");

        for (int j : arr)
            if (j % 2 == 1) System.out.print(" " + j);
    }

    private static void desordenar(int[] arr) {
        System.out.println("\ndesordenando...");

        int posicionAleatoria, posicionActual;

        for (int i = 0; i < arr.length; i++) {
            // Asigna el indice aleatorio
            posicionAleatoria = (int) (Math.random() * arr.length); /* posicionAleatoria = r.nextInt(n.length); */
            // Asigna el indice actual (variable temporal)
            posicionActual = arr[i];

            // SWAPPING
            // Intercambia el valor de la posicion actual por el valor de la posicion aleatoria
            arr[i] = arr[posicionAleatoria];
            // Intercambia el valor de la posicion aleatoria por el valor de la posicion actual
            arr[posicionAleatoria] = posicionActual;

        }

        System.out.println(toString(arr, 0));
    }

    private static void contador(int[] arr) {
        int c = 0, numero;
        do {
            System.out.print("\nNumero que desea contar (-1 salir): ");
            numero = input.nextInt();
            if (numero != -1) {
                for (int j : arr)
                    if (j == numero) c++;
                System.out.println("El " + numero + " se repite " + c + " veces.");
                c = 0;
            }

        } while (numero != -1);
    }

    private static String toString(int[] arr, int i) {
        return i < arr.length ? (i == 0 ? "Array: [" : "") + arr[i] + (i == arr.length - 1 ? "]" : ", ") + toString(arr, i + 1) : "";
    }

}
