package com.ylinor.itemizer.utils;

import com.google.common.collect.Maps;
import com.ylinor.itemizer.data.beans.ItemBean;
import org.apache.commons.lang3.Validate;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.data.manipulator.mutable.item.LoreData;
import org.spongepowered.api.data.meta.ItemEnchantment;
import org.spongepowered.api.data.value.mutable.ListValue;
import org.spongepowered.api.item.Enchantment;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author Feedthecookie
 */
public class ItemBuilder {

    private ItemType itemTypes;
    private int amount = 1;
    private byte data;
    private String name;
    private boolean glow = false;
    private boolean canDestroy = true;
    private boolean canPlace = true;
    private ListValue<Text> lore;
    private Map<Enchantment, Integer> enchants = Maps.newHashMap();

    private ItemStack item;
    private LoreData loreData;
    private EnchantmentData enchantmentData;

    public ItemBuilder(ItemType itemTypes) {
        this.itemTypes = itemTypes;
        item = ItemStack.builder().itemType(itemTypes).build();
        loreData = Sponge.getDataManager().getManipulatorBuilder(LoreData.class).get().create();
        lore = loreData.lore();
        enchantmentData = item.getOrCreate(EnchantmentData.class).get();
    }

    public ItemBuilder(ItemStack item) {
        this.itemTypes = item.getItem();
        this.amount = item.getQuantity();
        this.name = item.get(Keys.DISPLAY_NAME).orElse(Text.of(item.getTranslation().get())).toPlain();
        this.glow = item.get(Keys.GLOWING).orElse(false);
        this.loreData = Sponge.getDataManager().getManipulatorBuilder(LoreData.class).get().create();
        this.lore = loreData.lore();
        this.lore.addAll(item.get(Keys.ITEM_LORE).get());
        this.enchantmentData = item.getOrCreate(EnchantmentData.class).get();
        this.enchantmentData.addElements(item.get(Keys.ITEM_ENCHANTMENTS).get());
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setData(int data) {
        this.data = (byte) data;
        return this;
    }

    public ItemBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder addLore(Text lore) {
        this.lore.add(lore);
        return this;
    }

    public ItemBuilder setLore(List<Text> lore) {
        this.lore.set(lore);
        return this;
    }

    public ItemBuilder addEnchantment(Enchantment enchant, int level) {
        this.enchants.put(enchant, level);
        return this;
    }

    public ItemBuilder setEnchantments(Map<Enchantment, Integer> enchants) {
        this.enchants = enchants;
        return this;
    }

    public boolean isGlowing() {
        return this.glow;
    }

    public ItemBuilder setGlowing(boolean glow) {
        this.glow = glow;
        return this;
    }

    public ItemStack buildItemStack() {

        Validate.notNull(this.itemTypes, "Material cannot be null");
        ItemStack item = ItemStack.builder().itemType(itemTypes).build();

        if (this.name != null)
            item.offer(Keys.DISPLAY_NAME, Text.of(this.name));
        if (this.lore != null && !this.lore.isEmpty()) {
            loreData.set(lore);
            item.offer(loreData);
        }
        if (!this.enchants.isEmpty()) {
            for (Enchantment enchantment : enchants.keySet()) {
                enchantmentData.set(enchantmentData.enchantments()
                        .add(new ItemEnchantment(enchantment, enchants.get(enchantment))));
            }
        }

        item.offer(Keys.HIDE_CAN_DESTROY, canDestroy);
        item.offer(Keys.HIDE_CAN_PLACE, canPlace);
        item.offer(Keys.GLOWING, glow);

        return item;
    }

    public ItemBuilder setCanDestroy(boolean canDestroy) {
        this.canDestroy = canDestroy;
        return this;
    }

    public ItemBuilder setCanPlace(boolean canPlace) {
        this.canPlace = canPlace;
        return this;
    }

    /**
     * Build an itemstack from an ItemBean
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public static Optional<ItemStack> buildItemStack(ItemBean itemBean) {
        Optional<ItemType> optionalType = Sponge.getRegistry().getType(ItemType.class, itemBean.getType());
        if (optionalType.isPresent()) {
            ItemStack itemStack = ItemStack.builder().itemType(optionalType.get()).build();
            return Optional.ofNullable(itemStack);
        }
        return Optional.empty();
    }
}
