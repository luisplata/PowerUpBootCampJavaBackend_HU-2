package com.peryloth.usecase.registerloanrequest.command_queue;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.SolicitudValidation;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

public class UsuarioValidationQueue {
    private final List<SolicitudValidation> validations;

    private UsuarioValidationQueue(List<SolicitudValidation> validations) {
        this.validations = validations;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Mono<Void> validate(Solicitud solicitud) {
        return Flux.fromIterable(validations)
                .concatMap(validation -> validation.validate(solicitud))
                .then();
    }

    // -------------------------
    // Builder interno
    // -------------------------
    public static class Builder {
        private final List<SolicitudValidation> validations = new ArrayList<>();

        public Builder withValidation(SolicitudValidation validation) {
            validations.add(validation);
            return this;
        }

        public UsuarioValidationQueue build() {
            return new UsuarioValidationQueue(validations);
        }
    }
}
