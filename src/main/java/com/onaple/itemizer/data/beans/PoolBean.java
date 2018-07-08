package com.onaple.itemizer.data.beans;

import java.util.Map;

public class PoolBean {
    /** ID of the pool in config **/
    private int id;
    /** Map of items with linked probability **/
    private Map<Double, ItemBean> items;

    public PoolBean(int id, Map<Double, ItemBean> items) {
        this.id = id;
        this.items = items;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public Map<Double, ItemBean> getItems() {
        return items;
    }
    public void setItems(Map<Double, ItemBean> items) {
        this.items = items;
    }
}
