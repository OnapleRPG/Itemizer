package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Random;

@ConfigSerializable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PoolItemBean {
    private static final Random RANDOM = new Random();

    @Setting("probability")
    private Double probability;

    @Setting("maxQuantity")
    private Integer higherQuantityBound = 1;

    @Setting("minQuantity")
    private Integer lowerQuantityBound = 1;

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
