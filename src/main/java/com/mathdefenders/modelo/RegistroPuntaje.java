package com.mathdefenders.modelo;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Locale;
import java.util.Objects;

/**
 * Representa un registro histórico e inmutable de puntuación obtenida por un
 * jugador en un nivel específico. Implementa {@link Comparable} para definir
 * el orden natural del ranking (mayor puntaje primero).
 *
 * @author Yeimmy Bernal Tejedor
 * @version 1.0
 */
public final class RegistroPuntaje implements Comparable<RegistroPuntaje> {

    /**
     * Comparador alternativo que ordena primero por mayor porcentaje de
     * precisión, luego por mayor puntaje y finalmente según el orden natural.
     */
    public static final Comparator<RegistroPuntaje> POR_PRECISION = Comparator
            .comparingDouble(RegistroPuntaje::obtenerPorcentajePrecision).reversed()
            .thenComparing(Comparator.comparingInt(RegistroPuntaje::obtenerPuntaje).reversed())
            .thenComparing(Comparator.naturalOrder());

    /**
     * Comparador alternativo que ordena desde los registros más recientes
     * hasta los más antiguos.
     */
    public static final Comparator<RegistroPuntaje> POR_FECHA_RECIENTE = Comparator
            .comparing(RegistroPuntaje::obtenerFechaRegistro).reversed()
            .thenComparing(Comparator.naturalOrder());

    /**
     * Comparador alternativo que ordena alfabéticamente por el alias del jugador
     * sin distinguir mayúsculas y minúsculas.
     */
    public static final Comparator<RegistroPuntaje> POR_ALIAS = Comparator
            .comparing(RegistroPuntaje::obtenerAliasJugador, String.CASE_INSENSITIVE_ORDER)
            .thenComparing(Comparator.naturalOrder());

    /**
     * Alias del jugador que obtuvo la puntuación.
     */
    private final String aliasJugador;

    /**
     * Nivel de dificultad en el que se registró el puntaje.
     */
    private final NivelDificultad nivel;

    /**
     * Puntaje total alcanzado en la partida.
     */
    private final int puntaje;

    /**
     * Porcentaje de precisión alcanzado entre 0.0 y 100.0.
     */
    private final double porcentajePrecision;

    /**
     * Marca de fecha y hora exacta en que se consolidó el registro.
     */
    private final LocalDateTime fechaRegistro;

    /**
     * Crea un registro de puntaje asignando la fecha y hora actuales del sistema.
     *
     * @param aliasJugador alias del jugador.
     * @param nivel nivel de dificultad jugado.
     * @param puntaje puntuación alcanzada.
     * @param porcentajePrecision precisión lograda en porcentaje (0.0 a 100.0).
     * @throws IllegalArgumentException si algún parámetro es nulo o fuera de rango.
     */
    public RegistroPuntaje(
            String aliasJugador,
            NivelDificultad nivel,
            int puntaje,
            double porcentajePrecision) {
        this(aliasJugador, nivel, puntaje, porcentajePrecision, LocalDateTime.now());
    }

    /**
     * Crea un registro de puntaje con una marca temporal explícita.
     *
     * @param aliasJugador alias del jugador.
     * @param nivel nivel de dificultad jugado.
     * @param puntaje puntuación alcanzada.
     * @param porcentajePrecision precisión lograda en porcentaje (0.0 a 100.0).
     * @param fechaRegistro fecha y hora del registro.
     * @throws IllegalArgumentException si algún parámetro es nulo o fuera de rango.
     */
    public RegistroPuntaje(
            String aliasJugador,
            NivelDificultad nivel,
            int puntaje,
            double porcentajePrecision,
            LocalDateTime fechaRegistro) {
        if (aliasJugador == null || aliasJugador.trim().isEmpty()) {
            throw new IllegalArgumentException("El alias del registro de puntaje no puede estar vacío.");
        }
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel del registro de puntaje no puede ser nulo.");
        }
        if (puntaje < 0) {
            throw new IllegalArgumentException("El puntaje registrado no puede ser negativo.");
        }
        if (porcentajePrecision < 0.0 || porcentajePrecision > 100.0) {
            throw new IllegalArgumentException("El porcentaje de precisión debe estar entre 0 y 100.");
        }
        if (fechaRegistro == null) {
            throw new IllegalArgumentException("La fecha de registro no puede ser nula.");
        }

