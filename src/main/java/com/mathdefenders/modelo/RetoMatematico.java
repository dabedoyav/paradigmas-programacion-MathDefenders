package com.mathdefenders.modelo;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Representa un desafío aritmético inmutable compuesto por una expresión
 * matemática, su resultado exacto, cuatro opciones únicas de respuesta y el
 * nivel de dificultad al que pertenece.
 *
 * @author Diego Buitrago González
 * @version 1.0
 */
public final class RetoMatematico {

    /**
     * Número fijo de botones de opción múltiple (A, B, C y D) por cada reto.
     */
    public static final int CANTIDAD_OPCIONES = 4;

    /**
     * Expresión matemática legible presentada al jugador.
     */
    private final String expresion;

    /**
     * Resultado entero exacto de la expresión matemática.
     */
    private final int respuestaCorrecta;

    /**
     * Lista inmutable de exactamente cuatro opciones numéricas distintas.
     */
    private final List<Integer> opciones;

    /**
     * Nivel de dificultad al que corresponde este reto.
     */
    private final NivelDificultad nivel;

    /**
     * Construye un reto matemático validando que la expresión sea no vacía, que
     * existan exactamente cuatro opciones únicas sin valores nulos y que la
     * respuesta correcta forme parte de dichas opciones.
     *
     * @param expresion texto de la expresión matemática.
     * @param respuestaCorrecta resultado exacto de la operación.
     * @param opciones lista de cuatro opciones posibles de respuesta.
     * @param nivel nivel de dificultad asociado al reto.
     * @throws IllegalArgumentException si algún parámetro es inválido o las opciones no cumplen las reglas.
     */
    public RetoMatematico(
            String expresion,
            int respuestaCorrecta,
            List<Integer> opciones,
            NivelDificultad nivel) {
        if (expresion == null || expresion.trim().isEmpty()) {
            throw new IllegalArgumentException("La expresión del reto no puede ser nula ni estar vacía.");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel del reto no puede ser nulo.");
        }
        if (opciones == null || opciones.size() != CANTIDAD_OPCIONES) {
            throw new IllegalArgumentException("El reto debe contener exactamente " + CANTIDAD_OPCIONES + " opciones.");
        }
        for (Integer opcion : opciones) {
            if (opcion == null) {
                throw new IllegalArgumentException("Las opciones del reto no pueden contener valores nulos.");
            }
        }
        Set<Integer> opcionesUnicas = new HashSet<>(opciones);
        if (opcionesUnicas.size() != CANTIDAD_OPCIONES) {
            throw new IllegalArgumentException("Las cuatro opciones del reto deben ser distintas entre sí.");
        }
        if (!opcionesUnicas.contains(respuestaCorrecta)) {
            throw new IllegalArgumentException("La respuesta correcta debe estar incluida dentro de las opciones.");
        }

        this.expresion = expresion.trim();
        this.respuestaCorrecta = respuestaCorrecta;
        this.opciones = List.copyOf(opciones);
        this.nivel = nivel;
    }

    /**
     * Obtiene la expresión aritmética del reto.
     *
     * @return texto de la expresión matemática.
     */
    public String obtenerExpresion() {
        return expresion;
    }

    /**
     * Obtiene el valor entero exacto que resuelve la expresión.
     *
     * @return respuesta correcta del reto.
     */
    public int obtenerRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    /**
     * Obtiene la lista inmutable de las cuatro opciones de respuesta.
     *
     * @return lista inmutable de cuatro enteros.
     */
    public List<Integer> obtenerOpciones() {
        return opciones;
    }

    /**
     * Obtiene el nivel de dificultad al que pertenece el reto.
     *
     * @return nivel de dificultad.
     */
    public NivelDificultad obtenerNivel() {
        return nivel;
    }

    /**
     * Obtiene el valor numérico de la opción ubicada en el índice indicado.
     *
     * @param indice posición entre 0 (A) y 3 (D).
     * @return valor numérico de la opción.
     * @throws IllegalArgumentException si el índice está fuera del rango de 0 a 3.
     */
    public int obtenerOpcion(int indice) {
        validarIndiceOpcion(indice);
        return opciones.get(indice);
    }

    /**
     * Obtiene la letra identificadora (A, B, C o D) correspondiente al índice indicado.
     *
     * @param indice posición entre 0 y 3.
     * @return letra de la opción ('A', 'B', 'C' o 'D').
     * @throws IllegalArgumentException si el índice está fuera del rango de 0 a 3.
     */
    public char obtenerLetraOpcion(int indice) {
        validarIndiceOpcion(indice);
        return (char) ('A' + indice);
    }

    /**
     * Obtiene el índice (0 a 3) en el que se encuentra la respuesta correcta.
     *
     * @return posición de la respuesta correcta dentro de {@link #obtenerOpciones()}.
     */
    public int obtenerIndiceRespuestaCorrecta() {
        return opciones.indexOf(respuestaCorrecta);
    }

    /**
     * Verifica si el valor elegido por el jugador coincide con la respuesta correcta.
     *
     * @param valorSeleccionado valor seleccionado por el jugador.
     * @return {@code true} si coincide con la respuesta correcta; {@code false} en caso contrario.
     */
    public boolean esRespuestaCorrecta(int valorSeleccionado) {
        return valorSeleccionado == respuestaCorrecta;
    }

    /**
     * Verifica si el valor indicado pertenece al conjunto de las cuatro opciones del reto.
     *
     * @param valorSeleccionado valor a comprobar.
     * @return {@code true} si forma parte de las opciones ofrecidas; {@code false} en otro caso.
     */
    public boolean esOpcionValida(int valorSeleccionado) {
        return opciones.contains(valorSeleccionado);
    }

    /**
     * Valida que el índice de opción se encuentre dentro del rango permitido de 0 a 3.
     *
     * @param indice índice de opción a verificar.
     * @throws IllegalArgumentException si el índice es menor que 0 o mayor o igual que 4.
     */
    private static void validarIndiceOpcion(int indice) {
        if (indice < 0 || indice >= CANTIDAD_OPCIONES) {
            throw new IllegalArgumentException(
                    "El índice de opción debe estar entre 0 y " + (CANTIDAD_OPCIONES - 1) + ".");
        }
    }

    /**
     * Compara este reto con otro objeto por expresión, respuesta, opciones y nivel.
     *
     * @param objeto otro objeto a comparar.
     * @return {@code true} si representan exactamente el mismo reto; {@code false} en caso contrario.
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof RetoMatematico otro)) {
            return false;
        }
        return respuestaCorrecta == otro.respuestaCorrecta
                && expresion.equals(otro.expresion)
                && opciones.equals(otro.opciones)
                && nivel == otro.nivel;
    }

    /**
     * Calcula el código hash consistente con {@link #equals(Object)}.
     *
     * @return valor hash del reto matemático.
     */
    @Override
    public int hashCode() {
        return Objects.hash(expresion, respuestaCorrecta, opciones, nivel);
    }

    /**
     * Devuelve una representación textual legible del reto matemático.
     *
     * @return cadena descriptiva con el nivel, expresión y opciones.
     */
    @Override
    public String toString() {
        return String.format("RetoMatematico{nivel=%s, expresion='%s', opciones=%s}",
                nivel.name(), expresion, opciones);
    }
}
