package com.ylinor.itemizer.data.beans;

import java.util.List;
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
    /** Unbreakable attribute **/
    private boolean unbreakable;
    /** Map of enchants and their levels **/
    private Map<String, Integer> enchants;
    /** IDs of the miner abilities associated **/
    private List<Integer> miners;

    public ItemBean(int id, String type, String name, String lore, boolean unbreakable, Map<String, Integer> enchants, List<Integer> miners) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.unbreakable = unbreakable;
        this.enchants = enchants;
        this.miners = miners;
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

    public boolean isUnbreakable() {
        return unbreakable;
    }
    public void setUnbreakable(boolean unbreakable) {
        this.unbreakable = unbreakable;
    }

    public Map<String, Integer> getEnchants() {
        return enchants;
    }
    public void setEnchants(Map<String, Integer> enchants) {
        this.enchants = enchants;
    }

    public List<Integer> getMiners() {
        return miners;
    }
    public void setMiners(List<Integer> miners) {
        this.miners = miners;
    }
}
