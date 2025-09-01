package com.peryloth.usecase.registerloanrequest.command_queue.validations;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public class MontoValidation implements SolicitudValidation{
    @Override
    public Mono<Void> validate(Solicitud solicitud) {
        if(solicitud.getMonto() <= 0){
            return Mono.error(new RuntimeException("Monto debe ser mayor a 0"));
        }
        return Mono.empty();
    }
}
