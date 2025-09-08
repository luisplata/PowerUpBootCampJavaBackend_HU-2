package com.peryloth.usecase.updatesolicitud;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface IUpdateSolicitudUseCase {
    Mono<Solicitud> updateSolicitud(Long solicitudId, String nuevoEstado);
}
