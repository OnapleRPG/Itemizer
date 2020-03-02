package com.onaple.itemizer.data.beans.affix;

import com.onaple.itemizer.data.beans.ItemBean;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable

public class AffixRoot {

    public AffixRoot() {
    }

    public void setAffixes(List<AffixBean> affixes) {
        this.affixes = affixes;
    }

    @Setting("affixes")
    private List<AffixBean> affixes  = new ArrayList<>();

    public List<AffixBean> getAffixes() {
        return affixes;
    }
}
