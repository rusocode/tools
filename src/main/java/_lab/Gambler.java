package _lab;

import java.util.Scanner;

public class Gambler {

    private int gold = 4000;

    private static final int MIN_BET = 10;
    private static final int MAX_BET = 5000;
    private static final int PROBABILITY = 47; // 47% de probabilidades en ganar oro

    public static void main(String[] args) {
        Gambler gambler = new Gambler();
        int op;

        System.out.println("Gold: " + gambler.getGold());

        System.out.print("Bet 1 (yes) - 0 (not): ");
        op = new Scanner(System.in).nextInt();

        while (op != 0) {
            if (op == 1) {
                System.out.print("/BET ");
                gambler.bet(new Scanner(System.in).nextInt());
            } else System.out.println("Wrong option!");
            System.out.print("Bet 1 (yes) - 0 (not): ");
            op = new Scanner(System.in).nextInt();
        }

    }

    private void bet(int cant) {
        if (cant < MIN_BET) {
            System.out.println("The min bet is " + MIN_BET + " gold coins.");
            return;
        }
        if (cant > MAX_BET) {
            System.out.println("The max bet is " + MAX_BET + " gold coins.");
            return;
        }
        if (cant > gold) {
            System.out.println("You don't have that amount.");
            return;
        }
        if (random(1, 100) <= PROBABILITY) {
            addGold(cant);
            System.out.println("You have won " + cant + " gold coins!");
        } else {
            removeGold(cant);
            System.out.println("Sorry, you have lost " + cant + " gold coins.");
        }

        System.out.println("Gold: " + getGold());

    }

    /**
     * Devuelve un numero entero aleatorio entre un minimo y un mayor especificado.
     *
     * @param min valor minimo.
     * @param max valor maximo.
     */
    private int random(int min, int max) {
        return (int) ((Math.random() * (max - min + 1)) + min);
    }

    private void addGold(int cant) {
        this.gold += cant;
    }

    private void removeGold(int cant) {
        this.gold -= cant;
    }

    private int getGold() {
        return this.gold;
    }

}
