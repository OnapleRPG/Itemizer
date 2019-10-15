package com.onaple.itemizer.data.beans;

import lombok.Getter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeumimTo on 17.3.2019.
 */
@ConfigSerializable
@Getter
public class Mining {

    @Setting("miners")
    private List<MinerBean> miners  = new ArrayList<>();;

}
