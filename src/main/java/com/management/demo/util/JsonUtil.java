package com.management.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();


    public static String serialize(Object object){
        if(object != null) {
            try {
                return OBJECT_MAPPER.writeValueAsString(object);
            } catch (JsonProcessingException e) {
                log.warn("Serialization error :");
            }
        }
        return "";
    }

    public static <T> T deserialize(String value, TypeReference<T> type) throws IOException {
        return OBJECT_MAPPER.readValue(value,type);
    }

}
