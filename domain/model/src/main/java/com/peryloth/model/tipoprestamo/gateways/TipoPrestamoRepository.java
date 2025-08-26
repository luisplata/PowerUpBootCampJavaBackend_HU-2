package com.peryloth.model.tipoprestamo.gateways;

import com.peryloth.model.tipoprestamo.TipoPrestamo;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface TipoPrestamoRepository {
    Mono<TipoPrestamo> save(TipoPrestamo tipoPrestamo);

    Mono<TipoPrestamo> findById(Long id);

    Mono<TipoPrestamo> findByName(String name);

    Flux<TipoPrestamo> findAll();

    Mono<Void> deleteById(Long id);
}
