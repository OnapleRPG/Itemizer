package com.onaple.itemizer.commands.globalConfiguration;

import com.onaple.itemizer.Itemizer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Map;
import java.util.Optional;

public class ConfigureModifierCommand implements CommandExecutor{


    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> keyOpt = args.<String>getOne("Modifier");
        Optional<Object> valueOpt = args.getOne("Name");

       Itemizer.getItemizer().getGlobalConfig().getModifierRewrite();
        if (keyOpt.isPresent()) {
                if (valueOpt.isPresent()) {
                    Itemizer.getItemizer().getGlobalConfig().getModifierRewrite()
                            .putIfAbsent(keyOpt.get(), valueOpt.get().toString());
                    Itemizer.getItemizer().getGlobalConfig().getModifierRewrite()
                            .replace(keyOpt.get(), valueOpt.get().toString());
                } else {
                    String value = Itemizer.getItemizer().getGlobalConfig().getModifierRewrite().get(keyOpt.get());
                    if (value != null) {
                        src.sendMessage(Text.builder()
                                .append(Text.of( "§6" + keyOpt.get() + "§2 has value : "))
                                .append(Text.builder(value).color(TextColors.GOLD).build())
                                .build());
                    } else {
                        src.sendMessage(Text.builder("This modifier Has no rewrite").color(TextColors.RED).build());
                        return CommandResult.empty();
                    }
                }
        }
        Itemizer.getItemizer().saveGlobalConfig();
        src.sendMessage(Text.builder("Configuration saved").color(TextColors.GREEN).build());

        return CommandResult.success();
    }

}
