package com.fizzbuzz.resource;

import org.restlet.resource.Get;

public interface Gettable<T> {
    @Get("json")
    public T getResource();
}