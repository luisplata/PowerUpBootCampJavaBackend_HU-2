package com.peryloth.usecase.endeudamiento;

import reactor.core.publisher.Mono;

public interface CalcularCapacidadGateway {
    Mono<CalcularCapacidadResponse> calcular(CalcularCapacidadRequest request);
}
