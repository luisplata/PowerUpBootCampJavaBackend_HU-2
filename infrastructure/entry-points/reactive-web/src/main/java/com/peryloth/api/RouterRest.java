package com.peryloth.api;

import com.peryloth.api.dto.SolicitudRequestDTO;
import com.peryloth.api.dto.SolicitudResponseDTO;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    private final AuthFilter authFilter;

    public RouterRest(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {"application/json"},
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "loadRequest",
                    operation = @Operation(
                            operationId = "crearSolicitud",
                            summary = "Crea una nueva solicitud de préstamo",
                            description = "Este endpoint permite registrar una nueva solicitud de préstamo. " +
                                    "La solicitud se guarda con estado 'Pendiente de revisión'.",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(
                                            schema = @Schema(implementation = SolicitudRequestDTO.class)
                                    )
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Solicitud creada correctamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = SolicitudResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            ),
            @RouterOperation(
                    path = "/api/v1/solicitud",
                    produces = {"application/json"},
                    method = RequestMethod.GET,
                    beanClass = Handler.class,
                    beanMethod = "getSolicitudes",
                    operation = @Operation(
                            operationId = "listarSolicitudes",
                            summary = "Obtiene el listado de solicitudes pendientes",
                            description = "Requiere un JWT válido en el header `Authorization`. " +
                                    "Disponible solo para usuarios con rol ASESOR.",
                            parameters = {
                                    @io.swagger.v3.oas.annotations.Parameter(
                                            name = "Authorization",
                                            description = "Token JWT en formato: Bearer {token}",
                                            required = true,
                                            example = "Bearer eyJhbGciOiJIUzI1NiJ9..."
                                    )
                            },
                            responses = {
                                    @ApiResponse(
                                            responseCode = "200",
                                            description = "Listado de solicitudes",
                                            content = @Content(
                                                    schema = @Schema(implementation = SolicitudResponseDTO.class)
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400", description = "Error de validación"),
                                    @ApiResponse(responseCode = "401", description = "Token inválido o no proporcionado"),
                                    @ApiResponse(responseCode = "500", description = "Error interno")
                            }
                    )
            )
    })
    public RouterFunction<ServerResponse> routerFunction(Handler handler) {
        return route(POST("/api/v1/solicitud"), handler::loadRequest)
                .andNest(path("/api/v1/solicitud"),
                        route(GET(""), handler::getSolicitudes).filter(authFilter)
                );
    }
}
