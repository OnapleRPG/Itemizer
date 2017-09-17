package com.ylinor.itemizer;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = "itemizer", name = "Itemizer", version = "0.0.1")
public class Itemizer {

	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;

	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {
		logger.info("Hello!");

		ConfigurationNode rootNode = configurationLoader.load();
		System.out.println(rootNode.getNode("itemizer", "toto", "aze").getInt());
	}
}
