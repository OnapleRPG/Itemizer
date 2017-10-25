package com.ylinor.itemizer.utils;

import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.data.beans.ItemBean;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Feedthecookie
 */
public class ItemBuilder {
    /**
     * Build an itemstack from an ItemBean
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public static Optional<ItemStack> buildItemStack(ItemBean itemBean) {
        Optional<ItemType> optionalType = Sponge.getRegistry().getType(ItemType.class, itemBean.getType());
        if (optionalType.isPresent()) {
            ItemStack itemStack = ItemStack.builder().itemType(optionalType.get()).build();
            itemStack = ItemBuilder.defineItemStack(itemStack, itemBean);
            itemStack = ItemBuilder.enchantItemStack(itemStack, itemBean);
            return Optional.ofNullable(itemStack);
        } else {
            Itemizer.getLogger().warn("Unknown item type : " + itemBean.getType());
        }
        return Optional.empty();
    }

    /**
     * Define the characteristics of an ItemStack from an ItemBean
     * @param itemStack Item to edit
     * @param itemBean Data of the item to define
     * @return ItemStack edited
     */
    private static ItemStack defineItemStack(ItemStack itemStack, ItemBean itemBean) {
        // Item name
        if (itemBean.getName() != null && !itemBean.getName().isEmpty()) {
            itemStack.offer(Keys.DISPLAY_NAME, Text.of(itemBean.getName()));
        }
        // Item lore
        List<Text> lore = new ArrayList<>();
        for (String loreLine : itemBean.getLore().split("\n")) {
            lore.add(Text.of(loreLine));
        }
        itemStack.offer(Keys.ITEM_LORE, lore);
        // Item attributes
        itemStack.offer(Keys.UNBREAKABLE, itemBean.isUnbreakable());
        return itemStack;
    }

    /**
     * Enchant an ItemStack with an ItemBean data
     * @param itemStack Item to enchant
     * @param itemBean Data of the item to enchant
     * @return Enchanted (or not) ItemStack
     */
    private static ItemStack enchantItemStack(ItemStack itemStack, ItemBean itemBean) {
        EnchantmentData enchantmentData = itemStack.getOrCreate(EnchantmentData.class).get();
        for (Map.Entry<String, Integer> enchant : itemBean.getEnchants().entrySet()) {
            Optional<Enchantment> optionalEnchant = Sponge.getRegistry().getType(Enchantment.class, enchant.getKey());
            if (optionalEnchant.isPresent()) {
                enchantmentData.set(enchantmentData.enchantments().add(new ItemEnchantment(optionalEnchant.get(), enchant.getValue())));
            } else {
                Itemizer.getLogger().warn("Unknown enchant : " + enchant.getKey());
            }
        }
        itemStack.offer(enchantmentData);
        return itemStack;
    }
}
