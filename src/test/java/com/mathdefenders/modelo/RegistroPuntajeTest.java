package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class RegistroPuntajeTest {

    private static final LocalDateTime BASE = LocalDateTime.of(2026, 11, 20, 9, 30);

    @Test
    void igualdadYHashCodeIgnoranMayusculasEnElAliasPeroNoElRestoDeCampos() {
        RegistroPuntaje original = new RegistroPuntaje("Ana", NivelDificultad.BASICO, 1000, 90.0, BASE);
        RegistroPuntaje mismoConOtroCaso = new RegistroPuntaje("  ANA ", NivelDificultad.BASICO, 1000, 90.0, BASE);

        assertEquals(original, mismoConOtroCaso);
        assertEquals(original.hashCode(), mismoConOtroCaso.hashCode());
        assertEquals(0, original.compareTo(mismoConOtroCaso));
        assertEquals(original, original);
        assertNotEquals(original, null);
        assertNotEquals(original, "Ana");

        assertNotEquals(original, new RegistroPuntaje("Ana", NivelDificultad.INTERMEDIO, 1000, 90.0, BASE));
        assertNotEquals(original, new RegistroPuntaje("Ana", NivelDificultad.BASICO, 1100, 90.0, BASE));
        assertNotEquals(original, new RegistroPuntaje("Ana", NivelDificultad.BASICO, 1000, 80.0, BASE));
        assertNotEquals(original, new RegistroPuntaje("Ana", NivelDificultad.BASICO, 1000, 90.0, BASE.plusSeconds(1)));
        assertNotEquals(original, new RegistroPuntaje("Beto", NivelDificultad.BASICO, 1000, 90.0, BASE));
    }

    @Test
    void registroEquivalenteNoSeAgregaDosVecesAlGestor() {
        RegistroPuntaje original = new RegistroPuntaje("Ana", NivelDificultad.BASICO, 1000, 90.0, BASE);
        RegistroPuntaje equivalente = new RegistroPuntaje("ana", NivelDificultad.BASICO, 1000, 90.0, BASE);

        GestorPuntajes gestor = new GestorPuntajes();
        assertTrue(gestor.registrar(original));
        assertFalse(gestor.registrar(equivalente));
        assertEquals(1, gestor.obtenerCantidadRegistros());
    }

    @Test
    void ordenNaturalDesempataPorPrecisionFechaAliasYNivel() {
        RegistroPuntaje masPuntos = new RegistroPuntaje("Zeta", NivelDificultad.BASICO, 2000, 50.0, BASE);
        RegistroPuntaje masPrecisos = new RegistroPuntaje("Zeta", NivelDificultad.BASICO, 1000, 95.0, BASE);
        RegistroPuntaje antiguo = new RegistroPuntaje("Zeta", NivelDificultad.BASICO, 1000, 60.0, BASE);
        RegistroPuntaje reciente = new RegistroPuntaje("Zeta", NivelDificultad.BASICO, 1000, 60.0, BASE.plusDays(1));
        RegistroPuntaje aliasAnterior = new RegistroPuntaje("Alfa", NivelDificultad.BASICO, 1000, 60.0, BASE.plusDays(1));
        RegistroPuntaje nivelMayor = new RegistroPuntaje("Alfa", NivelDificultad.AVANZADO, 1000, 60.0, BASE.plusDays(1));

        assertTrue(masPuntos.compareTo(masPrecisos) < 0);
        assertTrue(masPrecisos.compareTo(antiguo) < 0);
        assertTrue(antiguo.compareTo(reciente) < 0);
        assertTrue(aliasAnterior.compareTo(reciente) < 0);
        assertTrue(aliasAnterior.compareTo(nivelMayor) < 0);
        assertTrue(nivelMayor.compareTo(aliasAnterior) > 0);
    }

    @Test
    void comparadoresAlternativosDesempatanConElOrdenNatural() {
        RegistroPuntaje a = new RegistroPuntaje("Ana", NivelDificultad.BASICO, 500, 80.0, BASE);
        RegistroPuntaje b = new RegistroPuntaje("Ana", NivelDificultad.BASICO, 900, 80.0, BASE);
        RegistroPuntaje c = new RegistroPuntaje("beto", NivelDificultad.BASICO, 900, 80.0, BASE);

        GestorPuntajes gestor = new GestorPuntajes();
        gestor.registrar(a);
        gestor.registrar(b);
        gestor.registrar(c);

        // Misma precisión: manda el mayor puntaje y luego el orden natural.
        assertEquals(List.of(b, c, a), gestor.obtenerOrdenadosPor(RegistroPuntaje.POR_PRECISION));
        // Misma fecha: desempata el orden natural.
        assertEquals(List.of(b, c, a), gestor.obtenerOrdenadosPor(RegistroPuntaje.POR_FECHA_RECIENTE));
        // Alias sin distinguir mayúsculas, luego orden natural (mayor puntaje primero).
        assertEquals(List.of(b, a, c), gestor.obtenerOrdenadosPor(RegistroPuntaje.POR_ALIAS));
    }

    @Test
    void constructorValidaTodosLosParametros() {
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje(null, NivelDificultad.BASICO, 10, 50.0, BASE));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("   ", NivelDificultad.BASICO, 10, 50.0, BASE));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Ana", null, 10, 50.0, BASE));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Ana", NivelDificultad.BASICO, -1, 50.0, BASE));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Ana", NivelDificultad.BASICO, 10, -0.1, BASE));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Ana", NivelDificultad.BASICO, 10, 100.1, BASE));
        assertThrows(IllegalArgumentException.class,
                () -> new RegistroPuntaje("Ana", NivelDificultad.BASICO, 10, 50.0, null));

        // Los extremos válidos se aceptan.
        RegistroPuntaje limiteInferior = new RegistroPuntaje("Ana", NivelDificultad.BASICO, 0, 0.0, BASE);
        RegistroPuntaje limiteSuperior = new RegistroPuntaje("Ana", NivelDificultad.BASICO, 0, 100.0, BASE);
        assertEquals(0.0, limiteInferior.obtenerPorcentajePrecision());
        assertEquals(100.0, limiteSuperior.obtenerPorcentajePrecision());
    }

    @Test
    void desdeResumenCopiaDatosYAsignaFechaActual() {
        ResumenPartida resumen = new ResumenPartida(
                "  Yeimmy ", NivelDificultad.INTERMEDIO, EstadoPartida.VICTORIA,
                1500, 9, 1, 10, 6, 2, 90.0, NivelDificultad.AVANZADO);

        LocalDateTime antes = LocalDateTime.now();
        RegistroPuntaje registro = RegistroPuntaje.desdeResumen(resumen);
        LocalDateTime despues = LocalDateTime.now();

        assertEquals("Yeimmy", registro.obtenerAliasJugador());
        assertEquals(NivelDificultad.INTERMEDIO, registro.obtenerNivel());
        assertEquals(1500, registro.obtenerPuntaje());
        assertEquals(90.0, registro.obtenerPorcentajePrecision());
        assertFalse(registro.obtenerFechaRegistro().isBefore(antes));
        assertFalse(registro.obtenerFechaRegistro().isAfter(despues));
    }

    @Test
    void toStringDescribeElRegistro() {
        RegistroPuntaje registro = new RegistroPuntaje("Ana", NivelDificultad.AVANZADO, 2100, 87.5, BASE);

        String texto = registro.toString();
        assertTrue(texto.contains("alias='Ana'"), texto);
        assertTrue(texto.contains("nivel=AVANZADO"), texto);
        assertTrue(texto.contains("puntaje=2100"), texto);
        assertTrue(texto.contains("87.5%") || texto.contains("87,5%"), texto);
    }
}
