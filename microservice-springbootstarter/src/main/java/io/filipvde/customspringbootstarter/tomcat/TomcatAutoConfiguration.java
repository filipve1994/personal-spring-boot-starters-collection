package io.filipvde.customspringbootstarter.tomcat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(GracefulShutdownCustomizer.class)
public class TomcatAutoConfiguration {

    private GracefulShutdownCustomizer customizer;

    @Autowired
    public TomcatAutoConfiguration(GracefulShutdownCustomizer customizer) {
        this.customizer = customizer;
    }

    @Bean
    public WebServerFactoryCustomizer tomcatCustomizer() {
        return container -> {
            if (container instanceof TomcatServletWebServerFactory) {
                ((TomcatServletWebServerFactory) container)
                        .addConnectorCustomizers(customizer);
            }
        };
    }

}
