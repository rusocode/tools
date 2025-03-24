package com.punkipunk._lab;

public class Instance {

    public static void main(String[] args) {

        ClassInstance.setName("Ruso");
        ClassInstance.printName();

        ObjectInstance obj = new ObjectInstance("Juan");
        obj.printName();

    }

}

class ClassInstance {

    // Atributo de clase que es compartido por todos los objetos de esta clase
    private static String name = "Rulo";

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        ClassInstance.name = name; // No se puede usar this en un contexto estatico
    }

    public static void printName() {
        System.out.println(name);
    }

}

class ObjectInstance {

    // Atributo de instancia
    private String name;

    ObjectInstance(String name) {
        this.name = name;
        // this.name = ClassInstance.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void printName() {
        System.out.println(name);
    }

}
