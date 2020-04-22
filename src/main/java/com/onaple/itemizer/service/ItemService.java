package com.onaple.itemizer.service;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.access.PoolDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.PoolItemBean;
import com.onaple.itemizer.exception.BadWorldNameException;
import com.onaple.itemizer.exception.ItemNotPresentException;
import com.onaple.itemizer.probability.ProbabilityFetcher;
import com.onaple.itemizer.utils.ItemBuilder;
import org.apache.commons.lang3.NotImplementedException;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.EntityTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;
import org.spongepowered.api.world.extent.Extent;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class ItemService implements IItemService {

    public ItemService() {
    }

    @Inject
    private ItemBuilder itemBuilder;

    @Inject
    private ItemDAO itemDAO;

    @Inject
    private PoolDAO poolDAO;

    @Inject
    private ProbabilityFetcher probabilityFetcher;

    /**
     * try to get an item from an item pool.
     *
     * @param id the id of the pool.
     * @return The itemStack or <b>ItemStack.empty()</b> if he you are unlucky.
     */
    @Override
    public Optional<ItemStack> fetch(String id) {
        Optional<PoolItemBean> poolItemBean = poolDAO.getPool(id).flatMap(pool -> probabilityFetcher.fetcher(pool.getItems()));
        if(poolItemBean.isPresent()){
            ItemStack item = poolItemBean.get().getItem();
            item.setQuantity(poolItemBean.get().getRandomQuantity());
            return Optional.of(item);
        }

        return Optional.empty();

    }


    /**
     * Get an item from Itemizer.
     *
     * @param id   id of the Itemizer item.
     * @param qte quantity of this item.
     * @return Return an itemStack with the represented item.
     */
    @Override
    public Optional<ItemStack> retrieve(String id,int qte) {
        Optional<ItemBean> optionalItem = itemDAO.getItem(id);
        return optionalItem.map(itemBean -> {
           ItemStack stack = itemBuilder.createItemStack(itemBean);
           stack.setQuantity(qte);
           return stack;
        });
    }

    @Override
    public Optional<ItemStack> retrieveBaseItem(String id) {
        Optional<ItemBean> optionalItem = itemDAO.getItem(id);
        return optionalItem.map(itemBean -> {
            ItemStack stack = itemBuilder.createBaseItem(itemBean);
            return stack;
        });
    }

    /**
     * Get an item from Itemizer.
     *
     * @param itemId   id of the Itemizer item.
     * @return Return an itemStack with the represented item.
     */
    @Override
    public Optional<ItemStack> retrieve(String itemId) {
        return retrieve(itemId,1);
    }

    @Override
    public ItemStack construct(ItemBean item) {
        return itemBuilder.createItemStack(item);
    }

    @Override
    public ItemStack constructBaseItem(ItemBean item) {
        return itemBuilder.createBaseItem(item);
    }

    /**
     * Check if a player have an Itemizer item.
     *
     * @param player   the target of your check.
     * @param id       the id of the item.
     * @param quantity the amount.
     * @return Return <b>true</b> if the targeted player have the item in enough quantity and <b>false</b> if he
     * * haven't
     * @throws ItemNotPresentException if the id does not exist
     */
    @Override
    public boolean hasItem(Player player, String id, int quantity) throws ItemNotPresentException {
        Inventory query = player.getInventory().query(QueryOperationTypes.ITEM_STACK_CUSTOM.of(itemStack1 -> {
            Optional<String> optionalId = itemStack1.get(ItemizerKeys.ITEM_ID);
            if (optionalId.isPresent() && optionalId.get().equals(id)) {
                return true;
            }
            return false;
        }));
        return query.capacity()>=quantity;
    }

    @Override
    public void register(String id, ItemStackSnapshot snapshot) {
        ItemBean itemBean = ItemBean.from(id,snapshot.createStack());
        itemDAO.save(itemBean);
    }

    @Override
    public void update(String id, ItemStackSnapshot snapshot) {
        //TODO implement update
        throw new NotImplementedException("sorry guys, I have not implemented this yet");
    }

    @Override
    public void instantiate(ItemStack itemStack, Location<World> location) {
        Extent extent = location.getExtent();
        Entity itemEntity = extent.createEntity(EntityTypes.ITEM, location.getPosition());
        itemEntity.offer(Keys.REPRESENTED_ITEM, itemStack.createSnapshot());
        location.spawnEntity(itemEntity);
    }
    /**
     * Instantiate an item entity in the world
     *
     * @param itemStack Item you want to summon
     * @param worldName the name of the world
     * @param x the x ordinate
     * @param y the y ordinate
     * @param z the z ordinate
     * @throws BadWorldNameException if the world with the given name does not exist
     */
    @Override
    public void instanciate(ItemStack itemStack, String worldName, double x, double y, double z) throws BadWorldNameException {
        World world = Sponge.getServer().getWorld(worldName).orElseThrow(BadWorldNameException::new);
        Location<World> location = world.getLocation(new Vector3d(x, y, z));
        instantiate(itemStack, location);
    }

    /**
     * Remove an item from player inventory
     * @param player the target
     * @param id the id
     * @param quantity the quantity to remove
     * @throws ItemNotPresentException
     */
    @Override
    public void removeItem(Player player, String id, Integer quantity) throws ItemNotPresentException {
        ItemStack itemStack = retrieve(id,quantity).orElseThrow(() -> new ItemNotPresentException(id));
        player.getInventory().query(QueryOperationTypes.ITEM_STACK_EXACT.of(itemStack)).clear();
    }
}
