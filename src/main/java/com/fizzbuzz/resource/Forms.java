package com.fizzbuzz.resource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.restlet.data.Form;
import org.restlet.resource.ResourceException;

import com.fizzbuzz.model.DictionarySerializable;

public class Forms {

    // this is just a collection of static methods. Make the constructor private to prevent instantiation.
    private Forms() {
    }

    public static <T extends Enum<T>> T getRequiredEnumArg(final Form form,
            final String paramName,
            final Class<T> clazz) {
        T result;

        String paramValue = form.getFirstValue(paramName);
        if (paramValue == null)
            throw new ResourceException(
                    org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST,
                    "missing required form parameter: " + paramName);

        try {
            result = Enum.valueOf(clazz, paramValue);
        }
        catch (IllegalArgumentException e) {
            String validValues = "";
            for (T enumVal : clazz.getEnumConstants()) {
                if (validValues.length() > 0)
                    validValues += ", ";
                validValues += enumVal.toString();
            }
            throw new ResourceException(
                    org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST,
                    "illegal value for required \"" + paramName
                            + "\" argument.  Valid values are: " + validValues);
        }
        return result;
    }

    public static long getRequiredLongArg(final Form form,
            final String paramName) {
        long result;

        String paramValue = form.getFirstValue(paramName);
        if (paramValue == null)
            throw new ResourceException(
                    org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST,
                    "missing required form parameter: " + paramName);

        try {
            result = Long.parseLong(paramValue);
        }
        catch (NumberFormatException e) {
            throw new ResourceException(
                    org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST,
                    "form parameter \"" + paramName
                            + "\" must be a valid long number");
        }
        return result;
    }

    public static String getRequiredStringArg(final Form form,
            final String paramName) {

        String result = form.getFirstValue(paramName);
        if (result == null)
            throw new ResourceException(
                    org.restlet.data.Status.CLIENT_ERROR_BAD_REQUEST,
                    "missing required form parameter: " + paramName);

        return result;
    }

    public static <K, V> Map<K, V> getFirstMap(final Form form,
            final String paramName) {
        Map<K, V> result = null;

        String jsonMap = form.getFirstValue(paramName, false, "");
        if (jsonMap.length() == 0)
            result = new HashMap<K, V>();
        else {

            TypeReference<HashMap<K, V>> mapTypeRef = new TypeReference<HashMap<K, V>>() {
            };
            try {
                result = new ObjectMapper().readValue(jsonMap, mapTypeRef);
            }
            catch (Exception e) {
            }
        }
        return result;
    }

    public static <K, V> void addMap(final Form form,
            final String paramName,
            final Map<K, V> map) {
        try {
            form.add(paramName, new ObjectMapper().writeValueAsString(map));
        }
        catch (Exception e) {
        }

    }

    public static Form toForm(DictionarySerializable obj) {
        Form result = new Form();
        for (Entry<String, String> entry : obj.toDictionary().entrySet()) {
            result.add(entry.getKey(), entry.getValue());
        }
        return result;

    }

}
