package com.peryloth.model.tipoprestamo;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TipoPrestamo {
    private Long idTipoPrestamo;
    private String nombre;
    private BigDecimal montoMinimo;
    private BigDecimal montoMaximo;
    private BigDecimal tasaInteres;
    private Boolean validacionAutomatica;

    @Override
    public String toString() {
        return "TipoPrestamo{" +
                "idTipoPrestamo=" + idTipoPrestamo +
                ", nombre='" + nombre + '\'' +
                ", montoMinimo=" + montoMinimo +
                ", montoMaximo=" + montoMaximo +
                ", tasaInteres=" + tasaInteres +
                ", validacionAutomatica=" + validacionAutomatica +
                '}';
    }
}