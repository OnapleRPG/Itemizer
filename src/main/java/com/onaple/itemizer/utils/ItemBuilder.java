package com.onaple.itemizer.utils;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemNbtFactory;
import com.onaple.itemizer.data.beans.affix.AffixFactory;
import com.onaple.itemizer.probability.ProbabilityFetcher;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Singleton
public class ItemBuilder {

    @Inject
    java.util.logging.Logger logger;

    @Inject
    private ProbabilityFetcher probabilityFetcher;

    private static final Random RANDOM = new Random();

    /**
     * Build an itemstack from an ItemBean
     *
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public ItemStack buildItemStack(ItemBean itemBean) {
        ItemStack item = itemBean.getItemStackSnapshot().createStack();
        Optional<String> stringOptional = item.get(ItemizerKeys.ITEM_ID);
        setCustomDatamanipulators(item, itemBean.getThirdParties());
        item = applyAffix(item, itemBean);
        return item;
    }


    private void setCustomDatamanipulators(ItemStack item, Set<ItemNbtFactory> thirdpartyConfigs) {
        for (ItemNbtFactory nbtFactory : thirdpartyConfigs) {
            nbtFactory.apply(item);
        }
    }

    public ItemBean registerItem(String id, ItemStack stack) {
        ItemBean itemRegistred = ItemBean.from(id, stack);
        Itemizer.getConfigurationHandler().getItemList().add(itemRegistred);
        Itemizer.getConfigurationHandler().saveItemConfig();
        return itemRegistred;
    }

    private ItemStack applyAffix(ItemStack itemStack, ItemBean item) {
        if (Objects.nonNull(item.getAffix())) {
            Optional<AffixFactory> optionalAffixFactory = probabilityFetcher.fetcher(item.getAffix().getTiers());
            if (optionalAffixFactory.isPresent()) {
                itemStack = optionalAffixFactory.get().apply(itemStack);
            }
        }
        return itemStack;
    }
}
