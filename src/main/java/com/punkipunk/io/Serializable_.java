package com.punkipunk.io;

import java.io.*;

/**
 * <p>
 * La serializacion consiste en convertir un objeto en bytes con el objetivo de distribuirlo a travez de la red a ordenadores
 * remotos y que en ese ordenador el objeto serializado sea restablecido al estado en el que se serializo.
 * <p>
 * El <b>serialVersionUID</b> es un identificador unico asociado con las clases serializables en Java. Aqui te explico sus
 * principales caracteristicas y propositos:
 * <ol>
 * <li>Definicion: Es un campo {@code static final long} que se utiliza durante la deserializacion para verificar que el remitente
 * y el receptor de un objeto serializado tienen clases compatibles para ese objeto.
 * <li>Proposito principal: Asegura la consistencia entre las versiones serializadas y deserializadas de una clase.
 * <li>Funcionamiento:
 * <ul>
 * <li>Cuando se serializa un objeto, Java incluye el serialVersionUID de la clase en los datos serializados.
 * <li>Durante la deserializacion, Java compara el serialVersionUID almacenado con el de la clase actual.
 * <li>Si no coinciden, se lanza una InvalidClassException.
 * </ul>
 * <li>Generacion:
 * <ul>
 * <li>Si no se declara explicitamente, Java genera uno automaticamente basado en varios aspectos de la clase.
 * <li>Sin embargo, se recomienda declararlo explicitamente para evitar problemas de compatibilidad.
 * </ul>
 * <li>Declaracion:
 * {@code private static final long serialVersionUID = 1L;}
 * <li>Cuando cambiarlo:
 * <ul>
 * <li>Se debe incrementar cuando se realizan cambios incompatibles en la estructura de la clase.
 * <li>Cambios menores o compatibles no requieren modificar el serialVersionUID.
 * </ul>
 * <li>Ventajas de declararlo explicitamente:
 * <ul>
 * <li>Control sobre la compatibilidad de versiones.
 * <li>Evita que cambios menores en la clase (que no afectan la serializacion) generen un nuevo UID automatico.
 * </ul>
 * <li>Consideraciones de seguridad:
 * <ul>
 * <li>Ayuda a prevenir ataques de deserializacion maliciosa al proporcionar un nivel adicional de verificacion.
 * </ul>
 * <li>Herramientas: IDEs como IntelliJ IDEA o Eclipse pueden generar automaticamente un serialVersionUID para clases
 * serializables.
 * </ol>
 * <p>
 * En resumen, el serialVersionUID es una herramienta crucial para gestionar la compatibilidad y seguridad en la serializacion de
 * objetos en Java. Su uso adecuado ayuda a mantener la integridad de los datos serializados a lo largo del tiempo y entre
 * diferentes versiones de una aplicacion.
 */

public class Serializable_ {

    public static void main(String[] args) {

        Admin jose = new Admin("Jose", 50000, 56);
        jose.setIncentive(5000);

        Employee[] employees = new Employee[3];
        employees[0] = jose; // Polimorfismo = es un = principio de sustitucion
        employees[1] = new Employee("Rulo", 26, 25000);
        employees[2] = new Employee("Miguel", 44, 10000);

        Employee ale = new Employee("Ale", 25000, 26);
        System.out.println(ale);
        System.out.println("Aumento del 20% para el empleado " + ale.getName());
        ale.increaseSalary(20);
        System.out.println(ale);

        try {

            ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream("C:/users/juand/Desktop/employee.dat"));
            output.writeObject(employees);
            output.close();

            ObjectInputStream input = new ObjectInputStream(new FileInputStream("C:/users/juand/Desktop/employee.dat"));
            Employee[] copyInput = (Employee[]) input.readObject();
            input.close();

            for (Employee e : copyInput)
                System.out.println(e);

        } catch (IOException | ClassNotFoundException e) {
            System.err.println("I/O error: " + e.getMessage());
        }

    }

}

class Employee implements Serializable { // Clase lista para convertirla a bytes

    @Serial
    private static final long serialVersionUID = 1L;

    protected final String name;
    private final int age;
    private double salary;

    public Employee(String name, int age, double salary) {
        this.name = name;
        this.age = age;
        this.salary = salary;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public double getSalary() {
        return salary;
    }

    public void increaseSalary(double percentage) {
        salary += salary * percentage / 100;
    }

    @Override
    public String toString() {
        return "Employee [name=" + name + ", age=" + age + ", salary=" + salary + "]";
    }

}

class Admin extends Employee {

    @Serial
    private static final long serialVersionUID = 1L;

    private double incentive;

    public Admin(String name, int age, double salary) {
        super(name, age, salary);
        incentive = 0;
    }

    public double getSalary() {
        return super.getSalary() + incentive;
    }

    public void setIncentive(double incentive) {
        this.incentive = incentive;
    }

    @Override
    public String toString() {
        return "Admin [name=" + name + ", incentive=" + incentive + "]";
    }

}
