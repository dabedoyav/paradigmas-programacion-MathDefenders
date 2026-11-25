package com.mathdefenders.modelo;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Un reto es cada pregunta que le sale al jugador en una ronda.
 *
 * <p>Trae la operación (por ejemplo "38 + 15 - 12"), la respuesta correcta,
 * las cuatro opciones que van en los botones A, B, C y D, y el nivel al que
 * pertenece. Una vez se crea el reto no se puede modificar: así nos aseguramos
 * de que la pregunta no cambie a mitad de la ronda por algún error.</p>
 *
 * @author Diego Buitrago González
 * @version 1.1
 */
public final class RetoMatematico {

    /**
     * Cantidad de opciones que tiene cada reto: una por cada botón (A, B, C y D).
     */
    public static final int CANTIDAD_OPCIONES = 4;

    /**
     * La operación que se le muestra al jugador, por ejemplo "6 x 7".
     */
    private final String expresion;

    /**
     * El resultado correcto de la operación.
     */
    private final int respuestaCorrecta;

    /**
     * Las cuatro opciones de respuesta, en el orden en que van en los botones.
     * Es una lista que no se puede modificar.
     */
    private final List<Integer> opciones;

    /**
     * El nivel de dificultad al que pertenece el reto (básico, intermedio o avanzado).
     */
    private final NivelDificultad nivel;

    /**
     * Crea un reto nuevo. Antes de guardarlo revisa que todo esté bien: que la
     * operación no venga vacía, que haya exactamente cuatro opciones distintas
     * y que la respuesta correcta sí esté entre ellas. Si algo falla, el reto
     * no se crea.
     *
     * @param expresion la operación que se le va a mostrar al jugador.
     * @param respuestaCorrecta el resultado correcto de la operación.
     * @param opciones las cuatro opciones de respuesta.
     * @param nivel el nivel de dificultad del reto.
     * @throws IllegalArgumentException si alguno de los datos no cumple las reglas.
     */
    public RetoMatematico(
            String expresion,
            int respuestaCorrecta,
            List<Integer> opciones,
            NivelDificultad nivel) {
        // Primero miramos si es null; si lo fuera y llamáramos trim(), se reventaría
        if (expresion == null || expresion.trim().isEmpty()) {
            throw new IllegalArgumentException("La expresión del reto no puede ser nula ni estar vacía.");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel del reto no puede ser nulo.");
        }
        // Tienen que ser exactamente 4 opciones, ni una más ni una menos
        if (opciones == null || opciones.size() != CANTIDAD_OPCIONES) {
            throw new IllegalArgumentException("El reto debe contener exactamente " + CANTIDAD_OPCIONES + " opciones.");
        }
        // Ninguna opción puede venir vacía (null), porque el botón quedaría sin número
        for (Integer opcion : opciones) {
            if (opcion == null) {
                throw new IllegalArgumentException("Las opciones del reto no pueden contener valores nulos.");
            }
        }
        // Truco: un Set no guarda repetidos. Si al pasar las opciones al Set
        // quedan menos de 4, es porque había números repetidos
        Set<Integer> opcionesUnicas = new HashSet<>(opciones);
        if (opcionesUnicas.size() != CANTIDAD_OPCIONES) {
            throw new IllegalArgumentException("Las cuatro opciones del reto deben ser distintas entre sí.");
        }
        // Si la respuesta correcta no está entre las opciones, el jugador nunca podría ganar
        if (!opcionesUnicas.contains(respuestaCorrecta)) {
            throw new IllegalArgumentException("La respuesta correcta debe estar incluida dentro de las opciones.");
        }

        // Ya validado todo, guardamos los datos
        this.expresion = expresion.trim();
        this.respuestaCorrecta = respuestaCorrecta;
        // Hacemos una copia que no se puede modificar. Así, si alguien cambia la
        // lista original después, nuestro reto queda intacto
        this.opciones = List.copyOf(opciones);
        this.nivel = nivel;
    }

    /**
     * Devuelve la operación del reto.
     *
     * @return el texto de la operación.
     */
    public String obtenerExpresion() {
        return expresion;
    }

    /**
     * Devuelve el resultado correcto de la operación.
     *
     * @return la respuesta correcta.
     */
    public int obtenerRespuestaCorrecta() {
        return respuestaCorrecta;
    }

    /**
     * Devuelve las cuatro opciones del reto. La lista es de solo lectura, así que
     * nadie la puede cambiar desde afuera.
     *
     * @return la lista con las cuatro opciones.
     */
    public List<Integer> obtenerOpciones() {
        return opciones;
    }

