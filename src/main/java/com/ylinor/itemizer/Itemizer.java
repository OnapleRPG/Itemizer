package com.ylinor.itemizer;

import com.ylinor.itemizer.commands.RetrieveCommand;
import com.ylinor.itemizer.data.handlers.ConfigurationHandler;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.nio.file.Path;

@Plugin(id = "itemizer", name = "Itemizer", version = "0.0.1")
public class Itemizer {

	@Inject
	@ConfigDir(sharedRoot=true)
	private Path configDir;

	private static Logger logger;
	@Inject
	private void setLogger(Logger logger) {
		this.logger = logger;
	}
	public static Logger getLogger() {
		return logger;
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {
		ConfigurationHandler.readItemsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_items.conf"));
		//ConfigurationHandler.readMinerConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_miner.conf"));

		CommandSpec retrieve = CommandSpec.builder()
				.description(Text.of("Retrieve an item from a configuration file with its id."))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
				.executor(new RetrieveCommand()).build();
		Sponge.getCommandManager().register(this, retrieve, "retrieve");

		logger.info("ITEMIZER initialized.");
	}

}
