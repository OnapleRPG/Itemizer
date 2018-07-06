package com.ylinor.itemizer;
import com.ylinor.itemizer.commands.*;

import com.ylinor.itemizer.data.handlers.ConfigurationHandler;
import com.ylinor.itemizer.service.ItemService;
import com.ylinor.itemizer.service.IItemService;
import com.ylinor.itemizer.utils.CraftRegister;
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

@Plugin(id = "itemizer", name = "Itemizer", version = "1.0.0")
public class Itemizer {

	private static Itemizer itemizer;
	public static Itemizer getItemizer(){return itemizer;}


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

	@Inject
	private CraftRegister craftRegister;



	@Listener
	public void preInit(GamePreInitializationEvent event) {

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
		craftRegister.register(ConfigurationHandler.getCraftList());

	}

	@Listener
	public void onGamePreInitialization(GamePreInitializationEvent event) {
		logger.info("Initalisation");
		itemizer = this;
		Sponge.getServiceManager().setProvider(getInstance(), IItemService.class,new ItemService());
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {

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


		CommandSpec reload = CommandSpec.builder()
				.description(Text.of("Reaload Itemizer configuration from files."))
				.permission("itemizer.admin")
				.executor(new ReloadCommand()).build();
		Sponge.getCommandManager().register(this, reload, "reload-itemizer");


		CommandSpec analyse = CommandSpec.builder()
				.executor(new GetUnsafeDataCommand()).build();
		Sponge.getCommandManager().register(this,analyse,"analysedata");

		CommandSpec giveItem = CommandSpec.builder()
				.executor(new giveTestObjectCommand()).build();
		Sponge.getCommandManager().register(this,giveItem,"give-test");



		logger.info("ITEMIZER initialized.");

	}
	public static PluginContainer getInstance() {
		return Sponge.getPluginManager().getPlugin("itemizer").orElse(null);
	}

	public int loadItems() throws Exception {
		initDefaultConfig("/itemizer/items.conf");
		return ConfigurationHandler.readItemsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer/items.conf"));
	}

	public int loadMiners() throws Exception {
		initDefaultConfig("/itemizer/miners.conf");
		return ConfigurationHandler.readMinerConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer/miners.conf"));
	}

	public int loadPools() throws Exception {
		initDefaultConfig("/itemizer/pools.conf");
		return ConfigurationHandler.readPoolsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer/pools.conf"));
	}

	public int loadCrafts() throws Exception {
		initDefaultConfig("/itemizer/crafts.conf");
		return ConfigurationHandler.readCraftConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer/crafts.conf"));
	}

	public void initDefaultConfig(String path){
		if (Files.notExists(Paths.get(configDir + path))) {
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
