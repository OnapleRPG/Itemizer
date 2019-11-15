package com.onaple.itemizer.commands;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.NewItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.item.inventory.ItemStack;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MigrateCommand implements CommandExecutor {
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        try {
            List<NewItemBean> itemBeanList = Itemizer.getConfigurationHandler().getItemList().stream().map(itemBean -> {
                Optional<ItemStack> snap= new ItemBuilder().buildItemStack(itemBean);
                String id = itemBean.getId();
                if(snap.isPresent()) {
                    return new NewItemBean(id, snap.get().createSnapshot());
                } else {return new NewItemBean();}
            }).collect(Collectors.toList());
            Itemizer.getLogger().info("new item to  import [{}]",itemBeanList);
            ConfigurationLoader<CommentedConfigurationNode> config = HoconConfigurationLoader.builder().setPath(Paths.get("item-for-v3.conf")).build();
            final TypeToken<List<NewItemBean>> token = new TypeToken<List<NewItemBean>>() {};

            CommentedConfigurationNode root = config.createEmptyNode();
            root.getNode("items").setValue(token, itemBeanList);
            config.save(root);
            return CommandResult.affectedItems(itemBeanList.size());
        } catch (Exception e) {
            Itemizer.getLogger().error("", e);
            return CommandResult.empty();
        }
    }
}
