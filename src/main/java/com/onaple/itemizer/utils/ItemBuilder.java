package com.onaple.itemizer.utils;

import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemNbtFactory;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Singleton;
import java.util.Set;

@Singleton
public class ItemBuilder {

    /**
     * Build an itemstack from an ItemBean
     *
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public ItemStack buildItemStack(ItemBean itemBean) {

        ItemStack item = itemBean.getItemStackSnapshot().createStack();
        item.offer(ItemizerKeys.ITEM_ID,itemBean.getId());
        setCustomDatamanipulators( item, itemBean.getThirdParties());
        return item;
    }


    private void setCustomDatamanipulators(ItemStack item, Set<ItemNbtFactory> thirdpartyConfigs ) {
        for (ItemNbtFactory nbtFactory : thirdpartyConfigs) {
            nbtFactory.apply(item);
        }
    }
}
