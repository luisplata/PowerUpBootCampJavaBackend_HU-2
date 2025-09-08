package com.peryloth.api;

import com.peryloth.api.dto.SolicitudRequestDTO;
import com.peryloth.api.dto.SolicitudResponseDTO;
import com.peryloth.api.dto.putSolicitudes.UpdateSolicitudRequestDTO;
import com.peryloth.api.mapper.SolicitudDTOMapper;
import com.peryloth.usecase.getallsolicitud.IGetAllSolicitudUseCase;
import com.peryloth.usecase.registerloanrequest.IRegisterLoanRequest;
import com.peryloth.usecase.updatesolicitud.IUpdateSolicitudUseCase;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Handler define la lógica de negocio que será llamada desde el Router.
 * <p>
 * Contiene las operaciones:
 * - loadRequest     : Crear una nueva solicitud.
 * - getSolicitudes  : Listar todas las solicitudes (solo rol ASESOR).
 * - updateSolicitud : Aprobar o rechazar una solicitud (solo rol ASESOR).
 */
@Component
@RequiredArgsConstructor
public class Handler {

    private static final Logger log = LoggerFactory.getLogger(Handler.class);

    private final IRegisterLoanRequest registerLoanRequest;
    private final SolicitudDTOMapper solicitudDTOMapper;
    private final IGetAllSolicitudUseCase getAllSolicitudUseCase;
    private final IUpdateSolicitudUseCase updateSolicitudUseCase;

    /**
     * Crea una nueva solicitud de crédito.
     * Estado inicial: "Pendiente de revisión".
     */
    public Mono<ServerResponse> loadRequest(ServerRequest serverRequest) {
        log.info("Iniciando proceso para crear nueva solicitud");
        return serverRequest.bodyToMono(SolicitudRequestDTO.class)
                .doOnNext(requestDTO -> log.info("Creando nueva solicitud para documento {}", requestDTO.getDocumentoIdentidad()))
                .flatMap(requestDTO -> registerLoanRequest.registerLoanRequest(
                                solicitudDTOMapper.toEntity(requestDTO),
                                requestDTO.getDocumentoIdentidad(),
                                requestDTO.getEmail()
                        )
                        .flatMap(entity -> {
                            log.info("Solicitud {} creada correctamente", entity.getIdSolicitud());
                            return ServerResponse.ok()
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(solicitudDTOMapper.toResponseDTO(entity));
                        }))
                .onErrorResume(IllegalArgumentException.class,
                        e -> {
                            log.warn("Error de validación al crear solicitud: {}", e.getMessage());
                            return ServerResponse.badRequest()
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .bodyValue("Error de validación: " + e.getMessage());
                        })
                .onErrorResume(e -> {
                    log.error("Error interno al crear solicitud", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.TEXT_PLAIN)
                            .bodyValue("Ocurrió un error interno");
                });
    }

    /**
     * Obtiene el listado de solicitudes.
     * Requiere que el usuario tenga rol ASESOR (AuthFilter se encarga de validarlo).
     */
    public Mono<ServerResponse> getSolicitudes(ServerRequest serverRequest) {
        String authHeader = serverRequest.headers().firstHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("Acceso no autorizado al intentar listar solicitudes");
            return ServerResponse.status(HttpStatus.UNAUTHORIZED).build();
        }

        String token = authHeader.substring(7);
        log.info("Listando solicitudes para token {}", token);

        return ServerResponse.ok()
                .contentType(MediaType.APPLICATION_JSON)
                .body(getAllSolicitudUseCase.getAllSolicitud(token), SolicitudResponseDTO.class)
                .onErrorResume(IllegalArgumentException.class,
                        e -> {
                            log.warn("Error de validación al obtener solicitudes: {}", e.getMessage());
                            return ServerResponse.badRequest()
                                    .contentType(MediaType.TEXT_PLAIN)
                                    .bodyValue("Error de validación: " + e.getMessage());
                        })
                .onErrorResume(e -> {
                    log.error("Error inesperado al obtener solicitudes", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .contentType(MediaType.TEXT_PLAIN)
                            .bodyValue("Ocurrió un error interno");
                });
    }

    /**
     * Actualiza el estado de una solicitud (Aprobado/Rechazado).
     * Envía un mensaje a SQS para notificación vía Lambda.
     */
    /**
     * HU-6: Aprobar o Rechazar Solicitud
     */
    public Mono<ServerResponse> updateSolicitud(ServerRequest serverRequest) {
        Long solicitudId = Long.valueOf(serverRequest.pathVariable("id"));
        return serverRequest.bodyToMono(UpdateSolicitudRequestDTO.class)
                .doOnNext(dto -> log.info("Petición de actualización recibida para solicitud {} con estado {}", solicitudId, dto.nuevoEstado()))
                .flatMap(requestDTO -> updateSolicitudUseCase.updateSolicitud(
                                solicitudId,
                                requestDTO.nuevoEstado()
                        )
                        .doOnNext(solicitud -> log.info("Solicitud {} actualizada a estado {}", solicitudId, requestDTO.nuevoEstado()))
                        .flatMap(entity -> ServerResponse.ok()
                                .contentType(MediaType.APPLICATION_JSON)
                                .bodyValue(solicitudDTOMapper.toResponseDTO(entity))))
                .onErrorResume(IllegalArgumentException.class, e -> {
                    log.warn("Error de validación en updateSolicitud: {}", e.getMessage());
                    return ServerResponse.badRequest().bodyValue("Error de validación: " + e.getMessage());
                })
                .onErrorResume(e -> {
                    log.error("Error inesperado en updateSolicitud", e);
                    return ServerResponse.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .bodyValue("Ocurrió un error inesperado");
                });
    }

}