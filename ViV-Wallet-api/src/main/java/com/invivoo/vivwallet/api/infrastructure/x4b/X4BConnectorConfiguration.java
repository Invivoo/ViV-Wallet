package com.invivoo.vivwallet.api.infrastructure.x4b;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@Configuration
public class X4BConnectorConfiguration {

    public static final String X4B_CONNECTOR_OBJECT_MAPPER = "x4b-connector-object-mapper";
    public static final String X4B_CONNECTOR_REST_TEMPLATE = "x4b-connector-rest-template";

    @Bean(X4B_CONNECTOR_OBJECT_MAPPER)
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectMapper.registerModule(new JavaTimeModule());

        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        objectMapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);

        return objectMapper;
    }

    @Bean(X4B_CONNECTOR_REST_TEMPLATE)
    public RestTemplate restTemplate(@Qualifier(X4B_CONNECTOR_OBJECT_MAPPER) ObjectMapper objectMapper) {
        return new RestTemplateBuilder().messageConverters(new StringHttpMessageConverter(), new MappingJackson2HttpMessageConverter(objectMapper))
                                        .build();
    }

}
