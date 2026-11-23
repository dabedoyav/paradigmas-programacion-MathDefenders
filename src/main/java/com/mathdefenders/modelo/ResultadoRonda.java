package com.mathdefenders.modelo;

import java.util.Optional;

/**
 * Representa el resultado inmutable de una ronda individual jugada dentro de
 * una {@link Partida}, detallando el reto, la respuesta, los puntos obtenidos,
 * el bono por racha y el estado resultante de la torre.
 *
 * @author Santiago Borda Gutiérrez
 * @version 1.0
 */
public final class ResultadoRonda {

    /**
     * Número secuencial de la ronda evaluada (entre 1 y 10).
     */
    private final int numeroRonda;

    /**
     * Reto matemático presentado en la ronda.
     */
    private final RetoMatematico reto;

    /**
     * Valor seleccionado por el jugador, o {@code null} si se agotó el tiempo.
     */
    private final Integer respuestaJugador;

    /**
     * Indica si la respuesta del jugador fue correcta.
     */
    private final boolean acierto;

    /**
     * Indica si la ronda finalizó por agotamiento del tiempo de respuesta.
     */
    private final boolean tiempoAgotado;

    /**
     * Puntos base sumados en esta ronda (cero si hubo fallo o tiempo agotado).
     */
    private final int puntosBaseObtenidos;

    /**
     * Puntos adicionales de bonificación obtenidos por racha consecutiva.
     */
    private final int bonoRachaObtenido;

    /**
     * Racha de aciertos consecutivos vigente al finalizar la ronda.
     */
    private final int rachaActual;

    /**
     * Vidas restantes de la torre después de resolver la ronda.
     */
    private final int vidasRestantes;

    /**
     * Estado de la partida inmediatamente posterior a esta ronda.
     */
    private final EstadoPartida estadoResultante;

    /**
     * Construye un registro inmutable con el desenlace de una ronda.
     *
     * @param numeroRonda número de ronda jugada, mayor que cero.
     * @param reto reto matemático evaluado.
     * @param respuestaJugador valor seleccionado por el jugador o {@code null} si expiró el tiempo.
     * @param acierto {@code true} si el jugador acertó; {@code false} en caso contrario.
     * @param tiempoAgotado {@code true} si la ronda terminó por tiempo agotado.
     * @param puntosBaseObtenidos puntos base otorgados en la ronda.
     * @param bonoRachaObtenido puntos extra por racha otorgados en la ronda.
     * @param rachaActual racha consecutiva acumulada al cierre de la ronda.
     * @param vidasRestantes vidas disponibles de la torre tras la ronda.
     * @param estadoResultante estado en el que queda la partida tras la ronda.
     * @throws IllegalArgumentException si los parámetros numéricos o de estado son inválidos.
     */
    public ResultadoRonda(
            int numeroRonda,
            RetoMatematico reto,
            Integer respuestaJugador,
            boolean acierto,
            boolean tiempoAgotado,
            int puntosBaseObtenidos,
            int bonoRachaObtenido,
            int rachaActual,
            int vidasRestantes,
            EstadoPartida estadoResultante) {
        if (numeroRonda <= 0) {
            throw new IllegalArgumentException("El número de ronda debe ser positivo.");
        }
        if (reto == null) {
            throw new IllegalArgumentException("El reto de la ronda no puede ser nulo.");
        }
        if (puntosBaseObtenidos < 0 || bonoRachaObtenido < 0 || rachaActual < 0 || vidasRestantes < 0) {
            throw new IllegalArgumentException("Los valores numéricos de la ronda no pueden ser negativos.");
        }
        if (estadoResultante == null) {
            throw new IllegalArgumentException("El estado resultante de la ronda no puede ser nulo.");
        }
        if (tiempoAgotado && acierto) {
            throw new IllegalArgumentException("Una ronda con tiempo agotado no puede registrarse como acierto.");
        }

        this.numeroRonda = numeroRonda;
        this.reto = reto;
        this.respuestaJugador = respuestaJugador;
        this.acierto = acierto;
        this.tiempoAgotado = tiempoAgotado;
        this.puntosBaseObtenidos = puntosBaseObtenidos;
        this.bonoRachaObtenido = bonoRachaObtenido;
        this.rachaActual = rachaActual;
        this.vidasRestantes = vidasRestantes;
        this.estadoResultante = estadoResultante;
    }

    /**
     * Obtiene el número de ronda evaluada.
     *
     * @return número de ronda (1 a 10).
     */
    public int obtenerNumeroRonda() {
        return numeroRonda;
    }

    /**
     * Obtiene el reto matemático que fue evaluado en la ronda.
     *
     * @return reto matemático de la ronda.
     */
    public RetoMatematico obtenerReto() {
        return reto;
    }

    /**
     * Obtiene la respuesta numérica seleccionada por el jugador si la hubo.
     *
     * @return {@link Optional} con el valor elegido, o vacío si se agotó el tiempo.
     */
    public Optional<Integer> obtenerRespuestaJugador() {
        return Optional.ofNullable(respuestaJugador);
    }

    /**
     * Indica si el jugador respondió correctamente en la ronda.
     *
     * @return {@code true} si acertó; {@code false} si falló o agotó el tiempo.
     */
    public boolean esAcierto() {
        return acierto;
    }

    /**
     * Indica si la ronda finalizó por agotamiento del tiempo.
     *
     * @return {@code true} si se agotó el tiempo; {@code false} en otro caso.
     */
    public boolean esTiempoAgotado() {
        return tiempoAgotado;
    }

    /**
     * Obtiene los puntos base obtenidos en esta ronda.
     *
     * @return puntos base ganados.
     */
    public int obtenerPuntosBaseObtenidos() {
        return puntosBaseObtenidos;
    }

    /**
     * Obtiene la bonificación por racha obtenida en esta ronda.
     *
     * @return puntos de bono por racha.
     */
    public int obtenerBonoRachaObtenido() {
        return bonoRachaObtenido;
    }

    /**
     * Obtiene la suma total de puntos ganados en esta ronda (base más bono).
     *
     * @return puntaje total de la ronda.
     */
    public int obtenerPuntosTotalesRonda() {
        return puntosBaseObtenidos + bonoRachaObtenido;
    }

    /**
     * Obtiene la racha consecutiva de aciertos al finalizar esta ronda.
     *
     * @return longitud de la racha actual.
     */
    public int obtenerRachaActual() {
        return rachaActual;
    }

    /**
     * Obtiene las vidas restantes de la torre al cierre de esta ronda.
     *
     * @return vidas disponibles.
     */
    public int obtenerVidasRestantes() {
        return vidasRestantes;
    }

    /**
     * Obtiene el estado en el que quedó la partida después de esta ronda.
     *
     * @return estado resultante de la partida.
     */
    public EstadoPartida obtenerEstadoResultante() {
        return estadoResultante;
    }

    /**
     * Devuelve una representación textual resumida del resultado de la ronda.
     *
     * @return cadena descriptiva con ronda, acierto, puntos y vidas.
     */
    @Override
    public String toString() {
        return String.format(
                "ResultadoRonda{ronda=%d, acierto=%s, tiempoAgotado=%s, puntos=%d, racha=%d, vidas=%d, estado=%s}",
                numeroRonda, acierto, tiempoAgotado, obtenerPuntosTotalesRonda(),
                rachaActual, vidasRestantes, estadoResultante);
    }
}
