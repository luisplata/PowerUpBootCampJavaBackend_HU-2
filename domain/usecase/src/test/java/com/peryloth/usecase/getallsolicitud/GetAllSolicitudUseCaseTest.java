package com.peryloth.usecase.getallsolicitud;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.usecase.registerloanrequest.IGetUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mockito;

import java.math.BigDecimal;

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
                .plazo(12)
                .email(email)
                .build();
    }

}
