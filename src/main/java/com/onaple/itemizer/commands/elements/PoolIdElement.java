package com.onaple.itemizer.commands.elements;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.access.PoolDAO;
import com.onaple.itemizer.data.beans.PoolBean;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.ArgumentParseException;
import org.spongepowered.api.command.args.CommandArgs;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.args.CommandElement;
import org.spongepowered.api.text.Text;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class PoolIdElement extends CommandElement {

    private PoolDAO poolDAO;
    public PoolIdElement(@Nullable Text key, PoolDAO poolDAO) {
        super(key);
        this.poolDAO = poolDAO;
    }

    @Nullable
    @Override
    protected Object parseValue(CommandSource source, CommandArgs args) throws ArgumentParseException {
        String id = args.next();
        return Itemizer.getConfigurationHandler().getPoolList().stream()
                .filter(poolBean -> poolBean.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new ArgumentParseException(Text.of("Id not found"),id,1));
    }

    @Override
    public List<String> complete(CommandSource src, CommandArgs args, CommandContext context) {
        try {
            String id = args.next();
            return Itemizer.getConfigurationHandler().getPoolList().stream().map(PoolBean::getId)
                    .filter(s -> s.contains(id))
                    .collect(Collectors.toList());
        } catch (ArgumentParseException e) {
            return Itemizer.getConfigurationHandler().getPoolList().stream().map(PoolBean::getId).collect(Collectors.toList());
        }

    }
}
