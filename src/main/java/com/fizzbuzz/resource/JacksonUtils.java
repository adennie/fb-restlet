package com.fizzbuzz.resource;

import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.restlet.data.Form;

import com.google.common.collect.ImmutableMap;

public class JacksonUtils {
    public static Map<String, String> serializeToJson(Object obj) {
        Map<String, String> result = null;
        ImmutableMap.Builder<String, String> builder = new ImmutableMap.Builder<String, String>();
        builder.put("className", obj.getClass().getName());
        try {
            builder.put("json", new ObjectMapper().writer().writeValueAsString(obj));
        }
        catch (JsonGenerationException e) {
            throw new RuntimeException(e);
        }
        catch (JsonMappingException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        result = builder.build();
        return result;
    }

    public static Object deserializeFromJson(Map<String, String> dict) {
        Object result = null;
        try {
            Class<?> clazz = Class.forName(dict.get("className"));
            result = new ObjectMapper().reader().withType(clazz).readValue(dict.get("json"));
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    public static Form toForm(Object obj) {
        Form result = new Form();
        Map<String, String> params = JacksonUtils.serializeToJson(obj);
        for (Entry<String, String> element : params.entrySet()) {
            result.add(element.getKey(), element.getValue());
        }
        return result;

    }

    public static Object parseForm(Form form) {
        return deserializeFromJson(form.getValuesMap());
    }

}
