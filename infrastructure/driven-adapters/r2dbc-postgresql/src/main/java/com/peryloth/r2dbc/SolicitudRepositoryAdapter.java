package com.peryloth.r2dbc;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.r2dbc.entities.SolicitudEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

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

    @Override
    public Mono<Solicitud> saveSolicitud(Solicitud solicitud) {
        SolicitudEntity entity = mapper.map(solicitud, SolicitudEntity.class);
        return repository.save(entity)
                .map(saved -> mapper.map(saved, Solicitud.class));
    }
}
