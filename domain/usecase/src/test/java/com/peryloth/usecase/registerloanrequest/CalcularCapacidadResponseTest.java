package com.peryloth.usecase.registerloanrequest;

import com.peryloth.usecase.endeudamiento.CalcularCapacidadResponse;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class CalcularCapacidadResponseTest {

    @Test
    void shouldCreateResponseCorrectly() {
        // arrange
        List<CalcularCapacidadResponse.PlanPago> planPagos = List.of(
                new CalcularCapacidadResponse.PlanPago(1, "500", "50", "450", "4550"),
                new CalcularCapacidadResponse.PlanPago(2, "500", "45", "455", "4095")
        );

        // act
        CalcularCapacidadResponse response = new CalcularCapacidadResponse(
                10000.0,   // capacidadMaxima
                2000.0,    // deudaMensualActual
                8000.0,    // capacidadDisponible
                500.0,     // cuotaNuevoPrestamo
                "APROBADO",// decision
                planPagos
        );

        // assert
        assertThat(response.capacidadMaxima()).isEqualTo(10000.0);
        assertThat(response.deudaMensualActual()).isEqualTo(2000.0);
        assertThat(response.capacidadDisponible()).isEqualTo(8000.0);
        assertThat(response.cuotaNuevoPrestamo()).isEqualTo(500.0);
        assertThat(response.decision()).isEqualTo("APROBADO");
        assertThat(response.planPagos()).hasSize(2);

        CalcularCapacidadResponse.PlanPago primerPago = response.planPagos().get(0);
        assertThat(primerPago.mes()).isEqualTo(1);
        assertThat(primerPago.cuota()).isEqualTo("500");
        assertThat(primerPago.interes()).isEqualTo("50");
        assertThat(primerPago.abonoCapital()).isEqualTo("450");
        assertThat(primerPago.saldoRestante()).isEqualTo("4550");
    }
}
