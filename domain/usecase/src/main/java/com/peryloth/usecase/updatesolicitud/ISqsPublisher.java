package com.peryloth.usecase.updatesolicitud;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface ISqsPublisher {
    Mono<Void> publishEstadoSolicitud(Solicitud solicitud);
}
