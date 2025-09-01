package com.peryloth.api;

import com.peryloth.api.dto.SolicitudRequestDTO;
import com.peryloth.api.mapper.SolicitudDTOMapper;
import com.peryloth.usecase.registerloanrequest.IRegisterLoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final IRegisterLoanRequest registerLoanRequest;
    private final SolicitudDTOMapper solicitudDTOMapper;

    public Mono<ServerResponse> loadRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SolicitudRequestDTO.class)
                .flatMap(requestDTO ->
                        registerLoanRequest.registerLoanRequest(solicitudDTOMapper.toEntity(requestDTO)).flatMap(entity ->
                                ServerResponse.ok().bodyValue(solicitudDTOMapper.toResponseDTO(entity))
                        )
                )
                .onErrorResume(IllegalArgumentException.class,
                        e -> ServerResponse.badRequest().bodyValue("Error de validación: " + e.getMessage()))
                .onErrorResume(e -> ServerResponse.status(500).bodyValue("Error interno: " + e.getMessage()));
    }
}
