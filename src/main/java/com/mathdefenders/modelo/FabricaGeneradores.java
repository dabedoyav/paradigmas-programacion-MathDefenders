package com.mathdefenders.modelo;

import java.util.Random;

/**
 * Fábrica utilitaria encargada de instanciar el {@link GeneradorNivel}
 * apropiado según el {@link NivelDificultad} solicitado, aplicando polimorfismo.
 *
 * @author Javier Buitrago Flórez
 * @version 1.0
 */
public final class FabricaGeneradores {

    /**
     * Constructor privado para impedir la instanciación de esta clase utilitaria.
     */
    private FabricaGeneradores() {
    }

    /**
     * Crea el generador de retos correspondiente al nivel de dificultad indicado.
     *
     * @param nivel nivel de dificultad deseado.
     * @return instancia concreta de {@link GeneradorNivel}.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    public static GeneradorNivel crear(NivelDificultad nivel) {
        return crear(nivel, new Random());
    }

    /**
     * Crea el generador de retos correspondiente al nivel indicado utilizando una
     * fuente pseudoaleatoria específica.
     *
     * @param nivel nivel de dificultad deseado.
     * @param generadorAleatorio fuente aleatoria empleada por el generador.
     * @return instancia concreta de {@link GeneradorNivel}.
     * @throws IllegalArgumentException si el nivel o el generador aleatorio son nulos.
     */
    public static GeneradorNivel crear(NivelDificultad nivel, Random generadorAleatorio) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel para crear el generador no puede ser nulo.");
        }
        if (generadorAleatorio == null) {
            throw new IllegalArgumentException("El generador aleatorio no puede ser nulo.");
        }
        return switch (nivel) {
            case BASICO -> new NivelBasico(generadorAleatorio);
            case INTERMEDIO -> new NivelIntermedio(generadorAleatorio);
            case AVANZADO -> new NivelAvanzado(generadorAleatorio);
        };
    }
}
