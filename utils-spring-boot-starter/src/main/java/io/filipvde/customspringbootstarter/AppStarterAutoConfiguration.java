package io.filipvde.customspringbootstarter;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Import;

import java.util.Locale;

@AutoConfiguration
@Import({

})
//@EnableScheduling
//@ComponentScan("io.filipvde.customspringbootstarter.config")
public class AppStarterAutoConfiguration {

    /**
     * Let's not depend on what the JVM has to say about it, fix it to 'en'
     */
    public AppStarterAutoConfiguration() {
        Locale.setDefault(Locale.ENGLISH);
    }
}
