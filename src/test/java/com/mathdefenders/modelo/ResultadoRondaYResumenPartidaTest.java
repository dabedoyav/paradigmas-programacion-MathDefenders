package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class ResultadoRondaYResumenPartidaTest {

    private static RetoMatematico retoDePrueba() {
        return new RetoMatematico("5 + 5", 10, List.of(8, 10, 12, 14), NivelDificultad.BASICO);
    }

    private static ResultadoRonda crearResultado(
            int numeroRonda,
            RetoMatematico reto,
            Integer respuesta,
            boolean acierto,
            boolean tiempoAgotado,
            int puntosBase,
            int bono,
            int racha,
            int vidas,
            EstadoPartida estado) {
        return new ResultadoRonda(numeroRonda, reto, respuesta, acierto, tiempoAgotado,
                puntosBase, bono, racha, vidas, estado);
    }

    @Test
    void resultadoRondaExponeDatosYSumaBaseMasBono() {
        RetoMatematico reto = retoDePrueba();
        ResultadoRonda resultado = crearResultado(
                4, reto, 10, true, false, 100, 50, 4, 2, EstadoPartida.EN_CURSO);

        assertEquals(4, resultado.obtenerNumeroRonda());
        assertEquals(reto, resultado.obtenerReto());
        assertEquals(Optional.of(10), resultado.obtenerRespuestaJugador());
        assertTrue(resultado.esAcierto());
        assertFalse(resultado.esTiempoAgotado());
        assertEquals(100, resultado.obtenerPuntosBaseObtenidos());
        assertEquals(50, resultado.obtenerBonoRachaObtenido());
        assertEquals(150, resultado.obtenerPuntosTotalesRonda());
        assertEquals(4, resultado.obtenerRachaActual());
        assertEquals(2, resultado.obtenerVidasRestantes());
        assertEquals(EstadoPartida.EN_CURSO, resultado.obtenerEstadoResultante());
    }

    @Test
    void resultadoRondaPorTiempoAgotadoNoTieneRespuestaDelJugador() {
        ResultadoRonda resultado = crearResultado(
                2, retoDePrueba(), null, false, true, 0, 0, 0, 1, EstadoPartida.EN_CURSO);

        assertTrue(resultado.obtenerRespuestaJugador().isEmpty());
        assertTrue(resultado.esTiempoAgotado());
        assertEquals(0, resultado.obtenerPuntosTotalesRonda());

        String texto = resultado.toString();
        assertTrue(texto.contains("ronda=2"), texto);
        assertTrue(texto.contains("tiempoAgotado=true"), texto);
        assertTrue(texto.contains("vidas=1"), texto);
    }

    @Test
    void resultadoRondaRechazaDatosInvalidos() {
        RetoMatematico reto = retoDePrueba();
        EstadoPartida enCurso = EstadoPartida.EN_CURSO;

        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(0, reto, 10, true, false, 100, 0, 1, 3, enCurso));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, null, 10, true, false, 100, 0, 1, 3, enCurso));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, reto, 10, true, false, -1, 0, 1, 3, enCurso));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, reto, 10, true, false, 100, -1, 1, 3, enCurso));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, reto, 10, true, false, 100, 0, -1, 3, enCurso));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, reto, 10, true, false, 100, 0, 1, -1, enCurso));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, reto, 10, true, false, 100, 0, 1, 3, null));
        assertThrows(IllegalArgumentException.class,
                () -> crearResultado(1, reto, null, true, true, 0, 0, 0, 3, enCurso),
                "Un tiempo agotado no puede ser un acierto.");
    }

    private static ResumenPartida crearResumen(
            String alias,
            NivelDificultad nivel,
            EstadoPartida estado,
            int puntaje,
            int aciertos,
            int errores,
            int rondas,
            int racha,
            int vidas,
            double precision,
            NivelDificultad desbloqueado) {
        return new ResumenPartida(alias, nivel, estado, puntaje, aciertos, errores,
                rondas, racha, vidas, precision, desbloqueado);
    }

    @Test
    void resumenPartidaExponeDatosNormalizaAliasYOpcionalDeDesbloqueo() {
        ResumenPartida conDesbloqueo = crearResumen(
                "  Santiago ", NivelDificultad.BASICO, EstadoPartida.VICTORIA,
                1400, 10, 0, 10, 10, 3, 100.0, NivelDificultad.INTERMEDIO);
        ResumenPartida sinDesbloqueo = crearResumen(
                "Santiago", NivelDificultad.BASICO, EstadoPartida.DERROTA,
                200, 2, 3, 5, 2, 0, 40.0, null);

        assertEquals("Santiago", conDesbloqueo.obtenerAliasJugador());
        assertEquals(NivelDificultad.BASICO, conDesbloqueo.obtenerNivel());
        assertEquals(EstadoPartida.VICTORIA, conDesbloqueo.obtenerEstadoFinal());
        assertEquals(1400, conDesbloqueo.obtenerPuntajeTotal());
        assertEquals(10, conDesbloqueo.obtenerAciertos());
        assertEquals(0, conDesbloqueo.obtenerErrores());
        assertEquals(10, conDesbloqueo.obtenerRondasJugadas());
        assertEquals(10, conDesbloqueo.obtenerMejorRacha());
        assertEquals(3, conDesbloqueo.obtenerVidasRestantes());
        assertEquals(100.0, conDesbloqueo.obtenerPorcentajePrecision());
        assertEquals(Optional.of(NivelDificultad.INTERMEDIO), conDesbloqueo.obtenerNivelDesbloqueado());
        assertTrue(sinDesbloqueo.obtenerNivelDesbloqueado().isEmpty());

        String texto = sinDesbloqueo.toString();
        assertTrue(texto.contains("jugador='Santiago'"), texto);
        assertTrue(texto.contains("nivel=BASICO"), texto);
        assertTrue(texto.contains("puntaje=200"), texto);
    }

    @Test
    void resumenPartidaRechazaDatosInvalidos() {
        NivelDificultad b = NivelDificultad.BASICO;
        EstadoPartida v = EstadoPartida.VICTORIA;

        assertThrows(IllegalArgumentException.class, () -> crearResumen(null, b, v, 0, 0, 0, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("  ", b, v, 0, 0, 0, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", null, v, 0, 0, 0, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, null, 0, 0, 0, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, -1, 0, 0, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, -1, 0, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, 0, -1, 0, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, 0, 0, -1, 0, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, 0, 0, 0, -1, 0, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, 0, 0, 0, 0, -1, 0.0, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, 0, 0, 0, 0, 0, -0.5, null));
        assertThrows(IllegalArgumentException.class, () -> crearResumen("Ana", b, v, 0, 0, 0, 0, 0, 0, 100.5, null));
    }
}
