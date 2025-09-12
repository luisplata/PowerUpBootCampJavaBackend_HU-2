package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.estados.EstadosConstantes;
import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadGateway;
import com.peryloth.usecase.endeudamiento.CalcularCapacidadRequest;
import com.peryloth.usecase.registerloanrequest.command_queue.UsuarioValidationQueue;
import com.peryloth.usecase.registerloanrequest.command_queue.validations.*;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class RegisterLoanRequestUseCase implements IRegisterLoanRequest {

    private final TipoPrestamoRepository tipoPrestamoReactRepository;
    private final SolicitudRepository solicitudRepository;
    private final EstadosRepository estadosRepository;
    private final IGetUserRepository getUserRepository;
    private final CalcularCapacidadGateway calcularCapacidadGateway;

    @Override
    public Mono<Solicitud> registerLoanRequest(Solicitud solicitud, String identification, String email, String token) {
        return UsuarioValidationQueue.builder()
                .withValidation(new DatosCliente(identification, email))
                .withValidation(new ClienteExisteValidation(getUserRepository, identification, email))
                .withValidation(new DatosRequeridos())
                .withValidation(new MontoValidation())
                .withValidation(new PlazoValidation())
                .build()
                .validate(solicitud)
                .then(tipoPrestamoReactRepository.findByName(solicitud.getTipoPrestamo().getNombre())
                        .doOnNext(saved -> System.out.println("Tipo Prestamo: " + saved))
                        .flatMap(tipoPrestamo -> {
                            solicitud.setTipoPrestamo(tipoPrestamo);
                            return estadosRepository.getEstadoById(solicitud.getEstado().getIdEstado())
                                    .doOnNext(saved -> System.out.println("Estado found: " + saved))
                                    .flatMap(estados -> {
                                        solicitud.setEstado(estados);
                                        return getUserRepository
                                                .getUserByEmail(solicitud.getEmail(), token)
                                                .switchIfEmpty(Mono.error(new IllegalArgumentException("No se encontro el usaurio")))
                                                .doOnNext(u -> System.out.println("user " + u))
                                                .flatMap(usuario ->
                                                        solicitudRepository.getAllSolicitudesByEmail(solicitud.getEmail())
                                                                .doOnNext(e -> System.out.println("s " + e))
                                                                .collectList()
                                                                .doOnNext(ws -> System.out.println("collect " + ws.size()))
                                                                .map(solicitudes -> solicitudes.stream()
                                                                        .filter(s -> "Activo".equalsIgnoreCase(s.getEstado().getNombre()))
                                                                        .map(s -> new CalcularCapacidadRequest.PrestamoActivo(
                                                                                s.getMonto(),
                                                                                s.getTipoPrestamo().getTasaInteres().doubleValue(),
                                                                                s.getPlazo()
                                                                        ))
                                                                        .toList())
                                                                .doOnNext(es -> System.out.println(" prestamos " + es.size()))
                                                                //TODO: what's was that
                                                                .flatMap(solicitudesActivas ->
                                                                        {
                                                                            CalcularCapacidadRequest request = new CalcularCapacidadRequest(
                                                                                    usuario.salario_base(),
                                                                                    solicitudesActivas,
                                                                                    new CalcularCapacidadRequest.NuevoPrestamo(
                                                                                            solicitud.getMonto(),
                                                                                            solicitud.getTipoPrestamo().getTasaInteres(),
                                                                                            solicitud.getPlazo()
                                                                                    )
                                                                            );
                                                                            return calcularCapacidadGateway.calcular(request)
                                                                                    .flatMap(result -> estadosRepository.getEstadoByNombre(result.decision())
                                                                                            .flatMap(newEstado -> {
                                                                                                solicitud.setEstado(newEstado);
                                                                                                return Mono.just(solicitud);
                                                                                            }).flatMap(solici -> solicitudRepository.saveSolicitud(solici)));
                                                                        }
                                                                ).switchIfEmpty(
                                                                        Mono.defer(() -> {
                                                                            System.out.println("No se encontraron solicitudes previas, guardando de todas formas...");
                                                                            CalcularCapacidadRequest request = new CalcularCapacidadRequest(
                                                                                    usuario.salario_base(),
                                                                                    new ArrayList<>(),
                                                                                    new CalcularCapacidadRequest.NuevoPrestamo(
                                                                                            solicitud.getMonto(),
                                                                                            solicitud.getTipoPrestamo().getTasaInteres(),
                                                                                            solicitud.getPlazo()
                                                                                    )
                                                                            );
                                                                            return calcularCapacidadGateway.calcular(request)
                                                                                    .flatMap(result -> estadosRepository.getEstadoByNombre(result.decision())
                                                                                            .flatMap(newEstado -> {
                                                                                                solicitud.setEstado(newEstado);
                                                                                                return Mono.just(solicitud);
                                                                                            }).flatMap(solici -> solicitudRepository.saveSolicitud(solici)));
                                                                        })
                                                                )
                                                );
                                    });
                        }).doOnNext(saved -> System.out.println("Registering loan request: " + saved)));
    }
}
