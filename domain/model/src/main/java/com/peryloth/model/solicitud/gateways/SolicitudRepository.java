package com.peryloth.model.solicitud.gateways;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface SolicitudRepository {
    Mono<Solicitud> saveSolicitud(Solicitud solicitud);

    Flux<Solicitud> getAllSolicitudes();

    Flux<Solicitud> getAllSolicitudesByEmail(String email);
    Flux<Solicitud> getAllSolicitudesByEmailAndEstado(String email, Estados estados);

    Mono<Solicitud> findById(Long id);
}
