package com.onaple.itemizer.data.beans;

import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Set;

public interface ItemLoreWriter {

    Set<Key> getKeys();

    void apply(ItemStack itemStack, List<Text> buffer);
}
