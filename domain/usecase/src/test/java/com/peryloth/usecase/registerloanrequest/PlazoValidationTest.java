package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.PlazoValidation;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class PlazoValidationTest {
    @Test
    void shouldFailWhenPlazoInvalid() {
        Solicitud solicitud = new Solicitud();
        solicitud.setPlazo(0);

        StepVerifier.create(new PlazoValidation().validate(solicitud))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldPassWhenPlazoValid() {
        Solicitud solicitud = new Solicitud();
        solicitud.setPlazo(12);

        StepVerifier.create(new PlazoValidation().validate(solicitud))
                .verifyComplete();
    }
}
