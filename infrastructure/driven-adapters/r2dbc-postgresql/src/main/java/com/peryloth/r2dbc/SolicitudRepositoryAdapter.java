package com.peryloth.r2dbc;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.r2dbc.entities.SolicitudEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Repository
public class SolicitudRepositoryAdapter extends ReactiveAdapterOperations<
        Solicitud,
        SolicitudEntity,
        Long,
        SolicitudReactRepository
        > implements SolicitudRepository {

    public SolicitudRepositoryAdapter(SolicitudReactRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Solicitud.class));
    }

    // Dentro de tu adapter de persistencia de Solicitud
    private SolicitudEntity toEntity(Solicitud s) {
        return SolicitudEntity.builder()
                .idSolicitud(s.getIdSolicitud())
                .monto(s.getMonto() != null ? java.math.BigDecimal.valueOf(s.getMonto()) : null)
                .plazo(s.getPlazoMeses()) // del dominio
                .email(s.getEmail())
                .estadoId(s.getEstado() != null ? s.getEstado().getIdEstado() : null)
                .tipoPrestamoId(s.getTipoPrestamo() != null ? s.getTipoPrestamo().getIdTipoPrestamo() : null)
                .build();
    }

    private Solicitud toDomain(SolicitudEntity e, Solicitud original) {
        // reusa los objetos ya resueltos en el use case (estado y tipoPrestamo), solo setea el id generado
        return original.toBuilder()
                .idSolicitud(e.getIdSolicitud())
                .build();
    }

    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        return repository.save(toEntity(solicitud))
                .map(saved -> toDomain(saved, solicitud));
    }

    @Override
    public Flux<Solicitud> getAllSolicitudes() {
        //TODO: filter  (aquellas que están "Pendiente de revisión", "Rechazadas", "Revision manual")
        return repository.findAll()
                .map(this::toEntity);
    }

}
