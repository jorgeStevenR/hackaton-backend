package com.hackathon.tutormatch.domain.model;

import com.hackathon.tutormatch.domain.exception.DatosInvalidosException;

import java.text.Normalizer;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Pattern;

/** Utilidades de texto del dominio. */
public final class Textos {

    /** "DIA-HORA": dias LUN..VIE, horas 08, 10, 14, 16. */
    private static final Pattern FORMATO_HORARIO = Pattern.compile("(LUN|MAR|MIE|JUE|VIE)-(08|10|14|16)");

    private Textos() {
    }

    /** Minusculas y sin tildes: " Cálculo " -> "calculo". */
    public static String normalizar(String texto) {
        if (texto == null) {
            return "";
        }
        return Normalizer.normalize(texto.trim(), Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toLowerCase(Locale.ROOT);
    }

    public static boolean estaVacio(String texto) {
        return texto == null || texto.isBlank();
    }

    /** Limpia espacios, descarta vacios y conserva el orden. */
    public static Set<String> limpiar(Collection<String> valores) {
        Set<String> resultado = new LinkedHashSet<>();
        if (valores != null) {
            for (String valor : valores) {
                if (!estaVacio(valor)) {
                    resultado.add(valor.trim());
                }
            }
        }
        return Collections.unmodifiableSet(resultado);
    }

    /** Pasa a mayusculas y valida el formato DIA-HORA. */
    public static Set<String> horariosValidos(Collection<String> horarios) {
        Set<String> resultado = new LinkedHashSet<>();
        for (String horario : limpiar(horarios)) {
            String normalizado = horario.toUpperCase(Locale.ROOT);
            if (!FORMATO_HORARIO.matcher(normalizado).matches()) {
                throw new DatosInvalidosException("Horario invalido '" + horario
                        + "'. Formato DIA-HORA, dias LUN..VIE y horas 08, 10, 14 o 16");
            }
            resultado.add(normalizado);
        }
        if (resultado.isEmpty()) {
            throw new DatosInvalidosException("Debe indicar al menos un horario");
        }
        return Collections.unmodifiableSet(resultado);
    }
}
