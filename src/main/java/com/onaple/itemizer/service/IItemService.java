package com.onaple.itemizer.service;

import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemLoreWriter;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public interface IItemService {

    void addItemLoreAppender(ItemLoreWriter writer);

    void addThirdpartyConfig(ItemNBTModule factory);

    Optional<ItemNBTModule> getFactoryByKeyId(String keyId);

    Optional<ItemStack> fetch(String id);

    Optional<ItemStack> retrieve(String id);
}
