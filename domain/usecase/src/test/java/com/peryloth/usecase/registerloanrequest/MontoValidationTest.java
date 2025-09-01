package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.MontoValidation;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class MontoValidationTest {
    @Test
    void shouldFailWhenMontoLessOrEqualZero() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(0.0);

        StepVerifier.create(new MontoValidation().validate(solicitud))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldPassWhenMontoPositive() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);

        StepVerifier.create(new MontoValidation().validate(solicitud))
                .verifyComplete();
    }
}
