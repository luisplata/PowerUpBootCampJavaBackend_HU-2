package com.peryloth.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
@Table("solicitud")
public class SolicitudEntity {

    @Id
    @Column("id_solicitud")
    private Long idSolicitud;

    @Column("usuario_id")
    private Long usuarioId;

    @Column("tipo_prestamo_id")
    private Long tipoPrestamoId;

    @Column("estado_id")
    private Long estadoId;

    @Column("monto")
    private Double monto;

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;
}
