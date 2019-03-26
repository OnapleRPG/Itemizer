package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

/**
 * Created by NeumimTo on 17.3.2019.
 */
@ConfigSerializable
public class ItemEnchant {

    @Setting("level")
    private int level;

    public int getLevel() {
        return level;
    }

    public ItemEnchant setLevel(int level) {
        this.level = level;
        return this;
    }
}
