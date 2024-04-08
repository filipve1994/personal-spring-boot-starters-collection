package io.filipvde.customspringbootstarter.caching;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@ConditionalOnProperty(name = "app.config.caching.enabled", matchIfMissing = true)
@Configuration
@Profile("!test")
@EnableCaching
public class EnableCachingConfig {
}
