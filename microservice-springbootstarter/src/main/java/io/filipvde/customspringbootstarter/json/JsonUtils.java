package io.filipvde.customspringbootstarter.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.annotation.Nonnull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.fve.customstarters.model.exceptions.JsonMappingException;

@UtilityClass
@Slf4j
public final class JsonUtils {

    private static ObjectMapper mapper = JsonMapper.builder()
            .findAndAddModules()
            .disable(
                    DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES
            )
            .enable(
                    SerializationFeature.INDENT_OUTPUT
            )
            .build();

    private static ObjectWriter jsonWriter = mapper.writerWithDefaultPrettyPrinter();

    @Nonnull
    public static String toJson(@Nonnull final Object obj) {
        try {
            return jsonWriter.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert Object to Json: " + e.toString());
            throw new JsonMappingException(e);
        }
    }

    @Nonnull
    public static <C> C toObject(@Nonnull final String data, @Nonnull final Class<C> dataClzz) {
        try {
            return mapper.readValue(data, dataClzz);
        } catch (JsonProcessingException e) {
            log.warn("Failed to convert Json to Object: " + e.toString());
            throw new JsonMappingException(e);
        }
    }

    @Nonnull
    public ObjectMapper getConfiguredObjectMapper() {
        return mapper;
    }
}
