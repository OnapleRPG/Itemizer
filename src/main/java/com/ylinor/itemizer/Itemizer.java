package com.ylinor.itemizer;

import com.ylinor.itemizer.commands.FetchCommand;
import com.ylinor.itemizer.commands.RetrieveCommand;
import com.ylinor.itemizer.data.access.ItemDAO;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.data.handlers.ConfigurationHandler;
import com.ylinor.itemizer.service.FetchService;
import com.ylinor.itemizer.service.IFetchService;
import com.ylinor.itemizer.utils.ItemBuilder;
import com.ylinor.itemizer.utils.PoolFetcher;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.nio.file.Path;
import java.util.Optional;

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


		Sponge.getServiceManager().setProvider(this, IFetchService.class,new FetchService());

		logger.info("ITEMIZER initialized.");

	}

	public Optional<ItemStack> retrieve(int id){
		Optional<ItemBean> optionalItem = ItemDAO.getItem(id);
		if (optionalItem.isPresent()) {
			Optional<ItemStack> optionalItemStack = ItemBuilder.buildItemStack(optionalItem.get());
		return optionalItemStack;
		}
		return Optional.empty();
		}

	public static PluginContainer getInstance(){
		return  Sponge.getPluginManager().getPlugin("brawlator").get();
	}

}
