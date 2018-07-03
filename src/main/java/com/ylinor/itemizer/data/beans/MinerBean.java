package com.ylinor.itemizer.data.beans;

import java.util.List;

public class MinerBean {
    /** ID of the miner **/
    private int id;
    /** Types that can be mined **/
    private List<String> mineTypes;
    /** Miners id that are inherited from **/
    private List<Integer> inheritances;

    public MinerBean(int id, List<String> mineTypes, List<Integer> inheritances) {
        this.id = id;
        this.mineTypes = mineTypes;
        this.inheritances = inheritances;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    public List<String> getMineTypes() {
        return mineTypes;
    }
    public void setMineTypes(List<String> mineTypes) {
        this.mineTypes = mineTypes;
    }

    public List<Integer> getInheritances() {
        return inheritances;
    }
    public void setInheritances(List<Integer> inheritances) {
        this.inheritances = inheritances;
    }
}
