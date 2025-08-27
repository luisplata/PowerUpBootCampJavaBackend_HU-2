package com.peryloth.r2dbc;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.r2dbc.entities.SolicitudEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.reactivecommons.utils.ObjectMapper;
import org.reactivecommons.utils.ObjectMapperImp;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class SolicitudRepositoryAdapterTest {

    private SolicitudReactRepository repository;
    private ObjectMapper mapper;
    private SolicitudRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        repository = mock(SolicitudReactRepository.class);
        mapper = new ObjectMapperImp();
        adapter = new SolicitudRepositoryAdapter(repository, mapper);
    }

    @Test
    void saveSolicitud_mapsAndPersistsCorrectly() {
        // Arrange (dominio -> entity)
        Solicitud solicitud = Solicitud.builder()
                .idSolicitud(null)
                .monto(5000.0)
                .plazoMeses(12)
                .email("test@email.com")
                .estado(Estados.builder().idEstado(1L).build())
                .tipoPrestamo(TipoPrestamo.builder().idTipoPrestamo(2L).build())
                .build();

        SolicitudEntity savedEntity = SolicitudEntity.builder()
                .idSolicitud(10L) // simula DB asignando ID
                .monto(java.math.BigDecimal.valueOf(5000.0))
                .plazo(12)
                .email("test@email.com")
                .estadoId(1L)
                .tipoPrestamoId(2L)
                .build();

        when(repository.save(any(SolicitudEntity.class)))
                .thenReturn(Mono.just(savedEntity));

        // Act
        Mono<Solicitud> result = adapter.saveSolicitud(solicitud);

        // Assert
        StepVerifier.create(result)
                .assertNext(saved -> {
                    assertThat(saved.getIdSolicitud()).isEqualTo(10L);
                    assertThat(saved.getEmail()).isEqualTo("test@email.com");
                    assertThat(saved.getMonto()).isEqualTo(5000.0);
                    assertThat(saved.getPlazoMeses()).isEqualTo(12);
                    assertThat(saved.getEstado().getIdEstado()).isEqualTo(1L);
                    assertThat(saved.getTipoPrestamo().getIdTipoPrestamo()).isEqualTo(2L);
                })
                .verifyComplete();

        // Verifica que el repositorio reciba la entidad correcta
        ArgumentCaptor<SolicitudEntity> captor = ArgumentCaptor.forClass(SolicitudEntity.class);
        verify(repository, times(1)).save(captor.capture());

        SolicitudEntity captured = captor.getValue();
        assertThat(captured.getEmail()).isEqualTo("test@email.com");
        assertThat(captured.getMonto()).isEqualTo(java.math.BigDecimal.valueOf(5000.0));
        assertThat(captured.getPlazo()).isEqualTo(12);
        assertThat(captured.getEstadoId()).isEqualTo(1L);
        assertThat(captured.getTipoPrestamoId()).isEqualTo(2L);
    }
}
