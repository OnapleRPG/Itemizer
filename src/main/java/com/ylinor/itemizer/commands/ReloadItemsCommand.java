package com.ylinor.itemizer.commands;

import com.ylinor.itemizer.data.handlers.ConfigurationHandler;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.config.ConfigDir;

import javax.inject.Inject;
import java.nio.file.Path;


public class ReloadItemsCommand implements CommandExecutor {
    @Inject
    @ConfigDir(sharedRoot=true)
    private Path configDir;
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        ConfigurationHandler.readItemsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_items.conf"));
        ConfigurationHandler.readMinerConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_miners.conf"));
        ConfigurationHandler.readPoolsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_pools.conf"));
        return CommandResult.success();
    }
}
