package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
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
        if (src instanceof Player) {
            String itemId = args.<String>getOne("id").orElse("");
            try {
                int id = Integer.parseInt(itemId);
                Optional<ItemBean> optionalItem = ItemDAO.getItem(id);
                if (optionalItem.isPresent()) {
                    Optional<ItemStack> optionalItemStack = ItemBuilder.buildItemStack(optionalItem.get());
                    if (optionalItemStack.isPresent()) {
                        ((Player) src).getInventory().offer(optionalItemStack.get());
                    } else {
                        ((Player) src).sendMessage(Text.of("Item " + id + " not valid."));
                    }
                } else {
                    ((Player) src).sendMessage(Text.of("Item " + id + " not found."));
                }
            } catch (NumberFormatException e) {
                ((Player) src).sendMessage(Text.of("Item id must be numeric."));
            }
        } else {
            Itemizer.getLogger().warn("Retrieve command can only be executed by a player.");
        }
        return CommandResult.empty();
    }
}
