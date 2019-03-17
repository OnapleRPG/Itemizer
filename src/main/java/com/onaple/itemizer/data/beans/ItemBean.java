package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class ItemBean {

    /** ID of the item in config **/
    @Setting("id")
    private String id = "";

    /** Type of the item **/
    @Setting("type")
    private ItemType type;

    /** Name of the item **/
    @Setting("name")
    private String name = "";

    /** Lore / description of the item **/
    @Setting("lore")
    private String lore = "";

    /** number of use of the item**/
    @Setting("durability")
    private int durability;

    /** Max number of use of the item **/
    @Setting("max_durability")
    private int maxDurability;

    /** Unbreakable attribute **/
    @Setting("unbreakable")
    private boolean unbreakable;

    /** Map of enchants and their levels **/
    @Setting
    private Map<String, ItemEnchant> enchants = new HashMap<>();
    /** IDs of the miner abilities associated **/
    @Setting
    private List<String> miners = new ArrayList<>();
    /** List of custom attribute of the item */
    @Setting
    private List<AttributeBean> attributeList = new ArrayList<>();

    @Setting
    private List<IItemBeanConfiguration> thirdpartyConfigs = new ArrayList<>();

    /** Map of all NBT*/
    //@Setting
    private Map<String,Object> nbtList = new HashMap<>();

    public Map<String, String> getBlockTrait() {
        return blockTrait;
    }

    public void setBlockTrait(Map<String, String> blockTrait) {
        this.blockTrait = blockTrait;
    }

    /** Map of block traits**/
    private Map<String,String> blockTrait = new HashMap<>();


    public Map<String, Object> getNbtList() {
        return nbtList;
    }

    public void setNbtList(Map<String, Object> nbtList) {
        this.nbtList = nbtList;
    }

    private String toolType;

    public ItemBean() {
    }

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

    public ItemType getType() {
        return type;
    }
    public void setType(ItemType type) {
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

    public Map<String, ItemEnchant> getEnchants() {
        return enchants;
    }
    public void setEnchants(Map<String, ItemEnchant> enchants) {
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

