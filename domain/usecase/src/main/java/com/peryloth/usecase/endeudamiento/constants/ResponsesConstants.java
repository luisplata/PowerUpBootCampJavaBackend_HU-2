package com.peryloth.usecase.endeudamiento.constants;

import com.peryloth.usecase.endeudamiento.CalcularCapacidadResponse;
import java.util.List;

public final class ResponsesConstants {

    private ResponsesConstants() {
        // Evitar instanciación
    }

    public static final CalcularCapacidadResponse APROBADO =
            new CalcularCapacidadResponse(
                    0.35,          // capacidadMaxima
                    0,             // deudaMensualActual
                    0.35,          // capacidadDisponible
                    425005.49,     // cuotaNuevoPrestamo
                    "APROBADO",    // decision
                    List.of()      // planPagos vacío
            );

    public static final CalcularCapacidadResponse RECHAZADO =
            new CalcularCapacidadResponse(
                    0.35,
                    0,
                    0.35,
                    425005.49,
                    "RECHAZADO",
                    List.of()
            );

    public static final CalcularCapacidadResponse INSUFICIENTE =
            new CalcularCapacidadResponse(
                    0.20,
                    5000,
                    0.15,
                    300000,
                    "INSUFICIENTE",
                    List.of()
            );
}
