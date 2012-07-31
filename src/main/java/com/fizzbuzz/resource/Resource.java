package com.fizzbuzz.resource;

import org.restlet.data.MediaType;

public interface Resource {
    public MediaType getMediaType(String format);
}
