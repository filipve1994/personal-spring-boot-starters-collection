package io.filipvde.customspringbootstarter.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("microservice.swagger")
public class OpenApiProperties {

    private String controllerPackage;

    public String getControllerPackage() {
        return controllerPackage;
    }

    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }
}
