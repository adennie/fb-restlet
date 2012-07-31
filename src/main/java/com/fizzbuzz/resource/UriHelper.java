package com.fizzbuzz.resource;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.google.common.collect.ImmutableMap;

public abstract class UriHelper {

    private ImmutableMap<Class<?>, String> mResourceInterfaceToUriPatternMap;
    private final String mResourceRoot;

    public static final String URL_TOKEN_ID = "id";

    protected UriHelper(final String resourceRoot) {
        mResourceRoot = resourceRoot;
    }

    protected void initMaps(final ImmutableMap<Class<?>, String> resourceInterfaceToUriPatternToMap) {
        mResourceInterfaceToUriPatternMap = resourceInterfaceToUriPatternToMap;
    }

    public Set<Entry<Class<?>, String>> geResourceInterfaceTotUriPatternMapEntries() {
        return mResourceInterfaceToUriPatternMap.entrySet();
    }

    // use this version of getUriForResourceInterface when the URI template contains tokens to be substituted
    public String getUriForResourceInterface(final Class<?> targetResourceInterface,
            final ImmutableMap<String, String> uriTokenToValueMap) {
        return formatUriTemplate(getUriTemplate(targetResourceInterface), uriTokenToValueMap);
    }

    // use this version of getUriForResourceInterface when the URI template does not contain tokens to be substituted
    public String getUriForResourceInterface(final Class<?> targetResourceInterface) {
        return getUriTemplate(targetResourceInterface);
    }

    public String formatUriTemplate(final String uriTemplate, final ImmutableMap<String, String> uriTokenToValueMap) {
        String result = uriTemplate;

        for (Map.Entry<String, String> entry : uriTokenToValueMap.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private String getUriTemplate(final Class<?> resourceInterface) {
        return mResourceRoot + getUriPattern(resourceInterface);
    }

    private String getUriPattern(final Class<?> resourceInterface) {
        return mResourceInterfaceToUriPatternMap.get(resourceInterface);
    }

}
