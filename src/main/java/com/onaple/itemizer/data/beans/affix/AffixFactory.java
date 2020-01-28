package com.onaple.itemizer.data.beans.affix;

import com.onaple.itemizer.probability.Probable;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStack;

@ConfigSerializable
public interface AffixFactory extends Probable {

    ItemStack apply(ItemStack itemStack);

}
