package com.peryloth.model.estados;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Estados {
    private Long idEstado;
    private String nombre;

    @Override
    public String toString() {
        return "Estados{" +
                "idEstado=" + idEstado +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
