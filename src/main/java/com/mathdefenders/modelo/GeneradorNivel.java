package com.mathdefenders.modelo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Clase base abstracta para los generadores de retos aritméticos de
 * MathDefenders: Torre de Cálculo. Encapsula el nivel asociado, el generador
 * de números aleatorios y la construcción uniforme de distractores únicos.
 *
 * @author Javier Buitrago Flórez
 * @version 1.0
 */
public abstract class GeneradorNivel {

    /**
     * Nivel de dificultad al que pertenece este generador.
     */
    private final NivelDificultad nivel;

    /**
     * Fuente pseudoaleatoria empleada para operandos y ordenamiento de opciones.
     */
    private final Random generadorAleatorio;

    /**
     * Construye un generador de retos con una fuente aleatoria estándar.
     *
     * @param nivel nivel de dificultad asociado.
     * @throws IllegalArgumentException si el nivel es nulo.
     */
    protected GeneradorNivel(NivelDificultad nivel) {
        this(nivel, new Random());
    }

    /**
     * Construye un generador de retos con una fuente aleatoria inyectable,
     * facilitando pruebas unitarias deterministas.
     *
     * @param nivel nivel de dificultad asociado.
     * @param generadorAleatorio instancia de {@link Random} utilizada para generar valores.
     * @throws IllegalArgumentException si el nivel o el generador aleatorio son nulos.
     */
    protected GeneradorNivel(NivelDificultad nivel, Random generadorAleatorio) {
        if (nivel == null) {
            throw new IllegalArgumentException("El nivel del generador no puede ser nulo.");
        }
        if (generadorAleatorio == null) {
            throw new IllegalArgumentException("El generador aleatorio no puede ser nulo.");
        }
        this.nivel = nivel;
        this.generadorAleatorio = generadorAleatorio;
    }

    /**
     * Obtiene el nivel de dificultad administrado por este generador.
     *
     * @return nivel de dificultad configurado.
     */
    public NivelDificultad obtenerNivel() {
        return nivel;
    }

    /**
     * Genera un nuevo reto matemático acorde con las operaciones y rangos
     * definidos por la subclase concreta.
     *
     * @return instancia inmutable de {@link RetoMatematico}.
     */
    public abstract RetoMatematico generarReto();

    /**
     * Devuelve un número entero aleatorio dentro del intervalo cerrado indicado.
     *
     * @param minimo límite inferior inclusivo.
     * @param maximo límite superior inclusivo.
     * @return entero aleatorio entre {@code minimo} y {@code maximo}.
     * @throws IllegalArgumentException si {@code minimo} es mayor que {@code maximo}.
     */
    protected int enteroAleatorioEnRango(int minimo, int maximo) {
        if (minimo > maximo) {
            throw new IllegalArgumentException("El valor mínimo no puede superar al valor máximo.");
        }
        return minimo + generadorAleatorio.nextInt(maximo - minimo + 1);
    }

    /**
     * Obtiene la instancia del generador pseudoaleatorio configurada.
     *
     * @return fuente aleatoria interna.
     */
    protected Random obtenerGeneradorAleatorio() {
        return generadorAleatorio;
    }

    /**
     * Construye una lista mezclada de cuatro opciones únicas que incluye la
     * respuesta correcta junto con tres distractores numéricos plausibles.
     *
     * @param respuestaCorrecta resultado exacto del reto matemático.
     * @return lista inmutable de cuatro enteros distintos en orden aleatorio.
     */
    protected List<Integer> construirOpciones(int respuestaCorrecta) {
        Set<Integer> opcionesUnicas = new LinkedHashSet<>();
        opcionesUnicas.add(respuestaCorrecta);

        int[] desplazamientos = {-10, -8, -6, -5, -4, -3, -2, -1, 1, 2, 3, 4, 5, 6, 8, 10};
        int intentos = 0;
        while (opcionesUnicas.size() < RetoMatematico.CANTIDAD_OPCIONES && intentos < 60) {
            int delta = desplazamientos[generadorAleatorio.nextInt(desplazamientos.length)];
            int candidato = respuestaCorrecta + delta;
            if (respuestaCorrecta >= 0 && candidato < 0) {
                candidato = respuestaCorrecta + Math.abs(delta);
            }
            opcionesUnicas.add(candidato);
            intentos++;
        }

        int respaldo = 1;
        while (opcionesUnicas.size() < RetoMatematico.CANTIDAD_OPCIONES) {
            opcionesUnicas.add(respuestaCorrecta + respaldo);
            respaldo++;
        }

        List<Integer> opcionesMezcladas = new ArrayList<>(opcionesUnicas);
        Collections.shuffle(opcionesMezcladas, generadorAleatorio);
        return List.copyOf(opcionesMezcladas);
    }

    /**
     * Crea un {@link RetoMatematico} completo a partir de la expresión y su
     * respuesta correcta, generando automáticamente las cuatro opciones únicas.
     *
     * @param expresion texto de la operación matemática.
     * @param respuestaCorrecta resultado entero exacto de la expresión.
     * @return nuevo reto matemático listo para jugar.
     */
    protected RetoMatematico crearReto(String expresion, int respuestaCorrecta) {
        return new RetoMatematico(
                expresion,
                respuestaCorrecta,
                construirOpciones(respuestaCorrecta),
                nivel);
    }
}
