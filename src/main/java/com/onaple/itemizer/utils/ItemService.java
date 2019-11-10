package com.onaple.itemizer.utils;

import com.flowpowered.math.vector.Vector3d;
import com.onaple.itemizer.Itemizer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemService {
    public static ItemStack retrieve(String retrieve){
        return Itemizer.getItemService().retrieve(retrieve).orElse(null);
    }
    public static void instanciate(ItemStack itemStack,String worldName, double x,double y,double z) throws Exception {
        World world = Sponge.getServer().getWorld(worldName).orElseThrow(Exception::new);
        Location<World> location = world.getLocation(new Vector3d(x,y,z));
        Itemizer.getItemService().instantiate(itemStack,location);
    }
}
