package com.peryloth.webclientcustom;

import com.peryloth.usecase.registerloanrequest.IGetUserRepository;
import com.peryloth.webclientcustom.dto.UserValidationRequest;
import com.peryloth.webclientcustom.helper.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@Component
@RequiredArgsConstructor
public class EstadosWebClientAdapter implements IGetUserRepository {

    private final WebClient webClient;
    private final JwtTokenProvider jwtTokenProvider;
    private static final Logger logger = Logger.getLogger(EstadosWebClientAdapter.class.getName());

    @Override
    public Mono<Boolean> isUserValid(String id, String email) {
        logger.info("EstadosWebClientAdapter: Validando usuario con identificación=" + id + ", email=" + email);
        return jwtTokenProvider.generateToken()
                .flatMap(jwtToken ->
                {
                    System.out.println("Generated JWT Token: " + jwtToken);
                    return webClient.post()
                            .uri("/api/v1/users/validate")
                            .header("Authorization", "Bearer " + jwtToken)
                            .bodyValue(new UserValidationRequest(id, email))
                            .exchangeToMono(response -> {
                                if (response.statusCode().is2xxSuccessful()) {
                                    // Caso 200 → usuario válido
                                    return response.bodyToMono(Boolean.class)
                                            .doOnNext(valid -> logger.info("✅ User valid response: " + valid));
                                } else if (response.statusCode().is4xxClientError()) {
                                    // Caso 401 → usuario inválido
                                    return response.bodyToMono(Boolean.class)
                                            .defaultIfEmpty(false)
                                            .doOnNext(valid -> logger.info("❌ User invalid response: " + valid));
                                } else {
                                    // Otros errores → lo manejamos como inválido
                                    return Mono.just(false);
                                }
                            });
                })
                .onErrorResume(ex -> {
                    logger.info("⚠️ Error en WebClient: " + ex.getMessage());
                    return Mono.just(false);
                });

    }

}
