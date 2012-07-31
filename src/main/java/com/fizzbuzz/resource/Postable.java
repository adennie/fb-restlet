package com.fizzbuzz.resource;

import org.restlet.resource.Post;

public interface Postable<T> {
    @Post
    public void postResource(T resource);
}