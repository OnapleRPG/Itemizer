package com.onaple.itemizer.data.beans.recipes;

import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.beans.crafts.ICraftRecipes;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.function.Predicate;

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

    protected Predicate<ItemStack> isMatchingId(String id){
        return (itemStack -> itemStack.get(ItemizerKeys.ITEM_ID).orElse(itemStack.getType().getName()).equals(id));
    }
    protected Predicate<ItemStackSnapshot> isSnapshotMatchingId(String id){
        return (itemStackSnapshot -> itemStackSnapshot.get(ItemizerKeys.ITEM_ID).orElse(itemStackSnapshot.getType().getName()).equals(id));
    }
}
