package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.block.BlockType;

import java.util.List;
import java.util.Map;

@ConfigSerializable
public class MinerBean {

    /** ID of the miner **/
    @Setting
    private String id;

    /** Types that can be mined **/
    @Setting
    private Map<String, BlockType> mineTypes;

    /** Miners id that are inherited from **/
    @Setting
    private List<String> inheritances;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Map<String, BlockType> getMineTypes() {
        return mineTypes;
    }

    public void setMineTypes(Map<String, BlockType> mineTypes) {
        this.mineTypes = mineTypes;
    }

    public List<String> getInheritances() {
        return inheritances;
    }

    public void setInheritances(List<String> inheritances) {
        this.inheritances = inheritances;
    }
}
