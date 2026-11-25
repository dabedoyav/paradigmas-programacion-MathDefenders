package com.mathdefenders.modelo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Pruebas de la clase {@link Torre}.
 *
 * <p>Acá revisamos que el sistema de vidas funcione como dicen las reglas del
 * juego: la torre arranca con 3 vidas, cada error le quita una, nunca queda en
 * negativo y cuando llega a cero se acaba la partida.</p>
 *
 * @author Diego Buitrago González
 * @version 1.1
 */
@DisplayName("Torre: sistema de vidas")
class TorreTest {

    /**
     * Torre normal (3 vidas) que usamos en las pruebas. Se crea de nuevo antes
     * de cada prueba para que una no afecte a la otra.
     */
    private Torre torre;

    /**
     * Antes de cada prueba arrancamos con una torre nueva y con todas sus vidas.
     */
    @BeforeEach
    void prepararTorre() {
        torre = new Torre();
    }

    /**
     * Revisa que la torre arranque con 3 vidas, al 100% y activa.
     */
    @Test
    @DisplayName("Inicia con 3 vidas, integridad 100% y activa")
    void iniciaConVidasCompletas() {
        assertEquals(3, Torre.VIDAS_INICIALES);
        assertEquals(Torre.VIDAS_INICIALES, torre.obtenerVidasActuales());
        assertEquals(Torre.VIDAS_INICIALES, torre.obtenerVidasMaximas());
        assertEquals(1.0, torre.obtenerPorcentajeIntegridad(), 0.0001);
        assertTrue(torre.estaActiva());
        assertFalse(torre.estaDestruida());
    }

    /**
     * Revisa que cada golpe le quite exactamente una vida y que el método
     * devuelva cuántas quedan.
     */
    @Test
    @DisplayName("Cada daño resta exactamente una vida")
    void recibirDanoRestaUnaVida() {
        assertEquals(2, torre.recibirDano());
        assertEquals(2, torre.obtenerVidasActuales());
        assertEquals(1, torre.recibirDano());
        assertEquals(1, torre.obtenerVidasActuales());
    }

    /**
     * Revisa que con una sola vida la torre siga activa, porque el jugador
     * gana el nivel si termina con al menos una vida.
     */
    @Test
    @DisplayName("Con una sola vida sigue activa")
    void conUnaVidaSigueActiva() {
        torre.recibirDano();
        torre.recibirDano();

        assertEquals(1, torre.obtenerVidasActuales());
        assertTrue(torre.estaActiva());
        assertFalse(torre.estaDestruida());
    }

    /**
     * Revisa el Game Over: después de 3 errores la torre queda destruida.
     */
    @Test
    @DisplayName("Tras 3 daños queda destruida (Game Over)")
    void tresDanosDestruyenLaTorre() {
        torre.recibirDano();
        torre.recibirDano();
        assertEquals(0, torre.recibirDano());

        assertTrue(torre.estaDestruida());
        assertFalse(torre.estaActiva());
        assertEquals(0.0, torre.obtenerPorcentajeIntegridad(), 0.0001);
    }

    /**
     * Revisa que, aunque le sigamos pegando a una torre ya destruida, las
     * vidas se queden en cero y no pasen a negativo.
     */
    @Test
    @DisplayName("Las vidas nunca bajan de cero")
    void vidasNoSonNegativas() {
        // Le pegamos 10 veces, muchas más de las vidas que tiene
        for (int i = 0; i < 10; i++) {
            torre.recibirDano();
        }

        assertEquals(0, torre.obtenerVidasActuales());
        assertEquals(0, torre.recibirDano());
        assertTrue(torre.estaDestruida());
    }

    /**
     * Revisa que el porcentaje de la barra de vida sea vidas actuales entre
     * vidas máximas, tanto en una torre normal como en una de 5 vidas.
     */
    @Test
    @DisplayName("El porcentaje de integridad es proporcional a las vidas")
    void porcentajeIntegridadProporcional() {
        torre.recibirDano();
        assertEquals(2.0 / 3.0, torre.obtenerPorcentajeIntegridad(), 0.0001);
        torre.recibirDano();
        assertEquals(1.0 / 3.0, torre.obtenerPorcentajeIntegridad(), 0.0001);

        // Con 5 vidas, después de un golpe quedan 4 de 5, o sea 0.8
        Torre torreDeCinco = new Torre(5);
        torreDeCinco.recibirDano();
        assertEquals(0.8, torreDeCinco.obtenerPorcentajeIntegridad(), 0.0001);
    }

    /**
     * Revisa que reiniciar devuelva todas las vidas, incluso si la torre ya
     * estaba destruida, para poder jugar otra vez.
     */
    @Test
    @DisplayName("Reiniciar restaura las vidas máximas")
    void reiniciarRestauraVidas() {
        torre.recibirDano();
        torre.recibirDano();
        torre.recibirDano();
        assertTrue(torre.estaDestruida());

        torre.reiniciar();

        assertEquals(Torre.VIDAS_INICIALES, torre.obtenerVidasActuales());
        assertEquals(1.0, torre.obtenerPorcentajeIntegridad(), 0.0001);
        assertTrue(torre.estaActiva());
    }

    /**
     * Revisa que se pueda crear una torre con otra cantidad de vidas y que al
     * reiniciarla vuelva a ese máximo y no a 3.
     */
    @Test
    @DisplayName("Acepta una cantidad personalizada de vidas")
    void vidasPersonalizadas() {
        Torre torreDeCinco = new Torre(5);
        assertEquals(5, torreDeCinco.obtenerVidasMaximas());
        assertEquals(5, torreDeCinco.obtenerVidasActuales());

        torreDeCinco.recibirDano();
        torreDeCinco.reiniciar();
        assertEquals(5, torreDeCinco.obtenerVidasActuales());

        // Una torre de una sola vida se cae con el primer error
        Torre torreDeUna = new Torre(1);
        assertEquals(0, torreDeUna.recibirDano());
        assertTrue(torreDeUna.estaDestruida());
    }

    /**
     * Revisa que no se pueda crear una torre con cero vidas o con vidas negativas.
     */
    @Test
    @DisplayName("Rechaza vidas máximas en cero o negativas")
    void rechazaVidasInvalidas() {
        assertThrows(IllegalArgumentException.class, () -> new Torre(0));
        assertThrows(IllegalArgumentException.class, () -> new Torre(-1));
        assertThrows(IllegalArgumentException.class, () -> new Torre(Integer.MIN_VALUE));
    }

    /**
     * Revisa que el texto de la torre muestre bien las vidas antes y después
     * de un golpe.
     */
    @Test
    @DisplayName("toString muestra vidas actuales sobre máximas")
    void representacionTextual() {
        assertEquals("Torre{vidas=3/3}", torre.toString());
        torre.recibirDano();
        assertEquals("Torre{vidas=2/3}", torre.toString());
    }
}
