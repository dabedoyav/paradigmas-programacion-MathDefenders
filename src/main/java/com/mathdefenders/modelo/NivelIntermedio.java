package com.mathdefenders.modelo;

import java.util.Random;

/**
 * Generador concreto para el Nivel 2 (Intermedio): multiplicación, división
 * entera exacta y operaciones combinadas simples con paréntesis en el rango
 * de 2 a 20.
 *
 * @author Javier Buitrago Flórez
 * @version 1.0
 */
public class NivelIntermedio extends GeneradorNivel {

    /**
     * Crea un generador de nivel intermedio con aleatoriedad estándar.
     */
    public NivelIntermedio() {
        super(NivelDificultad.INTERMEDIO);
    }

    /**
     * Crea un generador de nivel intermedio con una fuente aleatoria controlada.
     *
     * @param generadorAleatorio fuente pseudoaleatoria para pruebas o partidas reproducibles.
     */
    public NivelIntermedio(Random generadorAleatorio) {
        super(NivelDificultad.INTERMEDIO, generadorAleatorio);
    }

    /**
     * Genera un reto aritmético de multiplicación, división entera exacta o
     * expresión combinada de producto con suma o resta.
     *
     * @return reto matemático perteneciente al nivel intermedio.
     */
    @Override
    public RetoMatematico generarReto() {
        int minimo = obtenerNivel().obtenerValorMinimo();
        int maximo = obtenerNivel().obtenerValorMaximo();
        int tipoOperacion = enteroAleatorioEnRango(0, 3);

        if (tipoOperacion == 0) {
            int a = enteroAleatorioEnRango(minimo, maximo);
            int b = enteroAleatorioEnRango(minimo, maximo);
            return crearReto(a + " × " + b, a * b);
        }

        if (tipoOperacion == 1) {
            int divisor = enteroAleatorioEnRango(minimo, maximo);
            int cociente = enteroAleatorioEnRango(minimo, maximo);
            int dividendo = divisor * cociente;
            return crearReto(dividendo + " ÷ " + divisor, cociente);
        }

        int a = enteroAleatorioEnRango(minimo, maximo);
        int b = enteroAleatorioEnRango(minimo, maximo);
        int producto = a * b;
        int c = enteroAleatorioEnRango(minimo, Math.min(maximo, producto));

        if (tipoOperacion == 2) {
            return crearReto("(" + a + " × " + b + ") - " + c, producto - c);
        }
        return crearReto("(" + a + " × " + b + ") + " + c, producto + c);
    }
}
