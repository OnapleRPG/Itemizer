package com.onaple.itemizer.service;

import com.onaple.itemizer.data.beans.ItemLoreWriter;
import com.onaple.itemizer.exception.ItemNotPresentException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public interface IItemService {

    void addItemLoreAppender(ItemLoreWriter writer);

    Optional<ItemStack> fetch(String id);

    Optional<ItemStack> retrieve(String id);

    boolean hasItem(Player player, String id, int quantity) throws ItemNotPresentException;
}
