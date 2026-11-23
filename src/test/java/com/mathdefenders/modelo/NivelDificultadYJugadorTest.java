package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import org.junit.jupiter.api.Test;

class NivelDificultadYJugadorTest {

    @Test
    void nivelesDefinenRangosPuntajesYProgresionEsperados() {
        assertEquals(1, NivelDificultad.BASICO.obtenerValorMinimo());
        assertEquals(50, NivelDificultad.BASICO.obtenerValorMaximo());
        assertEquals(100, NivelDificultad.BASICO.obtenerPuntajeBase());

        assertEquals(2, NivelDificultad.INTERMEDIO.obtenerValorMinimo());
        assertEquals(20, NivelDificultad.INTERMEDIO.obtenerValorMaximo());
        assertEquals(150, NivelDificultad.INTERMEDIO.obtenerPuntajeBase());

        assertEquals(2, NivelDificultad.AVANZADO.obtenerValorMinimo());
        assertEquals(30, NivelDificultad.AVANZADO.obtenerValorMaximo());
        assertEquals(200, NivelDificultad.AVANZADO.obtenerPuntajeBase());

        assertEquals(Optional.of(NivelDificultad.INTERMEDIO), NivelDificultad.BASICO.obtenerSiguienteNivel());
        assertEquals(Optional.of(NivelDificultad.AVANZADO), NivelDificultad.INTERMEDIO.obtenerSiguienteNivel());
        assertEquals(Optional.empty(), NivelDificultad.AVANZADO.obtenerSiguienteNivel());
    }

    @Test
    void jugadorIniciaSoloConNivelBasicoDesbloqueadoYNormalizaAlias() {
        Jugador jugador = new Jugador("  DiegoB  ");

        assertEquals("DiegoB", jugador.obtenerAlias());
        assertTrue(jugador.tieneNivelDesbloqueado(NivelDificultad.BASICO));
        assertFalse(jugador.tieneNivelDesbloqueado(NivelDificultad.INTERMEDIO));
        assertFalse(jugador.tieneNivelDesbloqueado(NivelDificultad.AVANZADO));
        assertEquals(0, jugador.obtenerPuntajeMaximoGlobal());
    }

    @Test
    void jugadorValidaAliasNuloOVacioYActualizaAlias() {
        assertThrows(IllegalArgumentException.class, () -> new Jugador(null));
        assertThrows(IllegalArgumentException.class, () -> new Jugador("   "));

        Jugador jugador = new Jugador("Santiago");
        jugador.establecerAlias("  SantiagoB  ");
        assertEquals("SantiagoB", jugador.obtenerAlias());
        assertThrows(IllegalArgumentException.class, () -> jugador.establecerAlias(" "));
    }

    @Test
    void jugadorDesbloqueaNivelesProgresivamenteYRegistraRecords() {
        Jugador jugador = new Jugador("Yeimmy");

        assertEquals(Optional.of(NivelDificultad.INTERMEDIO),
                jugador.desbloquearSiguienteNivel(NivelDificultad.BASICO));
        assertTrue(jugador.tieneNivelDesbloqueado(NivelDificultad.INTERMEDIO));
        assertEquals(Optional.empty(),
                jugador.desbloquearSiguienteNivel(NivelDificultad.BASICO));

        assertEquals(Optional.of(NivelDificultad.AVANZADO),
                jugador.desbloquearSiguienteNivel(NivelDificultad.INTERMEDIO));
        assertEquals(Optional.empty(),
                jugador.desbloquearSiguienteNivel(NivelDificultad.AVANZADO));

        assertTrue(jugador.registrarPuntaje(NivelDificultad.BASICO, 1200));
        assertFalse(jugador.registrarPuntaje(NivelDificultad.BASICO, 900));
        assertTrue(jugador.registrarPuntaje(NivelDificultad.INTERMEDIO, 1650));

        assertEquals(1200, jugador.obtenerMejorPuntaje(NivelDificultad.BASICO));
        assertEquals(1650, jugador.obtenerMejorPuntaje(NivelDificultad.INTERMEDIO));
        assertEquals(1650, jugador.obtenerPuntajeMaximoGlobal());
        assertThrows(IllegalArgumentException.class, () -> jugador.registrarPuntaje(NivelDificultad.BASICO, -1));
    }

