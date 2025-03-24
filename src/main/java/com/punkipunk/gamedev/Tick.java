package com.punkipunk.gamedev;

/**
 * <h1>¿Que es un tick?</h1>
 * Un tick es una unidad de medida de tiempo, especificamente se refiere a una sola instancia de una accion repetida
 * (generalmente una accion amplia) en un juego, o el periodo de tiempo que consume esa accion. Los ticks se repiten y
 * (en su mayoria) son regulares; el termino proviene de uno de los sonidos que hace el reloj ("tick" y "tock").
 * <p>
 * Normalmente, un tick es una iteracion del Game Loop. Se puede decir, por ejemplo, que la logica del juego "marca
 * (tick)" una vez por frame, o que "durante el tick, las posiciones de los personajes se actualizan". Estas iteraciones
 * se ejecutan por lo general desde un ciclo while.
 * <br><br>
 * <h2>Ciclo While</h2>
 * El ciclo while busca ejecutarse a maxima velocidad, pero esto puede variar por factores como el codigo maquina, uso
 * de la CPU, velocidad del procesador y la forma en la que se ejecuta el ciclo (codigo fuente). El metodo {@link Tick#iterate()}
 * itera el ciclo a maxima velocidad cada 1 segundo.
 * <p>
 * Se advierte que no se debe confiar en la velocidad del ciclo para medir el tiempo, y en su lugar, se recomienda usar
 * temporizadores (timers).
 * <br><br>
 * Recursos:
 * <a href="https://gamedev.stackexchange.com/questions/81608/what-is-a-tick-in-the-context-of-game-development#:~:text=A%20tick%20is%20a%20unit,%22%20and%20%22tock%22">¿Que es un "tick" en el contexto del desarrollo de juegos?</a>
 * <a href="https://minecraft.fandom.com/wiki/Tick#:~:text=Advertisement-,Game%20tick,24000%20ticks%2C%20or%2020%20minutes">Tick en Minecraft</a>
 * <a href="https://www.quora.com/What-is-the-amount-of-times-that-a-while-loop-runs-per-second-in-C">¿Cual es la cantidad de veces que se ejecuta un ciclo while() por segundo en C#?</a>
 *
 * @author Juan Debenedetti
 */

public class Tick {

    /**
     * Muestra la cantidad de veces que se ejecuta el ciclo while por segundo usando {@code currentTimeMillis()} para
     * condicionar el tiempo.
     * <p>
     * Nota: El uso de {@code nanoTime()} es mas costoso (mas de 100 relojes de CPU) pero mas preciso, por lo tanto el
     * ciclo hara menos ejecuciones.
     */
    private static void iterate() {
        int c = 0;
        final int MILLISECONDS_PER_SECOND = 1000;
        final double NANOSECONDS_PER_SECOND = 1e9; // 1_000_000_000
        long startTime = System.nanoTime();
        while (true) {
            c++;
            if (System.nanoTime() - startTime >= NANOSECONDS_PER_SECOND) {
                startTime += (long) NANOSECONDS_PER_SECOND;
                System.out.println(c + " iteraciones en un segundo!");
                c = 0;
            }
        }
    }

    public static void main(String[] args) {
        iterate();
    }

}
