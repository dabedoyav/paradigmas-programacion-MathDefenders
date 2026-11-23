package com.mathdefenders.modelo;

/**
 * Representa los posibles estados del ciclo de vida de una partida en
 * MathDefenders: Torre de Cálculo.
 *
 * @author Diego Bedoya Velásquez
 * @version 1.0
 */
public enum EstadoPartida {

    /**
     * La partida sigue activa y el jugador aún tiene rondas pendientes y vidas disponibles.
     */
    EN_CURSO("En curso"),

    /**
     * El jugador completó las 10 rondas del nivel conservando al menos una vida.
     */
    VICTORIA("Victoria"),

    /**
     * La torre quedó sin vidas antes de completar las rondas del nivel.
     */
    DERROTA("Derrota");

    /**
     * Descripción legible del estado de la partida.
     */
    private final String etiqueta;

    /**
     * Inicializa el estado de la partida con su etiqueta descriptiva.
     *
     * @param etiqueta texto descriptivo del estado.
     */
    private EstadoPartida(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    /**
     * Obtiene la etiqueta legible del estado.
     *
     * @return etiqueta del estado.
     */
    public String obtenerEtiqueta() {
        return etiqueta;
    }

    /**
     * Indica si el estado corresponde a una partida concluida (victoria o derrota).
     *
     * @return {@code true} si la partida finalizó; {@code false} si continúa en curso.
     */
    public boolean esFinalizado() {
        return this != EN_CURSO;
    }

    /**
     * Devuelve la etiqueta textual del estado.
     *
     * @return etiqueta del estado de la partida.
     */
    @Override
    public String toString() {
        return etiqueta;
    }
}
