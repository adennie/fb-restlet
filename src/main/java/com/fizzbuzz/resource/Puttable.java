package com.fizzbuzz.resource;

import org.restlet.resource.Put;

public interface Puttable<T> {
    @Put
    public void putResource(T resource);
}