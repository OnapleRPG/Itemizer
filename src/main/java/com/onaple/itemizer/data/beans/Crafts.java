package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.ICraftRecipes;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

import java.util.List;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public class Crafts {

    @Setting("crafts")
    private List<ICraftRecipes> craftingRecipes;

    public List<ICraftRecipes> getCraftingRecipes() {
        return craftingRecipes;
    }
}
