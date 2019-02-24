package com.onaple.itemizer.data.beans;

import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class ItemBean {
    /** ID of the item in config **/
    @Setting("id")
    private String id;

    /** Type of the item **/
    @Setting("type")
    private String type;

    /** Name of the item **/
    @Setting("name")
    private String name;

    /** Lore / description of the item **/
    @Setting("lore")
    private String lore;

    /** number of use of the item**/
    @Setting("durability")
    private int durability;

    /** Max number of use of the item **/
    @Setting("maxDurability")
    private int maxDurability;


    /** Unbreakable attribute **/
    @Setting("unbreakable")
    private boolean unbreakable = false;

    /** Map of enchants and their levels **/
    @Setting("enchants")
    @CustomAdapter(EnchantsAdapter.class)
    private Map<String, Integer> enchants;

    /** IDs of the miner abilities associated **/
    @Setting("miners")
    private List<String> miners;

    /** List of custom attribute of the item */
    @Setting("attributes")
    private List<AttributeBean> attributeList;

    private List<IItemBeanConfiguration> thirdpartyConfigs = new ArrayList<>();

    /** Map of all NBT*/
    /*
    @Setting("nbt")
    @CustomAdapter(NBTAdapter.class)
    */
    private Map<String, Object> nbtList = new HashMap<>();

    public Map<String, String> getBlockTrait() {
        return blockTrait;
    }

    /** Map of block traits**/
    private Map<String,String> blockTrait = new HashMap<>();

    @Setting("toolType")
    private String toolType;

    @Setting("toolLevel")
    private int toolLevel;

    public Map<String, Object> getNbtList() {
        return nbtList;
    }

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

    public ItemBean() {
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

    public List<IItemBeanConfiguration> getThirdpartyConfigs() {
        return thirdpartyConfigs;
    }

    public void setThirdpartyConfigs(List<IItemBeanConfiguration> thirdpartyConfigs) {
        this.thirdpartyConfigs = thirdpartyConfigs;
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

