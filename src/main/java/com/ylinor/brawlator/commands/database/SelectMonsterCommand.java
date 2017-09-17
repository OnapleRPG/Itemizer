package com.ylinor.brawlator.commands.database;

import com.ylinor.brawlator.MonsterAction;
import com.ylinor.brawlator.data.beans.MonsterBean;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.Entity;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.chat.ChatTypes;
import org.spongepowered.api.world.Location;
import org.spongepowered.api.world.World;

import java.util.Optional;


public class SelectMonsterCommand implements CommandExecutor {

	public SelectMonsterCommand() {

	}
	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if (src instanceof Player && ((Player) src).hasPermission("database_read")) {
			int monsterId = (args.getOne("id").isPresent()) ? args.<Integer>getOne("id").get() : -1;
			Optional<MonsterBean> monsterOptional = MonsterAction.getMonster(monsterId);
			if(monsterOptional.isPresent()) {
				MonsterBean monster = monsterOptional.get();
				((Player) src).sendMessage(Text.of("MONSTER "+monster.getId()+" : "+monster.getName()+" | "+
					monster.getType()+" | "+monster.getHp()+" | "+monster.getAttackDamage()+" | "+monster.getSpeed()+" | "+
					monster.getKnockbackResistance()));
			} else {
				((Player) src).sendMessage(Text.of("MONSTER "+monsterId+" not found"));
			}
		}
		return CommandResult.success();
	}
}
