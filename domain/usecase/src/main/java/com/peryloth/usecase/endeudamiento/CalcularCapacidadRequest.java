package com.peryloth.usecase.endeudamiento;

import java.util.List;

public record CalcularCapacidadRequest(
        double ingresosTotales,
        List<PrestamoActivo> prestamosActivos,
        NuevoPrestamo nuevoPrestamo
) {
    public record PrestamoActivo(double monto, double tasaMensual, int plazoMeses) {
    }

    public record NuevoPrestamo(double monto, double tasaMensual, int plazoMeses) {
    }
}
