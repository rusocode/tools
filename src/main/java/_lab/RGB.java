package _lab;

/**
 * <h3>Representacion de colores</h3>
 * El valor RGB es un numero entero, por lo que se representa en la memoria por 4 bytes (o el equivalente a 32 bits).
 *
 * <p>Ejemplo:
 * <pre>{@code 00000001 00000010 00000011 00000100 }</pre>
 *
 * <p>Cada byte representa un componente del color:
 *
 * <ul>
 * <li>1er byte: valor alfa que corresponde a la opacidad
 * <li>2do byte: valor rojo
 * <li>3er byte: valor verde
 * <li>4to byte: valor azul
 * </ul>
 *
 * <h3>Simbolos 0xff y 0xffffff</h3>
 * 0xff representa el valor hexadecimal FF que es igual al entero 255. Su representacion binaria es:
 * <pre>{@code 00000000 00000000 00000000 11111111 }</pre>
 *
 * <p>De manera similar, 0xffffff esta representado por:
 * <pre>{@code 00000000 11111111 11111111 11111111 }</pre>
 *
 * <p>Corresponde al color blanco (rojo, verde y azul igual a 255).
 *
 * <br><br>
 *
 * <h3>¿Que hace "& 0xff"?</h3>
 * La cuestion es que obtienes el valor RGB en un entero, con un byte para cada componente. Algo asi como 0xAARRGGBB
 * (alfa, rojo, verde, azul). Al realizar un bit a bit y con 0xFF, conserva solo la ultima parte, que es azul. Para
 * otros canales, usaria:
 *
 * <pre>{@code
 * int alpha = (rgb >> 24) & 0xFF;
 * int red   = (rgb >> 16) & 0xFF;
 * int green = (rgb >>  8) & 0xFF;
 * int blue  = (rgb >>  0) & 0xFF;
 * }</pre>
 *
 * <p>En el caso alfa, puede omitir & 0xFF, porque no hace nada; lo mismo para cambiar por 0 en el caso azul.
 *
 * <br><br>
 *
 * <h3>Analizandolo mas en detalle</h3>
 *
 * <p>Cuando tiene un solo numero entero que contiene varios bytes de informacion, el enmascaramiento y el
 * desplazamiento son los procesos que se utilizan para acceder a las piezas individuales. Suponiendo que los bytes se
 * almacenen como tales (probablemente no lo esten, pero...), esto es lo que podria hacer para recuperarlos:
 *
 * <pre>{@code
 * aRGB: 255, 65, 33, 17
 * binary: 11111111 01000001 00100001 00010001
 * }</pre>
 *
 * <p>Para recuperar el valor rojo (65) de una variable x:
 *
 * <pre>{@code
 *
 * x & 0x00FF0000
 *
 *   11111111 01000001 00100001 00010001
 * * 00000000 11111111 00000000 00000000
 * -------------------------------------
 *   00000000 01000001 00000000 00000000
 * }</pre>
 *
 * <p>Luego, la operacion de cambio, para mover los bits a donde tengan sentido como un valor solitario:
 * <pre>{@code 00000000 01000001 00000000 00000000 >> 16 = 00000000 00000000 00000000 01000001 }</pre>
 *
 * <p>La mascara binaria captura solo el segundo byte del valor, estableciendo todos los demas bits en 0; solo los bits que
 * coinciden con el segundo byte permanecen como estan (multiplicados por 1, no por 0). Luego, cambia los bits a los 16
 * lugares correctos, para eliminar esos 0 adicionales. Obviamente, los 0 iniciales ya no importan, por lo que el
 * resultado es simplemente un 01000001 binario o un 65 decimal.
 *
 * <p>Recursos:
 * <a href="https://stackoverflow.com/questions/2615522/java-bufferedimage-getting-red-green-and-blue-individually">Java BufferedImage obteniendo rojo, verde y azul individualmente</a>
 * <a href="https://stackoverflow.com/questions/6126439/what-does-0xff-do">¿Que hace "& 0xff"?</a>
 * <a href="https://stackoverflow.com/questions/8436196/explaining-bitwise-shifting-argb-values-in-java">Explicacion de los valores aRGB de cambio bit a bit en Java</a>
 */

public class RGB {


}
