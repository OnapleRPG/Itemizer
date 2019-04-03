package com.onaple.itemizer.service;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public interface IItemService {

    Optional<ItemStack> fetch(String id);

    Optional<ItemStack> retrieve(String id);
}
