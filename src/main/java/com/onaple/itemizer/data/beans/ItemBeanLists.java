package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;


@ConfigSerializable
public class ItemBeanLists {

    @Setting("items")
    private List<ItemBean> items;

    public ItemBeanLists() {
    }

    public List<ItemBean> getItems() {
        return items;
    }
}
