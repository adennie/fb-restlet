package com.fizzbuzz.resource;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.fizzbuzz.exception.NotFoundException;
import com.google.common.collect.ImmutableMap;

public abstract class UriHelper {

    private ImmutableMap<Class<?>, String> mResourceInterfaceToUriPatternMap;
    private final String mResourceRoot;

    public static final String URL_TOKEN_ID = "id";

    protected UriHelper(final String resourceRoot) {
        mResourceRoot = resourceRoot;
    }

    protected void initMaps(final Map<Class<?>, String> resourceInterfaceToUriPatternToMap) {
        mResourceInterfaceToUriPatternMap = ImmutableMap.copyOf(resourceInterfaceToUriPatternToMap);
    }

    public Set<Entry<Class<?>, String>> geResourceInterfaceTotUriPatternMapEntries() {
        return mResourceInterfaceToUriPatternMap.entrySet();
    }

    // use this version of getUriForResourceInterface when the URI template contains tokens to be substituted
    public String getUriForResourceInterface(final Class<?> targetResourceInterface,
            final Map<String, String> uriTokenToValueMap) {
        return formatUriTemplate(getUriTemplate(targetResourceInterface), uriTokenToValueMap);
    }

    // use this version of getUriForResourceInterface when the URI template does not contain tokens to be substituted
    public String getUriForResourceInterface(final Class<?> targetResourceInterface) {
        return getUriTemplate(targetResourceInterface);
    }

    // suppress suggestion to make this static; if we did that, subclasses couldn't override it
    @SuppressWarnings("static-method")
    public String formatUriTemplate(final String uriTemplate,
            final Map<String, String> uriTokenToValueMap) {
        String result = uriTemplate;

        // make an defensive immutable copy of the provided map
        ImmutableMap<String, String> iMap = ImmutableMap.copyOf(uriTokenToValueMap);

        for (Map.Entry<String, String> entry : iMap.entrySet()) {
            result = result.replace("{" + entry.getKey() + "}", entry.getValue());
        }
        return result;
    }

    private String getUriTemplate(final Class<?> resourceInterface) {
        return mResourceRoot + getUriPattern(resourceInterface);
    }

    private String getUriPattern(final Class<?> resourceInterface) {
        String result = mResourceInterfaceToUriPatternMap.get(resourceInterface);
        if (result == null)
            throw new NotFoundException("class " + resourceInterface + " not found in UriHelper's map of interfaces to URI patterns");
        return result;
    }

}
