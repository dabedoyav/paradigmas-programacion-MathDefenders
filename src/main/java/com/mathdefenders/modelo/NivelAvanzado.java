package com.mathdefenders.modelo;

import java.util.Random;

/**
 * Generador concreto para el Nivel 3 (Avanzado): jerarquía de operaciones
 * combinada dentro del rango de 2 a 30. Mezcla expresiones <em>sin</em>
 * paréntesis, donde el jugador debe aplicar la precedencia (multiplicación y
 * división antes que suma y resta), con expresiones donde los paréntesis son
 * necesarios para alterar dicha precedencia. Ninguna expresión incluye
 * paréntesis redundantes.
 *
 * @author Javier Buitrago Flórez
 * @version 1.1
 */
public class NivelAvanzado extends GeneradorNivel {

    /**
     * Cantidad de plantillas de expresión que puede producir este generador
     * (los tipos van de 0 a {@code TIPOS_EXPRESION - 1}).
     */
    private static final int TIPOS_EXPRESION = 6;

    /**
     * Crea un generador de nivel avanzado con aleatoriedad estándar.
     */
    public NivelAvanzado() {
        super(NivelDificultad.AVANZADO);
    }

    /**
     * Crea un generador de nivel avanzado con una fuente aleatoria controlada.
     *
     * @param generadorAleatorio fuente pseudoaleatoria para pruebas o partidas reproducibles.
     */
    public NivelAvanzado(Random generadorAleatorio) {
        super(NivelDificultad.AVANZADO, generadorAleatorio);
    }

    /**
     * Genera un reto aritmético compuesto que refuerza la precedencia entre
     * división exacta, multiplicación, suma y resta. Las plantillas sin
     * paréntesis son {@code a + b × c}, {@code D ÷ d + c × e},
     * {@code a × b - D ÷ d} y {@code a × b + c × d}; las que sí requieren
     * paréntesis son {@code (a + b) × c - d} y {@code (a + b) × (c - d)}.
     * Todos los resultados son enteros no negativos y las divisiones son exactas.
     *
     * @return reto matemático perteneciente al nivel avanzado.
     */
    @Override
    public RetoMatematico generarReto() {
        int minimo = obtenerNivel().obtenerValorMinimo();
        int maximo = obtenerNivel().obtenerValorMaximo();
        int tipoOperacion = enteroAleatorioEnRango(0, TIPOS_EXPRESION - 1);

        switch (tipoOperacion) {
            case 0: {
                int a = enteroAleatorioEnRango(minimo, maximo);
                int b = enteroAleatorioEnRango(minimo, maximo);
                int c = enteroAleatorioEnRango(minimo, maximo);
                return crearReto(a + " + " + b + " × " + c, a + (b * c));
            }
            case 1: {
                int divisor = enteroAleatorioEnRango(minimo, maximo / 2);
                int cociente = enteroAleatorioEnRango(minimo, maximo / divisor);
                int dividendo = divisor * cociente;
                int c = enteroAleatorioEnRango(minimo, maximo);
                int d = enteroAleatorioEnRango(minimo, maximo);
                return crearReto(dividendo + " ÷ " + divisor + " + " + c + " × " + d,
                        cociente + (c * d));
            }
            case 2: {
                int divisor = enteroAleatorioEnRango(minimo, maximo / 2);
                int cociente = enteroAleatorioEnRango(minimo, maximo / divisor);
                int dividendo = divisor * cociente;
                int b = enteroAleatorioEnRango(minimo, maximo);
                int minimoA = Math.max(minimo, (cociente + b - 1) / b);
                int a = enteroAleatorioEnRango(minimoA, maximo);
                return crearReto(a + " × " + b + " - " + dividendo + " ÷ " + divisor,
                        (a * b) - cociente);
            }
            case 3: {
                int a = enteroAleatorioEnRango(minimo, maximo);
                int b = enteroAleatorioEnRango(minimo, maximo);
                int c = enteroAleatorioEnRango(minimo, maximo);
                int d = enteroAleatorioEnRango(minimo, maximo);
                return crearReto(a + " × " + b + " + " + c + " × " + d, (a * b) + (c * d));
            }
            case 4: {
                int a = enteroAleatorioEnRango(minimo, maximo);
                int b = enteroAleatorioEnRango(minimo, maximo);
                int c = enteroAleatorioEnRango(minimo, maximo);
                int parcial = (a + b) * c;
                int d = enteroAleatorioEnRango(minimo, Math.min(maximo, parcial));
                return crearReto("(" + a + " + " + b + ") × " + c + " - " + d, parcial - d);
            }
            default: {
                int a = enteroAleatorioEnRango(minimo, maximo);
                int b = enteroAleatorioEnRango(minimo, maximo);
                int c = enteroAleatorioEnRango(minimo + 1, maximo);
                int d = enteroAleatorioEnRango(minimo, c - 1);
                return crearReto("(" + a + " + " + b + ") × (" + c + " - " + d + ")",
                        (a + b) * (c - d));
            }
        }
    }
}
