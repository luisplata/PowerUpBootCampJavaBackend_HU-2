package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterLoanRequestUseCase implements IRegisterLoanRequest {

    private final TipoPrestamoRepository tipoPrestamoReactRepository;
    private final SolicitudRepository solicitudRepository;
    private final EstadosRepository estadosRepository;

    @Override
    public Mono<Solicitud> registerLoanRequest(Solicitud solicitud) {
        //TODO implement validations with a commandQuery pattern

        return tipoPrestamoReactRepository.findByName(solicitud.getTipoPrestamo().getNombre())
                .doOnNext(saved -> System.out.println("Registering loan request: " + saved))
                .flatMap(tipoPrestamo -> {
                    solicitud.setTipoPrestamo(tipoPrestamo);
                    return estadosRepository.getEstadoById(solicitud.getEstado().getIdEstado())
                            .doOnNext(saved -> System.out.println("Estado found: " + saved))
                            .flatMap(estados -> {
                                solicitud.setEstado(estados);
                                return solicitudRepository.saveSolicitud(solicitud);
                            });
                }).doOnNext(saved -> System.out.println("Registering loan request: " + saved));
    }
}
