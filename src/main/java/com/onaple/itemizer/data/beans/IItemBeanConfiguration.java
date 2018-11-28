package com.onaple.itemizer.data.beans;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.item.inventory.ItemStack;

public interface IItemBeanConfiguration {

    Key getKey();

    default void apply(ItemStack itemStack) {
        itemStack.offer(constructDataManipulator());
    }

    DataManipulator<?,?> constructDataManipulator();


}
