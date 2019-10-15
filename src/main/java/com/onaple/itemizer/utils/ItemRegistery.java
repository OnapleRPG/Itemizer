package com.onaple.itemizer.utils;

import com.onaple.itemizer.data.beans.ItemBean;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemRegistery {

    @Inject
    ItemManager itemManager;

    public ItemBean register(String itemId,ItemStack itemStack) {
        itemManager.setId(itemStack,itemId);
        ItemBean itemBean = new ItemBean();
        itemBean.setId(itemId);
        //item type
        itemBean.setItemStackSnapshot(itemStack.createSnapshot());
        return itemBean;
    }
}
