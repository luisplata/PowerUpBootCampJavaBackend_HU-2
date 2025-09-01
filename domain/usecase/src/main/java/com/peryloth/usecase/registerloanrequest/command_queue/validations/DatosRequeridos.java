package com.peryloth.usecase.registerloanrequest.command_queue.validations;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public class DatosRequeridos implements SolicitudValidation {
    @Override
    public Mono<Void> validate(Solicitud solicitud) {
        if (solicitud.getMonto() == null) {
            return Mono.error(new IllegalArgumentException("Falta monto"));
        }
        if (solicitud.getPlazoMeses() == null) {
            return Mono.error(new IllegalArgumentException("Falta plazo en meses"));
        }
        return Mono.empty();
    }
}
