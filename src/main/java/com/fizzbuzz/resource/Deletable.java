package com.fizzbuzz.resource;

import org.restlet.resource.Delete;

public interface Deletable {
    @Delete
    public void deleteResource();
}