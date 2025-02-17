package org.imazani.httpserver.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;

public class Json {

    private static ObjectMapper myObjectMapper = defaultObjectMapper();

    private static ObjectMapper defaultObjectMapper(){
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return om;
    }

    public static JsonNode parse(String jsonSRC) throws JsonProcessingException {
        return myObjectMapper.readTree(jsonSRC);
    }

    public static <A> A fromJson(JsonNode node, Class<A> objectClass) throws JsonProcessingException {
        return myObjectMapper.treeToValue(node, objectClass);
    }

    public static JsonNode toJson(Object obj) {
        return myObjectMapper.valueToTree(obj);
    }

    public static String toString (Json node) throws JsonProcessingException {
        return generateJson(node, false);
    }

    public static String toStringPretty (Json node) throws JsonProcessingException {
        return generateJson(node, true);
    }

    private static String generateJson (Object obj, boolean pretty) throws JsonProcessingException {
        ObjectWriter objectWriter = myObjectMapper.writer();

        if(pretty) {
            objectWriter = objectWriter.with(SerializationFeature.INDENT_OUTPUT);
        }

        return objectWriter.writeValueAsString(obj);
    }


}
