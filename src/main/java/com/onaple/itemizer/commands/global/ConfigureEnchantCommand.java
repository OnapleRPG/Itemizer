package com.onaple.itemizer.commands.global;

import com.onaple.itemizer.Itemizer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.util.Optional;

public class ConfigureEnchantCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<EnchantmentType> keyOpt = args.<EnchantmentType>getOne("Enchant");
        Optional<String> valueOpt = args.getOne("Name");

        if (keyOpt.isPresent()) {

                if (valueOpt.isPresent()) {
                    Itemizer.getItemizer().getGlobalConfig().getEnchantRewrite()
                            .putIfAbsent(keyOpt.get(), valueOpt.get());
                    Itemizer.getItemizer().getGlobalConfig().getEnchantRewrite()
                            .replace(keyOpt.get(), valueOpt.get());

                    Itemizer.getItemizer().saveGlobalConfig();
                    src.sendMessage(Text.builder("Configuration saved").color(TextColors.GREEN).build());

                } else {
                    String value = Itemizer.getItemizer().getGlobalConfig().getEnchantRewrite().get(keyOpt.get());
                    if (value != null) {
                        src.sendMessage(Text.builder()
                                .append(Text.of( "§6" + keyOpt.get() + "§2 has value : "))
                                .append(Text.builder(value).color(TextColors.GOLD).build())
                                .build());
                    } else {
                        src.sendMessage(Text.builder("This enchant Has no rewrite").color(TextColors.RED).build());
                        return CommandResult.empty();
                    }
                }

        }


        return CommandResult.success();
    }
}
