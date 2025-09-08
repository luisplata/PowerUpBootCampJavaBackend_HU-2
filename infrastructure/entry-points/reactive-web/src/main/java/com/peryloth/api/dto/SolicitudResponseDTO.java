package com.peryloth.api.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudResponseDTO {
    private Long idSolicitud;
    private String usuario_id;
    private String estado;
    private BigDecimal monto;
    private Integer plazo;
    private String tipoPrestamoNombre;
    private String email;
}
