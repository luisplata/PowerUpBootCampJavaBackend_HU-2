package com.peryloth.usecase.endeudamiento.constants;

import com.peryloth.usecase.endeudamiento.CalcularCapacidadResponse;

public final class ResponsesConstants {

    private ResponsesConstants() {
        // Evitar instanciación
    }

    public static final CalcularCapacidadResponse APROBADO =
            new CalcularCapacidadResponse("APROBADO", 10000.0, 500.0);

    public static final CalcularCapacidadResponse RECHAZADO =
            new CalcularCapacidadResponse("RECHAZADO", 0.0, 0.0);

    public static final CalcularCapacidadResponse INSUFICIENTE =
            new CalcularCapacidadResponse("INSUFICIENTE", 2000.0, 1000.0);


}
