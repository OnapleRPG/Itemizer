package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class NewItemBean {
    @Setting("id")
    String id;
    @Setting("item")
    ItemStackSnapshot item;
    @Setting("thirdParties")
    List<ItemNbtFactory> thirdParties = new ArrayList<>();

    public NewItemBean(String id, ItemStackSnapshot item) {
        this.id = id;
        this.item = item;
    }

    public NewItemBean() {
    }
}
