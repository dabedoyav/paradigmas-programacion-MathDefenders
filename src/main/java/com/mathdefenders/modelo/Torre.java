package com.mathdefenders.modelo;

/**
 * La torre es lo que el jugador tiene que defender en el juego.
 *
 * <p>Básicamente es un contador de vidas (los escudos): arranca con 3, cada vez
 * que el jugador se equivoca pierde una y, si llega a cero, se acaba la partida.
 * Esta clase no sabe nada de la parte gráfica; solo lleva la cuenta de las vidas
 * para que la pantalla las pueda mostrar.</p>
 *
 * @author Diego Buitrago González
 * @version 1.1
 */
public class Torre {

    /**
     * Número de vidas con las que arranca la torre en una partida normal.
     * Son 3, tal como quedó en las reglas de la Entrega 1.
     */
    public static final int VIDAS_INICIALES = 3;

    /**
     * Cuántas vidas puede tener la torre como máximo. Se define al crearla
     * y después no cambia (por eso es final).
     */
    private final int vidasMaximas;

    /**
     * Cuántas vidas le quedan a la torre en este momento. Este valor sí va
     * cambiando: baja con cada error y vuelve al máximo cuando se reinicia.
     */
    private int vidasActuales;

    /**
     * Crea una torre normal, con las {@value #VIDAS_INICIALES} vidas de siempre.
     * Es el constructor que usa la partida.
     */
    public Torre() {
        // Le pasamos el trabajo al otro constructor para no repetir código
        this(VIDAS_INICIALES);
    }

    /**
     * Crea una torre con la cantidad de vidas que uno quiera. Sirve para las
     * pruebas o si más adelante queremos niveles con más o menos vidas.
     *
     * @param vidasMaximas cuántas vidas va a tener la torre (tiene que ser mayor que cero).
     * @throws IllegalArgumentException si le mandan cero o un número negativo.
     */
    public Torre(int vidasMaximas) {
        // Una torre con 0 o menos vidas no tiene sentido, entonces no la dejamos crear
        if (vidasMaximas <= 0) {
            throw new IllegalArgumentException("Las vidas máximas de la torre deben ser mayores que cero.");
        }
        this.vidasMaximas = vidasMaximas;
        // La torre siempre arranca con todas las vidas llenas
        this.vidasActuales = vidasMaximas;
    }

    /**
     * Devuelve cuántas vidas le quedan a la torre.
     *
     * @return las vidas actuales (siempre entre 0 y el máximo).
     */
    public int obtenerVidasActuales() {
        return vidasActuales;
    }

    /**
     * Devuelve el número máximo de vidas que tiene esta torre.
     *
     * @return las vidas máximas.
     */
    public int obtenerVidasMaximas() {
        return vidasMaximas;
    }

    /**
     * Le quita una vida a la torre. Se llama cada vez que el jugador responde mal.
     * Si la torre ya está en cero, no le sigue restando (no hay vidas negativas).
     *
     * @return cuántas vidas quedaron después del golpe.
     */
    public int recibirDano() {
        // Solo restamos si todavía queda algo; así nunca bajamos de cero
        if (vidasActuales > 0) {
            vidasActuales--;
        }
        return vidasActuales;
    }

    /**
     * Dice si la torre ya se cayó, o sea, si se quedó sin vidas.
     * Es la condición de Game Over.
     *
     * @return {@code true} si las vidas llegaron a cero; {@code false} si todavía queda alguna.
     */
    public boolean estaDestruida() {
        return vidasActuales == 0;
    }

    /**
     * Dice si la torre sigue en pie, es decir, si le queda por lo menos una vida.
     *
     * @return {@code true} si todavía tiene vidas; {@code false} si ya está destruida.
     */
    public boolean estaActiva() {
        return vidasActuales > 0;
    }

    /**
     * Calcula qué tan "sana" está la torre, como un número entre 0.0 y 1.0.
     * Por ejemplo, con 2 de 3 vidas da más o menos 0.67. Este valor es justo
     * el que necesita la barra de progreso (ProgressBar) de JavaFX.
     *
     * @return la fracción de vidas que quedan frente al máximo.
     */
    public double obtenerPorcentajeIntegridad() {
        // Ojo con el (double): sin eso Java hace división entera y 2/3 daría 0
        return (double) vidasActuales / vidasMaximas;
    }

    /**
     * Deja la torre otra vez con todas sus vidas, para volver a jugar.
     */
    public void reiniciar() {
        this.vidasActuales = this.vidasMaximas;
    }

    /**
     * Muestra la torre como texto, algo como {@code Torre{vidas=2/3}}.
     * Es útil para revisar rápido el estado cuando estamos probando.
     *
     * @return un texto con las vidas actuales sobre las máximas.
     */
    @Override
    public String toString() {
        return String.format("Torre{vidas=%d/%d}", vidasActuales, vidasMaximas);
    }
}
