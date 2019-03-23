package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.data.serializers.MinerBeanAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.ItemType;

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
    private ItemType type;

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
    @Setting("max_durability")
    private int maxDurability;

    /** Unbreakable attribute **/
    @Setting("unbreakable")
    private boolean unbreakable;

    /** Map of enchants and their levels **/
    @Setting("enchants")
    private Map<String, ItemEnchant> enchants = new HashMap<>();
    /** IDs of the miner abilities associated **/
    @Setting("miners")
    @CustomAdapter(MinerBeanAdapter.class)
    private List<MinerBean> miners = new ArrayList<>();
    /** List of custom attribute of the item */
    @Setting("attributes")
    private List<AttributeBean> attributeList = new ArrayList<>();

    @Setting("nbt")
    private List<ItemNbtFactory> nbt = new ArrayList<>();

    public Map<String, String> getBlockTrait() {
        return blockTrait;
    }

    /** Map of block traits**/
    private Map<String,String> blockTrait = new HashMap<>();

    @Setting("toolType")
    private String toolType;

    @Setting("toolLevel")
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

    public ItemBean(ItemType type) {
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

    public List<MinerBean> getMiners() {
        return miners;
    }
    public void setMiners(List<MinerBean> miners) {
        this.miners = miners;
    }

    public List<AttributeBean> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<AttributeBean> attributeList) {
        this.attributeList = attributeList;
    }

    public List<ItemNbtFactory> getNbt() {
        return nbt;
    }

    public void setNbt(List<ItemNbtFactory> nbt) {
        this.nbt = nbt;
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

