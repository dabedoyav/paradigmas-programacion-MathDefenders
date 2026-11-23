package com.mathdefenders.modelo;

import java.util.Collections;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * Modela al jugador local de MathDefenders: Torre de Cálculo, encapsulando su
 * alias, los niveles que ha desbloqueado y sus mejores puntajes por nivel.
 *
 * @author Diego Bedoya Velásquez
 * @version 1.0
 */
public class Jugador {

    /**
     * Alias o nombre identificador del jugador dentro del juego.
     */
    private String alias;

    /**
     * Conjunto de niveles de dificultad habilitados para el jugador.
     */
    private final Set<NivelDificultad> nivelesDesbloqueados;

    /**
     * Mapa que registra la puntuación máxima alcanzada por el jugador en cada nivel.
     */
    private final Map<NivelDificultad, Integer> mejoresPuntajesPorNivel;

    /**
     * Crea un jugador nuevo con el nivel básico desbloqueado por defecto y
     * puntajes máximos inicializados en cero.
     *
     * @param alias nombre o alias del jugador.
     * @throws IllegalArgumentException si el alias es nulo o está en blanco.
     */
    public Jugador(String alias) {
        this.alias = validarYNormalizarAlias(alias);
        this.nivelesDesbloqueados = EnumSet.of(NivelDificultad.BASICO);
        this.mejoresPuntajesPorNivel = new EnumMap<>(NivelDificultad.class);
        for (NivelDificultad nivel : NivelDificultad.values()) {
            this.mejoresPuntajesPorNivel.put(nivel, 0);
        }
    }

    /**
     * Valida que el alias no sea nulo ni vacío y elimina espacios extremos.
     *
     * @param aliasCandidato alias suministrado por el usuario.
     * @return alias limpio sin espacios iniciales ni finales.
     * @throws IllegalArgumentException si el alias es nulo o queda vacío tras limpiar espacios.
     */
    private static String validarYNormalizarAlias(String aliasCandidato) {
        if (aliasCandidato == null || aliasCandidato.trim().isEmpty()) {
            throw new IllegalArgumentException("El alias del jugador no puede ser nulo ni estar vacío.");
        }
        return aliasCandidato.trim();
    }

    /**
     * Obtiene el alias actual del jugador.
     *
     * @return alias del jugador.
     */
    public String obtenerAlias() {
        return alias;
    }

    /**
     * Actualiza el alias del jugador validando que no sea nulo ni vacío.
     *
     * @param nuevoAlias nuevo alias del jugador.
     * @throws IllegalArgumentException si el nuevo alias es nulo o está en blanco.
     */
    public void establecerAlias(String nuevoAlias) {
        this.alias = validarYNormalizarAlias(nuevoAlias);
    }

    /**
     * Obtiene una vista inmutable de los niveles desbloqueados por el jugador.
     *
     * @return conjunto no modificable de niveles desbloqueados.
     */
    public Set<NivelDificultad> obtenerNivelesDesbloqueados() {
        return Collections.unmodifiableSet(EnumSet.copyOf(nivelesDesbloqueados));
    }

    /**
     * Verifica si el jugador tiene acceso al nivel especificado.
     *
     * @param nivel nivel de dificultad a consultar.
     * @return {@code true} si el nivel está desbloqueado; {@code false} en caso contrario.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    public boolean tieneNivelDesbloqueado(NivelDificultad nivel) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel a consultar no puede ser nulo.");
        }
        return nivelesDesbloqueados.contains(nivel);
    }

    /**
     * Desbloquea directamente el nivel indicado para el jugador.
     *
     * @param nivel nivel de dificultad a habilitar.
     * @return {@code true} si el nivel fue desbloqueado por primera vez;
     *         {@code false} si ya se encontraba desbloqueado.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    public boolean desbloquearNivel(NivelDificultad nivel) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel a desbloquear no puede ser nulo.");
        }
        return nivelesDesbloqueados.add(nivel);
    }

    /**
     * Desbloquea el nivel inmediatamente superior al nivel superado, siempre que
     * exista uno siguiente y aún no se encuentre desbloqueado.
     *
     * @param nivelSuperado nivel que el jugador acaba de completar con victoria.
     * @return un {@link Optional} con el nuevo nivel desbloqueado, o vacío si ya
     *         estaba desbloqueado o no existe un nivel superior.
     * @throws IllegalArgumentException si el nivel superado es nulo.
     */
    public Optional<NivelDificultad> desbloquearSiguienteNivel(NivelDificultad nivelSuperado) {
        if (nivelSuperado == null) {
            throw new IllegalArgumentException("El nivel superado no puede ser nulo.");
        }
        Optional<NivelDificultad> siguiente = nivelSuperado.obtenerSiguienteNivel();
        if (siguiente.isPresent() && nivelesDesbloqueados.add(siguiente.get())) {
            return siguiente;
        }
        return Optional.empty();
    }

