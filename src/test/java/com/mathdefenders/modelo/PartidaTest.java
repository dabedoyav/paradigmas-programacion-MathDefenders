package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

class PartidaTest {

    private static GeneradorNivel crearGeneradorFijo(NivelDificultad nivel) {
        return new GeneradorNivel(nivel) {
            private int secuencia = 1;

            @Override
            public RetoMatematico generarReto() {
                int a = 10 + secuencia;
                int b = 5;
                int correcta = a + b;
                secuencia++;
                return new RetoMatematico(
                        a + " + " + b,
                        correcta,
                        List.of(correcta - 2, correcta, correcta + 2, correcta + 4),
                        nivel);
            }
        };
    }

    private static int obtenerPrimerIncorrecto(RetoMatematico reto) {
        for (int opcion : reto.obtenerOpciones()) {
            if (!reto.esRespuestaCorrecta(opcion)) {
                return opcion;
            }
        }
        throw new IllegalStateException("No se encontró distractor.");
    }

    @Test
    void noPermiteIniciarEnNivelBloqueadoYValidaArgumentos() {
        Jugador jugador = new Jugador("Diego");

        assertThrows(IllegalStateException.class, () -> new Partida(jugador, NivelDificultad.INTERMEDIO));
        assertThrows(IllegalStateException.class, () -> new Partida(jugador, NivelDificultad.AVANZADO));
        assertThrows(IllegalArgumentException.class, () -> new Partida(null, NivelDificultad.BASICO));
        assertThrows(IllegalArgumentException.class, () -> new Partida(jugador, null));
        assertThrows(IllegalArgumentException.class,
                () -> new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.INTERMEDIO)));
    }

    @Test
    void partidaPerfectaEnNivelBasicoSumaBonoPorRachaYDesbloqueaIntermedio() {
        Jugador jugador = new Jugador("Yeimmy");
        Partida partida = new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.BASICO));

        for (int ronda = 1; ronda <= Partida.TOTAL_RONDAS; ronda++) {
            assertEquals(ronda, partida.obtenerRondaActual());
            RetoMatematico reto = partida.obtenerRetoActual();
            ResultadoRonda resultado = partida.responder(reto.obtenerRespuestaCorrecta());

            assertTrue(resultado.esAcierto());
            assertEquals(100, resultado.obtenerPuntosBaseObtenidos());
            assertEquals(ronda >= 3 ? 50 : 0, resultado.obtenerBonoRachaObtenido());
            assertEquals(ronda, resultado.obtenerRachaActual());
        }

        assertTrue(partida.estaFinalizada());
        assertEquals(EstadoPartida.VICTORIA, partida.obtenerEstado());
        assertEquals(1400, partida.obtenerPuntajeTotal());
        assertEquals(10, partida.obtenerAciertos());
        assertEquals(0, partida.obtenerErrores());
        assertEquals(10, partida.obtenerMejorRacha());
        assertEquals(100.0, partida.obtenerPorcentajePrecision(), 0.0001);
        assertTrue(jugador.tieneNivelDesbloqueado(NivelDificultad.INTERMEDIO));
        assertEquals(1400, jugador.obtenerMejorPuntaje(NivelDificultad.BASICO));

        ResumenPartida resumen = partida.obtenerResumen();
        assertEquals(EstadoPartida.VICTORIA, resumen.obtenerEstadoFinal());
        assertEquals(Optional.of(NivelDificultad.INTERMEDIO), resumen.obtenerNivelDesbloqueado());
    }

    @Test
    void victoriaConDosErroresConservaUnaVidaYReiniciaRachaCorrectamente() {
        Jugador jugador = new Jugador("Santiago");
        Partida partida = new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.BASICO));

        for (int ronda = 1; ronda <= Partida.TOTAL_RONDAS; ronda++) {
            RetoMatematico reto = partida.obtenerRetoActual();
            if (ronda == 4 || ronda == 7) {
                ResultadoRonda fallo = partida.responder(obtenerPrimerIncorrecto(reto));
                assertFalse(fallo.esAcierto());
                assertEquals(0, fallo.obtenerRachaActual());
                assertEquals(0, fallo.obtenerPuntosTotalesRonda());
            } else {
                partida.responderPorIndice(reto.obtenerIndiceRespuestaCorrecta());
            }
        }

        assertEquals(EstadoPartida.VICTORIA, partida.obtenerEstado());
        assertEquals(1, partida.obtenerTorre().obtenerVidasActuales());
        assertEquals(8, partida.obtenerAciertos());
        assertEquals(2, partida.obtenerErrores());
        assertEquals(3, partida.obtenerMejorRacha());
        assertEquals(900, partida.obtenerPuntajeTotal());
        assertEquals(80.0, partida.obtenerPorcentajePrecision(), 0.0001);
        assertTrue(jugador.tieneNivelDesbloqueado(NivelDificultad.INTERMEDIO));
    }

    @Test
    void tresErroresOTiempoAgotadoProvocanDerrotaYNoDesbloqueanNivel() {
        Jugador jugador = new Jugador("Javier");
        Partida partida = new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.BASICO));

        ResultadoRonda r1 = partida.responder(partida.obtenerRetoActual().obtenerRespuestaCorrecta());
        assertTrue(r1.esAcierto());

        ResultadoRonda r2 = partida.registrarTiempoAgotado();
        assertFalse(r2.esAcierto());
        assertTrue(r2.esTiempoAgotado());
        assertTrue(r2.obtenerRespuestaJugador().isEmpty());
        assertEquals(2, r2.obtenerVidasRestantes());

        partida.responder(obtenerPrimerIncorrecto(partida.obtenerRetoActual()));
        assertEquals(1, partida.obtenerTorre().obtenerVidasActuales());
        assertEquals(EstadoPartida.EN_CURSO, partida.obtenerEstado());

        ResultadoRonda r4 = partida.registrarTiempoAgotado();
        assertEquals(0, r4.obtenerVidasRestantes());
        assertEquals(EstadoPartida.DERROTA, partida.obtenerEstado());
        assertTrue(partida.estaFinalizada());
        assertFalse(jugador.tieneNivelDesbloqueado(NivelDificultad.INTERMEDIO));
        assertTrue(partida.obtenerResumen().obtenerNivelDesbloqueado().isEmpty());

        assertThrows(IllegalStateException.class, () -> partida.responder(10));
        assertThrows(IllegalStateException.class, () -> partida.responderPorIndice(0));
        assertThrows(IllegalStateException.class, partida::registrarTiempoAgotado);
    }

    @Test
    void opcionInvalidaSeRechazaSinConsumirVidasNiAvanzarRonda() {
        Jugador jugador = new Jugador("DiegoG");
        jugador.desbloquearNivel(NivelDificultad.INTERMEDIO);
        Partida partida = new Partida(jugador, NivelDificultad.INTERMEDIO,
                crearGeneradorFijo(NivelDificultad.INTERMEDIO));

        assertThrows(IllegalArgumentException.class, () -> partida.responder(-999));
        assertThrows(IllegalArgumentException.class, () -> partida.responderPorIndice(5));

        assertEquals(1, partida.obtenerRondaActual());
        assertEquals(3, partida.obtenerTorre().obtenerVidasActuales());
        assertTrue(partida.obtenerHistorialRondas().isEmpty());

        ResultadoRonda resultado = partida.responder(partida.obtenerRetoActual().obtenerRespuestaCorrecta());
        assertEquals(150, resultado.obtenerPuntosBaseObtenidos());
    }

    @Test
    void constructorConGeneradorExplicitoRechazaNivelOGeneradorNulos() {
        Jugador jugador = new Jugador("Nulos");

        assertThrows(IllegalArgumentException.class,
                () -> new Partida(jugador, null, crearGeneradorFijo(NivelDificultad.BASICO)));
        assertThrows(IllegalArgumentException.class,
                () -> new Partida(jugador, NivelDificultad.BASICO, null));
    }

    @Test
    void derrotaConservaElPuntajeParcialComoRecordYNoAvanzaLaRonda() {
        Jugador jugador = new Jugador("Derrotado");
        Partida partida = new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.BASICO));

        partida.responder(partida.obtenerRetoActual().obtenerRespuestaCorrecta());
        partida.responder(obtenerPrimerIncorrecto(partida.obtenerRetoActual()));
        partida.responder(obtenerPrimerIncorrecto(partida.obtenerRetoActual()));
        RetoMatematico retoFatal = partida.obtenerRetoActual();
        ResultadoRonda ultima = partida.responder(obtenerPrimerIncorrecto(retoFatal));

        assertEquals(EstadoPartida.DERROTA, ultima.obtenerEstadoResultante());
        assertEquals(EstadoPartida.DERROTA, partida.obtenerEstado());
        assertEquals(4, partida.obtenerRondaActual());
        assertEquals(4, partida.obtenerHistorialRondas().size());
        assertEquals(retoFatal, partida.obtenerRetoActual());
        assertEquals(100, partida.obtenerPuntajeTotal());
        assertEquals(100, jugador.obtenerMejorPuntaje(NivelDificultad.BASICO));
        assertEquals(1, partida.obtenerAciertos());
        assertEquals(3, partida.obtenerErrores());
        assertEquals(25.0, partida.obtenerPorcentajePrecision(), 0.0001);
        assertEquals(EstadoPartida.DERROTA, partida.obtenerResumen().obtenerEstadoFinal());
    }

    @Test
    void resumenYProgresoReflejanElEstadoParcialDeLaPartida() {
        Jugador jugador = new Jugador("Parcial");
        Partida partida = new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.BASICO));

        assertEquals(0.0, partida.obtenerPorcentajePrecision(), 0.0001);
        assertEquals(0.0, partida.obtenerProgresoRondas(), 0.0001);
        assertFalse(partida.estaFinalizada());
        assertEquals(jugador, partida.obtenerJugador());
        assertEquals(NivelDificultad.BASICO, partida.obtenerNivel());

        partida.responder(partida.obtenerRetoActual().obtenerRespuestaCorrecta());
        partida.responder(obtenerPrimerIncorrecto(partida.obtenerRetoActual()));

        assertEquals(50.0, partida.obtenerPorcentajePrecision(), 0.0001);
        assertEquals(0.2, partida.obtenerProgresoRondas(), 0.0001);

        ResumenPartida resumen = partida.obtenerResumen();
        assertEquals("Parcial", resumen.obtenerAliasJugador());
        assertEquals(EstadoPartida.EN_CURSO, resumen.obtenerEstadoFinal());
        assertEquals(100, resumen.obtenerPuntajeTotal());
        assertEquals(1, resumen.obtenerAciertos());
        assertEquals(1, resumen.obtenerErrores());
        assertEquals(2, resumen.obtenerRondasJugadas());
        assertEquals(1, resumen.obtenerMejorRacha());
        assertEquals(2, resumen.obtenerVidasRestantes());
        assertEquals(50.0, resumen.obtenerPorcentajePrecision(), 0.0001);
        assertTrue(resumen.obtenerNivelDesbloqueado().isEmpty());

        List<ResultadoRonda> historial = partida.obtenerHistorialRondas();
        assertEquals(2, historial.size());
        assertThrows(UnsupportedOperationException.class, () -> historial.add(historial.get(0)));
    }

    @Test
    void tiempoAgotadoReiniciaLaRachaYElBonoVuelveATresAciertosConsecutivos() {
        Jugador jugador = new Jugador("Racha");
        Partida partida = new Partida(jugador, NivelDificultad.BASICO, crearGeneradorFijo(NivelDificultad.BASICO));

        int[] bonosEsperados = {0, 0, 50};
        for (int bonoEsperado : bonosEsperados) {
            ResultadoRonda r = partida.responder(partida.obtenerRetoActual().obtenerRespuestaCorrecta());
            assertEquals(bonoEsperado, r.obtenerBonoRachaObtenido());
        }
        assertEquals(3, partida.obtenerRachaActual());

        ResultadoRonda tiempo = partida.registrarTiempoAgotado();
        assertTrue(tiempo.esTiempoAgotado());
        assertEquals(0, partida.obtenerRachaActual());
        assertEquals(3, partida.obtenerMejorRacha());

        for (int bonoEsperado : bonosEsperados) {
            ResultadoRonda r = partida.responder(partida.obtenerRetoActual().obtenerRespuestaCorrecta());
            assertEquals(bonoEsperado, r.obtenerBonoRachaObtenido());
        }

        // 6 aciertos: 6 x 100 base + 2 bonos de 50; el tiempo agotado no suma puntos.
        assertEquals(700, partida.obtenerPuntajeTotal());
        assertEquals(6, partida.obtenerAciertos());
        assertEquals(1, partida.obtenerErrores());
        assertEquals(2, partida.obtenerTorre().obtenerVidasActuales());
    }

    @Test
    void partidaConGeneradorEstandarEsJugableYTodasLasRespuestasCorrectasGanan() {
        Jugador jugador = new Jugador("Estandar");
        jugador.desbloquearNivel(NivelDificultad.INTERMEDIO);
        jugador.desbloquearNivel(NivelDificultad.AVANZADO);

        for (NivelDificultad nivel : NivelDificultad.values()) {
            Partida partida = new Partida(jugador, nivel);
            while (!partida.estaFinalizada()) {
                partida.responderPorIndice(partida.obtenerRetoActual().obtenerIndiceRespuestaCorrecta());
            }
            assertEquals(EstadoPartida.VICTORIA, partida.obtenerEstado());
            assertEquals(Partida.TOTAL_RONDAS, partida.obtenerHistorialRondas().size());
            assertEquals(Partida.TOTAL_RONDAS, partida.obtenerRondaActual());
            assertEquals(1.0, partida.obtenerProgresoRondas(), 0.0001);
            assertEquals(Torre.VIDAS_INICIALES, partida.obtenerTorre().obtenerVidasActuales());
            assertEquals(Partida.TOTAL_RONDAS * nivel.obtenerPuntajeBase()
                    + (Partida.TOTAL_RONDAS - Partida.RACHA_MINIMA_BONO + 1) * Partida.BONO_RACHA,
                    partida.obtenerPuntajeTotal());
        }
    }
}
