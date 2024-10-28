package org.example.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonParser {

    private static ObjectMapper objectMapper = new ObjectMapper();


     public<T> T fromJson(String json, Class<T> type){
         try {
             return objectMapper.readValue(json, type);
         } catch (JsonProcessingException e) {
             throw new RuntimeException(e);
         }
     }


}
