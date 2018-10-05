package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import com.onaple.itemizer.utils.PoolFetcher;
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
        if (src instanceof Player) {
            String poolId = args.<String>getOne("id").orElse("");
            Optional<Player> targetOptional = args.getOne("player");
            Player target ;
            if (targetOptional.isPresent()){
                target = targetOptional.get();
            } else {
                if(src instanceof Player){
                    target = (Player) src;
                } else{
                    src.sendMessage(Text.of("Target must be a player."));
                    return CommandResult.empty();
                }
            }
            Optional<ItemBean> optionalItem = PoolFetcher.fetchItemFromPool(poolId);
            if (optionalItem.isPresent()) {
                Optional<ItemStack> optionalItemStack = new ItemBuilder().buildItemStack(optionalItem.get());
                if (optionalItemStack.isPresent()) {
                    target.getInventory().offer(optionalItemStack.get());
                } else {
                    ((Player) src).sendMessage(Text.of("Item from pool " + poolId + " not valid."));
                }
            } else {
                ((Player) src).sendMessage(Text.of("Pool " + poolId + " returned nothing."));
            }
        } else {
            Itemizer.getLogger().warn("Fetch command can only be executed by a player.");
        }
        return CommandResult.empty();
    }
}
