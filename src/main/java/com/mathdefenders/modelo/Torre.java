package com.mathdefenders.modelo;

/**
 * Representa la torre científica que el jugador debe proteger mediante el
 * sistema de vidas o escudos activos.
 *
 * @author Diego Buitrago González
 * @version 1.0
 */
public class Torre {

    /**
     * Cantidad predeterminada de vidas (escudos) de la torre al iniciar una partida.
     */
    public static final int VIDAS_INICIALES = 3;

    /**
     * Capacidad máxima de vidas configurada para esta torre.
     */
    private final int vidasMaximas;

    /**
     * Cantidad actual de vidas disponibles en la torre.
     */
    private int vidasActuales;

    /**
     * Crea una torre con la cantidad estándar de {@value #VIDAS_INICIALES} vidas iniciales.
     */
    public Torre() {
        this(VIDAS_INICIALES);
    }

    /**
     * Crea una torre con una cantidad personalizada de vidas máximas.
     *
     * @param vidasMaximas número positivo de vidas iniciales de la torre.
     * @throws IllegalArgumentException si {@code vidasMaximas} es menor o igual que cero.
     */
    public Torre(int vidasMaximas) {
        if (vidasMaximas <= 0) {
            throw new IllegalArgumentException("Las vidas máximas de la torre deben ser mayores que cero.");
        }
        this.vidasMaximas = vidasMaximas;
        this.vidasActuales = vidasMaximas;
    }

    /**
     * Obtiene la cantidad de vidas restantes de la torre.
     *
     * @return vidas actuales entre 0 y {@link #obtenerVidasMaximas()}.
     */
    public int obtenerVidasActuales() {
        return vidasActuales;
    }

    /**
     * Obtiene la cantidad máxima de vidas configurada para la torre.
     *
     * @return vidas máximas de la torre.
     */
    public int obtenerVidasMaximas() {
        return vidasMaximas;
    }

    /**
     * Aplica un punto de daño a la torre disminuyendo sus vidas en uno, sin
     * descender por debajo de cero.
     *
     * @return cantidad de vidas restantes después de recibir el impacto.
     */
    public int recibirDano() {
        if (vidasActuales > 0) {
            vidasActuales--;
        }
        return vidasActuales;
    }

    /**
     * Indica si la torre perdió todas sus vidas.
     *
     * @return {@code true} si las vidas llegaron a cero; {@code false} en caso contrario.
     */
    public boolean estaDestruida() {
        return vidasActuales == 0;
    }

    /**
     * Indica si la torre conserva al menos una vida activa.
     *
     * @return {@code true} si la torre sigue en pie; {@code false} si está destruida.
     */
    public boolean estaActiva() {
        return vidasActuales > 0;
    }

    /**
     * Calcula la proporción de integridad actual de la torre en un rango de
     * 0.0 a 1.0, útil para controles gráficos como barras de progreso.
     *
     * @return fracción de vidas restantes respecto a las vidas máximas.
     */
    public double obtenerPorcentajeIntegridad() {
        return (double) vidasActuales / vidasMaximas;
    }

    /**
     * Restaura las vidas actuales de la torre a su capacidad máxima inicial.
     */
    public void reiniciar() {
        this.vidasActuales = this.vidasMaximas;
    }

    /**
     * Devuelve una representación textual del estado de salud de la torre.
     *
     * @return cadena con las vidas actuales sobre las vidas máximas.
     */
    @Override
    public String toString() {
        return String.format("Torre{vidas=%d/%d}", vidasActuales, vidasMaximas);
    }
}
