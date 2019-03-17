package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public interface ItemNbtFactory {

    Key getKey();

    default void apply(ItemStack itemStack) {
        itemStack.offer(constructDataManipulator());
    }

    DataManipulator<?,?> constructDataManipulator();

    CommentedConfigurationNode toNode();
}
