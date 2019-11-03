package com.onaple.itemizer.recipes;

import com.onaple.itemizer.data.beans.ICraftRecipes;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public abstract class AbstractCraftingRecipe implements ICraftRecipes {

    @Setting("id")
    protected int id;

    @Setting("result")
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    protected ItemStack result;
}
