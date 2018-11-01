package com.onaple.itemizer;
import com.onaple.itemizer.commands.*;

import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import com.onaple.itemizer.service.ItemService;
import com.onaple.itemizer.service.IItemService;
import com.onaple.itemizer.utils.CraftRegister;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Plugin(id = "itemizer", name = "Itemizer - Custom Items", version = "1.2.1",
		description = "Plugin to manage custom items and crafts",
		url = "http://onaple.fr",
		authors = {"Zessirb", "Selki"})
public class Itemizer {

	private static Itemizer itemizer;
	public static Itemizer getItemizer(){return itemizer;}


	@Inject
	@ConfigDir(sharedRoot=true)
	private Path configDir;

	private GlobalConfig globalConfig;

	public GlobalConfig getGlobalConfig() {
		return globalConfig;
	}

	private static Logger logger;
	@Inject
	private void setLogger(Logger logger) {
		this.logger = logger;
	}
	public static Logger getLogger() {
		return logger;
	}

	@Inject
	private CraftRegister craftRegister;

	private static ConfigurationHandler configurationHandler;
	@Inject
	public void setConfigurationHandler(ConfigurationHandler configurationHandler) {
		this.configurationHandler = configurationHandler;
	}
	public static ConfigurationHandler getConfigurationHandler() {
		return configurationHandler;
	}

	@Listener
	public void preInit(GamePreInitializationEvent event) {
		logger.info("Initalisation");
		loadGlobalConfig();
		try {
			loadItems();
		}
		catch (ObjectMappingException e) {
			Itemizer.getLogger().error("Error while reading configuration 'items' : " + e.getMessage());
		}
		catch (Exception e){
			Itemizer.getLogger().error(e.getMessage());
		}
		try {
			loadMiners();
		} catch (ObjectMappingException e) {
			Itemizer.getLogger().error("Error while reading configuration 'miners' : " + e.getMessage());
		}
		catch (Exception e){
			Itemizer.getLogger().error(e.getMessage());
		}

		try {
			loadPools();
		} catch (ObjectMappingException e) {
			Itemizer.getLogger().error("Error while reading configuration 'pools' : " + e.getMessage());
		}
		catch (Exception e){
			Itemizer.getLogger().error(e.getMessage());
		}
		try {
			loadCrafts();
		} catch (ObjectMappingException e) {
			Itemizer.getLogger().error("Error while reading configuration 'crafts' : " + e.getMessage());
		}
		catch (Exception e){
			Itemizer.getLogger().error(e.getMessage());
		}
		craftRegister.register(configurationHandler.getCraftList());

		itemizer = this;
		Sponge.getServiceManager().setProvider(getInstance(), IItemService.class,new ItemService());
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {

		CommandSpec retrieve = CommandSpec.builder()
				.description(Text.of("Retrieve an item from a configuration file with its id."))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))),
						GenericArguments.optional(GenericArguments.player(Text.of("player")))
				)
				.permission("itemizer.command.retrieve")
				.executor(new RetrieveCommand()).build();
		Sponge.getCommandManager().register(this, retrieve, "retrieve");

		CommandSpec fetch = CommandSpec.builder()
				.permission("itemizer.get")
				.description(Text.of("Try to retrieve an item from a pool describes in a configuration file with its id."))
				.arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))),
						GenericArguments.optional(GenericArguments.player(Text.of("player"))))
				.permission("itemizer.command.fetch")
				.executor(new FetchCommand()).build();
		Sponge.getCommandManager().register(this, fetch, "fetch");

		CommandSpec reload = CommandSpec.builder()
				.description(Text.of("Reaload Itemizer configuration from files."))
				.permission("itemizer.command.reload")
				.executor(new ReloadCommand()).build();
		Sponge.getCommandManager().register(this, reload, "reload-itemizer");

		CommandSpec getInfo = CommandSpec.builder()
				.description(Text.of("get information about item in main hand"))
				.permission("itemizer.command.analyse")
				.executor(new GetItemInfos()).build();
		Sponge.getCommandManager().register(this, getInfo, "analyse");

		logger.info("ITEMIZER initialized.");
	}


	public static PluginContainer getInstance() {
		return Sponge.getPluginManager().getPlugin("itemizer").orElse(null);
	}

	public void loadGlobalConfig(){
		initDefaultConfig("global.conf");
		try {
			this.globalConfig = configurationHandler.readGlobalConfiguration(
					configurationHandler.loadConfiguration(configDir+"/itemizer/global.conf"));
		} catch (Exception e) {
			logger.error(e.toString());
		}
	}

	public int loadItems() throws Exception {
		initDefaultConfig("items.conf");
		return configurationHandler.readItemsConfiguration(
				configurationHandler.loadConfiguration(configDir+"/itemizer/items.conf"));
	}

	public int loadMiners() throws Exception {
		initDefaultConfig("miners.conf");
		return configurationHandler.readMinerConfiguration(configurationHandler.loadConfiguration(configDir+"/itemizer/miners.conf"));
	}

	public int loadPools() throws Exception {
		initDefaultConfig("pools.conf");
		return configurationHandler.readPoolsConfiguration(configurationHandler.loadConfiguration(configDir+"/itemizer/pools.conf"));
	}

	public int loadCrafts() throws Exception {
		initDefaultConfig("crafts.conf");
		return configurationHandler.readCraftConfiguration(configurationHandler.loadConfiguration(configDir+"/itemizer/crafts.conf"));
	}

	private void initDefaultConfig(String path){
		if (Files.notExists(Paths.get(configDir+ "/itemizer/" + path))) {
			PluginContainer pluginInstance = getInstance();
			if (pluginInstance!= null) {
				Optional<Asset> itemsDefaultConfigFile = pluginInstance.getAsset(path);
				getLogger().info("No config file set for " + path + " default config will be loaded");
				if (itemsDefaultConfigFile.isPresent()) {
					try {
						itemsDefaultConfigFile.get().copyToDirectory(Paths.get(configDir+"/itemizer/"));
					} catch (IOException e) {
						Itemizer.getLogger().error("Error while setting default configuration : " + e.getMessage());
					}
				} else {
					logger.warn("Item default config not found");
				}
			}
		}
	}
}
