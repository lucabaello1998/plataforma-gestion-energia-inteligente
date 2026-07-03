package ar.edu.unahur.obj2.energia;

import ar.edu.unahur.obj2.energia.excepciones.CantidadInvalidaException;
import ar.edu.unahur.obj2.energia.excepciones.LimiteDeReservaException;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MainTest {

    private static final double DELTA = 0.001;

    @Test
    void bateria_arranca_con_el_nivel_de_energia_indicado() {
        Bateria bateria = new Bateria("B1", 1000.0);
        assertEquals("B1", bateria.getId());
        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void cargar_incrementa_el_nivel_de_energia() {
        Bateria bateria = new Bateria("B1", 1000.0);
        bateria.cargar(500.0);
        assertEquals(1500.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void consumir_reduce_el_nivel_de_energia() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        bateria.consumir(300.0);
        assertEquals(700.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void consumir_puede_usar_la_reserva_hasta_el_limite() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        bateria.consumir(6000.0); // 1000 - 6000 = -5000 = límite exacto, permitido
        assertEquals(-5000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void consumir_mas_alla_del_limite_de_reserva_lanza_excepcion_checked() {
        Bateria bateria = new Bateria("B1", 1000.0);
        assertThrows(LimiteDeReservaException.class, () -> bateria.consumir(6000.01));
    }

    @Test
    void consumo_que_excede_reserva_no_modifica_el_nivel_de_energia() {
        Bateria bateria = new Bateria("B1", 1000.0);
        assertThrows(LimiteDeReservaException.class, () -> bateria.consumir(10000.0));
        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void excepcion_de_limite_de_reserva_informa_bateria_y_cantidad() {
        Bateria bateria = new Bateria("B7", 0.0);
        LimiteDeReservaException ex = assertThrows(LimiteDeReservaException.class,
                () -> bateria.consumir(10000.0));
        assertEquals("B7", ex.getBateriaId());
        assertEquals(10000.0, ex.getCantidadSolicitada(), DELTA);
    }

    // ── CargaOperacion / ConsumoOperacion ────────────────────────────────────

    @Test
    void carga_con_cantidad_cero_o_negativa_lanza_excepcion_unchecked() {
        Bateria bateria = new Bateria("B1", 1000.0);
        assertThrows(CantidadInvalidaException.class, () -> new CargaOperacion(bateria, 0.0));
        assertThrows(CantidadInvalidaException.class, () -> new CargaOperacion(bateria, -50.0));
    }

    @Test
    void consumo_con_cantidad_cero_o_negativa_lanza_excepcion_unchecked() {
        Bateria bateria = new Bateria("B1", 1000.0);
        assertThrows(CantidadInvalidaException.class, () -> new ConsumoOperacion(bateria, 0.0));
        assertThrows(CantidadInvalidaException.class, () -> new ConsumoOperacion(bateria, -1.0));
    }

    @Test
    void ejecutar_carga_aplica_el_incremento_sobre_la_bateria() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        OperacionDeEnergia carga = new CargaOperacion(bateria, 200.0);
        carga.ejecutar();
        assertEquals(1200.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void deshacer_carga_revierte_exactamente_lo_cargado() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        OperacionDeEnergia carga = new CargaOperacion(bateria, 200.0);
        carga.ejecutar();
        carga.deshacer();
        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void ejecutar_consumo_aplica_la_reduccion_sobre_la_bateria() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        OperacionDeEnergia consumo = new ConsumoOperacion(bateria, 300.0);
        consumo.ejecutar();
        assertEquals(700.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void deshacer_consumo_revierte_exactamente_lo_consumido() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        OperacionDeEnergia consumo = new ConsumoOperacion(bateria, 300.0);
        consumo.ejecutar();
        consumo.deshacer();
        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void ejecutar_consumo_que_excede_reserva_propaga_excepcion_checked() {
        Bateria bateria = new Bateria("B1", 1000.0);
        OperacionDeEnergia consumo = new ConsumoOperacion(bateria, 10000.0);
        assertThrows(LimiteDeReservaException.class, consumo::ejecutar);
    }

    // ── Rutina ────────────────────────────────────────────────────────────────

    @Test
    void rutina_ejecuta_todas_las_operaciones_en_orden() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        Rutina rutina = new Rutina();
        rutina.agregarOperacion(new CargaOperacion(bateria, 500.0));
        rutina.agregarOperacion(new ConsumoOperacion(bateria, 200.0));

        rutina.ejecutar();

        assertEquals(1300.0, bateria.getNivelEnergia(), DELTA); // 1000 + 500 - 200
    }

    @Test
    void rutina_vacia_las_pendientes_luego_de_ejecutar_con_exito() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        Rutina rutina = new Rutina();
        rutina.agregarOperacion(new CargaOperacion(bateria, 500.0));

        rutina.ejecutar();

        assertTrue(rutina.getPendientes().isEmpty());
    }

    @Test
    void rutina_puede_tratarse_como_una_operacion_individual() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        Rutina rutina = new Rutina();
        rutina.agregarOperacion(new CargaOperacion(bateria, 500.0));

        OperacionDeEnergia operacion = rutina; // tratamiento uniforme
        operacion.ejecutar();

        assertEquals(1500.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void rutina_que_falla_revierte_las_operaciones_ya_ejecutadas_y_deja_el_estado_original() {
        Bateria bateria = new Bateria("B1", 1000.0);
        Rutina rutina = new Rutina();
        rutina.agregarOperacion(new CargaOperacion(bateria, 500.0));   // se ejecuta OK: 1500
        rutina.agregarOperacion(new ConsumoOperacion(bateria, 100.0)); // se ejecuta OK: 1400
        rutina.agregarOperacion(new ConsumoOperacion(bateria, 100000.0)); // falla: supera reserva

        assertThrows(LimiteDeReservaException.class, rutina::ejecutar);

        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void rutina_que_falla_tambien_queda_sin_pendientes() {
        Bateria bateria = new Bateria("B1", 1000.0);
        Rutina rutina = new Rutina();
        rutina.agregarOperacion(new CargaOperacion(bateria, 500.0));
        rutina.agregarOperacion(new ConsumoOperacion(bateria, 100000.0));

        assertThrows(LimiteDeReservaException.class, rutina::ejecutar);

        assertTrue(rutina.getPendientes().isEmpty());
    }

    @Test
    void rutina_anidada_se_revierte_completa_si_falla_una_operacion_posterior() {
        Bateria bateria = new Bateria("B1", 1000.0);

        Rutina rutinaAnidada = new Rutina();
        rutinaAnidada.agregarOperacion(new CargaOperacion(bateria, 500.0));
        rutinaAnidada.agregarOperacion(new ConsumoOperacion(bateria, 200.0));

        Rutina rutinaPrincipal = new Rutina();
        rutinaPrincipal.agregarOperacion(rutinaAnidada); // 1000 -> 1300, todo OK dentro de la anidada
        rutinaPrincipal.agregarOperacion(new ConsumoOperacion(bateria, 100000.0)); // falla

        assertThrows(LimiteDeReservaException.class, rutinaPrincipal::ejecutar);

        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }

    @Test
    void deshacer_manual_de_una_rutina_ya_ejecutada_revierte_todo_su_lote() throws LimiteDeReservaException {
        Bateria bateria = new Bateria("B1", 1000.0);
        Rutina rutina = new Rutina();
        rutina.agregarOperacion(new CargaOperacion(bateria, 500.0));
        rutina.agregarOperacion(new ConsumoOperacion(bateria, 200.0));

        rutina.ejecutar();
        assertEquals(1300.0, bateria.getNivelEnergia(), DELTA);

        rutina.deshacer();
        assertEquals(1000.0, bateria.getNivelEnergia(), DELTA);
    }
}
