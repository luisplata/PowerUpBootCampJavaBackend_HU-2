package com.peryloth.webclientcustom;

import com.peryloth.usecase.endeudamiento.CalcularCapacidadGateway;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadRequest;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class CapacidadDeEndeudamiento implements CalcularCapacidadGateway {

    private final WebClient webClient;
    private static final Logger logger = LoggerFactory.getLogger(CapacidadDeEndeudamiento.class);

    public CapacidadDeEndeudamiento(@Qualifier("endeudamiento") WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Mono<CalcularCapacidadResponse> calcular(CalcularCapacidadRequest request) {
        return webClient.post()
                .uri("/api/v1/calcular-capacidad")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(CalcularCapacidadResponse.class)
                .doOnNext(resp -> logger.info("Resultado de calcular capacidad: {}", resp))
                .onErrorResume(ex -> {
                    logger.error("Error llamando a calcular capacidad: {}", ex.getMessage(), ex);
                    return Mono.error(new RuntimeException("Error llamando a la Lambda de calcular capacidad", ex));
                });
    }
}
