package com.peryloth.r2dbc;

import com.peryloth.model.tipoprestamo.TipoPrestamo;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.peryloth.r2dbc.entities.TipoPrestamoEntity;
import com.peryloth.r2dbc.helper.ReactiveAdapterOperations;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class TipoPrestamoRepositoryAdapter extends ReactiveAdapterOperations<
        TipoPrestamo,
        TipoPrestamoEntity,
        Long,
        TipoPrestamoReactRepository
        > implements TipoPrestamoRepository {

    public TipoPrestamoRepositoryAdapter(TipoPrestamoReactRepository repository, ObjectMapper mapper) {
        super(repository, mapper, d -> mapper.map(d, TipoPrestamo.class));
    }

    @Override
    public Mono<TipoPrestamo> save(TipoPrestamo tipoPrestamo) {
        TipoPrestamoEntity entity = mapper.map(tipoPrestamo, TipoPrestamoEntity.class);
        return repository.save(entity)
                .map(saved -> mapper.map(saved, TipoPrestamo.class));
    }

    @Override
    public Mono<TipoPrestamo> findById(Long id) {
        return repository.findById(id)
                .map(entity -> mapper.map(entity, TipoPrestamo.class));
    }

    @Override
    public Mono<TipoPrestamo> findByName(String name) {
        return repository.findByNombre(name)
                .map(entity -> mapper.map(entity, TipoPrestamo.class))
                .switchIfEmpty(Mono.error(new IllegalArgumentException("TipoPrestamo no encontrado: " + name)));
    }

    @Override
    public Flux<TipoPrestamo> findAll() {
        return repository.findAll()
                .map(entity -> mapper.map(entity, TipoPrestamo.class));
    }

    @Override
    public Mono<Void> deleteById(Long id) {
        return repository.deleteById(id);
    }
}
