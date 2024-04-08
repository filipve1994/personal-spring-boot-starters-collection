package io.filipvde.customspringbootstarter.tomcat;

import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.Connector;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.context.event.ContextClosedEvent;

import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.RETURNS_DEEP_STUBS;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class GracefulShutdownCustomizerTest {
    @Test
    public void testGracefulShutdown_WithoutConnector() {
        log.info("testGracefulShutdown_WithoutConnector -start");

        ContextClosedEvent contextClosedEvent = mock(ContextClosedEvent.class);
        TomcatProperties properties = new TomcatProperties();
        GracefulShutdownCustomizer customizer = new GracefulShutdownCustomizer(properties);
        customizer.onApplicationEvent(contextClosedEvent);
        assertThat(customizer.isShuttingDown()).isTrue();

        log.info("testGracefulShutdown_WithoutConnector -stop");
    }

    @Test
    public void testGracefulShutdown_WithoutProperConnector() {
        log.info("testGracefulShutdown_WithoutProperConnector -start");

        ContextClosedEvent contextClosedEvent = mock(ContextClosedEvent.class);
        TomcatProperties properties = new TomcatProperties();
        GracefulShutdownCustomizer customizer = new GracefulShutdownCustomizer(properties);

        Connector connector = mock(Connector.class);
        when(connector.getProtocolHandler()).thenReturn(null);
        customizer.customize(connector);

        customizer.onApplicationEvent(contextClosedEvent);

        assertThat(customizer.isShuttingDown()).isTrue();

        log.info("testGracefulShutdown_WithoutProperConnector -stop");
    }


    @Test
    public void testGracefulShutdown() throws Exception {
        log.info("testGracefulShutdown -start");

        ContextClosedEvent contextClosedEvent = mock(ContextClosedEvent.class);
        ThreadPoolExecutor executor = mock(ThreadPoolExecutor.class);

        TomcatProperties properties = new TomcatProperties();
        properties.setSleepBeforePause(2);
        properties.setShutdownWaitTime(2);

        Connector connector = mock(Connector.class, RETURNS_DEEP_STUBS);
        when(connector.getProtocolHandler().getExecutor()).thenReturn(executor);

        GracefulShutdownCustomizer customizer = new GracefulShutdownCustomizer(properties);
        customizer.customize(connector);

        customizer.onApplicationEvent(contextClosedEvent);

        verify(connector).pause();
        verify(executor).shutdown();
        verify(executor).awaitTermination(2, TimeUnit.SECONDS);

        assertThat(customizer.isShuttingDown()).isTrue();

        log.info("testGracefulShutdown -stop");
    }
}