        this.aliasJugador = aliasJugador.trim();
        this.nivel = nivel;
        this.puntaje = puntaje;
        this.porcentajePrecision = porcentajePrecision;
        this.fechaRegistro = fechaRegistro;
    }

    /**
     * Crea un registro de puntaje a partir del resumen estadístico de una partida.
     *
     * @param resumen resumen de partida completada o en curso.
     * @return nuevo {@link RegistroPuntaje} con la fecha actual.
     * @throws IllegalArgumentException si el resumen es nulo.
     */
    public static RegistroPuntaje desdeResumen(ResumenPartida resumen) {
        if (resumen == null) {
            throw new IllegalArgumentException("El resumen de partida no puede ser nulo.");
        }
        return new RegistroPuntaje(
                resumen.obtenerAliasJugador(),
                resumen.obtenerNivel(),
                resumen.obtenerPuntajeTotal(),
                resumen.obtenerPorcentajePrecision());
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
     * Obtiene el nivel de dificultad asociado.
     *
     * @return nivel de dificultad.
     */
    public NivelDificultad obtenerNivel() {
        return nivel;
    }

    /**
     * Obtiene la puntuación registrada.
     *
     * @return puntaje obtenido.
     */
    public int obtenerPuntaje() {
        return puntaje;
    }

    /**
     * Obtiene el porcentaje de precisión registrado.
     *
     * @return porcentaje de precisión entre 0.0 y 100.0.
     */
    public double obtenerPorcentajePrecision() {
        return porcentajePrecision;
    }

    /**
     * Obtiene la fecha y hora en que se creó el registro.
     *
     * @return marca temporal del registro.
     */
    public LocalDateTime obtenerFechaRegistro() {
        return fechaRegistro;
    }

    /**
     * Define el orden natural del ranking: mayor puntaje primero; ante empate,
     * mayor precisión; luego fecha más antigua (quien lo logró primero), alias
     * alfabético y nivel.
     *
     * @param otro registro con el que se compara esta instancia.
     * @return entero negativo si va antes, cero si es equivalente o positivo si va después.
     */
    @Override
    public int compareTo(RegistroPuntaje otro) {
        int comparacionPuntaje = Integer.compare(otro.puntaje, this.puntaje);
        if (comparacionPuntaje != 0) {
            return comparacionPuntaje;
        }
        int comparacionPrecision = Double.compare(otro.porcentajePrecision, this.porcentajePrecision);
        if (comparacionPrecision != 0) {
            return comparacionPrecision;
        }
        int comparacionFecha = this.fechaRegistro.compareTo(otro.fechaRegistro);
        if (comparacionFecha != 0) {
            return comparacionFecha;
        }
        int comparacionAlias = this.aliasJugador.compareToIgnoreCase(otro.aliasJugador);
        if (comparacionAlias != 0) {
            return comparacionAlias;
        }
        return this.nivel.compareTo(otro.nivel);
    }

    /**
     * Compara dos registros de puntaje manteniendo consistencia con {@link #compareTo(RegistroPuntaje)}.
     *
     * @param objeto otro objeto a comparar.
     * @return {@code true} si ambos registros coinciden en todos sus criterios clave.
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof RegistroPuntaje otro)) {
            return false;
        }
        return puntaje == otro.puntaje
                && Double.compare(otro.porcentajePrecision, porcentajePrecision) == 0
                && nivel == otro.nivel
                && fechaRegistro.equals(otro.fechaRegistro)
                && aliasJugador.equalsIgnoreCase(otro.aliasJugador);
    }

    /**
     * Calcula el código hash consistente con {@link #equals(Object)}.
     *
     * @return valor hash del registro de puntaje.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                aliasJugador.toLowerCase(Locale.ROOT),
                nivel,
                puntaje,
                porcentajePrecision,
                fechaRegistro);
    }

    /**
     * Devuelve una representación textual del registro de puntuación.
     *
     * @return cadena descriptiva del registro.
     */
    @Override
    public String toString() {
        return String.format(
                "RegistroPuntaje{alias='%s', nivel=%s, puntaje=%d, precision=%.1f%%, fecha=%s}",
                aliasJugador, nivel.name(), puntaje, porcentajePrecision, fechaRegistro);
    }
}
