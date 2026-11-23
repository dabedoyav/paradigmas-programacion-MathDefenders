package com.mathdefenders.modelo;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

/**
 * Administra la tabla histórica de puntajes máximos locales utilizando un
 * contenedor ordenado ({@link TreeSet}) que clasifica automáticamente los
 * registros según el orden natural definido en {@link RegistroPuntaje}.
 *
 * @author Yeimmy Bernal Tejedor
 * @version 1.0
 */
public class GestorPuntajes {

    /**
     * Conjunto ordenado que almacena todos los registros de puntaje sin duplicados.
     */
    private final TreeSet<RegistroPuntaje> registros;

    /**
     * Crea un gestor de puntajes inicialmente vacío.
     */
    public GestorPuntajes() {
        this.registros = new TreeSet<>();
    }

    /**
     * Registra una nueva puntuación en el ranking ordenado.
     *
     * @param registro instancia de {@link RegistroPuntaje} a almacenar.
     * @return {@code true} si el registro se agregó; {@code false} si ya existía uno equivalente.
     * @throws IllegalArgumentException si el registro es nulo.
     */
    public boolean registrar(RegistroPuntaje registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro de puntaje no puede ser nulo.");
        }
        return registros.add(registro);
    }

    /**
     * Construye y registra un nuevo puntaje directamente a partir de un {@link ResumenPartida}.
     *
     * @param resumen resumen estadístico de la partida finalizada.
     * @return registro de puntaje creado y almacenado.
     * @throws IllegalArgumentException si el resumen es nulo.
     */
    public RegistroPuntaje registrarDesdeResumen(ResumenPartida resumen) {
        RegistroPuntaje registro = RegistroPuntaje.desdeResumen(resumen);
        registrar(registro);
        return registro;
    }

    /**
     * Obtiene todos los registros almacenados siguiendo su orden natural de clasificación.
     *
     * @return lista inmutable de puntajes ordenados de mayor a menor.
     */
    public List<RegistroPuntaje> obtenerTodosOrdenados() {
        return List.copyOf(registros);
    }

    /**
     * Obtiene los mejores registros hasta el límite especificado.
     *
     * @param limite cantidad máxima de registros a devolver (debe ser positiva).
     * @return lista inmutable con los mejores puntajes hasta el límite solicitado.
     * @throws IllegalArgumentException si el límite es menor o igual que cero.
     */
    public List<RegistroPuntaje> obtenerTop(int limite) {
        if (limite <= 0) {
            throw new IllegalArgumentException("El límite de registros para el Top debe ser mayor que cero.");
        }
        List<RegistroPuntaje> resultado = new ArrayList<>(Math.min(limite, registros.size()));
        for (RegistroPuntaje registro : registros) {
            if (resultado.size() >= limite) {
                break;
            }
            resultado.add(registro);
        }
        return List.copyOf(resultado);
    }

    /**
     * Filtra los registros ordenados correspondientes a un nivel de dificultad específico.
     *
     * @param nivel nivel de dificultad por filtrar.
     * @return lista inmutable de registros de ese nivel en orden de clasificación.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    public List<RegistroPuntaje> obtenerPorNivel(NivelDificultad nivel) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel a filtrar no puede ser nulo.");
        }
        List<RegistroPuntaje> filtrados = new ArrayList<>();
        for (RegistroPuntaje registro : registros) {
            if (registro.obtenerNivel() == nivel) {
                filtrados.add(registro);
            }
        }
        return List.copyOf(filtrados);
    }

    /**
     * Filtra los registros ordenados correspondientes al alias de un jugador,
     * sin distinguir mayúsculas y minúsculas.
     *
     * @param alias alias del jugador a consultar.
     * @return lista inmutable de registros pertenecientes al jugador indicado.
     * @throws IllegalArgumentException si el alias es nulo o está vacío.
     */
    public List<RegistroPuntaje> obtenerPorJugador(String alias) {
        if (alias == null || alias.trim().isEmpty()) {
            throw new IllegalArgumentException("El alias a filtrar no puede ser nulo ni estar vacío.");
        }
        String aliasBuscado = alias.trim();
        List<RegistroPuntaje> filtrados = new ArrayList<>();
        for (RegistroPuntaje registro : registros) {
            if (registro.obtenerAliasJugador().equalsIgnoreCase(aliasBuscado)) {
                filtrados.add(registro);
            }
        }
        return List.copyOf(filtrados);
    }

    /**
     * Obtiene el registro que encabeza la clasificación general si existe al menos uno.
     *
     * @return {@link Optional} con el mejor puntaje registrado, o vacío si no hay registros.
     */
    public Optional<RegistroPuntaje> obtenerMejorPuntaje() {
        if (registros.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(registros.first());
    }

    /**
     * Obtiene una lista inmutable con todos los registros ordenados según un
     * comparador alternativo suministrado.
     *
     * @param comparador criterio personalizado de ordenamiento.
     * @return lista inmutable ordenada según {@code comparador}.
     * @throws IllegalArgumentException si el comparador es nulo.
     */
    public List<RegistroPuntaje> obtenerOrdenadosPor(Comparator<RegistroPuntaje> comparador) {
        if (comparador == null) {
            throw new IllegalArgumentException("El comparador no puede ser nulo.");
        }
        List<RegistroPuntaje> ordenados = new ArrayList<>(registros);
        ordenados.sort(comparador);
        return List.copyOf(ordenados);
    }

    /**
     * Obtiene la cantidad total de puntajes almacenados en el gestor.
     *
     * @return número de registros en el ranking.
     */
    public int obtenerCantidadRegistros() {
        return registros.size();
    }

    /**
     * Elimina todos los registros almacenados en el gestor de puntajes.
     */
    public void limpiar() {
        registros.clear();
    }
}
