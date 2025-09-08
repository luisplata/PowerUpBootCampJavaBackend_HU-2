package com.peryloth.model.solicitud;

import com.peryloth.model.estados.Estados;
import com.peryloth.model.tipoprestamo.TipoPrestamo;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder(toBuilder = true)
public class Solicitud {
    private Long idSolicitud;
    private String usuario_id;           // lo llenaremos con documentoIdentidad
    private TipoPrestamo tipoPrestamo;   // debe traer idTipoPrestamo poblado antes de guardar
    private Estados estado;              // debe traer idEstado poblado antes de guardar
    private Double monto;
    private Integer plazo;
    private String email;
}