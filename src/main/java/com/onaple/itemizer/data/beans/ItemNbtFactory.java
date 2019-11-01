package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulator;
import org.spongepowered.api.data.manipulator.mutable.item.LoreData;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@ConfigSerializable
public interface ItemNbtFactory extends Comparable<ItemNbtFactory> {
    /**
     * Return the Key
     * @return
     */
    Key getKey();

    /**
     * Return the name of the Third Party
     * @return
     */
    String getName();

    /**
     * Apply the data to the item and update the lore
     * @param itemStack The item you want to apply the data
     */
    default void apply(ItemStack itemStack) {
       itemStack.offer(constructDataManipulator());
       if (this.getLore().isEmpty()){
           return;
       }
       Optional<LoreData> dataOptional = itemStack.get(LoreData.class);
       if(dataOptional.isPresent()) {
        LoreData data= dataOptional.get();
           data.addElement(Text.join(
                   Text.builder("--==#|| ").color(TextColors.GOLD).build(),
                   Text.builder(this.getName()).color(TextColors.GREEN).build(),
                   Text.builder(" ||#==--").color(TextColors.GOLD).build())
           );
           data.addElements(this.getLore());
           itemStack.offer(data);
       }
    }

    /**
     * get the dataManipulator to set to the item
     * @return
     */
    DataManipulator<?,?> constructDataManipulator();

    /**
     * return the list of the the lore
     * @return list of text line to set to the object
     */
    default List<Text> getLore(){
        return Collections.emptyList();
    }

}
