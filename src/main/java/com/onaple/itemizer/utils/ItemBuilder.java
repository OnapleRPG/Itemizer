package com.onaple.itemizer.utils;

import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemNbtFactory;
import com.onaple.itemizer.data.beans.affix.AffixFactory;
import com.onaple.itemizer.probability.ProbabilityFetcher;
import org.slf4j.Logger;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

@Singleton
public class ItemBuilder {

    @Inject
    Logger logger;


    @Inject
    private ProbabilityFetcher probabilityFetcher;

    private GlobalConfig globalConfig;

    private static final Random RANDOM = new Random();

    /**
     * Build an itemstack from an ItemBean
     *
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public ItemStack buildItemStack(ItemBean itemBean) {
        ItemStack item = itemBean.getItemStackSnapshot().createStack();
        setCustomDatamanipulators(item, itemBean.getThirdParties());
        item = applyAffix(item, itemBean);
        rewrite(item);
        return item;
    }

    private ItemStack rewrite(ItemStack itemStack) {
        globalConfig = Itemizer.getGlobalConfig();
        List<Text> lore = itemStack.get(Keys.ITEM_LORE).orElse(new ArrayList<>());
        itemStack.offer(Keys.HIDE_ATTRIBUTES, globalConfig.getHiddenFlags().get("Attributes_modifiers"));
        itemStack.offer(Keys.HIDE_CAN_DESTROY, globalConfig.getHiddenFlags().get("CanDestroy"));
        itemStack.offer(Keys.HIDE_CAN_PLACE, globalConfig.getHiddenFlags().get("CanPlaceOn"));
        if(globalConfig.getHiddenFlags().get("Enchantments")){
            itemStack.offer(Keys.HIDE_ENCHANTMENTS, true);
            rewriteEnchantment(itemStack,lore);
        }
        itemStack.offer(Keys.HIDE_MISCELLANEOUS, globalConfig.getHiddenFlags().get("Others"));
        if(globalConfig.getHiddenFlags().get("Unbreakable")){
            itemStack.offer(Keys.HIDE_UNBREAKABLE, true);
            rewirteUnbreakable(itemStack,lore);
        }
        itemStack.offer(Keys.ITEM_LORE,lore);
        return itemStack;
    }

    private void rewirteUnbreakable(ItemStack itemStack, List<Text> lore) {
        if (itemStack.get(Keys.UNBREAKABLE).orElse(false)) {
            TextColor unbreakableColor = globalConfig.getColorMap().getOrDefault("unbreakable", TextColors.WHITE);
            lore.add(Text.of(globalConfig.getUnbreakableRewrite(), unbreakableColor));
        }
    }

    private void rewriteEnchantment(ItemStack itemStack, List<Text> lore) {
        List<Enchantment> enchantments = itemStack.get(Keys.ITEM_ENCHANTMENTS).orElse(Collections.emptyList());
        TextColor enchantColor = globalConfig.getColorMap().getOrDefault("enchantments", TextColors.WHITE);
        for (Enchantment enchantment : enchantments) {
            String enchantmentName = globalConfig.getEnchantRewrite().get(enchantment.getType());
            if (enchantmentName != null) {
                lore.add(Text.of(enchantmentName + " " + enchantment.getLevel(), enchantColor));
            }
        }
    }

    private void rewriteAttribute(ItemStack itemStack) {

    }


    private void setCustomDatamanipulators(ItemStack item, Set<ItemNbtFactory> thirdpartyConfigs) {
        for (ItemNbtFactory nbtFactory : thirdpartyConfigs) {
            nbtFactory.apply(item);
        }
    }

    public ItemBean registerItem(String id, ItemStack stack) {
        ItemBean itemRegistered = ItemBean.from(id, stack);
        Itemizer.getConfigurationHandler().getItemList().add(itemRegistered);
        Itemizer.getConfigurationHandler().saveItemConfig();
        return itemRegistered;
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
