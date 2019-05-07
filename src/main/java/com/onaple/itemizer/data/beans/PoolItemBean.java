package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public class PoolItemBean {

    @Setting("probability")
    private Double probability;

    @Setting("quantity")
    private Integer quantity;

    @Setting("item")
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack item;

    public PoolItemBean() {
        quantity = 1;
    }

    public PoolItemBean(Double probability, ItemStack item, Integer quantity) {
        this.probability = probability;
        this.item = item;
        this.quantity = quantity;
    }

    public ItemStack getItem() {
        return item;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public Double getProbability() {
        return probability;
    }
}
