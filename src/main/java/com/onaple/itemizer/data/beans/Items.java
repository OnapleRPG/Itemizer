package com.onaple.itemizer.data.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NeumimTo on 27.12.2018.
 */
@ConfigSerializable
@Data
@NoArgsConstructor
public class Items {

    @Setting("items")
    private List<ItemBean> items  = new ArrayList<>();;

}
