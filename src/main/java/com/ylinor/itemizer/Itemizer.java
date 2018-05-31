package com.ylinor.itemizer;
import com.ylinor.itemizer.commands.FetchCommand;

import com.ylinor.itemizer.commands.ReloadCommand;

import com.ylinor.itemizer.commands.RetrieveCommand;
import com.ylinor.itemizer.data.handlers.ConfigurationHandler;
import com.ylinor.itemizer.service.ItemService;
import com.ylinor.itemizer.service.IItemService;
import com.ylinor.itemizer.utils.CraftRegister;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
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
import java.nio.file.Path;

@Plugin(id = "itemizer", name = "Itemizer", version = "0.0.1")
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

		logger.info("ITEMIZER initialized.");

	}
	public static PluginContainer getInstance(){
		return  Sponge.getPluginManager().getPlugin("itemizer").get();
	}

	public int loadItems() throws Exception {
		return ConfigurationHandler.readItemsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_items.conf"));
	}

	public int loadMiners() throws Exception {
		return ConfigurationHandler.readMinerConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_miners.conf"));
	}

	public int loadPools() throws Exception {
		return ConfigurationHandler.readPoolsConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_pools.conf"));
	}

	public int loadCrafts() throws Exception {
		return ConfigurationHandler.readCraftConfiguration(ConfigurationHandler.loadConfiguration(configDir+"/itemizer_crafts.conf"));
	}





}
