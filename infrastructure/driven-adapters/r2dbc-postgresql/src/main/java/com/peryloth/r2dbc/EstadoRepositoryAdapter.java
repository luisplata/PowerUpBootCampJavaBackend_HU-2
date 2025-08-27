package com.peryloth.r2dbc;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.r2dbc.entities.EstadoEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class EstadoRepositoryAdapter extends ReactiveAdapterOperations<
        Estados,
        EstadoEntity,
        Long,
        EstadoReactRepository
        > implements EstadosRepository {

    private static final Logger log = LoggerFactory.getLogger(EstadoRepositoryAdapter.class);

    public EstadoRepositoryAdapter(EstadoReactRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, Estados.class));
    }

    @Override
    public Mono<Estados> saveEstado(Estados estado) {
        return repository.save(mapper.map(estado, EstadoEntity.class))
                .map(saved -> mapper.map(saved, Estados.class))
                .doOnNext(e -> log.info("Estado guardado con id={}", e.getIdEstado()));
    }

    @Override
    public Mono<Estados> getEstadoById(Long id) {
        return repository.findById(id)
                .map(e -> mapper.map(e, Estados.class))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("Estado no encontrado con id=" + id)));
    }

    @Override
    public Flux<Estados> getAllEstados() {
        return repository.findAll()
                .map(e -> mapper.map(e, Estados.class));
    }
}
