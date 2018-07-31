package com.onaple.itemizer.data.beans;

import java.util.Map;

public class PoolBean {
    /** ID of the pool in config **/
    private String id;
    /** Map of items with linked probability **/
    private Map<Double, ItemBean> items;

    public PoolBean(String id, Map<Double, ItemBean> items) {
        this.id = id;
        this.items = items;
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
