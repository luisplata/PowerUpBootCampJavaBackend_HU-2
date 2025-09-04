package com.peryloth.usecase.getallsolicitud;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.usecase.registerloanrequest.IGetUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.List;

import static org.mockito.Mockito.*;

class GetAllSolicitudUseCaseTest {

    private IGetUserRepository getUserRepository;
    private SolicitudRepository solicitudRepository;
    private GetAllSolicitudUseCase useCase;

    @BeforeEach
    void setUp() {
        getUserRepository = Mockito.mock(IGetUserRepository.class);
        solicitudRepository = Mockito.mock(SolicitudRepository.class);
        useCase = new GetAllSolicitudUseCase(getUserRepository, solicitudRepository);
    }

    private Solicitud buildSolicitud(Long id, String email) {
        return Solicitud.builder()
                .idSolicitud(id)
                .usuario_id("123456")
                .tipoPrestamo(new TipoPrestamo(1L, "Préstamo Normal",
                        BigDecimal.valueOf(1000), BigDecimal.valueOf(5000),
                        BigDecimal.valueOf(0.05), true))
                .estado(new Estados(1L, "Pendiente de revisión"))
                .monto(50000.0)
                .plazoMeses(12)
                .email(email)
                .build();
    }

    @Test
    void shouldReturnSolicitudesWhenFound() {
        // Arrange
        String token = "valid-token";
        Solicitud solicitud = buildSolicitud(1L, "test@example.com");
        UsuarioResponseDTO usuario = new UsuarioResponseDTO("test@example.com", "Luis", 2000L);

        when(solicitudRepository.getAllSolicitudes()).thenReturn(Mono.just(List.of(solicitud)));
        when(getUserRepository.getUserByEmail(solicitud.getEmail(), token)).thenReturn(Mono.just(usuario));

        // Act & Assert
        StepVerifier.create(useCase.getAllSolicitud(token))
                .expectNextMatches(dto ->
                        dto.solicitud().getIdSolicitud().equals(1L) &&
                                dto.email().equals("test@example.com") &&
                                dto.nombre().equals("Luis") &&
                                dto.salario_base().equals(2000L)
                )
                .verifyComplete();

        verify(solicitudRepository, times(1)).getAllSolicitudes();
        verify(getUserRepository, times(1)).getUserByEmail("test@example.com", token);
    }

    @Test
    void shouldReturnErrorWhenNoSolicitudesFound() {
        // Arrange
        String token = "valid-token";
        when(solicitudRepository.getAllSolicitudes()).thenReturn(Mono.just(List.of()));

        // Act & Assert
        StepVerifier.create(useCase.getAllSolicitud(token))
                .expectErrorMatches(ex -> ex instanceof IllegalArgumentException &&
                        ex.getMessage().equals("No hay solicitudes registradas"))
                .verify();

        verify(solicitudRepository, times(1)).getAllSolicitudes();
        verifyNoInteractions(getUserRepository);
    }

    @Test
    void shouldPropagateErrorWhenGetUserFails() {
        // Arrange
        String token = "valid-token";
        Solicitud solicitud = buildSolicitud(2L, "fail@example.com");

        when(solicitudRepository.getAllSolicitudes()).thenReturn(Mono.just(List.of(solicitud)));
        when(getUserRepository.getUserByEmail(solicitud.getEmail(), token))
                .thenReturn(Mono.error(new RuntimeException("User service down")));

        // Act & Assert
        StepVerifier.create(useCase.getAllSolicitud(token))
                .expectErrorMatches(ex -> ex instanceof RuntimeException &&
                        ex.getMessage().equals("User service down"))
                .verify();

        verify(solicitudRepository, times(1)).getAllSolicitudes();
        verify(getUserRepository, times(1)).getUserByEmail("fail@example.com", token);
    }

    @Test
    void shouldReturnMultipleSolicitudes() {
        // Arrange
        String token = "valid-token";
        Solicitud solicitud1 = buildSolicitud(1L, "one@example.com");
        Solicitud solicitud2 = buildSolicitud(2L, "two@example.com");

        UsuarioResponseDTO user1 = new UsuarioResponseDTO("one@example.com", "UserOne", 1000L);
        UsuarioResponseDTO user2 = new UsuarioResponseDTO("two@example.com", "UserTwo", 2000L);

        when(solicitudRepository.getAllSolicitudes()).thenReturn(Mono.just(List.of(solicitud1, solicitud2)));
        when(getUserRepository.getUserByEmail("one@example.com", token)).thenReturn(Mono.just(user1));
        when(getUserRepository.getUserByEmail("two@example.com", token)).thenReturn(Mono.just(user2));

        // Act & Assert
        StepVerifier.create(useCase.getAllSolicitud(token))
                .expectNextMatches(dto -> dto.email().equals("one@example.com") && dto.nombre().equals("UserOne"))
                .expectNextMatches(dto -> dto.email().equals("two@example.com") && dto.nombre().equals("UserTwo"))
                .verifyComplete();

        verify(solicitudRepository, times(1)).getAllSolicitudes();
        verify(getUserRepository, times(1)).getUserByEmail("one@example.com", token);
        verify(getUserRepository, times(1)).getUserByEmail("two@example.com", token);
    }
}
