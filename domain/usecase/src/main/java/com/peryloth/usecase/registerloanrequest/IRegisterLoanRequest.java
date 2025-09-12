package com.peryloth.usecase.registerloanrequest;

import com.peryloth.model.solicitud.Solicitud;
import reactor.core.publisher.Mono;

public interface IRegisterLoanRequest {
    Mono<Solicitud> registerLoanRequest(Solicitud solicitud, String identification, String email, String token);
}
