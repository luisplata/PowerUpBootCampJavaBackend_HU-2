package com.peryloth.usecase.registerloanrequest.command_queue.validations;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public class DatosCliente implements SolicitudValidation{

    private final String identification;
    private final String email;

    public DatosCliente(String identification, String email) {
        this.identification = identification;
        this.email = email;
    }

    @Override
    public Mono<Void> validate(Solicitud solicitud) {
        if(identification.isEmpty() || email.isEmpty()){
            return Mono.error(new RuntimeException("Los datos del cliente no coinciden con los proporcionados"));
        }
        return Mono.empty();
    }
}
