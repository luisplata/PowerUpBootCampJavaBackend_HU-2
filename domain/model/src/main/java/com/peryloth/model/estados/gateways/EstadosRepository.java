package com.peryloth.model.estados.gateways;

import com.peryloth.model.estados.Estados;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface EstadosRepository {
    Mono<Estados> saveEstado(Estados estado);

    Mono<Estados> getEstadoById(Long id);

    Mono<Estados> getEstadoByNombre(String nombre);

    Flux<Estados> getAllEstados();
}
