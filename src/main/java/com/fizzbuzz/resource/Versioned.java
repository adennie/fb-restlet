package com.fizzbuzz.resource;

import org.restlet.data.MediaType;

import com.fizzbuzz.model.Version;

public interface Versioned
{
    public MediaType getJsonMediaType();

    public void upgradeFromVersion(final Version v);

}
