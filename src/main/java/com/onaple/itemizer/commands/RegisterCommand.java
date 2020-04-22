package com.onaple.itemizer.commands;


import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.Optional;

@Singleton
public class RegisterCommand implements CommandExecutor {

    @Inject
    ItemBuilder builder;


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
                if(itemStackOptional.get().get(ItemizerKeys.ITEM_ID).isPresent()) {
                    throw new CommandException(Text.of("Item already registered"));
                }
                ItemBean itemRegistered = builder.registerItem(itemId, itemStackOptional.get());
                ItemStack buildItemStack = builder.createItemStack(itemRegistered);
                ((Player) src).setItemInHand(HandTypes.MAIN_HAND,buildItemStack);
                src.sendMessage(Text.builder( "Item successfully added to the configuration with the ID :")
                        .append(Text.of(TextColors.GOLD, TextStyles.BOLD,itemRegistered.getId()))
                        .build());
            }
        }
        return CommandResult.success();
    }
}
