package com.peryloth.webclientcustom;

import com.peryloth.usecase.getallsolicitud.UsuarioResponseDTO;
import com.peryloth.usecase.registerloanrequest.IGetUserRepository;
import com.peryloth.webclientcustom.dto.GetUserByEmailRequestDTO;
import com.peryloth.webclientcustom.dto.GetUserByEmailResponseDTO;
import com.peryloth.webclientcustom.dto.TokenValidationResponse;
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
        return jwtTokenProvider.generateToken()
                .doOnNext(key -> logger.info("🔑 Generated JWT Token: " + key))
                .flatMap(jwtToken ->
                        webClient.post()
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
                                }))
                .onErrorResume(ex -> {
                    logger.info("⚠️ Error en WebClient: " + ex.getMessage());
                    return Mono.just(false);
                });

    }


    public Mono<Boolean> isTokenValid(String token) {
        return webClient.get()
                .uri("/api/v1/token/validate")
                .header("Authorization", "Bearer " + token)
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(TokenValidationResponse.class)
                                .map(dto -> "OK".equalsIgnoreCase(dto.status()))
                                .defaultIfEmpty(false);
                    } else if (response.statusCode().is4xxClientError()) {
                        // Token inválido
                        return Mono.just(false);
                    } else {
                        // Otros errores → se toma como inválido
                        return Mono.just(false);
                    }
                })
                .doOnNext(valid -> logger.info("🔑 Token valid: " + valid))
                .doOnError(ex -> logger.warning("⚠️ Error validando token: " + ex.getMessage()))
                .onErrorReturn(false);
    }

    public Mono<UsuarioResponseDTO> getUserByEmail(String email, String token) {
        return webClient.post()
                .uri("/api/v1/users/getUser")
                .header("Authorization", "Bearer " + token)
                .bodyValue(new GetUserByEmailRequestDTO(email))
                .exchangeToMono(response -> {
                    if (response.statusCode().is2xxSuccessful()) {
                        return response.bodyToMono(UsuarioResponseDTO.class)
                                .doOnNext(dto -> logger.info("✅ GetUserByEmail response: " + dto));
                    } else if (response.statusCode().is4xxClientError()) {
                        return Mono.error(new IllegalArgumentException("Usuario no encontrado o token inválido"));
                    } else {
                        return Mono.error(new IllegalArgumentException("Usuario no encontrado o token inválido"));
                    }
                })
                .doOnNext(valid -> logger.info("🔑 Token valid: " + valid))
                .doOnError(ex -> logger.warning("⚠️ Error validando token: " + ex.getMessage()));
    }


}
