package com.peryloth.sqspublisher;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsAsyncClient;

@Configuration
public class AwsConfig {

    @Bean
    public SqsAsyncClient sqsAsyncClient() {
        return SqsAsyncClient.builder()
                .region(Region.US_EAST_1) // cámbialo según tu región
                //.endpointOverride(URI.create("http://localhost:4566")) // si usas Localstack
                .build();
    }
}
