package com.peryloth.r2dbc.entities;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("estados")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class EstadoEntity {

    @Id
    @Column("id_estado")
    private Long idEstado;

    private String nombre;

    private String descripcion;
}
