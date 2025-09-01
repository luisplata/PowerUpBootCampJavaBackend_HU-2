package com.peryloth.usecase.registerloanrequest;

import reactor.core.publisher.Mono;

public interface IGetUserRepository {
    Mono<Boolean> isUserValid(String id, String email);
}
