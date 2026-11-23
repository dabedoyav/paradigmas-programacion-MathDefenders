package com.mathdefenders.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Motor principal de una partida de MathDefenders: Torre de Cálculo. Coordina
 * al {@link Jugador} (agregación), la {@link Torre} (composición), el
 * {@link GeneradorNivel} (polimorfismo) y la secuencia formal de 10 rondas,
 * puntaje, bonificaciones por racha y condiciones de victoria o derrota.
 *
 * @author Santiago Borda Gutiérrez
 * @version 1.0
 */
public class Partida {

    /**
     * Cantidad total de rondas requeridas para superar un nivel.
     */
    public static final int TOTAL_RONDAS = 10;

    /**
     * Mínimo de aciertos consecutivos necesarios para activar el bono por racha.
     */
    public static final int RACHA_MINIMA_BONO = 3;

    /**
     * Puntos adicionales otorgados por cada acierto a partir de la racha mínima.
     */
    public static final int BONO_RACHA = 50;

    /**
     * Jugador propietario de la partida (relación de agregación).
     */
    private final Jugador jugador;

    /**
     * Nivel de dificultad seleccionado para la partida.
     */
    private final NivelDificultad nivel;

    /**
     * Generador polimórfico encargado de producir los retos del nivel.
     */
    private final GeneradorNivel generador;

    /**
     * Torre científica defendida en esta partida (relación de composición).
     */
    private final Torre torre;

    /**
     * Lista ordenada con los resultados de cada ronda disputada (composición).
     */
    private final List<ResultadoRonda> historialRondas;

    /**
     * Estado actual de la partida.
     */
    private EstadoPartida estado;

    /**
     * Puntaje total acumulado por el jugador en la partida.
     */
    private int puntajeTotal;

    /**
     * Cantidad actual de aciertos consecutivos sin fallar.
     */
    private int rachaActual;

    /**
     * Racha máxima alcanzada durante toda la partida.
     */
    private int mejorRacha;

    /**
     * Total de respuestas correctas registradas en la partida.
     */
    private int aciertos;

    /**
     * Total de errores o tiempos agotados registrados en la partida.
     */
    private int errores;

    /**
     * Número de la ronda en curso (entre 1 y {@value #TOTAL_RONDAS}).
     */
    private int rondaActual;

    /**
     * Reto matemático activo que espera respuesta del jugador.
     */
    private RetoMatematico retoActual;

    /**
     * Nivel recién desbloqueado al obtener la victoria, o {@code null} si no aplica.
     */
    private NivelDificultad nivelDesbloqueadoEnPartida;

    /**
     * Inicia una nueva partida utilizando el generador estándar del nivel elegido.
     *
     * @param jugador jugador que disputará la partida.
     * @param nivel nivel de dificultad seleccionado.
     * @throws IllegalArgumentException si el jugador o el nivel son nulos.
     * @throws IllegalStateException si el jugador aún no ha desbloqueado el nivel.
     */
    public Partida(Jugador jugador, NivelDificultad nivel) {
        this(jugador, nivel, FabricaGeneradores.crear(validarNivelNoNulo(nivel)));
    }

    /**
     * Inicia una nueva partida con un generador específico (útil para pruebas
     * deterministas o variantes personalizadas del mismo nivel).
     *
     * @param jugador jugador que disputará la partida.
     * @param nivel nivel de dificultad seleccionado.
     * @param generador generador de retos correspondiente al nivel.
     * @throws IllegalArgumentException si algún parámetro es nulo o el generador
     *                                  no corresponde al nivel seleccionado.
     * @throws IllegalStateException si el jugador no tiene desbloqueado el nivel.
     */
    public Partida(Jugador jugador, NivelDificultad nivel, GeneradorNivel generador) {
        if (jugador == null) {
            throw new IllegalArgumentException("El jugador de la partida no puede ser nulo.");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel de la partida no puede ser nulo.");
        }
        if (generador == null) {
            throw new IllegalArgumentException("El generador de retos no puede ser nulo.");
        }
        if (generador.obtenerNivel() != nivel) {
            throw new IllegalArgumentException("El generador suministrado no coincide con el nivel seleccionado.");
        }
        if (!jugador.tieneNivelDesbloqueado(nivel)) {
            throw new IllegalStateException("El jugador aún no ha desbloqueado el nivel " + nivel.obtenerNombreVisible() + ".");
        }

        this.jugador = jugador;
        this.nivel = nivel;
        this.generador = generador;
        this.torre = new Torre();
        this.historialRondas = new ArrayList<>();
        this.estado = EstadoPartida.EN_CURSO;
        this.puntajeTotal = 0;
        this.rachaActual = 0;
        this.mejorRacha = 0;
        this.aciertos = 0;
        this.errores = 0;
        this.rondaActual = 1;
        this.retoActual = generador.generarReto();
        this.nivelDesbloqueadoEnPartida = null;
    }

