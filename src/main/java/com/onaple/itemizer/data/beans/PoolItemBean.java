package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import com.onaple.itemizer.probability.Probable;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Random;

@ConfigSerializable

public class PoolItemBean implements Probable {

    public PoolItemBean() {
    }

    public PoolItemBean(double probability, int higherQuantityBound, int lowerQuantityBound, ItemStack item) {
        this.probability = probability;
        this.higherQuantityBound = higherQuantityBound;
        this.lowerQuantityBound = lowerQuantityBound;
        this.item = item;
    }

    public static Random getRANDOM() {
        return RANDOM;
    }

    @Override
    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

    public int getHigherQuantityBound() {
        return higherQuantityBound;
    }

    public void setHigherQuantityBound(int higherQuantityBound) {
        this.higherQuantityBound = higherQuantityBound;
    }

    public int getLowerQuantityBound() {
        return lowerQuantityBound;
    }

    public void setLowerQuantityBound(int lowerQuantityBound) {
        this.lowerQuantityBound = lowerQuantityBound;
    }

    public ItemStack getItem() {
        return item;
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    private static final Random RANDOM = new Random();

    @Setting("probability")
    private double probability;

    @Setting("maxQuantity")
    private int higherQuantityBound = 1;

    @Setting("minQuantity")
    private int lowerQuantityBound = 1;

    @Setting("item")
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack item;


    public int getRandomQuantity(){
        int quantity = this.getLowerQuantityBound();
        if (this.getHigherQuantityBound() > this.getLowerQuantityBound()) {
            quantity += RANDOM.nextInt(this.getHigherQuantityBound() - this.getLowerQuantityBound());
        }
        return quantity;
    }

}
