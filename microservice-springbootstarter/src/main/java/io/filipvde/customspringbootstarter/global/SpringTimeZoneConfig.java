package io.filipvde.customspringbootstarter.global;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.TimeZone;

@Configuration
public class SpringTimeZoneConfig {

    @PostConstruct
    public void timezoneConfig(@Value("${app.default-timezone:UTC}") final String defaultTimezone) {
        TimeZone.setDefault(TimeZone.getTimeZone(defaultTimezone));
//        TimeZone.setDefault(TimeZone.getTimeZone("Europe/Brussels"));
    }

}