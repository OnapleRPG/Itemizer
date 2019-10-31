package com.onaple.itemizer.commands.elements;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class IdElement extends CommandElement {
    public IdElement(@Nullable Text key) {
        super(key);
    }

    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String id = args.next();
        Optional<ItemBean> itemBeanOptional =Itemizer.getItemDAO().getItem(id);
        return itemBeanOptional.orElseThrow(() -> new ArgumentParseException(Text.of("Id not found"),source.getName(),1));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        return Itemizer.getConfigurationHandler().getItemList().stream().map(ItemBean::getId).collect(Collectors.toList());
    }

    @Override
    public Text getUsage(CommandSource src) {
        return Text.of("<id>");
    }
}
