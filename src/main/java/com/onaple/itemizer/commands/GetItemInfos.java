package com.onaple.itemizer.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;
import java.util.Optional;

@SuppressWarnings("unchecked")
public class GetItemInfos implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if (src instanceof Player){
            Optional<ItemStack> objectInHandOpt = ((Player) src).getItemInHand(HandTypes.MAIN_HAND);
            if (!objectInHandOpt.isPresent()){
                src.sendMessage(Text.of("You don't have any item in hand, to analyse a item put it in your main hand"));
                return CommandResult.empty();
            } else {
                ItemStack objectToAnalyse = objectInHandOpt.get();
                DataContainer itemData = objectToAnalyse .toContainer();
                Optional<Map<?,?>> datamap = (Optional<Map<?, ?>>) itemData.getMap(DataQuery.of("UnsafeData"));
                datamap.ifPresent(stringObjectMap -> stringObjectMap.forEach((s, o) -> ((Player) src).sendMessage(Text.builder(s + " : ").color(TextColors.GREEN).append(
                        Text.builder(o.toString()).color(TextColors.GOLD).build()
                ).build())));
               return CommandResult.success();
            }
        }
        return CommandResult.empty();
    }
}
