package com.mathdefenders.modelo;

import java.util.Optional;

/**
 * Representa los tres niveles de dificultad disponibles en MathDefenders:
 * Torre de Cálculo, junto con sus rangos numéricos, puntaje base y tiempo
 * sugerido por ronda.
 *
 * @author Diego Bedoya Velásquez
 * @version 1.0
 */
public enum NivelDificultad {

    /**
     * Nivel 1: suma y resta directa con operandos entre 1 y 50 y 100 puntos base.
     */
    BASICO("Nivel 1: Básico", "Suma y resta directa", 1, 50, 100, 30),

    /**
     * Nivel 2: multiplicación, división exacta y operaciones combinadas simples
     * con operandos entre 2 y 20 y 150 puntos base.
     */
    INTERMEDIO("Nivel 2: Intermedio", "Multiplicación, división exacta y combinadas simples", 2, 20, 150, 25),

    /**
     * Nivel 3: jerarquía combinada con paréntesis con operandos entre 2 y 30
     * y 200 puntos base.
     */
    AVANZADO("Nivel 3: Avanzado", "Jerarquía combinada con paréntesis", 2, 30, 200, 20);

    /**
     * Nombre legible del nivel para mostrar en la interfaz de usuario.
     */
    private final String nombreVisible;

    /**
     * Descripción breve de las operaciones aritméticas incluidas en el nivel.
     */
    private final String descripcionOperaciones;

    /**
     * Valor mínimo permitido para los operandos del nivel.
     */
    private final int valorMinimo;

    /**
     * Valor máximo permitido para los operandos del nivel.
     */
    private final int valorMaximo;

    /**
     * Puntaje base otorgado por cada respuesta correcta en el nivel.
     */
    private final int puntajeBase;

    /**
     * Tiempo sugerido en segundos para responder cada ronda del nivel.
     */
    private final int tiempoSugeridoSegundos;

    /**
     * Construye una constante de dificultad con sus parámetros pedagógicos y de puntuación.
     *
     * @param nombreVisible nombre visible del nivel.
     * @param descripcionOperaciones descripción de las operaciones incluidas.
     * @param valorMinimo límite inferior de los operandos.
     * @param valorMaximo límite superior de los operandos.
     * @param puntajeBase puntos base obtenidos por acierto.
     * @param tiempoSugeridoSegundos tiempo de referencia por ronda en segundos.
     */
    private NivelDificultad(
            String nombreVisible,
            String descripcionOperaciones,
            int valorMinimo,
            int valorMaximo,
            int puntajeBase,
            int tiempoSugeridoSegundos) {
        this.nombreVisible = nombreVisible;
        this.descripcionOperaciones = descripcionOperaciones;
        this.valorMinimo = valorMinimo;
        this.valorMaximo = valorMaximo;
        this.puntajeBase = puntajeBase;
        this.tiempoSugeridoSegundos = tiempoSugeridoSegundos;
    }

    /**
     * Obtiene el nombre legible del nivel.
     *
     * @return nombre visible del nivel.
     */
    public String obtenerNombreVisible() {
        return nombreVisible;
    }

    /**
     * Obtiene la descripción de las operaciones evaluadas en el nivel.
     *
     * @return descripción de las operaciones aritméticas.
     */
    public String obtenerDescripcionOperaciones() {
        return descripcionOperaciones;
    }

    /**
     * Obtiene el límite inferior de los valores generados en este nivel.
     *
     * @return valor mínimo permitido.
     */
    public int obtenerValorMinimo() {
        return valorMinimo;
    }

    /**
     * Obtiene el límite superior de los valores generados en este nivel.
     *
     * @return valor máximo permitido.
     */
    public int obtenerValorMaximo() {
        return valorMaximo;
    }

    /**
     * Obtiene el puntaje base asignado por cada respuesta correcta.
     *
     * @return puntaje base por acierto.
     */
    public int obtenerPuntajeBase() {
        return puntajeBase;
    }

    /**
     * Obtiene el tiempo sugerido en segundos para resolver cada reto del nivel.
     *
     * @return tiempo sugerido en segundos.
     */
    public int obtenerTiempoSugeridoSegundos() {
        return tiempoSugeridoSegundos;
    }

    /**
     * Obtiene el siguiente nivel en orden de progresión si existe.
     *
     * @return un {@link Optional} con el siguiente nivel, o vacío si este es el máximo.
     */
    public Optional<NivelDificultad> obtenerSiguienteNivel() {
        return switch (this) {
            case BASICO -> Optional.of(INTERMEDIO);
            case INTERMEDIO -> Optional.of(AVANZADO);
            case AVANZADO -> Optional.empty();
        };
    }

    /**
     * Devuelve la representación legible del nivel de dificultad.
     *
     * @return nombre visible del nivel.
     */
    @Override
    public String toString() {
        return nombreVisible;
    }
}