    /**
     * Valida de forma anticipada que el nivel no sea nulo antes de delegar en la fábrica.
     *
     * @param nivel nivel de dificultad recibido en el constructor abreviado.
     * @return el mismo nivel validado.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    private static NivelDificultad validarNivelNoNulo(NivelDificultad nivel) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel de la partida no puede ser nulo.");
        }
        return nivel;
    }

    /**
     * Evalúa el valor numérico seleccionado por el jugador para el reto actual,
     * actualiza puntuación, racha y vidas, y avanza o finaliza la partida.
     *
     * @param valorSeleccionado valor numérico elegido entre las cuatro opciones del reto actual.
     * @return resultado detallado e inmutable de la ronda disputada.
     * @throws IllegalStateException si la partida ya finalizó.
     * @throws IllegalArgumentException si el valor no pertenece a las opciones del reto actual.
     */
    public ResultadoRonda responder(int valorSeleccionado) {
        verificarPartidaEnCurso();
        if (!retoActual.esOpcionValida(valorSeleccionado)) {
            throw new IllegalArgumentException("El valor seleccionado no corresponde a ninguna opción del reto actual.");
        }

        boolean acierto = retoActual.esRespuestaCorrecta(valorSeleccionado);
        return procesarDesenlaceRonda(valorSeleccionado, acierto, false);
    }

    /**
     * Evalúa la opción seleccionada según su índice (0 a 3, equivalente a A-D).
     *
     * @param indiceOpcion índice del botón elegido entre 0 y 3.
     * @return resultado detallado de la ronda disputada.
     * @throws IllegalStateException si la partida ya finalizó.
     * @throws IllegalArgumentException si el índice está fuera del rango de 0 a 3.
     */
    public ResultadoRonda responderPorIndice(int indiceOpcion) {
        verificarPartidaEnCurso();
        int valorElegido = retoActual.obtenerOpcion(indiceOpcion);
        return responder(valorElegido);
    }

    /**
     * Registra el agotamiento de tiempo/turno en la ronda actual, aplicando un
     * punto de daño a la torre, reiniciando la racha y actualizando el estado.
     *
     * @return resultado detallado de la ronda perdida por tiempo agotado.
     * @throws IllegalStateException si la partida ya finalizó.
     */
    public ResultadoRonda registrarTiempoAgotado() {
        verificarPartidaEnCurso();
        return procesarDesenlaceRonda(null, false, true);
    }

    /**
     * Verifica que la partida continúe activa antes de aceptar nuevas acciones.
     *
     * @throws IllegalStateException si la partida ya concluyó con victoria o derrota.
     */
    private void verificarPartidaEnCurso() {
        if (estado.esFinalizado()) {
            throw new IllegalStateException("La partida ya finalizó con estado: " + estado + ".");
        }
    }

    /**
     * Aplica las reglas formales de puntuación, racha, daño a la torre y
     * transición entre rondas o cierre de partida.
     *
     * @param respuestaJugador valor elegido o {@code null} cuando se agotó el tiempo.
     * @param acierto indica si la respuesta fue correcta.
     * @param tiempoAgotado indica si se agotó el tiempo del turno.
     * @return instancia de {@link ResultadoRonda} registrada en el historial.
     */
    private ResultadoRonda procesarDesenlaceRonda(
            Integer respuestaJugador,
            boolean acierto,
            boolean tiempoAgotado) {
        RetoMatematico retoEvaluado = this.retoActual;
        int rondaEvaluada = this.rondaActual;
        int puntosBase = 0;
        int bono = 0;

        if (acierto) {
            this.aciertos++;
            this.rachaActual++;
            if (this.rachaActual > this.mejorRacha) {
                this.mejorRacha = this.rachaActual;
            }
            puntosBase = this.nivel.obtenerPuntajeBase();
            if (this.rachaActual >= RACHA_MINIMA_BONO) {
                bono = BONO_RACHA;
            }
            this.puntajeTotal += puntosBase + bono;
        } else {
            this.errores++;
            this.rachaActual = 0;
            this.torre.recibirDano();
        }

        if (this.torre.estaDestruida()) {
            this.estado = EstadoPartida.DERROTA;
            this.jugador.registrarPuntaje(this.nivel, this.puntajeTotal);
        } else if (rondaEvaluada >= TOTAL_RONDAS) {
            this.estado = EstadoPartida.VICTORIA;
            this.jugador.registrarPuntaje(this.nivel, this.puntajeTotal);
            this.nivelDesbloqueadoEnPartida = this.jugador
                    .desbloquearSiguienteNivel(this.nivel)
                    .orElse(null);
        } else {
            this.rondaActual++;
            this.retoActual = this.generador.generarReto();
        }

        ResultadoRonda resultado = new ResultadoRonda(
                rondaEvaluada,
                retoEvaluado,
                respuestaJugador,
                acierto,
                tiempoAgotado,
                puntosBase,
                bono,
                this.rachaActual,
                this.torre.obtenerVidasActuales(),
                this.estado);
        this.historialRondas.add(resultado);
        return resultado;
    }

