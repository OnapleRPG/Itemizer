package com.onaple.itemizer.utils;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.exception.ItemNotPresentException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;



@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemService {

    /**
     * Get an item from Itemizer.
     *
     * @param itemId   id of the Itemizer item.
     * @param quantity quantity of this item.
     * @return Return an itemStack with the represented item.
     * @throws ItemNotPresentException throw exception when the id does not exit
     */
    public static ItemStack retrieve(String itemId, Integer quantity) throws ItemNotPresentException {
        ItemStack item = Itemizer.getItemService().retrieve(itemId).orElseThrow(() -> new ItemNotPresentException(itemId));
        item.setQuantity(quantity);
        return item;
    }

    /**
     * try to get an item from an item pool.
     *
     * @param poolId the id of the pool.
     * @return The itemStack or <b>null</b> if he you are unlucky.
     */
    public static ItemStack fetch(String poolId) {
        return Itemizer.getItemService().fetch(poolId).orElse(null);
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
    public boolean hasItem(Player player, String id, Integer quantity) throws ItemNotPresentException {
        return Itemizer.getItemService().hasItem(player, id, quantity);
    }

    /**
     * Instantiate an item entity in the world
     *
     * @param itemStack Item you want to summon
     * @param worldName the name of the world
     * @param x the x ordinate
     * @param y the y ordinate
     * @param z the z ordinate
     * @throws Exception if the world with the given name does not exist
     */
    public static void instanciate(ItemStack itemStack, String worldName, double x, double y, double z) throws Exception {
        World world = Sponge.getServer().getWorld(worldName).orElseThrow(Exception::new);
        Location<World> location = world.getLocation(new Vector3d(x, y, z));
        Itemizer.getItemService().instantiate(itemStack, location);
    }
}
