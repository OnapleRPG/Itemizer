package com.onaple.itemizer.utils;

import com.onaple.itemizer.Itemizer;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.spongepowered.api.item.inventory.ItemStack;


@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ItemService {
    public static ItemStack retrieve(String retrieve){
        return Itemizer.getItemService().retrieve(retrieve).orElse(null);
    }
}
