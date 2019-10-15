package com.onaple.itemizer.data.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.Set;
import java.util.TreeSet;

@ConfigSerializable
@Data
@NoArgsConstructor
public class ItemBean {

    /** ID of the item in config **/
    @Setting("id")
    private String id;

    @Setting("item")
    private ItemStackSnapshot itemStackSnapshot;

    @Setting("thirdParties")
    private Set<ItemNbtFactory> thirdParties = new TreeSet<>();

}

