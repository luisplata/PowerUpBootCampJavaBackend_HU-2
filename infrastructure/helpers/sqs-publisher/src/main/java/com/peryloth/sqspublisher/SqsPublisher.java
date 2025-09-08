package com.peryloth.sqspublisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.peryloth.model.solicitud.Solicitud;
import com.peryloth.usecase.updatesolicitud.ISqsPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

@Component
@RequiredArgsConstructor
public class SqsPublisher implements ISqsPublisher {

    private final SqsAsyncClient sqsAsyncClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Ideal: inyectar desde application.yml
    private final String queueUrl = "https://sqs.us-east-1.amazonaws.com/326623589662/solicitudes-sqs";

    @Override
    public Mono<Void> publishEstadoSolicitud(Solicitud solicitud) {
        return Mono.fromCallable(() -> toJson(solicitud))
                .flatMap(json -> Mono.fromFuture(() ->
                        sqsAsyncClient.sendMessage(SendMessageRequest.builder()
                                .queueUrl(queueUrl)
                                .messageBody(json)
                                .build())
                ))
                .doOnSuccess(resp -> System.out.println("Mensaje enviado a SQS: " + resp.messageId()))
                .doOnError(err -> System.err.println("Error enviando mensaje a SQS: " + err.getMessage()))
                .then();
    }

    private String toJson(Solicitud solicitud) throws JsonProcessingException {
        return objectMapper.writeValueAsString(solicitud);
    }
}
