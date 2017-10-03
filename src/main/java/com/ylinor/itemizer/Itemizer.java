package com.ylinor.itemizer;

import com.ylinor.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.slf4j.Logger;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.network.ClientConnectionEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.util.Arrays;

@Plugin(id = "itemizer", name = "Itemizer", version = "0.0.1")
public class Itemizer {

	@Inject
	private Logger logger;

	@Inject
	@DefaultConfig(sharedRoot = true)
	private ConfigurationLoader<CommentedConfigurationNode> configurationLoader;

	@Listener
	public void onServerStart(GameStartedServerEvent event) throws Exception {
		logger.info(" [Itemizer] ----- Hello! ----- ");

		ConfigurationNode rootNode = configurationLoader.load();
		//System.out.println(rootNode.getNode("itemizer", "toto", "aze").getInt());
	}

	@Listener
    public void onJoin(ClientConnectionEvent.Join event) {
        Player player = event.getTargetEntity();
        System.out.println("Connect: " + player.getName());
        for (Inventory i : player.getInventory().slots()) {
            if (i.size() == 0) {
                i.offer(new ItemBuilder(ItemTypes.DIAMOND_SWORD).setName("§6Épée").setLore(Arrays.asList(Text.of("First line"), Text.of("§cSecond line"))).setGlowing(true).buildItemStack());
                break;
            }
        }
    }

}
