package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la clase {@link RetoMatematico}.
 *
 * <p>Acá revisamos que cada reto cumpla las reglas del juego: que la operación
 * no venga vacía, que tenga exactamente cuatro opciones distintas (A, B, C y D),
 * que una de ellas sea la correcta y que, una vez creado, nadie lo pueda cambiar.</p>
 *
 * @author Diego Buitrago González
 * @version 1.1
 */
@DisplayName("RetoMatematico: validación, consulta e inmutabilidad")
class RetoMatematicoTest {

    /**
     * Reto de ejemplo del Nivel 1, el mismo de la Entrega 1 (38 + 15 - 12 = 41).
     * La respuesta correcta queda en la opción B.
     */
    private RetoMatematico retoBasico;

    /**
     * Antes de cada prueba creamos el reto de ejemplo desde cero.
     */
    @BeforeEach
    void prepararReto() {
        retoBasico = new RetoMatematico("38 + 15 - 12", 41, List.of(39, 41, 43, 45), NivelDificultad.BASICO);
    }

    /**
     * Revisa que el reto devuelva los mismos datos con los que lo creamos.
     */
    @Test
    @DisplayName("Expone expresión, respuesta, opciones y nivel")
    void consultasDevuelvenLosDatosDelReto() {
        assertEquals("38 + 15 - 12", retoBasico.obtenerExpresion());
        assertEquals(41, retoBasico.obtenerRespuestaCorrecta());
        assertEquals(List.of(39, 41, 43, 45), retoBasico.obtenerOpciones());
        assertEquals(NivelDificultad.BASICO, retoBasico.obtenerNivel());
        assertEquals(RetoMatematico.CANTIDAD_OPCIONES, retoBasico.obtenerOpciones().size());
    }

    /**
     * Revisa que se pueda sacar cada opción por su posición (de 0 a 3).
     */
    @Test
    @DisplayName("Consulta cada opción por índice")
    void obtenerOpcionPorIndice() {
        assertEquals(39, retoBasico.obtenerOpcion(0));
        assertEquals(41, retoBasico.obtenerOpcion(1));
        assertEquals(43, retoBasico.obtenerOpcion(2));
        assertEquals(45, retoBasico.obtenerOpcion(3));
    }

    /**
     * Revisa que cada posición corresponda a la letra del botón en pantalla.
     */
    @Test
    @DisplayName("Asigna las letras A, B, C y D a los índices 0 a 3")
    void letrasDeLasOpciones() {
        assertEquals('A', retoBasico.obtenerLetraOpcion(0));
        assertEquals('B', retoBasico.obtenerLetraOpcion(1));
        assertEquals('C', retoBasico.obtenerLetraOpcion(2));
        assertEquals('D', retoBasico.obtenerLetraOpcion(3));
    }

    /**
     * Revisa que no deje pedir posiciones por fuera de 0 a 3, ni para el
     * número ni para la letra.
     */
    @Test
    @DisplayName("Rechaza índices de opción fuera de rango")
    void rechazaIndicesFueraDeRango() {
        assertThrows(IllegalArgumentException.class, () -> retoBasico.obtenerOpcion(-1));
        assertThrows(IllegalArgumentException.class, () -> retoBasico.obtenerOpcion(4));
        assertThrows(IllegalArgumentException.class, () -> retoBasico.obtenerLetraOpcion(-1));
        assertThrows(IllegalArgumentException.class, () -> retoBasico.obtenerLetraOpcion(4));
    }

    /**
     * Revisa que encuentre bien la posición de la respuesta correcta, sin
     * importar si quedó en la A, en la B o en la D.
     */
    @Test
    @DisplayName("Ubica el índice de la respuesta correcta")
    void indiceRespuestaCorrecta() {
        assertEquals(1, retoBasico.obtenerIndiceRespuestaCorrecta());

        RetoMatematico enA = new RetoMatematico("6 x 7", 42, List.of(42, 40, 44, 48), NivelDificultad.INTERMEDIO);
        RetoMatematico enD = new RetoMatematico("6 x 7", 42, List.of(40, 44, 48, 42), NivelDificultad.INTERMEDIO);
        assertEquals(0, enA.obtenerIndiceRespuestaCorrecta());
        assertEquals(3, enD.obtenerIndiceRespuestaCorrecta());
    }

    /**
     * Revisa que solo la respuesta correcta cuente como acierto.
     */
    @Test
    @DisplayName("Valida si la respuesta elegida es la correcta")
    void validaRespuestaDelJugador() {
        assertTrue(retoBasico.esRespuestaCorrecta(41));
        assertFalse(retoBasico.esRespuestaCorrecta(39));
        assertFalse(retoBasico.esRespuestaCorrecta(43));
        assertFalse(retoBasico.esRespuestaCorrecta(0));
    }

    /**
     * Revisa que sepa si un número está o no entre las cuatro opciones.
     */
    @Test
    @DisplayName("Reconoce si un valor es una de las opciones")
    void reconoceOpcionesValidas() {
        assertTrue(retoBasico.esOpcionValida(39));
        assertTrue(retoBasico.esOpcionValida(45));
        assertFalse(retoBasico.esOpcionValida(100));
    }

