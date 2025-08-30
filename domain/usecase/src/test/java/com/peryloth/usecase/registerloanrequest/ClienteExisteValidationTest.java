package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.ClienteExisteValidation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class ClienteExisteValidationTest {

    @Test
    void shouldFailWhenUserNotValid() {
        IGetUserRepository repo = Mockito.mock(IGetUserRepository.class);
        Mockito.when(repo.isUserValid("123", "test@test.com"))
                .thenReturn(Mono.just(false));

        ClienteExisteValidation validation = new ClienteExisteValidation(repo, "123", "test@test.com");

        StepVerifier.create(validation.validate(new Solicitud()))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldPassWhenUserValid() {
        IGetUserRepository repo = Mockito.mock(IGetUserRepository.class);
        Mockito.when(repo.isUserValid("123", "test@test.com"))
                .thenReturn(Mono.just(true));

        ClienteExisteValidation validation = new ClienteExisteValidation(repo, "123", "test@test.com");

        StepVerifier.create(validation.validate(new Solicitud()))
                .verifyComplete();
    }
}
