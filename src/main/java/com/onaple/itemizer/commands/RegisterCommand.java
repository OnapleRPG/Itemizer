package com.onaple.itemizer.commands;


import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemRegistery;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.util.Optional;

public class RegisterCommand implements CommandExecutor {

    @Inject
    private ItemRegistery itemRegistery;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if( src instanceof Player){
            String itemId = args.<String>getOne("id").orElseThrow(
                    () -> new CommandException(Text.of("You must specify an id")));
            if(Itemizer.getConfigurationHandler().getItemList()
                    .stream()
                    .anyMatch(itemBean -> itemBean.getId().equals(itemId)
                    )){
                throw new CommandException(Text.of("Id already exist"));
            }
            Optional<ItemStack> itemStackOptional = ((Player) src).getItemInHand(HandTypes.MAIN_HAND);
            if(itemStackOptional.isPresent()){
                ItemStack itemToRegister = itemStackOptional.get();
                ItemBean itemRegistred = itemRegistery.register(itemId,itemToRegister);
                Itemizer.getConfigurationHandler().getItemList().add(itemRegistred);

                ((Player) src).sendMessage(Text.of( "Object succesfully added to the database with the index "
                        + itemRegistred.getId()));
            }
        }
        return CommandResult.success();
    }
}
