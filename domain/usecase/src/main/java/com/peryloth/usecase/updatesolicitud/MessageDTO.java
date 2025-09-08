package com.peryloth.usecase.updatesolicitud;

import lombok.Builder;

@Builder
public record MessageDTO(String to, String subject, String body) {
}