    /**
     * Obtiene el jugador asociado a la partida.
     *
     * @return jugador propietario de la partida.
     */
    public Jugador obtenerJugador() {
        return jugador;
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
     * Obtiene la torre defendida durante esta partida.
     *
     * @return torre de la partida.
     */
    public Torre obtenerTorre() {
        return torre;
    }

    /**
     * Obtiene el estado actual de la partida.
     *
     * @return estado actual ({@code EN_CURSO}, {@code VICTORIA} o {@code DERROTA}).
     */
    public EstadoPartida obtenerEstado() {
        return estado;
    }

    /**
     * Obtiene el puntaje acumulado hasta el momento.
     *
     * @return puntaje total actual.
     */
    public int obtenerPuntajeTotal() {
        return puntajeTotal;
    }

    /**
     * Obtiene la racha vigente de aciertos consecutivos.
     *
     * @return racha actual.
     */
    public int obtenerRachaActual() {
        return rachaActual;
    }

    /**
     * Obtiene la racha consecutiva más alta lograda en la partida.
     *
     * @return mejor racha alcanzada.
     */
    public int obtenerMejorRacha() {
        return mejorRacha;
    }

    /**
     * Obtiene el total de respuestas correctas hasta el momento.
     *
     * @return cantidad de aciertos.
     */
    public int obtenerAciertos() {
        return aciertos;
    }

    /**
     * Obtiene el total de errores o tiempos agotados hasta el momento.
     *
     * @return cantidad de errores.
     */
    public int obtenerErrores() {
        return errores;
    }

    /**
     * Obtiene el número de la ronda en curso o la última ronda jugada si finalizó.
     *
     * @return número de ronda entre 1 y {@value #TOTAL_RONDAS}.
     */
    public int obtenerRondaActual() {
        return rondaActual;
    }

    /**
     * Obtiene el reto matemático activo (o el último evaluado si la partida concluyó).
     *
     * @return reto matemático actual.
     */
    public RetoMatematico obtenerRetoActual() {
        return retoActual;
    }

    /**
     * Obtiene el historial inmutable de resultados de las rondas jugadas.
     *
     * @return lista no modificable de {@link ResultadoRonda}.
     */
    public List<ResultadoRonda> obtenerHistorialRondas() {
        return Collections.unmodifiableList(historialRondas);
    }

    /**
     * Calcula el porcentaje de precisión actual del jugador entre 0.0 y 100.0.
     *
     * @return porcentaje de aciertos sobre rondas jugadas, o 0.0 si no se ha jugado ninguna.
     */
    public double obtenerPorcentajePrecision() {
        if (historialRondas.isEmpty()) {
            return 0.0;
        }
        return (aciertos * 100.0) / historialRondas.size();
    }

    /**
     * Calcula la fracción de avance de rondas completadas en un rango de 0.0 a 1.0.
     *
     * @return progreso normalizado respecto a {@value #TOTAL_RONDAS} rondas.
     */
    public double obtenerProgresoRondas() {
        return (double) historialRondas.size() / TOTAL_RONDAS;
    }

    /**
     * Genera un resumen estadístico inmutable con el estado actual o final de la partida.
     *
     * @return instancia de {@link ResumenPartida}.
     */
    public ResumenPartida obtenerResumen() {
        return new ResumenPartida(
                jugador.obtenerAlias(),
                nivel,
                estado,
                puntajeTotal,
                aciertos,
                errores,
                historialRondas.size(),
                mejorRacha,
                torre.obtenerVidasActuales(),
                obtenerPorcentajePrecision(),
                nivelDesbloqueadoEnPartida);
    }

    /**
     * Indica si la partida ya concluyó (por victoria o derrota).
     *
     * @return {@code true} si finalizó; {@code false} si sigue en curso.
     */
    public boolean estaFinalizada() {
        return estado.esFinalizado();
    }
}
