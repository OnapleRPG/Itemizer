package com.ylinor.itemizer.data.beans;

import java.util.Map;

public class ItemBean {
    /** ID of the item in config **/
    private int id;
    /** Type of the item **/
    private String type;
    /** Name of the item **/
    private String name;
    /** Lore / description of the item **/
    private String lore;
    /** Map of enchants and their levels **/
    private Map<String, Integer> enchants;

    public ItemBean(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getLore() {
        return lore;
    }
    public void setLore(String lore) {
        this.lore = lore;
    }

    public Map<String, Integer> getEnchants() {
        return enchants;
    }
    public void setEnchants(Map<String, Integer> enchants) {
        this.enchants = enchants;
    }
}
