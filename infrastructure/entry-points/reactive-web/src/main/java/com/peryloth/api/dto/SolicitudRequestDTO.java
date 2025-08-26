package com.peryloth.api.dto;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class SolicitudRequestDTO {
    private String documentoIdentidad;  // identifica al cliente
    private String tipoPrestamoId;        // referencia al tipo de préstamo ya existente
    private BigDecimal monto;           // monto solicitado
    private Integer plazoMeses;         // plazo en meses
}
