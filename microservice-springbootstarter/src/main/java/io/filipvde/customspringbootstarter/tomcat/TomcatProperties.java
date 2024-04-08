package io.filipvde.customspringbootstarter.tomcat;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "microservice.tomcat")
public class TomcatProperties {

    private int sleepBeforePause = 10;
    private int shutdownWaitTime = 10;

    public int getSleepBeforePause() {
        return sleepBeforePause;
    }

    public void setSleepBeforePause(int sleepBeforePause) {
        this.sleepBeforePause = sleepBeforePause;
    }

    public int getShutdownWaitTime() {
        return shutdownWaitTime;
    }

    public void setShutdownWaitTime(int shutdownWaitTime) {
        this.shutdownWaitTime = shutdownWaitTime;
    }
}
