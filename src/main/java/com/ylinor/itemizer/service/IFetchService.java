package com.ylinor.itemizer.service;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public interface IFetchService {
    Optional<ItemStack> Fetch(int id);
}
