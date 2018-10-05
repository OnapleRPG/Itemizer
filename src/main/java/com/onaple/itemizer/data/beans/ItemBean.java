package com.onaple.itemizer.data.beans;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemBean {
    /** ID of the item in config **/
    private String id = "";
    /** Type of the item **/
    private String type;
    /** Name of the item **/
    private String name = "";
    /** Lore / description of the item **/
    private String lore = "";
    /** number of use of the item**/
    private int durability;
    /** Max number of use of the item **/
    private int maxDurability;
    /** Unbreakable attribute **/
    private boolean unbreakable = false;
    /** Map of enchants and their levels **/
    private Map<String, Integer> enchants = new HashMap<>();
    /** IDs of the miner abilities associated **/
    private List<String> miners = new ArrayList<>();
    /** List of custom attribute of the item */
    private List<AttributeBean> attributeList ;

    private String toolType;

    private int toolLevel;

    public String getToolType() {
        return toolType;
    }

    public void setToolType(String toolType) {
        this.toolType = toolType;
    }

    public int getToolLevel() {
        return toolLevel;
    }

    public void setToolLevel(int toolLevel) {
        this.toolLevel = toolLevel;
    }

    public ItemBean(String type) {
        this.type = type;
        this.attributeList = new ArrayList<>();
    }
    public ItemBean(String id, String type, String name, String lore, int durability, boolean unbreakable, Map<String, Integer> enchants, List<String> miners, List<AttributeBean> attributeList) {
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

    public int getMaxDurability() {
        return maxDurability;
    }
    public void setMaxDurability(int maxDurability) {
        this.maxDurability = maxDurability;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
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

    public List<String> getMiners() {
        return miners;
    }
    public void setMiners(List<String> miners) {
        this.miners = miners;
    }

    public List<AttributeBean> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AttributeBean> attributeList) {
        this.attributeList = attributeList;
    }

    @Override
    public String toString() {
        return "ItemBean{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", name='" + name + '\'' +
                ", lore='" + lore + '\'' +
                ", durability=" + durability +
                ", maxDurability=" + maxDurability +
                ", unbreakable=" + unbreakable +
                ", enchants=" + enchants +
                ", miners=" + miners +
                ", attributeList=" + attributeList +
                '}';
    }
}

