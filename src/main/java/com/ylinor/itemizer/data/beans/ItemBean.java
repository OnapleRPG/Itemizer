package com.ylinor.itemizer.data.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBean {
    /** ID of the item in config **/
    private int id = 0;
    /** Type of the item **/
    private String type;
    /** Name of the item **/
    private String name = "";
    /** Lore / description of the item **/
    private String lore = "";
    /** number of use of the item**/
    private int durability;
    /** Unbreakable attribute **/
    private boolean unbreakable = false;
    /** Map of enchants and their levels **/
    private Map<String, Integer> enchants = new HashMap<>();
    /** IDs of the miner abilities associated **/
    private List<Integer> miners = new ArrayList<>();
    /** List of custom attribute of the item */
    private List<AttributeBean> attributeList ;

    public ItemBean(String type) {
        this.type = type;
        this.attributeList = new ArrayList<>();
    }
    public ItemBean(int id, String type, String name, String lore, int durability, boolean unbreakable, Map<String, Integer> enchants, List<Integer> miners, List<AttributeBean> attributeList) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.lore = lore;
        this.durability = durability;
        this.unbreakable = unbreakable;
        this.enchants = enchants;
        this.miners = miners;
        this.attributeList = attributeList;
    }

    public int getDurability() {
        return durability;
    }

    public void setDurability(int durability) {
        this.durability = durability;
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

    public List<AttributeBean> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AttributeBean> attributeList) {
        this.attributeList = attributeList;
    }
}

