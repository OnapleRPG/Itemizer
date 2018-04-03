package com.ylinor.itemizer.commands;

import com.ylinor.itemizer.Itemizer;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

public class ReloadCommand implements CommandExecutor {
    public ReloadCommand() {
    }

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
            int itemCount = Itemizer.getItemizer().loadItems();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Items configuration reload successful. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + itemCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" dialogs are loaded.").color(TextColors.GREEN).build())
                    .build());
        } catch (ObjectMappingException e) {
            src.sendMessage(Text.builder()
                    .append(Text.builder("Items configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());

        }
        catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("Items configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }

        try {
            int minersCount = Itemizer.getItemizer().loadMiners();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Miners configuration reload successful. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + minersCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" miners are loaded.").color(TextColors.GREEN).build())
                    .build());
        } catch (ObjectMappingException e) {
            src.sendMessage(Text.builder()
                    .append(Text.builder("Miners configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }
        catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("Miners configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }

        try {
            int poolCount = Itemizer.getItemizer().loadPools();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Pools configuration reload successful. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + poolCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" pools are loaded.").color(TextColors.GREEN).build())
                    .build());
        } catch (ObjectMappingException e) {
            src.sendMessage(Text.builder()
                    .append(Text.builder("Pools configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }
        catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("Pools configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }


        try {
            int craftCount = Itemizer.getItemizer().loadCrafts();
            src.sendMessage(Text.builder()
                    .append(Text.builder("Crafts configuration reload successful. ").color(TextColors.GREEN).build())
                    .append(Text.builder("" + craftCount).color(TextColors.GOLD).build())
                    .append(Text.builder(" crafts are loaded.").color(TextColors.GREEN).build())
                    .build());
            src.sendMessage(Text.builder("To includes new crafts in the games you must restart the server.")
                    .color(TextColors.GOLD).build());
        } catch (ObjectMappingException e) {
            src.sendMessage(Text.builder()
                    .append(Text.builder("Crafts configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }
        catch (Exception e){
            src.sendMessage(Text.builder()
                    .append(Text.builder("crafts configuration reload failed. ").color(TextColors.DARK_RED).build())
                    .append(Text.builder(e.getMessage()).color(TextColors.RED).build())
                    .build());
        }

        src.sendMessage(Text.builder("-----------------------------").color(TextColors.RED).build());
        return CommandResult.success();
    }
}