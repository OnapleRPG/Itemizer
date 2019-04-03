package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@ConfigSerializable
public interface ItemNbtFactory extends Comparable {
    /**
     * Return the Key
     * @return
     */
    Key getKey();

    /**
     * Return the name of the Third Party
     * @return
     */
    String getName();

    /**
     * Apply the data to the item
     * @param itemStack The item you want to apply the data
     */
    default void apply(ItemStack itemStack) {
        itemStack.offer(constructDataManipulator());
    }

    /**
     * get the dataManipulator to set to the item
     * @return
     */
    DataManipulator<?,?> constructDataManipulator();

    /**
     * return the list of the the lore
     * @return list of text line to set to the object
     */
    default List<Text> getLore(){
        return new ArrayList<>();
    }

}
