package com.onaple.itemizer.service;

import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.exception.BadWorldNameException;
import com.onaple.itemizer.exception.ItemNotPresentException;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;

public interface IItemService {

    Optional<ItemStack> fetch(String id);

    Optional<ItemStack> retrieve(String id);

    Optional<ItemStack> retrieve(String id, int qte);

    Optional<ItemStack> retrieveBaseItem(String id);

    ItemStack construct(ItemBean item);

    ItemStack constructBaseItem(ItemBean item);

    boolean hasItem(Player player, String id, int quantity) throws ItemNotPresentException;

    void register(String id, ItemStackSnapshot snapshot);

    void update(String id, ItemStackSnapshot snapshot);

    void instantiate(ItemStack itemStack, Location<World> location) throws ItemNotPresentException;

    void instanciate(ItemStack itemStack, String worldName, double x, double y, double z) throws BadWorldNameException;

    void removeItem(Player player, String id, Integer quantity) throws ItemNotPresentException;

}
