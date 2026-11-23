package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.junit.jupiter.api.Test;

class GeneradoresNivelTest {

    @Test
    void fabricaRetornaSubclasePolimorficaCorrectaYValidaNulos() {
        assertInstanceOf(NivelBasico.class, FabricaGeneradores.crear(NivelDificultad.BASICO));
        assertInstanceOf(NivelIntermedio.class, FabricaGeneradores.crear(NivelDificultad.INTERMEDIO));
        assertInstanceOf(NivelAvanzado.class, FabricaGeneradores.crear(NivelDificultad.AVANZADO));

        assertThrows(IllegalArgumentException.class, () -> FabricaGeneradores.crear(null));
        assertThrows(IllegalArgumentException.class,
                () -> FabricaGeneradores.crear(NivelDificultad.BASICO, null));
    }

    @Test
    void nivelBasicoGeneraDoscientosRetosMatematicamenteCorrectosEnRango() {
        GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.BASICO, new Random(101L));
        for (int i = 0; i < 200; i++) {
            RetoMatematico reto = generador.generarReto();
            validarRetoComun(reto, NivelDificultad.BASICO);
            for (int numero : extraerEnteros(reto.obtenerExpresion())) {
                assertTrue(numero >= 1 && numero <= 50,
                        "Operando fuera de rango básico [1..50] en: " + reto.obtenerExpresion());
            }
        }
    }

    @Test
    void nivelIntermedioGeneraDoscientosRetosExactosYCorrectos() {
        GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.INTERMEDIO, new Random(202L));
        for (int i = 0; i < 200; i++) {
            RetoMatematico reto = generador.generarReto();
            validarRetoComun(reto, NivelDificultad.INTERMEDIO);
            if (!reto.obtenerExpresion().contains("÷")) {
                for (int numero : extraerEnteros(reto.obtenerExpresion())) {
                    assertTrue(numero >= 2 && numero <= 20,
                            "Operando fuera de rango intermedio [2..20] en: " + reto.obtenerExpresion());
                }
            } else {
                List<Integer> nums = extraerEnteros(reto.obtenerExpresion());
                assertEquals(2, nums.size());
                int dividendo = nums.get(0);
                int divisor = nums.get(1);
                assertEquals(0, dividendo % divisor);
                assertTrue(divisor >= 2 && divisor <= 20);
                assertTrue(dividendo / divisor >= 2 && dividendo / divisor <= 20);
            }
        }
    }

    @Test
    void nivelAvanzadoGeneraDoscientosRetosCorrectosEnRangoYNoNegativos() {
        GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.AVANZADO, new Random(303L));
        for (int i = 0; i < 200; i++) {
            RetoMatematico reto = generador.generarReto();
            validarRetoComun(reto, NivelDificultad.AVANZADO);
            assertTrue(reto.obtenerRespuestaCorrecta() >= 0,
                    "El resultado avanzado no debe ser negativo: " + reto.obtenerExpresion());
            for (int numero : extraerEnteros(reto.obtenerExpresion())) {
                assertTrue(numero >= 2 && numero <= 30,
                        "Número fuera de rango avanzado [2..30] en: " + reto.obtenerExpresion());
            }
        }
    }

    @Test
    void nivelAvanzadoMezclaExpresionesSinParentesisQueExigenPrecedenciaYConParentesisNecesarios() {
        GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.AVANZADO, new Random(404L));
        int sinParentesis = 0;
        int conParentesis = 0;

        for (int i = 0; i < 300; i++) {
            RetoMatematico reto = generador.generarReto();
            String expresion = reto.obtenerExpresion();

            if (expresion.contains("(")) {
                conParentesis++;
                assertTrue(expresion.startsWith("("),
                        "El paréntesis debe agrupar una suma o resta inicial: " + expresion);
                assertTrue(expresion.contains("+"),
                        "Los paréntesis deben alterar la precedencia (agrupar una suma): " + expresion);
            } else {
                sinParentesis++;
                boolean tieneAditivo = expresion.contains("+") || expresion.contains("-");
                boolean tieneMultiplicativo = expresion.contains("×") || expresion.contains("÷");
                assertTrue(tieneAditivo && tieneMultiplicativo,
                        "Sin paréntesis debe mezclar ambos niveles de precedencia: " + expresion);
                assertNotEquals(
                        evaluarIzquierdaADerecha(expresion),
                        (double) reto.obtenerRespuestaCorrecta(),
                        1e-9,
                        "Ignorar la precedencia daría el mismo resultado en: " + expresion);
            }
        }

        assertTrue(sinParentesis > 0, "Debe generar expresiones sin paréntesis.");
        assertTrue(conParentesis > 0, "Debe generar expresiones con paréntesis necesarios.");
    }

    @Test
    void construirOpcionesProduceCuatroValoresUnicosNoNegativosParaRespuestasPequenas() {
        for (long semilla = 1; semilla <= 25; semilla++) {
            GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.BASICO, new Random(semilla));
            for (int respuesta = 0; respuesta <= 12; respuesta++) {
                List<Integer> opciones = generador.construirOpciones(respuesta);

                assertEquals(RetoMatematico.CANTIDAD_OPCIONES, opciones.size());
                assertEquals(RetoMatematico.CANTIDAD_OPCIONES, new HashSet<>(opciones).size(),
                        "Opciones repetidas para respuesta " + respuesta + ": " + opciones);
                assertTrue(opciones.contains(respuesta));
                for (int opcion : opciones) {
                    assertTrue(opcion >= 0,
                            "Opción negativa " + opcion + " para respuesta " + respuesta);
                }
                assertThrows(UnsupportedOperationException.class, () -> opciones.add(99));
            }
        }
    }

    @Test
    void mismaSemillaGeneraLaMismaSecuenciaDeRetos() {
        for (NivelDificultad nivel : NivelDificultad.values()) {
            GeneradorNivel primero = FabricaGeneradores.crear(nivel, new Random(7L));
            GeneradorNivel segundo = FabricaGeneradores.crear(nivel, new Random(7L));
            for (int i = 0; i < 50; i++) {
                assertEquals(primero.generarReto(), segundo.generarReto());
            }
        }
    }

    @Test
    void construirOpcionesUsaValoresDeRespaldoSiLosDesplazamientosAleatoriosSeRepiten() {
        // Random degenerado: siempre elige el mismo desplazamiento (-10), de modo que
        // el bucle principal nunca reúne cuatro opciones y debe actuar el respaldo.
        Random siempreCero = new Random() {
            @Override
            public int nextInt(int limite) {
                return 0;
            }
        };
        GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.BASICO, siempreCero);

        for (int respuesta : new int[] {0, 3, 50}) {
            List<Integer> opciones = generador.construirOpciones(respuesta);
            assertEquals(4, new HashSet<>(opciones).size(), "Opciones: " + opciones);
            assertTrue(opciones.contains(respuesta));
            assertTrue(opciones.stream().allMatch(opcion -> opcion >= 0), "Opciones: " + opciones);
        }
    }

    @Test
    void construirOpcionesAdmiteRespuestasNegativasSinRepetirValores() {
        GeneradorNivel generador = FabricaGeneradores.crear(NivelDificultad.BASICO, new Random(5L));
        for (int respuesta = -20; respuesta < 0; respuesta++) {
            List<Integer> opciones = generador.construirOpciones(respuesta);
            assertEquals(4, new HashSet<>(opciones).size());
            assertTrue(opciones.contains(respuesta));
        }
    }

    @Test
    void constructoresPorDefectoCreanGeneradoresFuncionales() {
        List<GeneradorNivel> generadores = List.of(new NivelBasico(), new NivelIntermedio(), new NivelAvanzado());
        for (GeneradorNivel generador : generadores) {
            assertNotNull(generador.obtenerGeneradorAleatorio());
            validarRetoComun(generador.generarReto(), generador.obtenerNivel());
        }
        assertEquals(NivelDificultad.BASICO, generadores.get(0).obtenerNivel());
        assertEquals(NivelDificultad.INTERMEDIO, generadores.get(1).obtenerNivel());
        assertEquals(NivelDificultad.AVANZADO, generadores.get(2).obtenerNivel());

        assertThrows(IllegalArgumentException.class, () -> new GeneradorNivel(null) {
            @Override
            public RetoMatematico generarReto() {
                return null;
            }
        });
    }

    @Test
    void generadoresValidanParametrosInvalidos() {
        assertThrows(IllegalArgumentException.class, () -> new NivelBasico(null));
        assertThrows(IllegalArgumentException.class, () -> new NivelIntermedio(null));
        assertThrows(IllegalArgumentException.class, () -> new NivelAvanzado(null));

        GeneradorNivel generador = new NivelBasico(new Random(1L));
        assertEquals(NivelDificultad.BASICO, generador.obtenerNivel());
        assertThrows(IllegalArgumentException.class, () -> generador.enteroAleatorioEnRango(10, 5));
        assertEquals(7, generador.enteroAleatorioEnRango(7, 7));
    }

    private static void validarRetoComun(RetoMatematico reto, NivelDificultad nivelEsperado) {
        assertEquals(nivelEsperado, reto.obtenerNivel());
        assertEquals(RetoMatematico.CANTIDAD_OPCIONES, reto.obtenerOpciones().size());
        assertEquals(RetoMatematico.CANTIDAD_OPCIONES, new HashSet<>(reto.obtenerOpciones()).size());
        assertTrue(reto.obtenerOpciones().contains(reto.obtenerRespuestaCorrecta()));

        int evaluado = EvaluadorAritmetico.evaluar(reto.obtenerExpresion());
        assertEquals(evaluado, reto.obtenerRespuestaCorrecta(),
                "Evaluación incorrecta para expresión: " + reto.obtenerExpresion());
    }

    /**
     * Evalúa una expresión sin paréntesis operando estrictamente de izquierda a
     * derecha, es decir, ignorando la precedencia. Se usa aritmética en
     * {@code double} porque ese orden erróneo puede producir divisiones inexactas.
     */
    private static double evaluarIzquierdaADerecha(String expresion) {
        String[] tokens = expresion.trim().split("\\s+");
        double acumulado = Double.parseDouble(tokens[0]);
        for (int i = 1; i < tokens.length; i += 2) {
            double operando = Double.parseDouble(tokens[i + 1]);
            switch (tokens[i]) {
                case "+" -> acumulado += operando;
                case "-" -> acumulado -= operando;
                case "×" -> acumulado *= operando;
                case "÷" -> acumulado /= operando;
                default -> throw new IllegalStateException("Operador desconocido: " + tokens[i]);
            }
        }
        return acumulado;
    }

    private static List<Integer> extraerEnteros(String expresion) {
        List<Integer> numeros = new ArrayList<>();
        Matcher matcher = Pattern.compile("\\d+").matcher(expresion);
        while (matcher.find()) {
            numeros.add(Integer.parseInt(matcher.group()));
        }
        return numeros;
    }

    private static final class EvaluadorAritmetico {
        private final String texto;
        private int posicion;

        private EvaluadorAritmetico(String texto) {
            this.texto = texto;
            this.posicion = 0;
        }

        private static int evaluar(String expresion) {
            EvaluadorAritmetico parser = new EvaluadorAritmetico(expresion);
            int resultado = parser.leerExpresion();
            parser.saltarEspacios();
            if (parser.posicion != parser.texto.length()) {
                throw new IllegalStateException("Expresión no consumida por completo: " + expresion);
            }
            return resultado;
        }

        private int leerExpresion() {
            int valor = leerTermino();
            while (true) {
                saltarEspacios();
                if (coincide('+')) {
                    valor += leerTermino();
                } else if (coincide('-')) {
                    valor -= leerTermino();
                } else {
                    return valor;
                }
            }
        }

        private int leerTermino() {
            int valor = leerFactor();
            while (true) {
                saltarEspacios();
                if (coincide('×') || coincide('*')) {
                    valor *= leerFactor();
                } else if (coincide('÷') || coincide('/')) {
                    int divisor = leerFactor();
                    assertEquals(0, valor % divisor, "División no exacta en: " + texto);
                    valor /= divisor;
                } else {
                    return valor;
                }
            }
        }

        private int leerFactor() {
            saltarEspacios();
            if (coincide('(')) {
                int valor = leerExpresion();
                saltarEspacios();
                if (!coincide(')')) {
                    throw new IllegalStateException("Falta paréntesis de cierre en: " + texto);
                }
                return valor;
            }
            int inicio = posicion;
            while (posicion < texto.length() && Character.isDigit(texto.charAt(posicion))) {
                posicion++;
            }
            return Integer.parseInt(texto.substring(inicio, posicion));
        }

        private void saltarEspacios() {
            while (posicion < texto.length() && Character.isWhitespace(texto.charAt(posicion))) {
                posicion++;
            }
        }

        private boolean coincide(char esperado) {
            if (posicion < texto.length() && texto.charAt(posicion) == esperado) {
                posicion++;
                return true;
            }
            return false;
        }
    }
}
