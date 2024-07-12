package _lab;

/**
 * Conversor de binarios a decimales y de decimales a binarios.
 * <p>
 * Un byte es comparable a una pequeña caja que almacena un numero entre 0 y 255, equivalente a 8 bits. La memoria RAM está
 * compuesta de celdas de un byte. Por ejemplo, 1 GB de RAM contiene aproximadamente 1,073,741,824 de estas "cajas".
 * <p>
 * Cada celda de byte tiene 8 espacios para bits, con valores posicionales basados en potencias de 2 (de derecha a izquierda: 2^0
 * = 1 hasta 2^7 = 128). Un bit '1' suma su valor posicional, mientras un '0' lo ignora.
 * <p>
 * Para representar numeros mayores, se utilizan multiples bytes en conjunto.
 * <p>
 * Links:
 * <a href="https://masterplc.com/calculadora/convertir-binario-a-decimal/">Conversor</a>
 */

public class BinaryConverter {

    private static final int BINARY_LENGTH = 8, BASE = 2, LAST_BIT_POSITION = 7;

    /**
     * Genera un binario aleatorio.
     *
     * @return un binario aleatorio.
     */
    private static byte[] generateRandomBinary() {
        byte[] binary = new byte[BINARY_LENGTH];
        for (int i = 0; i < BINARY_LENGTH; i++)
            binary[i] = (byte) (Math.random() * 2); // Genera un bit aleatorio entre 0 y 1
        System.out.print("Binario > ");
        for (byte bit : binary)
            System.out.print(bit);
        return binary;
    }

    /**
     * Convierte el binario a decimal.
     *
     * @param binary binario.
     * @return el decimal.
     */
    private static int convertBinaryToDecimal(byte[] binary) {
        int decimal = 0;
        for (int i = binary.length - 1; i >= 0; i--)
            if (binary[i] == 1) decimal += (int) Math.pow(BASE, calculateExp(i)); // Calcula la potencia del bit actual
        return decimal;
    }

    /**
     * Convierte el decimal a binario.
     *
     * @param decimal decimal.
     * @return el binario.
     */
    private static String convertDecimalToBinary(int decimal) {
        StringBuilder binary = new StringBuilder(), reverse = new StringBuilder();
        for (int i = 0; i < BINARY_LENGTH; i++) {
            if (decimal % 2 == 0) binary.append("0");
            else binary.append("1");
            decimal /= 2;
        }
        for (int i = BINARY_LENGTH - 1; i >= 0; i--)
            reverse.append(binary.charAt(i));
        return reverse.toString();
    }

    /**
     * Calcula el exponente del bit.
     *
     * @param i indice del bit.
     * @return el exponente del bit.
     */
    private static int calculateExp(int i) {
        return LAST_BIT_POSITION - i;
    }

    public static void main(String[] args) {
        // System.out.println("\nDecimal > " + convertBinaryToDecimal(generateRandomBinary()));
        // System.out.println("Decimal > " + convertBinaryToDecimal(new byte[]{0, 0, 0, 0, 0, 1, 0, 0}));
        // System.out.println(convertDecimalToBinary(2));
    }

}
