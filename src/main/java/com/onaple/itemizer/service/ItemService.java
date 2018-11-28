package com.onaple.itemizer.service;

import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import com.onaple.itemizer.utils.PoolFetcher;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.*;

public class ItemService implements IItemService {

    private Map<String, IItemBeanFactory> thirdPartyConfigs = new HashMap<>();

    public void addThirdpartyConfig(IItemBeanFactory factory) {
        thirdPartyConfigs.put(factory.getKeyId(), factory);
    }

    public IItemBeanFactory getFactoryByKeyId(String keyId) {
        return thirdPartyConfigs.get(keyId);
    }

    @Override
    public Optional<ItemStack> fetch(String id) {
        Optional<ItemBean> optionalItem = PoolFetcher.fetchItemFromPool(id);
        if (optionalItem.isPresent()) {
            return new ItemBuilder().buildItemStack(optionalItem.get());
        }
        return Optional.empty();



    }
    @Override
    public Optional<ItemStack> retrieve(String id) {
        Optional<ItemBean> optionalItem = ItemDAO.getItem(id);
        if (optionalItem.isPresent()) {
            return new ItemBuilder().buildItemStack(optionalItem.get());
        }
        return Optional.empty();

    }
}
