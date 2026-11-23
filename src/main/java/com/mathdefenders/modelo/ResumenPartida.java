package com.mathdefenders.modelo;

import java.util.Optional;

/**
 * Representa un resumen estadístico inmutable de una {@link Partida}, listo
 * para presentarse en la pantalla final y alimentar el ranking de puntajes.
 *
 * @author Santiago Borda Gutiérrez
 * @version 1.0
 */
public final class ResumenPartida {

    /**
     * Alias del jugador que disputó la partida.
     */
    private final String aliasJugador;

    /**
     * Nivel de dificultad jugado.
     */
    private final NivelDificultad nivel;

    /**
     * Estado de la partida al generar el resumen.
     */
    private final EstadoPartida estadoFinal;

    /**
     * Puntaje total acumulado durante la partida.
     */
    private final int puntajeTotal;

    /**
     * Cantidad total de respuestas acertadas.
     */
    private final int aciertos;

    /**
     * Cantidad total de errores o tiempos agotados.
     */
    private final int errores;

    /**
     * Número total de rondas disputadas en la partida.
     */
    private final int rondasJugadas;

    /**
     * Racha máxima de aciertos consecutivos alcanzada en la partida.
     */
    private final int mejorRacha;

    /**
     * Vidas restantes de la torre al generar el resumen.
     */
    private final int vidasRestantes;

    /**
     * Porcentaje de precisión entre 0.0 y 100.0.
     */
    private final double porcentajePrecision;

    /**
     * Nuevo nivel desbloqueado gracias a esta victoria, o {@code null} si no aplica.
     */
    private final NivelDificultad nivelDesbloqueado;

    /**
     * Construye un resumen estadístico inmutable de la partida.
     *
     * @param aliasJugador alias del jugador.
     * @param nivel nivel jugado.
     * @param estadoFinal estado actual o final de la partida.
     * @param puntajeTotal puntaje total acumulado.
     * @param aciertos número de aciertos.
     * @param errores número de errores.
     * @param rondasJugadas cantidad de rondas completadas.
     * @param mejorRacha mejor racha de aciertos consecutivos.
     * @param vidasRestantes vidas restantes de la torre.
     * @param porcentajePrecision porcentaje de aciertos sobre rondas jugadas.
     * @param nivelDesbloqueado nuevo nivel desbloqueado o {@code null} si no se desbloqueó ninguno.
     * @throws IllegalArgumentException si los datos obligatorios son nulos o inconsistentes.
     */
    public ResumenPartida(
            String aliasJugador,
            NivelDificultad nivel,
            EstadoPartida estadoFinal,
            int puntajeTotal,
            int aciertos,
            int errores,
            int rondasJugadas,
            int mejorRacha,
            int vidasRestantes,
            double porcentajePrecision,
            NivelDificultad nivelDesbloqueado) {
        if (aliasJugador == null || aliasJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("El alias del jugador en el resumen no puede estar vacío.");
        }
        if (nivel == null || estadoFinal == null) {
            throw new IllegalArgumentException("El nivel y el estado del resumen no pueden ser nulos.");
        }
        if (puntajeTotal < 0 || aciertos < 0 || errores < 0 || rondasJugadas < 0
                || mejorRacha < 0 || vidasRestantes < 0) {
            throw new IllegalArgumentException("Los contadores del resumen no pueden ser negativos.");
        }
        if (porcentajePrecision < 0.0 || porcentajePrecision > 100.0) {
            throw new IllegalArgumentException("El porcentaje de precisión debe estar entre 0 y 100.");
        }

        this.aliasJugador = aliasJugador.trim();
        this.nivel = nivel;
        this.estadoFinal = estadoFinal;
        this.puntajeTotal = puntajeTotal;
        this.aciertos = aciertos;
        this.errores = errores;
        this.rondasJugadas = rondasJugadas;
        this.mejorRacha = mejorRacha;
        this.vidasRestantes = vidasRestantes;
        this.porcentajePrecision = porcentajePrecision;
        this.nivelDesbloqueado = nivelDesbloqueado;
    }

    /**
     * Obtiene el alias del jugador.
     *
     * @return alias del jugador.
     */
    public String obtenerAliasJugador() {
        return aliasJugador;
    }

    /**
     * Obtiene el nivel de dificultad disputado.
     *
     * @return nivel de dificultad.
     */
    public NivelDificultad obtenerNivel() {
        return nivel;
    }

    /**
     * Obtiene el estado final o actual de la partida.
     *
     * @return estado de la partida.
     */
    public EstadoPartida obtenerEstadoFinal() {
        return estadoFinal;
    }

    /**
     * Obtiene el puntaje total acumulado.
     *
     * @return puntaje total.
     */
    public int obtenerPuntajeTotal() {
        return puntajeTotal;
    }

    /**
     * Obtiene el total de respuestas correctas.
     *
     * @return cantidad de aciertos.
     */
    public int obtenerAciertos() {
        return aciertos;
    }

    /**
     * Obtiene el total de respuestas incorrectas o tiempos agotados.
     *
     * @return cantidad de errores.
     */
    public int obtenerErrores() {
        return errores;
    }

    /**
     * Obtiene el número de rondas jugadas.
     *
     * @return cantidad de rondas jugadas.
     */
    public int obtenerRondasJugadas() {
        return rondasJugadas;
    }

    /**
     * Obtiene la mejor racha de aciertos consecutivos lograda.
     *
     * @return mejor racha de la partida.
     */
    public int obtenerMejorRacha() {
        return mejorRacha;
    }

    /**
     * Obtiene la cantidad de vidas restantes de la torre.
     *
     * @return vidas restantes.
     */
    public int obtenerVidasRestantes() {
        return vidasRestantes;
    }

    /**
     * Obtiene el porcentaje de precisión alcanzado en la partida (0.0 a 100.0).
     *
     * @return porcentaje de precisión.
     */
    public double obtenerPorcentajePrecision() {
        return porcentajePrecision;
    }

    /**
     * Obtiene el nivel que se desbloqueó al superar esta partida, si aplica.
     *
     * @return {@link Optional} con el nuevo nivel desbloqueado, o vacío en caso contrario.
     */
    public Optional<NivelDificultad> obtenerNivelDesbloqueado() {
        return Optional.ofNullable(nivelDesbloqueado);
    }

    /**
     * Devuelve una representación textual del resumen estadístico.
     *
     * @return cadena descriptiva del resumen de partida.
     */
    @Override
    public String toString() {
        return String.format(
                "ResumenPartida{jugador='%s', nivel=%s, estado=%s, puntaje=%d, precision=%.1f%%, mejorRacha=%d}",
                aliasJugador, nivel.name(), estadoFinal, puntajeTotal, porcentajePrecision, mejorRacha);
    }
}
