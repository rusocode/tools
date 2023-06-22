package rxjava.operators;

import io.reactivex.rxjava3.core.Observable;

/**
 * El operador contains es muy util para verificar si un Observable contiene un objeto especifico o no.
 */

public class Contains {

    public static void main(String[] args) {
        // containsWithPremitive();
        containsWithNonPremitive();
    }

    /**
     * El operador contains comprueba si el numero existe en la emision observable. Tan pronto como obtiene el elemento,
     * emite true o falso de lo contrario.
     */
    private static void containsWithPremitive() {
        Observable.just(1, 2, 3, 4, 5)
                .contains(3)
                .subscribe(System.out::println);
    }

    /**
     * El operador contains comprueba si el objeto especificado existe en la emision observable en funcion del codigo
     * hash del objeto. Tan pronto como obtiene el elemento, emite true o falso en caso contrario.
     */
    private static void containsWithNonPremitive() {
        User user = new User("mroydroid");
        Observable.just(user)
                .contains(user)
                .subscribe(System.out::println);
    }

    /**
     * Clase estatica para fines de demostracion.
     */
    private static class User {
        String name;

        User(String name) {
            this.name = name;
        }
    }

}
