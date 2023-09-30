package com.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.demo.model.OperationResponse;

public class JsonUtil {

    private static final ObjectMapper MAPPER;

    static {
        MAPPER = new ObjectMapper();
        MAPPER.registerModule(new JavaTimeModule());
    }

    public static <T> T parse(String json, Class<T> type) {
        T result = null;
        try {
            result = MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            //ignore Exception, we expect well formed JSON
            e.printStackTrace();
        }
        return result;
    }

    public static String toJsonString(OperationResponse operationResponse) {
        try {
            return MAPPER.writeValueAsString(operationResponse);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return null;
    }
}
