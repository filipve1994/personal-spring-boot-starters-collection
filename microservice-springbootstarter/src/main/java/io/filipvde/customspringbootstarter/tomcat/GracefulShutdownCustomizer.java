package io.filipvde.customspringbootstarter.tomcat;

import jakarta.annotation.Nonnull;
import org.apache.catalina.connector.Connector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Component
@EnableConfigurationProperties(TomcatProperties.class)
public class GracefulShutdownCustomizer implements TomcatConnectorCustomizer, ApplicationListener<ContextClosedEvent> {
    private static final Logger LOG = LoggerFactory.getLogger(GracefulShutdownCustomizer.class);

    private volatile Connector connector;
    private boolean isShuttingDown;

    @Autowired
    private TomcatProperties tomcatProperties;

    @Override
    public void customize(@Nonnull final Connector connector) {
        this.connector = connector;
    }

    @Override
    public void onApplicationEvent(@Nonnull final ContextClosedEvent event) {
        isShuttingDown = true;
        if (connector == null) {
            LOG.warn("Skipping gracefull shutdown - no valid connector found");
            return;
        }
        if (connector.getProtocolHandler() == null) {
            LOG.warn("Skipping gracefull shutdown - no valid protocolhandler found");
            return;
        }
        try {
            LOG.info("Starting gracefull shutdown");
            Thread.sleep(((long)tomcatProperties.getSleepBeforePause()) * 1000);
            this.connector.pause();
            final Executor executor = this.connector.getProtocolHandler().getExecutor();
            if (executor instanceof ThreadPoolExecutor) {
                final ThreadPoolExecutor threadPoolExecutor = (ThreadPoolExecutor) executor;
                threadPoolExecutor.shutdown();
                if (!threadPoolExecutor.awaitTermination(tomcatProperties.getShutdownWaitTime(), TimeUnit.SECONDS)) {
                    LOG.warn("Tomcat thread pool did not shut down gracefully within "
                            + "{} seconds. Proceeding with forceful shutdown", tomcatProperties.getShutdownWaitTime());
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        /*
         * Disabled, kills any further logging in all unittests,
         * which in turn fails the LoggingRequestInterceptorTest, that test needs working logging.
         * Be aware, this will for sure fail on TFS, but might work locally, depending on order.
        // Shutdown logging to free files
        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();
        loggerContext.stop();
        */
    }

    public boolean isShuttingDown() {
        return isShuttingDown;
    }
}
