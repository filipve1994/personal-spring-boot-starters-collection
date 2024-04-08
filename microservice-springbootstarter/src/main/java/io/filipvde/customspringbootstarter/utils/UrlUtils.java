package io.filipvde.customspringbootstarter.utils;

import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.net.MalformedURLException;
import java.net.URL;

@Slf4j
public class UrlUtils {

    private UrlUtils() {
        //utils
    }


    public static String joinQueryparms(@Nonnull String url, @Nullable String queryparms) {
        if (StringUtils.isEmpty(queryparms)) {
            return url;
        }
        String middle = url.contains("?") ? "&" : "?";
        return url + middle + queryparms;
    }

    public static URL parseUrl(String url) {
        try {
            return new URL(url);
        } catch (MalformedURLException e) {
            log.error("Not a URL: " + url);

            throw new RuntimeException(e);
        }
    }
}
