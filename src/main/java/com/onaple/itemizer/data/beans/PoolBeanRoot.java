package com.onaple.itemizer.data.beans;

import cz.neumimto.config.blackjack.and.hookers.annotations.AsCollectionImpl;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

@ConfigSerializable
public class PoolBeanRoot {

    @Setting("pools")
    @AsCollectionImpl(ArrayList.class)
    private List<PoolBean> poolList;

    public List<PoolBean> getPoolList() {
        return poolList;
    }
}
