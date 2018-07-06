package com.ylinor.itemizer.commands;

import com.google.common.collect.ImmutableList;
import com.ylinor.itemizer.data.beans.AttributeBean;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.utils.ItemBuilder;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.HandTypes;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class giveTestObjectCommand implements CommandExecutor{
    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        if(src instanceof Player){
          /*  ItemStack itemStack = ItemStack.builder().itemType(ItemTypes.DIAMOND_SWORD).build();

            List<DataContainer> containers = new ArrayList();

            DataContainer dataContainer = DataContainer.createNew();
            dataContainer.set(DataQuery.of("AttributeName"),"generic.attackDamage");
            dataContainer.set(DataQuery.of("Name"),"generic.attackDamage");
            dataContainer.set(DataQuery.of("Amount"),15);
            dataContainer.set(DataQuery.of("Operation"),0);
            dataContainer.set(DataQuery.of("Slot"),"mainhand");
            dataContainer.set(DataQuery.of("UUIDMost"),58736);
            dataContainer.set(DataQuery.of("UUIDLeast"),617612);


            containers.add(dataContainer);

            DataContainer c = itemStack.toContainer();
            c.set(DataQuery.of("UnsafeData","AttributeModifiers"),containers);

            ItemStack modifiedItem = ItemStack.builder().fromContainer(c).build();

            modifiedItem.offer(Keys.ITEM_DURABILITY,5);*/
            AttributeBean attributeBean = new AttributeBean("generic.maxHealth","mainhand",10,1);
            AttributeBean attributeBean2 = new AttributeBean("generic.attackDamage","mainhand",8,1);

            List<AttributeBean> attributeBeans = new ArrayList<>();
            attributeBeans.add(attributeBean);
            attributeBeans.add(attributeBean2);
            ItemBean itemBean = new ItemBean("diamond_sword");
            itemBean.setAttributeList(attributeBeans);

            ((Player) src).setItemInHand(HandTypes.MAIN_HAND, ItemBuilder.buildItemStack(itemBean).orElse(null));
            return CommandResult.success();
        }
        return CommandResult.empty();

    }
}
