package com.peryloth.usecase.registerloanrequest.command_queue.validations;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public class PlazoValidation implements SolicitudValidation {
    @Override
    public Mono<Void> validate(Solicitud solicitud) {
        if (solicitud.getPlazo() <= 0) {
            return Mono.error(new RuntimeException("Plazo debe ser mayor a 0"));
        }
        return Mono.empty();
    }
}
