package com.peryloth.usecase.endeudamiento;

public record CalcularCapacidadResponse(
        String decision,
        double capacidadDisponible,
        double cuotaPrestamoNuevo
) {
}
