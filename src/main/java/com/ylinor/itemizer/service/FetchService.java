package com.ylinor.itemizer.service;

import com.ylinor.itemizer.data.access.ItemDAO;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.utils.ItemBuilder;
import com.ylinor.itemizer.utils.PoolFetcher;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class FetchService implements IFetchService {

    @Override
    public Optional<ItemStack> fetch(int id) {
        Optional<ItemBean> optionalItem = PoolFetcher.fetchItemFromPool(id);
        if (optionalItem.isPresent()) {
            Optional<ItemStack> optionalItemStack = ItemBuilder.buildItemStack(optionalItem.get());
            return optionalItemStack;
        }
        return Optional.empty();



    }

    @Override
    public Optional<ItemStack> retrieve(int id) {
        Optional<ItemBean> optionalItem = ItemDAO.getItem(id);
        if (optionalItem.isPresent()) {
            Optional<ItemStack> optionalItemStack = ItemBuilder.buildItemStack(optionalItem.get());
            return optionalItemStack;
        }
        return Optional.empty();

    }
}