    @Test
    void jugadorDesbloqueaNivelDirectamenteYValidaNulos() {
        Jugador jugador = new Jugador("Directo");

        assertTrue(jugador.desbloquearNivel(NivelDificultad.AVANZADO));
        assertFalse(jugador.desbloquearNivel(NivelDificultad.AVANZADO));
        assertTrue(jugador.tieneNivelDesbloqueado(NivelDificultad.AVANZADO));
        assertFalse(jugador.tieneNivelDesbloqueado(NivelDificultad.INTERMEDIO));

        assertThrows(IllegalArgumentException.class, () -> jugador.desbloquearNivel(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.tieneNivelDesbloqueado(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.desbloquearSiguienteNivel(null));
        assertThrows(IllegalArgumentException.class, () -> jugador.registrarPuntaje(null, 10));
        assertThrows(IllegalArgumentException.class, () -> jugador.obtenerMejorPuntaje(null));

        assertEquals(jugador, jugador);
        assertNotEquals(jugador, null);
        assertNotEquals(jugador, "Directo");

        jugador.registrarPuntaje(NivelDificultad.BASICO, 300);
        String texto = jugador.toString();
        assertTrue(texto.contains("alias='Directo'"), texto);
        assertTrue(texto.contains("maximoGlobal=300"), texto);
    }

    @Test
    void enumeracionesExponenEtiquetasYEstadoFinalizado() {
        assertEquals("Nivel 1: Básico", NivelDificultad.BASICO.obtenerNombreVisible());
        assertEquals(NivelDificultad.INTERMEDIO.obtenerNombreVisible(), NivelDificultad.INTERMEDIO.toString());
        assertEquals(30, NivelDificultad.BASICO.obtenerTiempoSugeridoSegundos());
        assertEquals(25, NivelDificultad.INTERMEDIO.obtenerTiempoSugeridoSegundos());
        assertEquals(20, NivelDificultad.AVANZADO.obtenerTiempoSugeridoSegundos());
        for (NivelDificultad nivel : NivelDificultad.values()) {
            assertFalse(nivel.obtenerDescripcionOperaciones().isBlank());
        }

        assertFalse(EstadoPartida.EN_CURSO.esFinalizado());
        assertTrue(EstadoPartida.VICTORIA.esFinalizado());
        assertTrue(EstadoPartida.DERROTA.esFinalizado());
        assertEquals("En curso", EstadoPartida.EN_CURSO.obtenerEtiqueta());
        assertEquals("Victoria", EstadoPartida.VICTORIA.toString());
        assertEquals("Derrota", EstadoPartida.DERROTA.toString());
    }

    @Test
    void jugadorDevuelveColeccionesInmutablesEIgualdadPorAliasSinMayusculas() {
        Jugador jugador = new Jugador("Javier");
        Set<NivelDificultad> niveles = jugador.obtenerNivelesDesbloqueados();
        Map<NivelDificultad, Integer> puntajes = jugador.obtenerMejoresPuntajesPorNivel();

        assertThrows(UnsupportedOperationException.class, () -> niveles.add(NivelDificultad.AVANZADO));
        assertThrows(UnsupportedOperationException.class, () -> puntajes.put(NivelDificultad.BASICO, 9999));

        Jugador mismoJugador = new Jugador("JAVIER");
        Jugador otroJugador = new Jugador("Diego");
        assertEquals(jugador, mismoJugador);
        assertEquals(jugador.hashCode(), mismoJugador.hashCode());
        assertNotEquals(jugador, otroJugador);
    }
}
