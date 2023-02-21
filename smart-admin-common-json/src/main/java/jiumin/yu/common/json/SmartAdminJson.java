package jiumin.yu.common.json;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author Yujiumin
 * @date 2022/11/20
 */
public class SmartAdminJson {

    private static final ObjectMapper OBJECT_MAPPER = SmartAdminObjectMapper.objectMapper;

    public static String toJsonString(Object object) throws JsonProcessingException {
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Number) {
            return object.toString();
        } else {
            return OBJECT_MAPPER.writeValueAsString(object);
        }
    }

    public static String toJsonString(Object object, boolean throwEx) {
        if (object instanceof String) {
            return (String) object;
        } else if (object instanceof Number) {
            return object.toString();
        } else {
            try {
                return OBJECT_MAPPER.writeValueAsString(object);
            } catch (JsonProcessingException ex) {
                if (throwEx) {
                    throw new RuntimeException(ex);
                }
                return null;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T toBean(String content, Class<T> clazz, boolean throwEx) {
        try {
            if (clazz.isAssignableFrom(String.class)) {
                return (T) clazz;
            } else if (clazz.isAssignableFrom(Number.class)) {
                return (T) clazz;
            } else {
                return OBJECT_MAPPER.readValue(content, clazz);
            }
        } catch (JsonProcessingException ex) {
            if (throwEx) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

    public static <T> T toBean(String content, TypeReference<T> typeReference, boolean throwEx) {
        try {
            OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            return OBJECT_MAPPER.readValue(content, typeReference);
        } catch (JsonProcessingException ex) {
            if (throwEx) {
                throw new RuntimeException(ex);
            }
        }
        return null;
    }

}
