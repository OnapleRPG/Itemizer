package com.ylinor.brawlator;

import javax.inject.Inject;


import com.ylinor.brawlator.commands.InvokeCommand;
import com.ylinor.brawlator.commands.database.SelectMonsterCommand;
import com.ylinor.brawlator.data.handler.SqliteHandler;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.text.Text;

@Plugin(id = "brawlator", name = "Brawlator", version = "0.0.1")
public class Brawlator {
	@Inject
	private Logger logger;
	
	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info("Brawlator plugin initialized.");

		SqliteHandler.testConnection();

		/// Commandes du plugin

		CommandSpec invoke = CommandSpec.builder()
                .description(Text.of("Invoke a monster whose id is registered into the database"))
				.arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("id"))))
				.executor(new InvokeCommand()).build();
		Sponge.getCommandManager().register(this, invoke, "invoke");


		CommandSpec monsterSelect = CommandSpec.builder()
                .permission("ylinor.brawlator.database_read")
                .description(Text.of("Select and print a monster from database"))
                .arguments(GenericArguments.onlyOne(GenericArguments.integer(Text.of("id"))))
                .executor(new SelectMonsterCommand()).build();

		CommandSpec monsterDatabase = CommandSpec.builder()
                .description(Text.of("Query database about monsters"))
                .child(monsterSelect, "select").build();
        Sponge.getCommandManager().register(this, monsterDatabase, "monsters");

	}
}
