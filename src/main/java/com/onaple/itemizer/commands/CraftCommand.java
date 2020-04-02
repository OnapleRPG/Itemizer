package com.onaple.itemizer.commands;

import com.flowpowered.math.vector.Vector2i;
import org.slf4j.Logger;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.SlotPos;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Singleton
public class CraftCommand implements CommandExecutor {

    @Inject
    private PluginContainer pluginContainer;
    @Inject
    private Logger logger;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
        List<ItemStack> craft = Arrays.asList(
                ItemStack.of(ItemTypes.CARROT),
                ItemStack.of(ItemTypes.POTATO),
                ItemStack.of(ItemTypes.FISH)
        );
        Inventory inventory = Inventory.builder()
                .of(InventoryArchetype.builder()
                        .with(InventoryArchetypes.CHEST)
                        .property(InventoryDimension.of(9,2))
                        .title(Text.of("Crafts"))
                        .build("testid","test"))
                        .listener(ClickInventoryEvent.class, clickInventoryEvent -> {
                            List<ItemStack> stackList = new ArrayList<>();
                            clickInventoryEvent.getTargetInventory()
                            .query(QueryOperationTypes.INVENTORY_PROPERTY.of(
                                    SlotPos.lessThanOrEqual(new Vector2i(8,0)))
                            )
                                    .slots()
                                    .forEach(inventory1 -> inventory1.peek().ifPresent(stackList::add));

                            logger.info("item quantity {}",stackList.size());

                        })
                .build(pluginContainer);
       /* InventoryTransactionResult set = inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of( SlotPos.of(7, 0)))
                .offer(separator);*/
        ((Player)src).openInventory(inventory);
        return CommandResult.empty();
    }
}
