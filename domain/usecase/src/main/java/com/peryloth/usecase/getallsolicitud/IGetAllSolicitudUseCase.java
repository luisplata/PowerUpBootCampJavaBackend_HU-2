package com.peryloth.usecase.getallsolicitud;

import reactor.core.publisher.Flux;

public interface IGetAllSolicitudUseCase {
    Flux<SolicitudResponseDTO> getAllSolicitud(String token);
}
