package com.peryloth.model.tipoprestamo;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class TipoPrestamoTest {

    @Test
    void shouldCreateTipoPrestamoWithAllArgsConstructor() {
        TipoPrestamo tipo = new TipoPrestamo(1L, "Hipotecario",
                BigDecimal.valueOf(10000), BigDecimal.valueOf(50000),
                BigDecimal.valueOf(0.08), false);

        assertThat(tipo.getIdTipoPrestamo()).isEqualTo(1L);
        assertThat(tipo.getNombre()).isEqualTo("Hipotecario");
        assertThat(tipo.getMontoMinimo()).isEqualTo(BigDecimal.valueOf(10000));
        assertThat(tipo.getMontoMaximo()).isEqualTo(BigDecimal.valueOf(50000));
        assertThat(tipo.getTasaInteres()).isEqualTo(BigDecimal.valueOf(0.08));
        assertThat(tipo.getValidacionAutomatica()).isFalse();
    }

    @Test
    void shouldBuildTipoPrestamoWithBuilder() {
        TipoPrestamo tipo = TipoPrestamo.builder()
                .idTipoPrestamo(2L)
                .nombre("Consumo")
                .montoMinimo(BigDecimal.valueOf(500))
                .montoMaximo(BigDecimal.valueOf(2000))
                .tasaInteres(BigDecimal.valueOf(0.1))
                .validacionAutomatica(true)
                .build();

        assertThat(tipo.getNombre()).isEqualTo("Consumo");
        assertThat(tipo.getTasaInteres()).isEqualTo(BigDecimal.valueOf(0.1));
        assertThat(tipo.getValidacionAutomatica()).isTrue();
    }

    @Test
    void shouldUseSettersAndGetters() {
        TipoPrestamo tipo = new TipoPrestamo();
        tipo.setNombre("Microcredito");
        tipo.setValidacionAutomatica(true);

        assertThat(tipo.getNombre()).isEqualTo("Microcredito");
        assertThat(tipo.getValidacionAutomatica()).isTrue();
    }

    @Test
    void shouldReturnToString() {
        TipoPrestamo tipo = new TipoPrestamo(3L, "Vehículo",
                BigDecimal.valueOf(5000), BigDecimal.valueOf(15000),
                BigDecimal.valueOf(0.06), true);

        assertThat(tipo.toString()).contains("Vehículo");
    }
}
