package com.onaple.itemizer.commands.globalConfiguration;

import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;
import java.util.Optional;

public class ConfigureColorCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<GlobalConfig.RewriteFlagColorList> keyOpt = args.<GlobalConfig.RewriteFlagColorList>getOne("Key");
        Optional<TextColor> valueOpt = args.getOne("Color");


        if (keyOpt.isPresent()) {
            if (valueOpt.isPresent()) {
                Itemizer.getItemizer().getGlobalConfig().getColorMap().putIfAbsent(keyOpt.get(), valueOpt.get());
                Itemizer.getItemizer().getGlobalConfig().getColorMap().replace(keyOpt.get(), valueOpt.get());
                Itemizer.getItemizer().saveGlobalConfig();
                src.sendMessage(Text.builder("Configuration saved").color(TextColors.GREEN).build());
            } else {
                String value =  Itemizer.getItemizer().getGlobalConfig().getColorMap().get(keyOpt.get()).getName();
                src.sendMessage(Text.builder()
                        .append(Text.of( "§6" + keyOpt.get() + "§2 has value : "))
                        .append(Text.builder(value).color(TextColors.GOLD).build())
                        .build());

            }
        }


        return CommandResult.success();
    }
}
