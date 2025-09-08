package com.peryloth.usecase.updatesolicitud;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.estados.gateways.EstadosRepository;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.solicitud.gateways.SolicitudRepository;
import com.peryloth.model.tipoprestamo.gateways.TipoPrestamoRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * Caso de uso: Aprobar o Rechazar una solicitud de crédito.
 * HU-6: El asesor toma la decisión final, y se envía un evento a SQS
 * para que el solicitante reciba notificación por correo electrónico.
 */
@RequiredArgsConstructor
public class UpdateSolicitudUseCase implements IUpdateSolicitudUseCase {

    private final SolicitudRepository solicitudRepository;
    private final TipoPrestamoRepository tipoPrestamoRepository;
    private final EstadosRepository estadosRepository;
    private final ISqsPublisher sqsPublisher;

    @Override
    public Mono<Solicitud> updateSolicitud(Long solicitudId, String nuevoEstado) {
        System.out.println(
                String.format("Iniciando actualización de solicitud [%d] al estado [%s]", solicitudId, nuevoEstado)
        );

        return solicitudRepository.findById(solicitudId)
                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                        "Solicitud no encontrada con ID: " + solicitudId)))
                .flatMap(this::hydrateSolicitud)
                .flatMap(solicitud ->
                        estadosRepository.getEstadoByNombre(nuevoEstado.toUpperCase())
                                .switchIfEmpty(Mono.error(new IllegalArgumentException(
                                        "Estado inválido: " + nuevoEstado)))
                                .flatMap(estado -> actualizarSolicitud(solicitud, estado))
                )
                .doOnError(e -> System.out.println(
                        String.format("Error al actualizar la solicitud [%d]: %s", solicitudId, e.getMessage())
                ));
    }

    private Mono<Solicitud> actualizarSolicitud(Solicitud solicitud, Estados nuevoEstado) {
        solicitud.setEstado(nuevoEstado);

        return solicitudRepository.saveSolicitud(solicitud)
                .doOnNext(saved -> System.out.println(
                        String.format("Solicitud [%d] actualizada a [%s]",
                                saved.getIdSolicitud(), saved.getEstado().getNombre())
                ))
                .flatMap(saved ->
                        sqsPublisher.publishEstadoSolicitud(saved)
                                .doOnSuccess(v -> System.out.println(
                                        String.format("Evento enviado a SQS para solicitud [%d]",
                                                saved.getIdSolicitud())
                                ))
                                .thenReturn(saved)
                );
    }

    private Mono<Solicitud> hydrateSolicitud(Solicitud solicitud) {
        if (solicitud.getEstado() == null || solicitud.getTipoPrestamo() == null) {
            return Mono.error(new IllegalStateException("La solicitud no tiene relaciones cargadas (estado o tipoPrestamo nulos)"));
        }

        return Mono.zip(
                estadosRepository.getEstadoById(solicitud.getEstado().getIdEstado()),
                tipoPrestamoRepository.findById(solicitud.getTipoPrestamo().getIdTipoPrestamo())
        ).map(tuple -> {
            solicitud.setEstado(tuple.getT1());
            solicitud.setTipoPrestamo(tuple.getT2());
            return solicitud;
        });
    }


}
