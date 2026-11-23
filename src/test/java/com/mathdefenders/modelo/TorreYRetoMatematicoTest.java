package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;

class TorreYRetoMatematicoTest {

    @Test
    void torreIniciaConTresVidasRecibeDanoYReiniciaCorrectamente() {
        Torre torre = new Torre();

        assertEquals(Torre.VIDAS_INICIALES, torre.obtenerVidasActuales());
        assertEquals(1.0, torre.obtenerPorcentajeIntegridad(), 0.0001);
        assertTrue(torre.estaActiva());
        assertFalse(torre.estaDestruida());

        assertEquals(2, torre.recibirDano());
        assertEquals(1, torre.recibirDano());
        assertEquals(1.0 / 3.0, torre.obtenerPorcentajeIntegridad(), 0.0001);
        assertEquals(0, torre.recibirDano());
        assertEquals(0, torre.recibirDano());

        assertTrue(torre.estaDestruida());
        assertFalse(torre.estaActiva());

        torre.reiniciar();
        assertEquals(3, torre.obtenerVidasActuales());
        assertTrue(torre.estaActiva());
    }

    @Test
    void torreValidaVidasMaximasPositivas() {
        assertThrows(IllegalArgumentException.class, () -> new Torre(0));
        assertThrows(IllegalArgumentException.class, () -> new Torre(-2));

        Torre torrePersonalizada = new Torre(5);
        assertEquals(5, torrePersonalizada.obtenerVidasMaximas());
        assertEquals(5, torrePersonalizada.obtenerVidasActuales());
    }

    @Test
    void retoMatematicoEsInmutableYConsultaOpcionesYLetras() {
        List<Integer> listaOriginal = new ArrayList<>(List.of(39, 41, 43, 45));
        RetoMatematico reto = new RetoMatematico(
                "38 + 15 - 12", 41, listaOriginal, NivelDificultad.BASICO);

        listaOriginal.set(1, 999);

        assertEquals("38 + 15 - 12", reto.obtenerExpresion());
        assertEquals(41, reto.obtenerRespuestaCorrecta());
        assertEquals(NivelDificultad.BASICO, reto.obtenerNivel());
        assertEquals(List.of(39, 41, 43, 45), reto.obtenerOpciones());
        assertEquals(1, reto.obtenerIndiceRespuestaCorrecta());
        assertEquals('A', reto.obtenerLetraOpcion(0));
        assertEquals('B', reto.obtenerLetraOpcion(1));
        assertEquals('C', reto.obtenerLetraOpcion(2));
        assertEquals('D', reto.obtenerLetraOpcion(3));
        assertTrue(reto.esRespuestaCorrecta(41));
        assertFalse(reto.esRespuestaCorrecta(39));
        assertTrue(reto.esOpcionValida(45));
        assertFalse(reto.esOpcionValida(999));

        assertThrows(UnsupportedOperationException.class, () -> reto.obtenerOpciones().add(50));
        assertThrows(IllegalArgumentException.class, () -> reto.obtenerOpcion(-1));
        assertThrows(IllegalArgumentException.class, () -> reto.obtenerOpcion(4));
    }

    @Test
    void retoMatematicoComparaPorContenidoYSeDescribeEnTexto() {
        RetoMatematico reto = new RetoMatematico("  5 + 5  ", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO);
        RetoMatematico igual = new RetoMatematico("5 + 5", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO);

        assertEquals("5 + 5", reto.obtenerExpresion());
        assertEquals(reto, igual);
        assertEquals(reto.hashCode(), igual.hashCode());
        assertEquals(reto, reto);
        assertNotEquals(reto, null);
        assertNotEquals(reto, "5 + 5");

        assertNotEquals(reto, new RetoMatematico("6 + 4", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO));
        assertNotEquals(reto, new RetoMatematico("5 + 5", 10, List.of(10, 8, 12, 14), NivelDificultad.BASICO));
        assertNotEquals(reto, new RetoMatematico("5 + 5", 12, List.of(8, 10, 12, 14), NivelDificultad.BASICO));
        assertNotEquals(reto, new RetoMatematico("5 + 5", 10, List.of(8, 10, 12, 14), NivelDificultad.INTERMEDIO));

        assertEquals("RetoMatematico{nivel=BASICO, expresion='5 + 5', opciones=[8, 10, 12, 14]}",
                reto.toString());
        assertEquals("Torre{vidas=3/3}", new Torre().toString());
    }

    @Test
    void retoMatematicoRechazaEntradasInvalidasODuplicadas() {
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico(" ", 10, List.of(8, 9, 10, 11), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(8, 9, 10), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(8, 10, 10, 12), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(1, 2, 3, 4), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, Arrays.asList(10, null, 11, 12), NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class,
                () -> new RetoMatematico("5 + 5", 10, List.of(8, 9, 10, 11), null));
    }
}
