package com.ylinor.brawlator.commands;

import com.ylinor.brawlator.MonsterAction;
import com.ylinor.brawlator.data.beans.MonsterBean;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;



public class InvokeCommand implements CommandExecutor {

	public InvokeCommand() {

	}
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player) {
			World world = ((Player) src).getWorld();
			Location location = ((Player) src).getLocation();

			int monsterId = (args.getOne("id").isPresent()) ? args.<Integer>getOne("id").get() : -1;

			Optional<MonsterBean> monster = MonsterAction.getMonster(monsterId);
			if(monster.isPresent()) {
				Optional<Entity> monsterOptionalEntity = MonsterAction.invokeMonster(world, location, monster.get());
				((Player) src).sendMessage(Text.of("MONSTER "+monsterId+" spawned."));
				if (monsterOptionalEntity.isPresent()) {
					Entity monsterEntity = monsterOptionalEntity.get();
				}
			} else {
				((Player) src).sendMessage(Text.of("MONSTER "+monsterId+" not found."));
			}
		}
		return CommandResult.success();
	}
}
