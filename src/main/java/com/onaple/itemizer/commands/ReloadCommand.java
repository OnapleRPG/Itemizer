package com.onaple.itemizer.commands;

import com.onaple.itemizer.Itemizer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadCommand implements CommandExecutor {

    /**
     * Command that reloads configuration
     * @param args Arguments provided with the command
     * @return Command result state
     * @throws CommandException
     */
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Itemizer.getLogger().info("Reloading Itemizer configuration...");
        src.sendMessage(Text.builder("-----------------------------").color(TextColors.RED).build());
        try {
            int itemCount = Itemizer.getConfigurationHandler().readItemsConfiguration();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Items configuration successfully reloaded. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + itemCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" items loaded.").color(TextColors.GREEN).build())
                    .build());
        } catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("Items configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }

        try {
            int poolCount = Itemizer.getConfigurationHandler().readPoolsConfiguration();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Pools configuration successfully reloaded. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + poolCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" pools are loaded.").color(TextColors.GREEN).build())
                    .build());
        } catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("Pools configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }

        try {
            int craftCount = Itemizer.getConfigurationHandler().readCraftConfiguration();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Crafts configuration successfully reloaded. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + craftCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" crafts are loaded.").color(TextColors.GREEN).build())
                    .build());
            src.sendMessage(Text.builder("To includes new crafts in the games you must restart the server.")
                    .color(TextColors.GOLD).build());
        } catch (ObjectMappingException e) {
            src.sendMessage(Text.builder()
                    .append(Text.builder("Crafts configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .build());
            Itemizer.getLogger().error("Crafts configuration reload failed {}", e.getCause().getMessage());
        } catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("crafts configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }

        src.sendMessage(Text.builder("-----------------------------").color(TextColors.RED).build());
        return CommandResult.success();
    }
}