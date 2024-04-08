package io.filipvde.customspringbootstarter.swagger;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import jakarta.annotation.Nonnull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ConditionalOnProperty("microservice.swagger.controller-package")
@EnableConfigurationProperties({ ApiInfoProperties.class, OpenApiProperties.class })
public class OpenApi3AutoConfiguration implements WebMvcConfigurer {

  @Autowired
  private ApiInfoProperties apiInfoProperties;

  @Autowired
  private OpenApiProperties openApi3Properties;

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
      .info(apiInfo())
      .externalDocs(new ExternalDocumentation()
        .description("FVE MicroService Documentation"));
  }

  @Bean
  @Nonnull
  public Info apiInfo() {
    return apiInfoProperties.buildApiInfo();
  }

}
