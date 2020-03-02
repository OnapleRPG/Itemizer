package com.onaple.itemizer.data.beans;

import cz.neumimto.config.blackjack.and.hookers.annotations.AsCollectionImpl;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@ConfigSerializable
public class PoolBean {

    public PoolBean() {
    }

    public PoolBean(String id, List<PoolItemBean> items) {
        this.id = id;
        this.items = items;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<PoolItemBean> getItems() {
        return items;
    }

    public void setItems(List<PoolItemBean> items) {
        this.items = items;
    }

    /** ID of the pool in config **/
    @Setting("id")
    private String id;

    /** List of items & quantity with linked probability **/
    @Setting("items")
    @AsCollectionImpl(ArrayList.class)
    private List<PoolItemBean> items;
  }
