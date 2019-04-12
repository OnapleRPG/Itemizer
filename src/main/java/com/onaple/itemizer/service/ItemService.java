package com.onaple.itemizer.service;

import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.exception.ItemNotPresentException;
import com.onaple.itemizer.utils.ItemBuilder;
import com.onaple.itemizer.utils.PoolFetcher;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ItemService implements IItemService {

    public ItemService() {
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

    @Override
    public boolean hasItem(Player player, String id, int quantity) throws ItemNotPresentException {
        ItemStack itemStack = retrieve(id).orElseThrow(() -> new ItemNotPresentException(id));
        itemStack.setQuantity(quantity);
          return player.getInventory().contains(itemStack);
    }
}
