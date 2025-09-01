package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.DatosCliente;
import org.junit.jupiter.api.Test;
import reactor.test.StepVerifier;

class DatosClienteTest {

    @Test
    void shouldFailWhenIdentificationEmpty() {
        DatosCliente validation = new DatosCliente("", "test@test.com");
        StepVerifier.create(validation.validate(new Solicitud()))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldFailWhenEmailEmpty() {
        DatosCliente validation = new DatosCliente("123", "");
        StepVerifier.create(validation.validate(new Solicitud()))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldPassWhenValid() {
        DatosCliente validation = new DatosCliente("123", "test@test.com");
        StepVerifier.create(validation.validate(new Solicitud()))
                .verifyComplete();
    }
}
