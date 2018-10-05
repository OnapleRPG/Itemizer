package com.onaple.itemizer.data.beans;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerBean {
    /** ID of the miner **/
    private String id;
    /** Types that can be mined **/
    private Map<String, String> mineTypes;
    /** Miners id that are inherited from **/
    private List<String> inheritances;

    public MinerBean(String id, Map<String,String> mineTypes, List<String> inheritances) {
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
