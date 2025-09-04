package com.peryloth.api;

import com.peryloth.api.dto.SolicitudRequestDTO;
import com.peryloth.api.dto.SolicitudResponseDTO;
import com.peryloth.api.dto.getSolicitudes.GetAllSolicitudesRequestDTO;
import com.peryloth.api.mapper.SolicitudDTOMapper;
import com.peryloth.usecase.getallsolicitud.IGetAllSolicitudUseCase;
import com.peryloth.usecase.registerloanrequest.IRegisterLoanRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class Handler {

    private final IRegisterLoanRequest registerLoanRequest;
    private final SolicitudDTOMapper solicitudDTOMapper;
    private final IGetAllSolicitudUseCase getAllSolicitudUseCase;

    public Mono<ServerResponse> loadRequest(ServerRequest serverRequest) {
        return serverRequest.bodyToMono(SolicitudRequestDTO.class)
                .flatMap(requestDTO ->
                        registerLoanRequest.registerLoanRequest(solicitudDTOMapper.toEntity(requestDTO), requestDTO.getDocumentoIdentidad(), requestDTO.getEmail())
                                .flatMap(entity ->
                                        ServerResponse.ok().bodyValue(solicitudDTOMapper.toResponseDTO(entity))
                                )
                )
                .onErrorResume(IllegalArgumentException.class,
                        e -> ServerResponse.badRequest().bodyValue("Error de validación: " + e.getMessage()))
                .onErrorResume(e -> ServerResponse.status(500).bodyValue("Error interno: " + e.getMessage()));
    }

    public Mono<ServerResponse> getSolicitudes(ServerRequest serverRequest) {
        String authHeader = serverRequest.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);

        return ServerResponse.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_JSON)
                .body(getAllSolicitudUseCase.getAllSolicitud(token), SolicitudResponseDTO.class)
                .onErrorResume(IllegalArgumentException.class,
                        e -> ServerResponse.badRequest()
                                .contentType(org.springframework.http.MediaType.TEXT_PLAIN)
                                .bodyValue("Error de validación: " + e.getMessage())
                )
                .onErrorResume(e ->
                        ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                .bodyValue("Ocurrió un error inesperado")
                );
    }

}
