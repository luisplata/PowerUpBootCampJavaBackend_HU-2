package com.peryloth.model.estados;

public final class EstadosConstantes {

    private EstadosConstantes() {
        // Evita la instanciación
    }

    public static final Estados PENDIENTE_REVISION = new Estados(
            1L,
            "Pendiente de revisión"
    );

    public static final Estados APROBADO = new Estados(
            2L,
            "Aprobado"
    );

    public static final Estados RECHAZADO = new Estados(
            4L,
            "Rechazado"
    );
}
