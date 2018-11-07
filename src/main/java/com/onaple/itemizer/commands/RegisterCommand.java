package com.onaple.itemizer.commands;


import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemDeconstructor;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

public class RegisterCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if( src instanceof Player){
            Optional<ItemStack> itemStackOptional = ((Player) src).getItemInHand(HandTypes.MAIN_HAND);
            if(itemStackOptional.isPresent()){
                ItemStack itemToRegister = itemStackOptional.get();
                ItemBean itemRegistred = new ItemDeconstructor().register(itemToRegister);
                Itemizer.getConfigurationHandler().getItemList().add(itemRegistred);
            }
        }
        return CommandResult.success();
    }
}
