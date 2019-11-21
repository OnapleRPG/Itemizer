package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.PoolBean;
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
 * Player command to fetch an item from a pool defined in a configuration file
 */
public class FetchCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<PoolBean> pool = args.<PoolBean>getOne("pool");
        Optional<Player> targetOptional = args.getOne("player");
        Player target;
        if (targetOptional.isPresent()) {
            target = targetOptional.get();
        } else if (src instanceof Player) {
            target = (Player) src;
        } else {
            throw new CommandException(Text.builder(src.getName() + "is not a valid player").toText());
        }
        if (pool.isPresent()) {
            ItemStack optionalItem = pool.get().fetch();
            if (optionalItem.isEmpty()) {
                src.sendMessage(Text.of("Bad luck! Pool " + pool.get().getId() + " returned nothing."));
            } else {
                target.getInventory().offer(optionalItem);

            }
            return CommandResult.success();
        } else {
            Itemizer.getLogger().warn("This item pool does not exist.");
        }
        return CommandResult.empty();
    }
}
