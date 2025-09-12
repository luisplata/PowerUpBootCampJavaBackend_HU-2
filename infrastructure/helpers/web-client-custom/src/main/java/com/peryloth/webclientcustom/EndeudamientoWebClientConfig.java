package com.peryloth.webclientcustom;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class EndeudamientoWebClientConfig {

    @Bean
    @Qualifier("endeudamiento")
    public WebClient endeudamientoWebClient(
            WebClient.Builder builder,
            @Value("${endeudamiento-service.base-url}") String baseUrl) {
        System.out.println(">>> ENDEUDAMIENTO BASE URL endeudamiento-service.base-url = " + baseUrl);
        return builder.baseUrl(baseUrl).build();
    }
}