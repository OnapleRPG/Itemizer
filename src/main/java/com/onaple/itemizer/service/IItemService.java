package com.onaple.itemizer.service;

import com.onaple.itemizer.exception.ItemNotPresentException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Optional;

public interface IItemService {

    Optional<ItemStack> fetch(String id);

    Optional<ItemStack> retrieve(String id);

    boolean hasItem(Player player, String id, int quantity) throws ItemNotPresentException;

    void register(String id, ItemStackSnapshot snapshot);

    void update(String id, ItemStackSnapshot snapshot);
}
