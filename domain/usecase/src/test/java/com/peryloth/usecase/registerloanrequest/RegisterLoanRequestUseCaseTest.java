package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class RegisterLoanRequestUseCaseTest {

    private TipoPrestamoRepository tipoRepo;
    private SolicitudRepository solicitudRepo;
    private EstadosRepository estadosRepo;
    private IGetUserRepository userRepo;
    private RegisterLoanRequestUseCase useCase;

    @BeforeEach
    void setup() {
        tipoRepo = Mockito.mock(TipoPrestamoRepository.class);
        solicitudRepo = Mockito.mock(SolicitudRepository.class);
        estadosRepo = Mockito.mock(EstadosRepository.class);
        userRepo = Mockito.mock(IGetUserRepository.class);

        useCase = new RegisterLoanRequestUseCase(tipoRepo, solicitudRepo, estadosRepo, userRepo);
    }

    @Test
    void shouldRegisterLoanSuccessfully() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(12);
        solicitud.setTipoPrestamo(TipoPrestamo.builder()
                .nombre("Personal")
                .build());
        solicitud.setEstado(new Estados(1L, "Pendiente"));

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(TipoPrestamo.builder()
                        .nombre("Personal")
                        .build()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.isUserValid("123", "test@test.com"))
                .thenReturn(Mono.just(true));
        Mockito.when(solicitudRepo.saveSolicitud(solicitud))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com"))
                .expectNext(solicitud)
                .verifyComplete();
    }

    @Test
    void shouldFailWhenUserInvalid() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(12);
        solicitud.setTipoPrestamo(TipoPrestamo.builder()
                .nombre("Personal")
                .build());
        solicitud.setEstado(new Estados(1L, "Pendiente"));

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(TipoPrestamo.builder()
                        .nombre("Personal")
                        .build()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.isUserValid("123", "test@test.com"))
                .thenReturn(Mono.just(false));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldFailWhenUserIsNull() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(12);
        solicitud.setTipoPrestamo(TipoPrestamo.builder()
                .nombre("Personal")
                .build());
        solicitud.setEstado(new Estados(1L, "Pendiente"));

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(TipoPrestamo.builder()
                        .nombre("Personal")
                        .build()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.isUserValid("123", "test@test.com"))
                .thenReturn(Mono.just(Boolean.FALSE));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com"))
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    void shouldRegisterLoanWhenUserIsValidTrue() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(12);
        solicitud.setTipoPrestamo(TipoPrestamo.builder()
                .nombre("Personal")
                .build());
        solicitud.setEstado(new Estados(1L, "Pendiente"));

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(TipoPrestamo.builder().nombre("Personal").build()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.isUserValid("123", "test@test.com"))
                .thenReturn(Mono.just(Boolean.TRUE)); // 👈 Caso True

        Mockito.when(solicitudRepo.saveSolicitud(solicitud))
                .thenReturn(Mono.just(solicitud));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com"))
                .expectNext(solicitud)
                .verifyComplete();
    }
}
