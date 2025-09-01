package com.peryloth.usecase.registerloanrequest.command_queue.validations;

import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.registerloanrequest.IGetUserRepository;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

public class ClienteExisteValidation implements SolicitudValidation {

    private final IGetUserRepository getUserRepository;
    private final String identification;
    private final String email;

    private static final Logger logger = Logger.getLogger(ClienteExisteValidation.class.getName());


    public ClienteExisteValidation(IGetUserRepository getUserRepository, String identificacion, String email) {
        this.getUserRepository = getUserRepository;
        this.identification = identificacion;
        this.email = email;
    }


    @Override
    public Mono<Void> validate(Solicitud solicitud) {
        logger.info("Validando usuario con identificación=" + identification + ", email=" + email);

        return getUserRepository.isUserValid(identification, email)
                .flatMap(isValid -> {
                    if (Boolean.FALSE.equals(isValid)) {
                        logger.warning("Usuario no válido: " + identification + ", " + email);
                        return Mono.error(new RuntimeException("User not valid"));
                    }
                    logger.info("Usuario válido: " + identification + ", " + email);
                    return Mono.empty();
                });
    }

}