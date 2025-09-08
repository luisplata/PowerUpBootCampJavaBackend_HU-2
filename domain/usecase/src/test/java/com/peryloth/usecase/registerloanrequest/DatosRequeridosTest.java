package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.DatosRequeridos;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

public class DatosRequeridosTest {
    @Test
    void shouldFailWhenMontoNull() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(null);
        solicitud.setPlazo(12);

        StepVerifier.create(new DatosRequeridos().validate(solicitud))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldFailWhenPlazoNull() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(null);

        StepVerifier.create(new DatosRequeridos().validate(solicitud))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldPassWhenAllValid() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(12);

        StepVerifier.create(new DatosRequeridos().validate(solicitud))
                .verifyComplete();
    }
}
