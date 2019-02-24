package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ConfigSerializable
public class MinerBean {

    /** ID of the miner **/
    @Setting("id")
    private String id;

    /** Types that can be mined **/
    @Setting("mineTypes")
    private Map<String, String> mineTypes;

    /** Miners id that are inherited from **/
    @Setting("inheritaces")
    private List<String> inheritances;

    public MinerBean(String id, Map<String,String> mineTypes, List<String> inheritances) {
        this.id = id;

        this.mineTypes = mineTypes;
        this.inheritances = inheritances;
    }

    public MinerBean() {
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }


    public Map<String, String> getMineTypes() {
        return mineTypes;
    }

    public void setMineTypes(Map<String, String> mineTypes) {
        this.mineTypes = mineTypes;
    }

    public List<String> getInheritances() {
        return inheritances;
    }
    public void setInheritances(List<String> inheritances) {
        this.inheritances = inheritances;
    }
}
