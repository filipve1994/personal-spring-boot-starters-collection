package io.filipvde.customspringbootstarter.jackson;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "microservice.jackson")
public class JacksonProperties {

  private Integer maxStringLength = 20_000_000;
}
