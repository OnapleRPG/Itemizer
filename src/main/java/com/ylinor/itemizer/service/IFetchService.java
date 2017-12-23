package com.ylinor.itemizer.service;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public interface IFetchService {
    Optional<ItemStack> fetch(int id);
    Optional<ItemStack> retrieve(int id);
}
