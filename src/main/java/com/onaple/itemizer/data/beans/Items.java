package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

/**
 * Created by NeumimTo on 27.12.2018.
 */
@ConfigSerializable
public class Items {

    @Setting("items")
    private List<ItemBean> items;

    public Items() {
    }

    public List<ItemBean> getItems() {
        return items;
    }
}
