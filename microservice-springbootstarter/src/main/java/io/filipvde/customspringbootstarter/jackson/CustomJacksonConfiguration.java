package io.filipvde.customspringbootstarter.jackson;

import com.fasterxml.jackson.core.StreamReadConstraints;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JacksonProperties.class)
public class CustomJacksonConfiguration {

    private final JacksonProperties jacksonProperties;

    @Autowired
    public CustomJacksonConfiguration(@Nonnull final JacksonProperties jacksonProperties) {
        this.jacksonProperties = jacksonProperties;
    }


    /**
     * In Jackson version 2.15.0 they introduced a max character length for deserializing content; by default this was set to 5 million characters.
     * However, in some flows we transfer files as Base64 strings, which can easily go past that limit. So, here we augment the default Spring Boot
     * ObjectMapper to set default limit to 20 million characters or whatever the user specified in their property file.
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer customStreamReadConstraints() {
        return builder -> builder.postConfigurer(objectMapper ->
                objectMapper.getFactory().setStreamReadConstraints(StreamReadConstraints.builder().maxStringLength(jacksonProperties.getMaxStringLength()).build()));
    }
}
