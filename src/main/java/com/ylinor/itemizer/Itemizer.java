package com.ylinor.itemizer;

import com.ylinor.itemizer.commands.FetchCommand;
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
import org.spongepowered.api.plugin.PluginContainer;
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
		ConfigurationHandler.readMinerConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_miners.conf"));
		ConfigurationHandler.readPoolsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_pools.conf"));

		CommandSpec retrieve = CommandSpec.builder()
				.description(Text.of("Retrieve an item from a configuration file with its id."))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
				.executor(new RetrieveCommand()).build();
		Sponge.getCommandManager().register(this, retrieve, "retrieve");

		CommandSpec fetch = CommandSpec.builder()
				.description(Text.of("Try to retrieve an item from a pool describes in a configuration file with its id."))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
				.executor(new FetchCommand()).build();
		Sponge.getCommandManager().register(this, fetch, "fetch");

		logger.info("ITEMIZER initialized. ");


		/*CustomItemService service = Sponge.getServiceManager().provide(CustomItemService.class)
				.orElseThrow(() -> new IllegalStateException("Could not access the CustomItemLibrary service."));
		service.register(
				magicWandDefinition = CustomFeatureDefinition.itemToolBuilder()
						// Required fields:
						.plugin(Itemizer.getInstance())  // The owner plugin
						.typeId("magic_wand")  // The definition id
						.itemStackSnapshot(ItemTypes.SHEARS.getTemplate())  // Used to build magic wands
						.defaultModel("magic_wand_primary")  // Models are located at `assets/%PLUGIN_ID%/models/tools/%MODEL%.json` in the JAR
						// Optional fields:
						.additionalModel("magic_wand_secondary")
						.additionalAsset("textures/tools/magic_wand_texture.png")
						.build()
		);*/
	}
	public static PluginContainer getInstance(){
		return  Sponge.getPluginManager().getPlugin("brawlator").get();
	}

}
