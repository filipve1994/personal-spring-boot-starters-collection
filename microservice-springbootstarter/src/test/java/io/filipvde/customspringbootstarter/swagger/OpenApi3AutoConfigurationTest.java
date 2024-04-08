package io.filipvde.customspringbootstarter.swagger;

import io.swagger.v3.oas.models.OpenAPI;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

public class OpenApi3AutoConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(DelegatingWebMvcConfiguration.class, BuildProperties.class, OpenApi3AutoConfiguration.class));

    @Test
    public void testSwaggerAutoConfiguration() {
        contextRunner
                .withPropertyValues("microservice.swagger.controller-package=org.fve.customstarters.config",
                        "microservice.swagger.api-info.title=Test",
                        "microservice.swagger.api-info.description=Test Description",
                        "microservice.swagger.api-info.licence=Internal",
                        "microservice.swagger.api-info.terms-of-service-url=localhost:8080/tos",
                        "microservice.swagger.api-info.licence-url=localhost:8080/licence",
                        "microservice.swagger.api-info.contact.name=FVE",
                        "microservice.swagger.api-info.contact.url=localhost:8080/fve",
                        "microservice.swagger.api-info.contact.email=team-devops@fve.be")
                .run(context -> {
                    assertThat(context).hasNotFailed().hasSingleBean(OpenAPI.class);
                });
    }
}