    /**
     * Devuelve el nivel de dificultad del reto.
     *
     * @return el nivel del reto.
     */
    public NivelDificultad obtenerNivel() {
        return nivel;
    }

    /**
     * Devuelve el número que va en un botón según su posición
     * (0 es A, 1 es B, 2 es C y 3 es D).
     *
     * @param indice la posición del botón, de 0 a 3.
     * @return el número de esa opción.
     * @throws IllegalArgumentException si la posición no está entre 0 y 3.
     */
    public int obtenerOpcion(int indice) {
        validarIndiceOpcion(indice);
        return opciones.get(indice);
    }

    /**
     * Devuelve la letra del botón según su posición: 0 es 'A', 1 es 'B', etc.
     *
     * @param indice la posición del botón, de 0 a 3.
     * @return la letra de la opción ('A', 'B', 'C' o 'D').
     * @throws IllegalArgumentException si la posición no está entre 0 y 3.
     */
    public char obtenerLetraOpcion(int indice) {
        validarIndiceOpcion(indice);
        // En Java un char es un número por debajo: 'A' + 1 da 'B', 'A' + 2 da 'C'...
        return (char) ('A' + indice);
    }

    /**
     * Dice en qué posición (de 0 a 3) quedó la respuesta correcta. La pantalla
     * lo puede usar para pintar de verde el botón correcto.
     *
     * @return la posición de la respuesta correcta dentro de las opciones.
     */
    public int obtenerIndiceRespuestaCorrecta() {
        return opciones.indexOf(respuestaCorrecta);
    }

    /**
     * Revisa si lo que eligió el jugador es la respuesta correcta.
     * Es lo que se llama cuando el jugador hace clic en un botón.
     *
     * @param valorSeleccionado el número que eligió el jugador.
     * @return {@code true} si acertó; {@code false} si no.
     */
    public boolean esRespuestaCorrecta(int valorSeleccionado) {
        return valorSeleccionado == respuestaCorrecta;
    }

    /**
     * Revisa si un número es una de las cuatro opciones del reto.
     *
     * @param valorSeleccionado el número que queremos revisar.
     * @return {@code true} si está entre las opciones; {@code false} si no.
     */
    public boolean esOpcionValida(int valorSeleccionado) {
        return opciones.contains(valorSeleccionado);
    }

    /**
     * Método de apoyo que revisa que la posición del botón esté entre 0 y 3.
     * Lo usan varios métodos, así no toca repetir el mismo if en cada uno.
     *
     * @param indice la posición que queremos revisar.
     * @throws IllegalArgumentException si la posición es menor que 0 o mayor que 3.
     */
    private static void validarIndiceOpcion(int indice) {
        if (indice < 0 || indice >= CANTIDAD_OPCIONES) {
            throw new IllegalArgumentException(
                    "El índice de opción debe estar entre 0 y " + (CANTIDAD_OPCIONES - 1) + ".");
        }
    }

    /**
     * Compara dos retos por su contenido. Si tienen la misma operación, la misma
     * respuesta, las mismas opciones en el mismo orden y el mismo nivel, se
     * consideran iguales aunque sean objetos distintos.
     *
     * @param objeto el otro objeto con el que se compara.
     * @return {@code true} si son el mismo reto; {@code false} si no.
     */
    @Override
    public boolean equals(Object objeto) {
        // Si es literalmente el mismo objeto, de una son iguales
        if (this == objeto) {
            return true;
        }
        // Si no es un RetoMatematico (o es null), no pueden ser iguales
        if (!(objeto instanceof RetoMatematico otro)) {
            return false;
        }
        return respuestaCorrecta == otro.respuestaCorrecta
                && expresion.equals(otro.expresion)
                && opciones.equals(otro.opciones)
                && nivel == otro.nivel;
    }

    /**
     * Calcula el código hash del reto con los mismos datos que usa equals.
     * Java exige que si dos objetos son iguales tengan el mismo hash; si no,
     * cosas como HashSet o HashMap funcionan mal.
     *
     * @return el código hash del reto.
     */
    @Override
    public int hashCode() {
        return Objects.hash(expresion, respuestaCorrecta, opciones, nivel);
    }

    /**
     * Muestra el reto como texto, con el nivel, la operación y las opciones.
     * A propósito no muestra la respuesta correcta, para que no se filtre.
     *
     * @return un texto que describe el reto.
     */
    @Override
    public String toString() {
        return String.format("RetoMatematico{nivel=%s, expresion='%s', opciones=%s}",
                nivel.name(), expresion, opciones);
    }
}
