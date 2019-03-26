package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

/**
 * Created by NeumimTo on 17.3.2019.
 */
@ConfigSerializable
public class Mining {

    @Setting("miners")
    private List<MinerBean> miners;

    public List<MinerBean> getMiners() {
        return miners;
    }
}
