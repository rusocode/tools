package concurrency;

/**
 * Java sucede antes de la garantia es un conjunto de reglas que rigen como se permite que la VM y la
 * CPU de Java reordenen las instrucciones para mejorar el rendimiento. La garantia de que sucede antes
 * hace posible que los subprocesos confien en cuando se sincroniza un valor de variable hacia o desde
 * la memoria principal, y que otras variables se han sincronizado al mismo tiempo. Java sucede antes de
 * que la garantia se centre en el acceso a variables volatiles y variables a las que se accede desde
 * dentro de bloques sincronizados.
 * <p>
 * Fuentes: <a href="https://jenkov.com/tutorials/java-concurrency/java-happens-before-guarantee.html">jenkov.com</a>
 */

public class VisibilityGuarantee {

	/* -Reordenacion de instrucciones
	 * Las CPU modernas tienen la capacidad de ejecutar instrucciones en paralelo si las instrucciones no dependen
	 * unas de otras. Por ejemplo, las siguientes dos instrucciones no dependen una de la otra y, por lo tanto,
	 * pueden ejecutarse en paralelo: */

	// int a, b = 1, c = 2, d, e = 2, f = 3, l = 1, m = 3, n = 2, y = 1, x = 3, z = 2;

	// a = b + c;
	// d = e + f;

	/* Sin embargo, las siguientes dos instrucciones no pueden ejecutarse facilmente en paralelo, porque la segunda
	 * instruccion depende del resultado de la primera instruccion: */

	// a = b + f;
	// d = a + e;

	/* Imagine que las dos instrucciones anteriores fueran parte de un conjunto mas grande de instrucciones,
	 * como el siguiente: */

	// a = b + c;
	// d = a + e;
	// l = m + n;
	// y = x + z;

	/* Las instrucciones se pueden reordenar como se muestra a continuacion. Luego, la CPU puede ejecutar al menos
	 * las primeras 3 instrucciones en paralelo, y tan pronto como finalicen las primeras instrucciones, puede comenzar
	 * a ejecutar la cuarta instruccion. */

	// a = b + c;
	// l = m + n;
	// y = x + z;
	// d = a + e;

	/* Como puede ver, reordenar las instrucciones puede aumentar la ejecucion paralela de instrucciones en la CPU. Una mayor
	 * paralelizacion significa un mayor rendimiento.
	 *
	 * ver "Problemas de reordenamiento de instrucciones en computadoras con multiples CPU" */

	/* -La garantia de visibilidad volatil de Java
	 * La palabra clave Java volatile proporciona algunas garantias de visibilidad para cuando las escrituras y lecturas de
	 * variables volatiles dan como resultado la sincronizacion del valor de la variable hacia y desde la memoria principal.
	 * Esta sincronizacion hacia y desde la memoria principal es lo que hace que el valor sea visible para otros subprocesos.
	 * De ahi el termino garantia de visibilidad.
	 *
	 * En esta seccion, cubrire brevemente la garantia de visibilidad volatil de Java y explicare como el reordenamiento de
	 * instrucciones puede romper la garantia de visibilidad volatil. Es por eso que tambien tenemos la volatilidad de Java
	 * antes de la garantia, para colocar algunas restricciones en el reordenamiento de instrucciones para que la garantia
	 * de visibilidad volatil no se rompa por la reordenacion de instrucciones. */

	/* -La garantia de visibilidad de escritura volatil de Java
	 * Cuando escribe en una variable de Java volatile, se garantiza que el valor se escribira directamente en la memoria
	 * principal. Ademas, todas las variables visibles para el subproceso que escribe en la variable volatil tambien se
	 * sincronizaran con la memoria principal.
	 *
	 * Para ilustrar la garantia de visibilidad de escritura volatil de Java, mire este ejemplo:
	 * this.nonVolatileVarA = 34;
	 * this.nonVolatileVarB = new String("Text");
	 * this.volatileVarC = 300;
	 *
	 * Este ejemplo contiene dos escrituras en variables no volatiles y una escritura en una variable volatil.
	 *
	 * Cuando la tercera instruccion en el ejemplo anterior escribe en la variable volatil volatileVarC, los valores de las dos
	 * variables no volatiles tambien se sincronizaran con la memoria principal, porque estas variables son visibles para el
	 * subproceso cuando escribe en la variable volatil. */

	/* -La garantia de visibilidad de lectura volatil de Java
	 * Cuando lee el valor de un Java volatile, se garantiza que el valor se lea directamente desde la memoria. Ademas, todas
	 * las variables visibles para el subproceso que lee la variable volatil tambien tendran sus valores actualizados desde
	 * la memoria principal.
	 *
	 * Para ilustrar la garantia de visibilidad de lectura volatil de Java, mire este ejemplo:
	 * c = other.volatileVarC;
	 * b = other.nonVolatileB;
	 * a = other.nonVolatileA;
	 *
	 * Observe que la primera instruccion es una lectura de una variable volatil (other.volatileVarC). Cuando se lee
	 * other.volatileVarC desde la memoria principal, other.nonVolatileB y other.nonVolatileA tambien se leen desde la memoria principal. */

}
