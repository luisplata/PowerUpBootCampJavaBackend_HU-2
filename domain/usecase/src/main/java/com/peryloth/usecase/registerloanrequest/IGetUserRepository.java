package com.peryloth.usecase.registerloanrequest;

import com.peryloth.usecase.getallsolicitud.UsuarioResponseDTO;
import reactor.core.CorePublisher;
import reactor.core.publisher.Mono;

public interface IGetUserRepository {
    Mono<Boolean> isUserValid(String id, String email);

    Mono<Boolean> isTokenValid(String token);

    Mono<UsuarioResponseDTO> getUserByEmail(String email, String token);
}
