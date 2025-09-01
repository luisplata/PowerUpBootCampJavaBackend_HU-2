package com.peryloth.usecase.registerloanrequest.command_queue.validations;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface SolicitudValidation {
    Mono<Void> validate(Solicitud solicitud);
}

