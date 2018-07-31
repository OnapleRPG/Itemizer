package com.onaple.itemizer.data.beans;

import java.util.List;

public class MinerBean {
    /** ID of the miner **/
    private String id;
    /** Types that can be mined **/
    private List<String> mineTypes;
    /** Miners id that are inherited from **/
    private List<String> inheritances;

    public MinerBean(String id, List<String> mineTypes, List<String> inheritances) {
        this.id = id;
        this.mineTypes = mineTypes;
        this.inheritances = inheritances;
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public List<String> getMineTypes() {
        return mineTypes;
    }
    public void setMineTypes(List<String> mineTypes) {
        this.mineTypes = mineTypes;
    }

    public List<String> getInheritances() {
        return inheritances;
    }
    public void setInheritances(List<String> inheritances) {
        this.inheritances = inheritances;
    }
}
