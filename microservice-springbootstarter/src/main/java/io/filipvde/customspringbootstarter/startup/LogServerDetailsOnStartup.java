package io.filipvde.customspringbootstarter.startup;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.context.ServletWebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Optional;

// https://www.baeldung.com/spring-boot-running-port
@Service
@Slf4j
public class LogServerDetailsOnStartup {


    @Autowired
    private Environment environment;
    private static final String SERVER_SSL_KEY_STORE = "server.ssl.key-store";
    private static final String SERVER_PORT = "server.port";
    private static final String SERVER_SERVLET_CONTEXT_PATH = "server.servlet.context-path";
    private static final String SPRINGDOC_SWAGGER_UI_PATH = "springdoc.swagger-ui.path";
    private static final String DEFAULT_PROFILE = "default";


    private static final String H2_DB_CONSOLE_ENABLED = "spring.h2.console.enabled";
    private static final String H2_DB_CONSOLE_PATH = "spring.h2.console.path";

    private int port;

    public int getPort() {
        return port;
    }

    @EventListener
    public void onApplicationEvent(final ServletWebServerInitializedEvent event) {
        log.info("LogServerDetailsOnStartup: onApplicationEvent");
        port = event.getWebServer().getPort();
        log.info("LogServerDetailsOnStartup: onApplicationEvent: port random is: " + port);

        //

        String protocol = Optional.ofNullable(environment.getProperty(SERVER_SSL_KEY_STORE)).map(key -> "https").orElse("http");
        String host = getServerIP();
//        String serverPort = Optional.ofNullable(environment.getProperty(SERVER_PORT)).orElse("8080");
        String serverPort = String.valueOf(port);
        String contextPath = Optional.ofNullable(environment.getProperty(SERVER_SERVLET_CONTEXT_PATH)).orElse("");

        String[] activeProfiles = Optional.of(environment.getActiveProfiles()).orElse(new String[0]);

        String activeProfile = (activeProfiles.length > 0) ? String.join(",", activeProfiles) : DEFAULT_PROFILE;

        String swaggerUI = Optional.ofNullable(environment.getProperty(SPRINGDOC_SWAGGER_UI_PATH)).orElse("/swagger-ui/index.html");

        String h2ConsoleUiPath = Optional.ofNullable(environment.getProperty(H2_DB_CONSOLE_PATH)).orElse("/h2-console");
        String h2Enabled = Optional.ofNullable(environment.getProperty(H2_DB_CONSOLE_ENABLED)).orElse("false");
        boolean h2EnabledCheck = h2Enabled.equalsIgnoreCase("true");

        log.info("h2enabled: {}, h2enabledCheck: {}, h2ConsoleUiPath: {}: ", h2Enabled, h2EnabledCheck, h2ConsoleUiPath);

        log.info(
                """
                        \n
                        Access Swagger UI URL: {}://{}:{}{}{}
                        Active Profile: {}
                        """,
                protocol, host, serverPort, contextPath, swaggerUI,
                activeProfile
        );

        if (h2EnabledCheck) {
            log.info(
                    """
                            \n
                            Access Swagger UI URL: {}://{}:{}{}{}
                            Access h2 UI URL: {}://{}:{}{}{}
                            Active Profile: {}
                            """,
                    protocol, host, serverPort, contextPath, swaggerUI,
                    protocol, host, serverPort, contextPath, h2ConsoleUiPath,
                    activeProfile
            );
        }

    }


    private String getServerIP() {
        try {
            return InetAddress.getLocalHost().getHostAddress();

        } catch (UnknownHostException e) {
            log.error("Error resolving host address", e);
            return "unknown";
        }
    }
}