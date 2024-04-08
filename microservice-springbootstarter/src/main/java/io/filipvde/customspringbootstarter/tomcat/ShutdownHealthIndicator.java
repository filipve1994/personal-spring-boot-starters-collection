package io.filipvde.customspringbootstarter.tomcat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class ShutdownHealthIndicator implements HealthIndicator {

    @Autowired
    private GracefulShutdownCustomizer shutdownCustomizer;

    @Override
    public Health health() {
        return shutdownCustomizer.isShuttingDown() ? Health.down().build() : Health.up().build();
    }
}
