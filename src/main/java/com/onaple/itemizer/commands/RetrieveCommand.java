package com.onaple.itemizer.commands;

import com.onaple.itemizer.data.beans.ItemBean;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.Optional;

/**
 * Player command to retrieve an item from a configuration file
 */
public class RetrieveCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<ItemBean> optionalItem = args.getOne("id");
        Optional<Player> targetOptional = args.getOne("player");
        Optional<Integer> amountOptional = args.getOne("quantity");
        Player target;
        if (targetOptional.isPresent()) {
            target = targetOptional.get();
        } else {
            if (src instanceof Player) {
                target = (Player) src;
            } else {
                src.sendMessage(Text.of("Target must be a player."));
                return CommandResult.empty();
            }
        }
        if (optionalItem.isPresent()) {
            ItemBean item = optionalItem.get();
            ItemStack itemStack = item.getItemStackSnapshot().createStack();
            amountOptional.ifPresent(itemStack::setQuantity);
            target.getInventory().offer(itemStack);
        }

        return CommandResult.empty();
    }
}
