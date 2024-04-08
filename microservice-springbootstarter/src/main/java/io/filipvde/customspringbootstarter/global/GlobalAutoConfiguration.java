package io.filipvde.customspringbootstarter.global;


import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

//@AutoConfiguration
@Configuration
@Import({
        AppConfig.class,
        SpringTimeZoneConfig.class,

})
public class GlobalAutoConfiguration {
}
