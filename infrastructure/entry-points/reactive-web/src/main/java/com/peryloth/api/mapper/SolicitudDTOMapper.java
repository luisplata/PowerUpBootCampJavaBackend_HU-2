package com.peryloth.api.mapper;

import com.peryloth.api.dto.SolicitudRequestDTO;
import com.peryloth.api.dto.SolicitudResponseDTO;
import com.peryloth.model.estados.Estados;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SolicitudDTOMapper {

    @Mapping(target = "idSolicitud", ignore = true)
    @Mapping(target = "usuario_id", ignore = true)
    @Mapping(target = "tipoPrestamo", expression = "java(toTipoPrestamo(dto.getTipoPrestamoId()))")
    @Mapping(target = "estado", expression = "java(toEstadoPendiente())")
    @Mapping(target = "fechaCreacion", expression = "java(java.time.LocalDateTime.now())")
    Solicitud toEntity(SolicitudRequestDTO dto);

    @Mapping(target = "usuarioId", source = "usuario_id")
    @Mapping(target = "estado", source = "estado.nombre")
    @Mapping(target = "tipoPrestamoNombre", source = "tipoPrestamo.nombre")
    @Mapping(target = "plazoMeses", ignore = true)
    SolicitudResponseDTO toResponseDTO(Solicitud entity);

    // Métodos auxiliares que MapStruct llama
    default TipoPrestamo toTipoPrestamo(String nombre) {
        TipoPrestamo tp = new TipoPrestamo();
        tp.setNombre(nombre);
        return tp;
    }

    default Estados toEstadoPendiente() {
        Estados e = new Estados();
        e.setIdEstado(1L);
        e.setNombre("Pendiente de revisión");
        return e;
    }
}
