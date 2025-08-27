package com.peryloth.model.estados;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class EstadosTest {

    @Test
    void shouldCreateEstadoWithAllArgsConstructor() {
        Estados estado = new Estados(1L, "Pendiente");
        assertThat(estado.getIdEstado()).isEqualTo(1L);
        assertThat(estado.getNombre()).isEqualTo("Pendiente");
    }

    @Test
    void shouldBuildEstadoWithBuilder() {
        Estados estado = Estados.builder()
                .idEstado(2L)
                .nombre("Aprobado")
                .build();

        assertThat(estado.getIdEstado()).isEqualTo(2L);
        assertThat(estado.getNombre()).isEqualTo("Aprobado");
    }

    @Test
    void shouldUseSettersAndGetters() {
        Estados estado = new Estados();
        estado.setNombre("Rechazado");
        assertThat(estado.getNombre()).isEqualTo("Rechazado");
    }

    @Test
    void shouldReturnToString() {
        Estados estado = new Estados(3L, "Finalizado");
        assertThat(estado.toString()).contains("Finalizado");
    }
}
