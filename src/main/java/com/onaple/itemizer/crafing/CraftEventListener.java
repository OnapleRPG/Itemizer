package com.onaple.itemizer.crafing;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.crafing.event.CraftSuccessfulEvent;
import com.onaple.itemizer.crafing.event.RowCraftEvent;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.filter.cause.First;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class CraftEventListener {

    @Listener
    public void onCraft(RowCraftEvent event, @First Player player){
        Inventory craftResultSlot = player.getOpenInventory().get()
                .query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(8)));
        if(event.getResult().isPresent()) {
            craftResultSlot.set(event.getResult().get());
        } else {
            craftResultSlot.set(CraftService.SEPARATOR.createStack());
        }

    }

    @Listener
    public void onSuccessfulCraft(CraftSuccessfulEvent event,@First Inventory inventory){

        Optional<String> idOptional = event.getCraftResult().get(ItemizerKeys.ITEM_ID);
        if(idOptional.isPresent()){
            Optional<ItemStack> retrieve = Itemizer.getItemService().retrieve(idOptional.get());
            event.setCraftResult(retrieve.get());
        }
        }

}
