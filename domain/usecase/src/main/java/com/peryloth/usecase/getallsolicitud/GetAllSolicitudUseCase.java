package com.peryloth.usecase.getallsolicitud;

import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.usecase.registerloanrequest.IGetUserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;

@RequiredArgsConstructor
public class GetAllSolicitudUseCase implements IGetAllSolicitudUseCase {

    private final IGetUserRepository getUserRepository;

    private final SolicitudRepository solicitudRepository;

    @Override
    public Flux<SolicitudResponseDTO> getAllSolicitud(String token) {
        return solicitudRepository.getAllSolicitudes()
                .flatMapMany(Flux::fromIterable) // List<Solicitud> → Flux<Solicitud>
                .flatMap(solicitud ->
                        getUserRepository.getUserByEmail(solicitud.getEmail(), token)
                                .map(usuario -> new SolicitudResponseDTO(
                                        solicitud,
                                        usuario.nombre(),
                                        usuario.email(),
                                        usuario.salario_base()
                                ))
                )
                .switchIfEmpty(Flux.error(new IllegalArgumentException("No hay solicitudes registradas")));
    }
}
