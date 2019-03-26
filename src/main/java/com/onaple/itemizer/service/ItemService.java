package com.onaple.itemizer.service;

import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemLoreWriter;
import com.onaple.itemizer.utils.ItemBuilder;
import com.onaple.itemizer.utils.PoolFetcher;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Singleton
public class ItemService implements IItemService {

    public ItemService() {
    }

    private Map<String, ItemNBTModule> thirdPartyConfigs = new HashMap<>();

    private Map<Key, ItemLoreWriter> customLoreAppenders = new HashMap<>();

    public Set<ItemLoreWriter> getItemLoreAppenders(Set<Key> keys) {
        Set<ItemLoreWriter> writers = new HashSet<>();
        for (Map.Entry<Key, ItemLoreWriter> entry : customLoreAppenders.entrySet()) {
            if (keys.contains(entry.getKey())) {
                writers.add(entry.getValue());
            }
        }
        return writers;
    }

    @Override
    public void addItemLoreAppender(ItemLoreWriter writer) {
        writer.getKeys().stream().forEach(a -> customLoreAppenders.put(a, writer));
    }

    @Override
    public void addThirdpartyConfig(ItemNBTModule factory) {
        thirdPartyConfigs.put(factory.getKeyId(), factory);
    }

    @Override
    public Optional<ItemNBTModule> getFactoryByKeyId(String keyId) {
        return Optional.ofNullable(thirdPartyConfigs.get(keyId));
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
