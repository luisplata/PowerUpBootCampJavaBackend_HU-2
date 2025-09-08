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
    @Mapping(target = "usuario_id", source = "documentoIdentidad")
    @Mapping(target = "tipoPrestamo", expression = "java(toTipoPrestamo(dto.getTipoPrestamoId()))")
    @Mapping(target = "estado", expression = "java(toEstadoPendiente())")
    @Mapping(target = "monto", source = "monto")
    @Mapping(target = "plazo", source = "plazo")
    Solicitud toEntity(SolicitudRequestDTO dto);

    @Mapping(target = "usuario_id", source = "usuario_id")
    @Mapping(target = "estado", source = "estado.nombre")
    @Mapping(target = "tipoPrestamoNombre", source = "tipoPrestamo.nombre")
    @Mapping(target = "plazo", source = "plazo")
    @Mapping(target = "monto", expression = "java(entity.getMonto() != null ? java.math.BigDecimal.valueOf(entity.getMonto()) : null)")
    SolicitudResponseDTO toResponseDTO(Solicitud entity);

    // Helpers
    default TipoPrestamo toTipoPrestamo(String nombre) {
        TipoPrestamo tp = new TipoPrestamo();
        tp.setNombre(nombre);
        return tp;
    }

    default Estados toEstadoPendiente() {
        Estados e = new Estados();
        e.setIdEstado(1L); // "Pendiente de revisión"
        e.setNombre("Pendiente de revisión");
        return e;
    }
}
