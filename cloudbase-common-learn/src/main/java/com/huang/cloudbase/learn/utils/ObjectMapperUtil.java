package com.huang.cloudbase.learn.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * used for transform object
 *
 * @author huangjunbiao
 */
public class ObjectMapperUtil {

    private ObjectMapperUtil() {
    }

    private static final Logger logger = LoggerFactory.getLogger(ObjectMapperUtil.class);

    private static final ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    public static String readValueAsString(String value) {
        String result;
        try {
            result = objectMapper.readValue(value, String.class);
        } catch (JsonProcessingException e) {
            logger.warn("转为String失败");
            return null;
        }
        return result;
    }

    public static Map<String, Object> readValueAsMap(String value) {
        Map<String, Object> map = null;
        try {
            map = objectMapper.readValue(value, objectMapper.getTypeFactory()
                                                    .constructMapType(HashMap.class, String.class, Object.class));
        } catch (JsonProcessingException e) {
            logger.warn("转为Map<String, Object>失败");
        }
        return map;
    }

    public static JsonNode readValueToJsonNode(String value) {
        try {
            return objectMapper.readTree(value);
        } catch (JsonProcessingException e) {
            logger.warn("转化为JsonNode失败");
        }
        return null;
    }

    /**
     * 将值转为Java类，需要注意的是若此次转换不匹配或失败则返回null，调用时需甄别是否有影响
     *
     * @param value     值
     * @param className 类名
     * @return object对象
     */
    public static Object readValueToJavaObject(String value, String className) {
        Object result = null;
        try {
            result = objectMapper.readValue(value, Class.forName(className));
        } catch (JsonProcessingException e) {
            logger.warn("转化为{}失败", className, e);
        } catch (ClassNotFoundException e) {
            logger.warn("找不到类{}", className, e);
        }
        if (result == null && StringUtils.hasText(value)) {
            logger.warn("此次转换为{}失败，原结果为{}，将返回null", className, value);
        }
        return result;
    }

    /**
     * 将值转为Java类，需要注意的是若此次转换不匹配或失败则返回null，调用时需甄别是否有影响
     *
     * @param value        值
     * @param responseType 结果类
     * @param <T> 返回是个泛型
     * @return T结果
     */
    public static <T> T readValueToJavaObject(String value, Class<T> responseType) {
        T result = null;
        try {
            result = objectMapper.readValue(value, responseType);
        } catch (JsonProcessingException e) {
            logger.warn("json转对象失败: {}", e.toString());
        }
        if (result == null && StringUtils.hasText(value)) {
            logger.warn("此次转换为{}失败，原结果为{}，将返回null", responseType, value);
        }
        return result;
    }
}
