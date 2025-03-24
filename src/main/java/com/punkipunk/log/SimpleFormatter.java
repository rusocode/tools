package com.punkipunk.log;

import java.text.SimpleDateFormat;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * IMPORTANTE: esta clase es obsoleta en comparacion con la API Log4j.
 * <p>
 * Imprime un breve resumen del registro en un formato legible por humanos. El resumen suele ser de 1 o 2 lineas.
 *
 * @author Juan Debenedetti
 */

public class SimpleFormatter extends Formatter {

    // Formato para la fecha del registro
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd-HH:mm:ssZ");

    /**
     * Formatea el mensaje.
     *
     * @param register mensaje sin procesar del registro.
     * @return cadena formateada.
     */
    @Override
    public String format(LogRecord register) {
        return register.getLevel() + ";" +
                // Formatea la fecha del registro en "yyyy/MM/dd-HH:mm:ssZ"
                sdf.format(register.getMillis()) + ";" +
                // Obtiene el nombre de la clase que (supuestamente) emitio la solicitud de registro
                register.getSourceClassName() + ";" +
                // Obtiene el nombre del metodo que (supuestamente) emitio la solicitud de registro
                register.getSourceMethodName() + ";" +
                // Devuelve un mensaje localizado y formateado
                formatMessage(register) + "\n";
    }

}
