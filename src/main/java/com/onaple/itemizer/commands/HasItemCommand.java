package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.exception.ItemNotPresentException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Arrays;
import java.util.Optional;

public class HasItemCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> refOptional = args.getOne(Text.of("reference"));
        if (refOptional.isPresent()) {
            Optional<Player> playerOptional = args.getOne(Text.of("player"));
            if (playerOptional.isPresent()) {
                Integer quantity = args.<Integer>getOne(Text.of("quantity")).orElse(1);
                try {
                   boolean hasItem = Itemizer.getItemService().hasItem(playerOptional.get(),refOptional.get(),quantity);
                   if (hasItem){
                       src.sendMessage(Text.builder("Player")
                               .append(Arrays.asList(
                                       Text.builder(playerOptional.get().getName()).build(),
                                       Text.builder("have the item ").color(TextColors.GREEN).build(),
                                       Text.builder(refOptional.get()).build()
                       )).build());
                   } else {
                       src.sendMessage(Text.builder("Player").color(TextColors.GREEN)
                               .append(Arrays.asList(
                                       Text.builder(playerOptional.get().getName()).build(),
                                       Text.builder("don't have the item ").color(TextColors.GREEN).build(),
                                       Text.builder(refOptional.get()).build()
                               )).build());
                   }
                } catch (ItemNotPresentException e) {
                    Itemizer.getLogger().error("error while test if player have item",e);
                    src.sendMessage(Text.builder("the ref does not exist").color(TextColors.RED).build());
                    return CommandResult.empty();
                }
            }
        }
        return CommandResult.success();
    }
}
