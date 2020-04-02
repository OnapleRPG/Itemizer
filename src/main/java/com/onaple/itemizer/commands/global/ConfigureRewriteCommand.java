package com.onaple.itemizer.commands.global;

import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextColors;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Optional;

public class ConfigureRewriteCommand implements CommandExecutor {

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<String> keyOpt = args.<String>getOne("Key");
        Optional<Object> valueOpt = args.getOne("Name");
        if (keyOpt.isPresent()) {
            Class<GlobalConfig> aClass = GlobalConfig.class;
            try {
                if (valueOpt.isPresent()) {
                    String methodName = "set" + keyOpt.get();
                    Method setter = aClass.getMethod(methodName, valueOpt.get().getClass());
                    setter.invoke(Itemizer.getItemizer().getGlobalConfig(), valueOpt.get());
                    src.sendMessage(Text.builder("Value updated : ").color(TextColors.GREEN)
                            .append(Text.of("§6"+keyOpt.get()))
                            .append(Text.of("§2 Has value "))
                            .append(Text.of("§6"+valueOpt.get()))
                            .build());
                } else {
                    String methodName = "get" + keyOpt.get();
                    Method getter = aClass.getMethod(methodName);
                    Object value = getter.invoke(Itemizer.getItemizer().getGlobalConfig());
                    src.sendMessage(Text.builder(value.toString()).color(TextColors.GOLD).build());
                }
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
               Itemizer.getLogger().error("",e);
            }
        } else {
            src.sendMessage(Text.builder("Specify a key").color(TextColors.RED).build());
        }

        Itemizer.getItemizer().saveGlobalConfig();
        src.sendMessage(Text.builder("Configuration saved").color(TextColors.GREEN).build());

        return CommandResult.success();
    }
}
