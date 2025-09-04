package com.peryloth.usecase.getallsolicitud;

import com.peryloth.model.solicitud.Solicitud;

public record SolicitudResponseDTO(Solicitud solicitud, String nombre, String email, Long salario_base) {
}
