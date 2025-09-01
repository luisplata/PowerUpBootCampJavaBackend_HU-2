package com.peryloth.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("solicitud")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class SolicitudEntity {

    @Id
    @Column("id_solicitud")
    private Long idSolicitud;

    @Column("monto")
    private BigDecimal monto;

    @Column("plazo")
    private Integer plazo; // <- requerido por tu DDL original

    @Column("email")
    private String email;  // <- si tu DDL lo tiene NOT NULL

    @Column("id_estado")
    private Long estadoId; // FK

    @Column("id_tipo_prestamo")
    private Long tipoPrestamoId; // FK

    @Column("fecha_creacion")
    private LocalDateTime fechaCreacion;
}
