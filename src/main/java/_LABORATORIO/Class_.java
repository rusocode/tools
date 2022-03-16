package _LABORATORIO;

public class Class_ {

	public static void main(String[] args) {

		try {

			/* Crea una nueva instancia de la clase representada por el objeto Class. */
			Thread t = (Thread) Class.forName("java.lang.Thread").getDeclaredConstructor().newInstance();
			/* Esta forma de crear una instancia esta en desuso. */
			// Thread t = (Thread) Class.forName("java.lang.Thread").newInstance();
			System.out.println(t.getId());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
