package io.filipvde.customspringbootstarter.tomcat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;

import static org.assertj.core.api.Assertions.assertThat;

public class TomcatAutoConfigurationTest {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(TomcatAutoConfiguration.class));

    @Test
    public void testSystemProperty() {
        contextRunner
                .run(context -> assertThat(context).hasSingleBean(WebServerFactoryCustomizer.class));
    }
}
