package com.onaple.itemizer.data.beans;

import org.spongepowered.api.data.key.Key;

public abstract class AbstractItemBeanConfiguration implements IItemBeanConfiguration {

    private final Key<?> key;

    public AbstractItemBeanConfiguration(Key<?> key) {
        this.key = key;
    }

    @Override
    public Key getKey() {
        return key;
    }
}
