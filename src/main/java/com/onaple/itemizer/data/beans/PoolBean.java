package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.Map;

@ConfigSerializable
public class PoolBean {

    /** ID of the pool in config **/
    @Setting("id")
    private String id;

    /** Map of items with linked probability **/
    @Setting("items")
    private Map<Double, ItemBean> items;

    public PoolBean(String id, Map<Double, ItemBean> items) {
        this.id = id;
        this.items = items;
    }

    public PoolBean() {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public Map<Double, ItemBean> getItems() {
        return items;
    }
    public void setItems(Map<Double, ItemBean> items) {
        this.items = items;
    }
}
