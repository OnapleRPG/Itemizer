package com.onaple.itemizer.data.beans;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeumimTo on 27.12.2018.
 */
@ConfigSerializable
public class ItemsRoot {

    public ItemsRoot() {
    }

    @Setting("items")
    private List<ItemBean> items  = new ArrayList<>();

    public List<ItemBean> getItems() {
        return items;
    }

    public void setItems(List<ItemBean> items) {
        this.items = items;
    }
}
