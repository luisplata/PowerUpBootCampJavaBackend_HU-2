package com.peryloth.usecase.endeudamiento;

import java.util.List;

public record CalcularCapacidadResponse(
        double capacidadMaxima,
        double deudaMensualActual,
        double capacidadDisponible,
        double cuotaNuevoPrestamo,
        String decision,
        List<PlanPago> planPagos
) {
    public record PlanPago(
            int mes,
            String cuota,
            String interes,
            String abonoCapital,
            String saldoRestante
    ) {}
}
