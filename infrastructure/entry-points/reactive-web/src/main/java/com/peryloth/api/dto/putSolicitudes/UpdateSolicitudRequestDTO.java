package com.peryloth.api.dto.putSolicitudes;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UpdateSolicitudRequestDTO(
        @NotBlank(message = "El nuevo estado no puede estar vacío")
        String nuevoEstado // Valores esperados: "APROBADO" o "RECHAZADO"
) {
}