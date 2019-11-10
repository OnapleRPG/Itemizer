package com.onaple.itemizer.service;

import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.exception.ItemNotPresentException;
import com.onaple.itemizer.utils.ItemBuilder;
import com.onaple.itemizer.utils.PoolFetcher;
import lombok.NoArgsConstructor;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
@NoArgsConstructor
public class ItemService implements IItemService {

    @Inject
    private ItemBuilder itemBuilder;

    @Inject
    private ItemDAO itemDAO;

    @Override
    public Optional<ItemStack> fetch(String id) {
        return PoolFetcher.fetchItemFromPool(id);
    }

    @Override
    public Optional<ItemStack> retrieve(String id) {
        Optional<ItemBean> optionalItem = itemDAO.getItem(id);
        if (optionalItem.isPresent()) {
            return Optional.ofNullable(itemBuilder.buildItemStack(optionalItem.get()));
        }
        return Optional.empty();
    }

    @Override
    public ItemStack construct(ItemBean item) {
        return itemBuilder.buildItemStack(item);
    }

    @Override
    public boolean hasItem(Player player, String id, int quantity) throws ItemNotPresentException {
        ItemStack itemStack = retrieve(id).orElseThrow(() -> new ItemNotPresentException(id));
        itemStack.setQuantity(quantity);
          return player.getInventory().contains(itemStack);
    }

    @Override
    public void register(String id, ItemStackSnapshot snapshot) {
        ItemBean itemBean = ItemBean.from(id,snapshot.createStack());
        itemDAO.save(itemBean);
    }

    @Override
    public void update(String id, ItemStackSnapshot snapshot) {
    }

    @Override
    public void instantiate(ItemStack itemStack, Location<World> location) throws ItemNotPresentException {
        Extent extent = location.getExtent();
        Entity itemEntity = extent.createEntity(EntityTypes.ITEM, location.getPosition());
        itemEntity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
        location.spawnEntity(itemEntity);
    }
}
