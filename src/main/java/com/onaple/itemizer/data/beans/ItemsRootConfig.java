package com.onaple.itemizer.data.beans;

import cz.neumimto.config.blackjack.and.hookers.annotations.AsCollectionImpl;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class ItemsRootConfig {

    @Setting("items")
    @AsCollectionImpl(ArrayList.class)
    private List<ItemBean> items;

    public List<ItemBean> getItems() {
        return items;
    }
}