    /**
     * Registra un puntaje obtenido en un nivel determinado y actualiza el récord
     * personal si el nuevo puntaje supera el anterior.
     *
     * @param nivel nivel en el que se obtuvo el puntaje.
     * @param puntaje puntuación alcanzada en la partida.
     * @return {@code true} si se estableció un nuevo récord personal en ese nivel;
     *         {@code false} en caso contrario.
     * @throws IllegalArgumentException si el nivel es nulo o el puntaje es negativo.
     */
    public boolean registrarPuntaje(NivelDificultad nivel, int puntaje) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel del puntaje no puede ser nulo.");
        }
        if (puntaje < 0) {
            throw new IllegalArgumentException("El puntaje registrado no puede ser negativo.");
        }
        int mejorActual = mejoresPuntajesPorNivel.get(nivel);
        if (puntaje > mejorActual) {
            mejoresPuntajesPorNivel.put(nivel, puntaje);
            return true;
        }
        return false;
    }

    /**
     * Obtiene la puntuación máxima alcanzada por el jugador en el nivel indicado.
     *
     * @param nivel nivel de dificultad consultado.
     * @return mejor puntaje registrado en ese nivel.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    public int obtenerMejorPuntaje(NivelDificultad nivel) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel consultado no puede ser nulo.");
        }
        return mejoresPuntajesPorNivel.get(nivel);
    }

    /**
     * Obtiene la puntuación máxima alcanzada por el jugador considerando todos los niveles.
     *
     * @return puntaje máximo global del jugador.
     */
    public int obtenerPuntajeMaximoGlobal() {
        int maximo = 0;
        for (int puntaje : mejoresPuntajesPorNivel.values()) {
            if (puntaje > maximo) {
                maximo = puntaje;
            }
        }
        return maximo;
    }

    /**
     * Obtiene una copia inmutable del mapa de mejores puntajes por nivel.
     *
     * @return mapa no modificable con el récord por cada nivel.
     */
    public Map<NivelDificultad, Integer> obtenerMejoresPuntajesPorNivel() {
        return Collections.unmodifiableMap(new EnumMap<>(mejoresPuntajesPorNivel));
    }

    /**
     * Compara dos jugadores por su alias sin distinguir mayúsculas y minúsculas.
     *
     * @param objeto otro objeto a comparar.
     * @return {@code true} si ambos representan al mismo alias; {@code false} en otro caso.
     */
    @Override
    public boolean equals(Object objeto) {
        if (this == objeto) {
            return true;
        }
        if (!(objeto instanceof Jugador otro)) {
            return false;
        }
        return alias.equalsIgnoreCase(otro.alias);
    }

    /**
     * Calcula el código hash del jugador usando su alias en minúsculas.
     *
     * @return valor hash del jugador.
     */
    @Override
    public int hashCode() {
        return Objects.hash(alias.toLowerCase(Locale.ROOT));
    }

    /**
     * Devuelve una representación textual resumida del jugador.
     *
     * @return cadena descriptiva del jugador.
     */
    @Override
    public String toString() {
        return String.format("Jugador{alias='%s', desbloqueados=%s, maximoGlobal=%d}",
                alias, nivelesDesbloqueados, obtenerPuntajeMaximoGlobal());
    }
}
