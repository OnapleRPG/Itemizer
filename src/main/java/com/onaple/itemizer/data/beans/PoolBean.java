package com.onaple.itemizer.data.beans;

import cz.neumimto.config.blackjack.and.hookers.annotations.AsCollectionImpl;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

@ConfigSerializable
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PoolBean {

    private static final Random RANDOM = new Random();

    /** ID of the pool in config **/
    @Setting("id")
    private String id;

    /** List of items & quantity with linked probability **/
    @Setting("items")
    @AsCollectionImpl(ArrayList.class)
    private List<PoolItemBean> items;


    /**
     * Retrieve an optional random item from a pool. if the pool does not exist you will get
     * an empty Optional. and if the pool return nothing you will get an empty ItemStack.
     *
     * @return Random ItemStack based on configuration probability. Might be Optional.empty()
     * if the pool don't exist or ItemStack.empty() if the pool return nothing
     */
    public ItemStack fetch() {
        ItemStack item = ItemStack.empty();
        double randomValue = RANDOM.nextDouble();
        Iterator<PoolItemBean> iterator = this.getItems().iterator();
        double accumulatedProbabilities = 0;
        while (item.isEmpty() && iterator.hasNext()) {
            PoolItemBean poolItem = iterator.next();
            accumulatedProbabilities += poolItem.getProbability();
            if (randomValue <= accumulatedProbabilities) {
                item = poolItem.getItem();

                item.setQuantity(poolItem.getRandomQuantity());
            }
        }
        return item;
    }
  }
