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

    /** ID of the pool in config **/
    @Setting("id")
    private String id;

    /** List of items & quantity with linked probability **/
    @Setting("items")
    @AsCollectionImpl(ArrayList.class)
    private List<PoolItemBean> items;
  }
