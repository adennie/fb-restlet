package com.fizzbuzz.resource;

import com.fizzbuzz.model.Version;

public interface Versioned
{
    public void upgradeFromVersion(final Version v);

}
