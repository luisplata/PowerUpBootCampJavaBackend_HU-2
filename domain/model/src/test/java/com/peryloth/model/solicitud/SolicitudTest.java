package com.peryloth.model.solicitud;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class SolicitudTest {

    @Test
    void shouldCreateSolicitudWithAllArgsConstructor() {
        TipoPrestamo tipo = new TipoPrestamo(1L, "Personal",
                BigDecimal.valueOf(1000), BigDecimal.valueOf(5000),
                BigDecimal.valueOf(0.05), true);
        Estados estado = new Estados(1L, "Pendiente");

        Solicitud solicitud = new Solicitud(1L, "12345", tipo, estado,
                3000.0, 12, "user@mail.com");

        assertThat(solicitud.getIdSolicitud()).isEqualTo(1L);
        assertThat(solicitud.getUsuario_id()).isEqualTo("12345");
        assertThat(solicitud.getMonto()).isEqualTo(3000.0);
        assertThat(solicitud.getPlazo()).isEqualTo(12);
        assertThat(solicitud.getEmail()).isEqualTo("user@mail.com");
        assertThat(solicitud.getTipoPrestamo().getNombre()).isEqualTo("Personal");
        assertThat(solicitud.getEstado().getNombre()).isEqualTo("Pendiente");
    }

    @Test
    void shouldBuildSolicitudWithBuilder() {
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(2L)
                .usuario_id("99999")
                .monto(2000.0)
                .plazo(6)
                .email("builder@mail.com")
                .build();

        assertThat(solicitud.getUsuario_id()).isEqualTo("99999");
        assertThat(solicitud.getPlazo()).isEqualTo(6);
        assertThat(solicitud.getEmail()).isEqualTo("builder@mail.com");
    }

    @Test
    void shouldUseSettersAndGetters() {
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario_id("A100");
        solicitud.setMonto(1500.0);

        assertThat(solicitud.getUsuario_id()).isEqualTo("A100");
        assertThat(solicitud.getMonto()).isEqualTo(1500.0);
    }

    @Test
    void shouldReturnToString() {
        Solicitud solicitud = new Solicitud();
        solicitud.setUsuario_id("TEST");

        assertThat(solicitud.toString()).contains("TEST");
    }
}
