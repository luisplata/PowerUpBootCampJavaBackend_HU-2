package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.peryloth.usecase.registerloanrequest.command_queue.UsuarioValidationQueue;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterLoanRequestUseCase implements IRegisterLoanRequest {

    private final TipoPrestamoRepository tipoPrestamoReactRepository;
    private final SolicitudRepository solicitudRepository;
    private final EstadosRepository estadosRepository;
    private final IGetUserRepository getUserRepository;

    @Override
    public Mono<Solicitud> registerLoanRequest(Solicitud solicitud, String identification, String email) {
        return UsuarioValidationQueue.builder()
                .withValidation(new DatosCliente(identification, email))
                .withValidation(new ClienteExisteValidation(getUserRepository, identification, email))
                .withValidation(new DatosRequeridos())
                .withValidation(new MontoValidation())
                .withValidation(new PlazoValidation())
                .build()
                .validate(solicitud)
                .then(tipoPrestamoReactRepository.findByName(solicitud.getTipoPrestamo().getNombre())
                        .doOnNext(saved ->
                                System.out.println("Registering loan request: " + saved))
                        .flatMap(tipoPrestamo -> {
                            solicitud.setTipoPrestamo(tipoPrestamo);
                            return estadosRepository.getEstadoById(solicitud.getEstado().getIdEstado())
                                    .doOnNext(saved -> System.out.println("Estado found: " + saved))
                                    .flatMap(estados -> {
                                        solicitud.setEstado(estados);
                                        return getUserRepository.isUserValid(identification, email)
                                                .flatMap(isValid -> {
                                                    if (Boolean.FALSE.equals(isValid)) {
                                                        return Mono.error(new RuntimeException("User not valid"));
                                                    }
                                                    return solicitudRepository.saveSolicitud(solicitud);
                                                });
                                    });
                        }).doOnNext(saved -> System.out.println("Registering loan request: " + saved)));
    }
}
