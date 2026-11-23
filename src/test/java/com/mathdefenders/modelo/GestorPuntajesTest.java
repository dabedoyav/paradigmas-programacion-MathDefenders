package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class GestorPuntajesTest {

    @Test
    void registroPuntajeOrdenaNaturalmentePorPuntajePrecisionFechaYAlias() {
        LocalDateTime base = LocalDateTime.of(2026, 11, 20, 10, 0);
        RegistroPuntaje alto = new RegistroPuntaje("Ana", NivelDificultad.AVANZADO, 2400, 100.0, base);
        RegistroPuntaje empateMasPreciso = new RegistroPuntaje("Carlos", NivelDificultad.INTERMEDIO, 1500, 90.0, base.plusMinutes(5));
        RegistroPuntaje empateMenosPreciso = new RegistroPuntaje("Beto", NivelDificultad.INTERMEDIO, 1500, 80.0, base);
        RegistroPuntaje bajo = new RegistroPuntaje("Diana", NivelDificultad.BASICO, 900, 70.0, base.plusMinutes(10));

        GestorPuntajes gestor = new GestorPuntajes();
        gestor.registrar(bajo);
        gestor.registrar(empateMenosPreciso);
        gestor.registrar(alto);
        gestor.registrar(empateMasPreciso);

        assertEquals(List.of(alto, empateMasPreciso, empateMenosPreciso, bajo),
                gestor.obtenerTodosOrdenados());
        assertEquals(alto, gestor.obtenerMejorPuntaje().orElseThrow());
        assertEquals(List.of(alto, empateMasPreciso), gestor.obtenerTop(2));
        assertEquals(4, gestor.obtenerCantidadRegistros());
    }

    @Test
    void gestorFiltraPorNivelYJugadorYAplicaComparadoresPersonalizados() {
        LocalDateTime base = LocalDateTime.of(2026, 11, 20, 14, 0);
        RegistroPuntaje r1 = new RegistroPuntaje("Zapata", NivelDificultad.BASICO, 1400, 85.0, base);
        RegistroPuntaje r2 = new RegistroPuntaje("Alvarez", NivelDificultad.INTERMEDIO, 1100, 100.0, base.plusHours(2));
        RegistroPuntaje r3 = new RegistroPuntaje("zapata", NivelDificultad.INTERMEDIO, 1800, 90.0, base.plusHours(1));

        GestorPuntajes gestor = new GestorPuntajes();
        gestor.registrar(r1);
        gestor.registrar(r2);
        gestor.registrar(r3);

        assertEquals(List.of(r3, r2), gestor.obtenerPorNivel(NivelDificultad.INTERMEDIO));
        assertEquals(List.of(r3, r1), gestor.obtenerPorJugador("  ZAPATA "));
        assertEquals(List.of(r2, r3, r1), gestor.obtenerOrdenadosPor(RegistroPuntaje.POR_PRECISION));
        assertEquals(List.of(r2, r3, r1), gestor.obtenerOrdenadosPor(RegistroPuntaje.POR_FECHA_RECIENTE));
        assertEquals(List.of(r2, r3, r1), gestor.obtenerOrdenadosPor(RegistroPuntaje.POR_ALIAS));

        gestor.limpiar();
        assertEquals(0, gestor.obtenerCantidadRegistros());
        assertTrue(gestor.obtenerMejorPuntaje().isEmpty());
    }

    @Test
    void gestorCreaRegistroDesdeResumenYValidaEntradas() {
        ResumenPartida resumen = new ResumenPartida(
                "Yeimmy",
                NivelDificultad.AVANZADO,
                EstadoPartida.VICTORIA,
                2150,
                10,
                0,
                10,
                10,
                3,
                100.0,
                null);

        GestorPuntajes gestor = new GestorPuntajes();
        RegistroPuntaje creado = gestor.registrarDesdeResumen(resumen);

        assertEquals("Yeimmy", creado.obtenerAliasJugador());
        assertEquals(2150, creado.obtenerPuntaje());
        assertEquals(1, gestor.obtenerCantidadRegistros());

        assertThrows(IllegalArgumentException.class, () -> RegistroPuntaje.desdeResumen(null));
        assertThrows(IllegalArgumentException.class, () -> gestor.registrar(null));
        assertThrows(IllegalArgumentException.class, () -> gestor.obtenerTop(0));
        assertThrows(IllegalArgumentException.class, () -> gestor.obtenerPorNivel(null));
        assertThrows(IllegalArgumentException.class, () -> gestor.obtenerPorJugador(" "));
        assertThrows(IllegalArgumentException.class, () -> gestor.obtenerOrdenadosPor(null));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Test", NivelDificultad.BASICO, -5, 50.0));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Test", NivelDificultad.BASICO, 100, 120.0));
    }
}
