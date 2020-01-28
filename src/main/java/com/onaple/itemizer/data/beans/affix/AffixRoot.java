package com.onaple.itemizer.data.beans.affix;

import com.onaple.itemizer.data.beans.ItemBean;
import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
@NoArgsConstructor
public class AffixRoot {

    @Setting("affixes")
    private List<AffixBean> affixes  = new ArrayList<>();

    public List<AffixBean> getAffixes() {
        return affixes;
    }
}