    /**
     * Revisa que acepte resultados negativos o en cero, que pueden salir en
     * las restas del nivel básico.
     */
    @Test
    @DisplayName("Acepta resultados negativos y cero")
    void aceptaResultadosNegativosYCero() {
        RetoMatematico negativo = new RetoMatematico("5 - 12", -7, List.of(-7, 7, -5, 17), NivelDificultad.BASICO);
        RetoMatematico cero = new RetoMatematico("9 - 9", 0, List.of(1, 0, -1, 18), NivelDificultad.BASICO);

        assertTrue(negativo.esRespuestaCorrecta(-7));
        assertTrue(cero.esRespuestaCorrecta(0));
    }

    /**
     * Revisa que si cambiamos la lista original después de crear el reto,
     * las opciones del reto sigan iguales (porque el reto guardó su propia copia).
     */
    @Test
    @DisplayName("No se ve afectado si cambia la lista original")
    void copiaDefensivaDeLasOpciones() {
        List<Integer> listaOriginal = new ArrayList<>(List.of(39, 41, 43, 45));
        RetoMatematico reto = new RetoMatematico("38 + 15 - 12", 41, listaOriginal, NivelDificultad.BASICO);

        // Le cambiamos la respuesta correcta a la lista original...
        listaOriginal.set(1, 999);

        // ...y el reto ni se entera
        assertEquals(List.of(39, 41, 43, 45), reto.obtenerOpciones());
        assertTrue(reto.esRespuestaCorrecta(41));
    }

    /**
     * Revisa que la lista de opciones que devuelve el reto no se pueda
     * modificar desde afuera (ni agregar, ni cambiar, ni borrar).
     */
    @Test
    @DisplayName("La lista de opciones devuelta es de solo lectura")
    void opcionesSonInmutables() {
        List<Integer> opciones = retoBasico.obtenerOpciones();

        assertThrows(UnsupportedOperationException.class, () -> opciones.add(50));
        assertThrows(UnsupportedOperationException.class, () -> opciones.set(0, 50));
        assertThrows(UnsupportedOperationException.class, () -> opciones.remove(0));
    }

    /**
     * Revisa que a la operación se le quiten los espacios de más al principio y al final.
     */
    @Test
    @DisplayName("Elimina los espacios sobrantes de la expresión")
    void recortaEspaciosDeLaExpresion() {
        RetoMatematico reto = new RetoMatematico("   5 + 5  ", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO);
        assertEquals("5 + 5", reto.obtenerExpresion());
    }

    /**
     * Revisa que no deje crear un reto con la operación en null, vacía o solo con espacios.
     */
    @Test
    @DisplayName("Rechaza expresiones nulas o vacías")
    void rechazaExpresionInvalida() {
        List<Integer> opciones = List.of(8, 9, 10, 11);
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico(null, 10, opciones, NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("", 10, opciones, NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("   ", 10, opciones, NivelDificultad.BASICO));
    }

    /**
     * Revisa que no deje crear un reto sin nivel.
     */
    @Test
    @DisplayName("Rechaza un nivel nulo")
    void rechazaNivelNulo() {
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(8, 9, 10, 11), null));
    }

    /**
     * Revisa que el reto exija exactamente cuatro opciones: ni null, ni vacía,
     * ni tres, ni cinco.
     */
    @Test
    @DisplayName("Exige exactamente cuatro opciones")
    void exigeCuatroOpciones() {
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, null, NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(9, 10, 11), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(8, 9, 10, 11, 12), NivelDificultad.BASICO));
    }

    /**
     * Revisa que no se acepten opciones en null ni números repetidos.
     */
    @Test
    @DisplayName("Rechaza opciones nulas o repetidas")
    void rechazaOpcionesNulasORepetidas() {
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, Arrays.asList(10, null, 11, 12), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(8, 10, 10, 12), NivelDificultad.BASICO));
    }

    /**
     * Revisa que la respuesta correcta tenga que estar sí o sí entre las opciones.
     */
    @Test
    @DisplayName("Exige que la respuesta correcta esté entre las opciones")
    void exigeRespuestaEntreOpciones() {
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(1, 2, 3, 4), NivelDificultad.BASICO));
    }

    /**
     * Revisa que dos retos con el mismo contenido sean iguales y tengan el
     * mismo hash, y que con cualquier diferencia ya sean distintos.
     */
    @Test
    @DisplayName("equals y hashCode comparan por contenido")
    void igualdadPorContenido() {
        RetoMatematico reto = new RetoMatematico("5 + 5", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO);
        RetoMatematico igual = new RetoMatematico(" 5 + 5 ", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO);

        assertEquals(reto, reto);
        assertEquals(reto, igual);
        assertEquals(reto.hashCode(), igual.hashCode());
        assertNotEquals(reto, null);
        assertNotEquals(reto, "5 + 5");

        // Cambiando una sola cosa (operación, respuesta, orden de opciones o nivel) ya no son iguales
        assertNotEquals(reto, new RetoMatematico("6 + 4", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO));
        assertNotEquals(reto, new RetoMatematico("5 + 5", 12, List.of(8, 10, 12, 14), NivelDificultad.BASICO));
        assertNotEquals(reto, new RetoMatematico("5 + 5", 10, List.of(10, 8, 12, 14), NivelDificultad.BASICO));
        assertNotEquals(reto, new RetoMatematico("5 + 5", 10, List.of(8, 10, 12, 14), NivelDificultad.INTERMEDIO));
    }

    /**
     * Revisa que el texto del reto muestre el nivel, la operación y las opciones.
     */
    @Test
    @DisplayName("toString describe nivel, expresión y opciones")
    void representacionTextual() {
        assertEquals("RetoMatematico{nivel=BASICO, expresion='38 + 15 - 12', opciones=[39, 41, 43, 45]}",
                retoBasico.toString());
    }
}
