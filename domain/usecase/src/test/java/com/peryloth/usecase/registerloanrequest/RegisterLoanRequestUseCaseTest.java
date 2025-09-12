package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadGateway;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadRequest;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadResponse;
import com.peryloth.usecase.getallsolicitud.UsuarioResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

class RegisterLoanRequestUseCaseTest {

    private TipoPrestamoRepository tipoRepo;
    private SolicitudRepository solicitudRepo;
    private EstadosRepository estadosRepo;
    private IGetUserRepository userRepo;
    private CalcularCapacidadGateway calcularCapacidadGateway;
    private RegisterLoanRequestUseCase useCase;

    @BeforeEach
    void setup() {
        tipoRepo = Mockito.mock(TipoPrestamoRepository.class);
        solicitudRepo = Mockito.mock(SolicitudRepository.class);
        estadosRepo = Mockito.mock(EstadosRepository.class);
        userRepo = Mockito.mock(IGetUserRepository.class);
        calcularCapacidadGateway = Mockito.mock(CalcularCapacidadGateway.class);

        useCase = new RegisterLoanRequestUseCase(
                tipoRepo, solicitudRepo, estadosRepo, userRepo, calcularCapacidadGateway
        );
    }

    private Solicitud buildSolicitud() {
        Solicitud solicitud = new Solicitud();
        solicitud.setMonto(1000.0);
        solicitud.setPlazo(12);
        solicitud.setTipoPrestamo(
                TipoPrestamo.builder()
                        .nombre("Personal")
                        .tasaInteres(BigDecimal.valueOf(0.05))
                        .build()
        );
        solicitud.setEstado(new Estados(1L, "Pendiente"));
        solicitud.setEmail("test@test.com");
        return solicitud;
    }

    @Test
    void shouldRegisterLoanWithEmptyPreviousSolicitudes() {
        Solicitud solicitud = buildSolicitud();

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(solicitud.getTipoPrestamo()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.getUserByEmail("test@test.com", "token"))
                .thenReturn(Mono.just(new UsuarioResponseDTO("123", "test@test.com", 5000L)));

        // No hay solicitudes activas
        Mockito.when(solicitudRepo.getAllSolicitudesByEmail("test@test.com"))
                .thenReturn(Flux.empty());

        List<CalcularCapacidadResponse.PlanPago> planes = List.of(
                new CalcularCapacidadResponse.PlanPago(1, "500", "50", "450", "4550")
        );

        Mockito.when(calcularCapacidadGateway.calcular(Mockito.any(CalcularCapacidadRequest.class)))
                .thenReturn(Mono.just(
                        new CalcularCapacidadResponse(0.3, 0.0, 0.3, 100.0, "APROBADO", planes)
                ));

        Mockito.when(estadosRepo.getEstadoByNombre("APROBADO"))
                .thenReturn(Mono.just(new Estados(2L, "Aprobado")));

        Mockito.when(solicitudRepo.saveSolicitud(Mockito.any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com", "token"))
                .expectNextMatches(s -> s.getEstado().getNombre().equals("Aprobado"))
                .verifyComplete();
    }

    @Test
    void shouldRegisterLoanWithActiveSolicitudes() {
        Solicitud solicitud = buildSolicitud();

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(solicitud.getTipoPrestamo()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.getUserByEmail("test@test.com", "token"))
                .thenReturn(Mono.just(new UsuarioResponseDTO("123", "test@test.com", 5000L)));

        // Una solicitud activa previa
        Solicitud activa = buildSolicitud();
        activa.setEstado(new Estados(9L, "Activo"));
        Mockito.when(solicitudRepo.getAllSolicitudesByEmail("test@test.com"))
                .thenReturn(Flux.just(activa));

        List<CalcularCapacidadResponse.PlanPago> planes = List.of(
                new CalcularCapacidadResponse.PlanPago(1, "500", "50", "450", "4550")
        );

        Mockito.when(calcularCapacidadGateway.calcular(Mockito.any(CalcularCapacidadRequest.class)))
                .thenReturn(Mono.just(
                        new CalcularCapacidadResponse(0.3, 0.0, 0.3, 100.0, "APROBADO", planes)
                ));

        Mockito.when(estadosRepo.getEstadoByNombre("RECHAZADO"))
                .thenReturn(Mono.just(new Estados(3L, "Rechazado")));

        Mockito.when(solicitudRepo.saveSolicitud(Mockito.any()))
                .thenAnswer(inv -> Mono.just(inv.getArgument(0)));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com", "token"))
                .expectNextMatches(s -> s.getEstado().getNombre().equals("Rechazado"))
                .verifyComplete();
    }

    @Test
    void shouldFailWhenUserNotFound() {
        Solicitud solicitud = buildSolicitud();

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(solicitud.getTipoPrestamo()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.getUserByEmail("test@test.com", "token"))
                .thenReturn(Mono.empty());

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com", "token"))
                .expectError(IllegalArgumentException.class)
                .verify();
    }

    @Test
    void shouldPropagateErrorFromCalcularCapacidad() {
        Solicitud solicitud = buildSolicitud();

        Mockito.when(tipoRepo.findByName("Personal"))
                .thenReturn(Mono.just(solicitud.getTipoPrestamo()));
        Mockito.when(estadosRepo.getEstadoById(1L))
                .thenReturn(Mono.just(new Estados(1L, "Pendiente")));
        Mockito.when(userRepo.getUserByEmail("test@test.com", "token"))
                .thenReturn(Mono.just(new UsuarioResponseDTO("123", "test@test.com", 5000L)));
        Mockito.when(solicitudRepo.getAllSolicitudesByEmail("test@test.com"))
                .thenReturn(Flux.empty());

        Mockito.when(calcularCapacidadGateway.calcular(Mockito.any()))
                .thenReturn(Mono.error(new RuntimeException("Error remoto")));

        StepVerifier.create(useCase.registerLoanRequest(solicitud, "123", "test@test.com", "token"))
                .expectError(RuntimeException.class)
                .verify();
    }
}
