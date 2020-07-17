package com.onaple.itemizer.commands.manager;

import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.managers.ItemLoreManager;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoreManagerCommand implements CommandExecutor {
    @Override
    @SuppressWarnings("unchecked")
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        Optional<ItemBean> item= args.getOne("id");
        List<Text> lore = args.<List>getOne("lore").orElse(new ArrayList<>());
        if(item.isPresent()){
            ItemLoreManager itemLoreManager = ItemLoreManager.of(item.get(),lore);
            itemLoreManager.apply();
            return CommandResult.success();
        }
        return CommandResult.empty();
    }

}
