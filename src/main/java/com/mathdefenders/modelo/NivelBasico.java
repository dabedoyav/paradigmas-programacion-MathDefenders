package com.mathdefenders.modelo;

import java.util.Random;

/**
 * Generador concreto para el Nivel 1 (Básico): operaciones directas de suma y
 * resta con operandos entre 1 y 50.
 *
 * @author Javier Buitrago Flórez
 * @version 1.0
 */
public class NivelBasico extends GeneradorNivel {

    /**
     * Crea un generador de nivel básico con aleatoriedad estándar.
     */
    public NivelBasico() {
        super(NivelDificultad.BASICO);
    }

    /**
     * Crea un generador de nivel básico con una fuente aleatoria controlada.
     *
     * @param generadorAleatorio fuente pseudoaleatoria para pruebas o partidas reproducibles.
     */
    public NivelBasico(Random generadorAleatorio) {
        super(NivelDificultad.BASICO, generadorAleatorio);
    }

    /**
     * Genera un reto aritmético de suma directa, resta directa o expresión
     * combinada de suma y resta dentro del rango de 1 a 50.
     *
     * @return reto matemático perteneciente al nivel básico.
     */
    @Override
    public RetoMatematico generarReto() {
        int minimo = obtenerNivel().obtenerValorMinimo();
        int maximo = obtenerNivel().obtenerValorMaximo();
        int tipoOperacion = enteroAleatorioEnRango(0, 2);

        int a = enteroAleatorioEnRango(minimo, maximo);
        int b = enteroAleatorioEnRango(minimo, maximo);

        if (tipoOperacion == 0) {
            return crearReto(a + " + " + b, a + b);
        }
        if (tipoOperacion == 1) {
            int mayor = Math.max(a, b);
            int menor = Math.min(a, b);
            return crearReto(mayor + " - " + menor, mayor - menor);
        }

        int sumaParcial = a + b;
        int limiteSustraendo = Math.min(maximo, sumaParcial);
        int c = enteroAleatorioEnRango(minimo, limiteSustraendo);
        return crearReto(a + " + " + b + " - " + c, sumaParcial - c);
    }
}
