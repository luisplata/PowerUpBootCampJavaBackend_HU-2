package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterLoanRequestUseCaseTest {

    private TipoPrestamoRepository tipoPrestamoRepository;
    private SolicitudRepository solicitudRepository;
    private EstadosRepository estadosRepository;
    private IGetUserRepository getUserRepository;

    private RegisterLoanRequestUseCase useCase;

    @BeforeEach
    void setUp() {
        tipoPrestamoRepository = mock(TipoPrestamoRepository.class);
        solicitudRepository = mock(SolicitudRepository.class);
        estadosRepository = mock(EstadosRepository.class);

        useCase = new RegisterLoanRequestUseCase(
                tipoPrestamoRepository, solicitudRepository, estadosRepository, getUserRepository
        );
    }

    @Test
    void registerLoanRequest_success() {
//        // Arrange
//        TipoPrestamo tipoPrestamo = new TipoPrestamo();
//        tipoPrestamo.setNombre("Normal");
//
//        Estados estado = new Estados();
//        estado.setIdEstado(1L);
//        estado.setNombre("Pendiente");
//
//        Solicitud solicitud = new Solicitud();
//        solicitud.setTipoPrestamo(tipoPrestamo);
//        solicitud.setEstado(estado);
//
//        when(tipoPrestamoRepository.findByName("Normal"))
//                .thenReturn(Mono.just(tipoPrestamo));
//        when(estadosRepository.getEstadoById(1L))
//                .thenReturn(Mono.just(estado));
//        when(solicitudRepository.saveSolicitud(any(Solicitud.class)))
//                .thenReturn(Mono.just(solicitud));
//
//        // Act + Assert
//        StepVerifier.create(useCase.registerLoanRequest(solicitud, "12345678", ""))
//                .expectNextMatches(saved ->
//                        saved.getTipoPrestamo().getNombre().equals("Normal") &&
//                                saved.getEstado().getIdEstado() == 1L
//                )
//                .verifyComplete();
//
//        // Verify mocks were called
//        verify(tipoPrestamoRepository).findByName("Normal");
//        verify(estadosRepository).getEstadoById(1L);
//        verify(solicitudRepository).saveSolicitud(solicitud);
    }
}
