package com.ylinor.itemizer.commands;


import com.google.common.collect.ImmutableList;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.Container;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.*;


public class GetUnsafeDataCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
            Player player = (Player) src;
           Optional<ItemStack> itemOptional = player.getItemInHand(HandTypes.MAIN_HAND);
           if(itemOptional.isPresent()){
               itemOptional.get().getContainers().forEach(dataManipulator -> dataManipulator.getKeys().forEach(key -> src.sendMessage(Text.of(key))));

               LinkedList<DataContainer> nbt_map = new LinkedList();
               Optional<Object> mapOptional = itemOptional.get().toContainer().get(DataQuery.of("UnsafeData","AttributeModifiers"));
               if(mapOptional.isPresent()) {
                   player.sendMessage(Text.of(mapOptional.get().toString()));
                   /* nbt_map.addAll((ImmutableList)mapOptional.get());
                   LinkedList<DataContainer> newMap = new LinkedList();
                   DataContainer dataContainer = DataContainer.createNew();
                   dataContainer.set()
                   for (DataContainer dc:nbt_map) {
                       src.sendMessage(Text.of(dc));
                       dc.set(DataQuery.of("Amount"),15);


                     newMap.add(dc);

                   }
                   ItemStack item = itemOptional.get();


                   item.toContainer().set(DataQuery.of("UnsafeData","AttributeModifiers"),newMap);

                   ((Player) src).setItemInHand(HandTypes.OFF_HAND,item);*/



               } else {
                   src.sendMessage(Text.of("No unsafe data"));
               }
           }
           return CommandResult.success();
        }
        return CommandResult.empty();
    }
}
