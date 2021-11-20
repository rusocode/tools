package _LABORATORIO;

public class MetodosStrings {

	public static void main(String[] args) {

		String s = "quemado";

		// Reemplaza la cadena omitiendo los dos primeros y ultimos caracteres
		System.out.println(s.replaceAll(s.substring(2, s.length() - 2), "java"));

		// String str = "Estoy quemado";

		// Devuelve el caracter del indice especificado
		// System.out.println(str.charAt(0));
		/* Devuelve el indice dentro de esta cadena de la primera aparicion de la subcadena especificada o -1 si no existe. */
		// System.out.println(str.indexOf('o'));
		/* Devuelve el indice dentro de esta cadena de la primera aparicion de la subcadena especificada o -1 si no existe,
		 * comenzando en el indice especificado. */
		// System.out.println(str.indexOf('o', 4));

	}

}
