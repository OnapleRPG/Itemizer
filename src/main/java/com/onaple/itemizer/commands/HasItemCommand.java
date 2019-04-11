package com.onaple.itemizer.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class HasItemCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<Integer> refOptional = args.getOne(Text.of("reference"));
        if (refOptional.isPresent()) {
            Optional<Player> playerOptional = args.getOne(Text.of("player"));
            if (playerOptional.isPresent()) {
                Integer quantity = args.<Integer>getOne(Text.of("quantity")).orElse(1);

            }
        }
        return null;
    }
}
