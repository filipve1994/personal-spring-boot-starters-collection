package io.filipvde.customspringbootstarter;

import io.filipvde.customspringbootstarter.utils.CollectionUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;

import java.util.Enumeration;
import java.util.List;
import java.util.Optional;

public class SecurityHeaders {

    public static final String HDR_BEARER_START = "Bearer ";
    public static final String HDR_BASIC_START = "BASIC ";

    public static final String HDR_AUTHORIZATION = HttpHeaders.AUTHORIZATION;

    private SecurityHeaders() {
        // utility
    }

    public static Optional<String> findBearerToken(@Nonnull final HttpRequest httpRequest) {
        List<String> authHeaders = httpRequest.getHeaders().get(SecurityHeaders.HDR_AUTHORIZATION);
        if (authHeaders == null) {
            return Optional.empty();
        }

        return authHeaders.stream()
                .filter(hv -> StringUtils.startsWithIgnoreCase(hv, SecurityHeaders.HDR_AUTHORIZATION))
                .map(hv -> hv.substring(HDR_BEARER_START.length()))
                .map(String::trim)
                .findFirst();
    }

    public static Optional<String> findBearerToken(@Nonnull final HttpServletRequest httpRequest) {
        Enumeration<String> authHeaders = httpRequest.getHeaders(SecurityHeaders.HDR_AUTHORIZATION);
        if (authHeaders == null) {
            return Optional.empty();
        }

        return CollectionUtils.enumerationAsStream(authHeaders)
                .filter(hv -> StringUtils.startsWithIgnoreCase(hv, SecurityHeaders.HDR_AUTHORIZATION))
                .map(hv -> hv.substring(HDR_BEARER_START.length()))
                .map(String::trim)
                .findFirst();
    }


}
