package com.onaple.itemizer;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public interface ICraftRecipes {
    void register();
}
