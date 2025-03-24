package com.punkipunk.functional.v5_flujo;

import com.punkipunk.functional.v5_flujo.interfaces.*;

import java.util.Random;

/**
 * Los metodos pueden operar de manera encadenana ya que todos retornan un flujo, menos el ultimo metodo calculate() que
 * es una operacion terminal.
 */

public class Launcher {

    public Launcher() {
        // Encadenamiento de metodos del flujo
        Integer total = Flujo.get(10, new Getter<Integer>() {
            final Random random = new Random();

            @Override
            public Integer get() {
                return random.nextInt(10);
            }
        }).filter(new Predicate<>() {
            @Override
            public boolean test(Integer value) {
                return value % 2 == 0;
            }
        }).transform(new OperadorUnario<>() {
            @Override
            public Integer transform(Integer value) {
                return value * value;
            }
        }).actuar(new Consumer<>() {
            @Override
            public void consume(Integer value) {
                System.out.println(value);
            }
        }).calculate(0, new OperadorBianario<>() {
            @Override
            public Integer calculate(Integer value1, Integer value2) {
                return value1 + value2;
            }
        });
        System.out.println("Suma: " + total);
    }

    public static void main(String[] args) {
        new Launcher();
    }

}
