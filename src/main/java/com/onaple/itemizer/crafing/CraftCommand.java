package com.onaple.itemizer.crafing;

import com.onaple.itemizer.crafing.event.CraftSuccessfulEvent;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import org.slf4j.Logger;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.item.inventory.ClickInventoryEvent;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.item.inventory.InventoryArchetype;
import org.spongepowered.api.item.inventory.InventoryArchetypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.item.inventory.Slot;
import org.spongepowered.api.item.inventory.property.InventoryDimension;
import org.spongepowered.api.item.inventory.property.SlotIndex;
import org.spongepowered.api.item.inventory.query.QueryOperationTypes;
import org.spongepowered.api.item.inventory.transaction.InventoryTransactionResult;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Singleton
public class CraftCommand implements CommandExecutor {



    @Inject
    private PluginContainer pluginContainer;
    @Inject
    private ConfigurationHandler configurationHandler;
    @Inject
    private CraftService craftService;
    @Inject
    private Logger logger;

    @Override
    public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {

        Inventory inventory = Inventory.builder()
                .of(InventoryArchetype.builder()
                        .with(InventoryArchetypes.CHEST)
                        .property(InventoryDimension.of(9, 1))
                        .title(Text.of("Crafts"))
                        .build("testid", "test"))
                .listener(ClickInventoryEvent.class, (clickInventoryEvent) -> {
                    Optional<Slot> inventoryEventSlot = clickInventoryEvent.getSlot();
                    Player player = clickInventoryEvent.getCause().first(Player.class).get();
                    if (inventoryEventSlot.isPresent()) {
                        Optional<SlotIndex> inventoryProperty = inventoryEventSlot.get().getInventoryProperty(SlotIndex.class);
                        if (inventoryProperty.isPresent() && inventoryProperty.get().getValue() <= 7) {
                            craftService.craft(player,getIngredient(clickInventoryEvent.getTargetInventory()));
                        }
                    }
                })
                .listener(ClickInventoryEvent.class,clickInventoryEvent -> {
                    ItemStackSnapshot stackSnapshot = clickInventoryEvent.getCursorTransaction().getFinal();
                    if (stackSnapshot.equals(CraftService.SEPARATOR)){
                        clickInventoryEvent.setCancelled(true);
                    }
                })
                .listener(ClickInventoryEvent.class, clickInventoryEvent -> {
                    Player player = clickInventoryEvent.getCause().first(Player.class).get();
                    Optional<Slot> inventoryEventSlot = clickInventoryEvent.getSlot();
                        if(inventoryEventSlot.isPresent()) {
                            Optional<SlotIndex> inventoryProperty = inventoryEventSlot.get().getInventoryProperty(SlotIndex.class);
                            if (inventoryProperty.isPresent() && inventoryProperty.get().getValue() == 8) {
                                ItemStackSnapshot itemStackSnapshot = clickInventoryEvent.getCursorTransaction().getFinal();
                                ItemStackSnapshot holdItem = clickInventoryEvent.getCursorTransaction().getOriginal();
                                if (!itemStackSnapshot.equals(CraftService.SEPARATOR) && holdItem.equals(ItemStackSnapshot.NONE)) {
                                    EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build();
                                    Cause cause = Cause.of(eventContext, clickInventoryEvent.getTargetInventory(), player, pluginContainer);
                                    CraftSuccessfulEvent event = new CraftSuccessfulEvent(cause, itemStackSnapshot.createStack());
                                    Sponge.getEventManager().post(event);
                                    clickInventoryEvent.setCancelled(event.isCancelled());
                                    if(!event.isCancelled()){
                                        reset(clickInventoryEvent.getTargetInventory());
                                        clickInventoryEvent.getCursorTransaction().setCustom(event.getCraftResult().createSnapshot());
                                    }
                                } else {
                                    clickInventoryEvent.setCancelled(true);
                                }
                            }
                        }
                })
                .build(pluginContainer);
        reset(inventory);
        ((Player) src).openInventory(inventory);
        return CommandResult.empty();
    }
    private List<ItemStack> getIngredient(Inventory craftInventory){
        List<ItemStack> stackList = new ArrayList<>();
        craftInventory
                .query(QueryOperationTypes.INVENTORY_PROPERTY.of(
                        SlotIndex.greaterThanOrEqual(SlotIndex.of(7))
                ))
                .slots()
                .forEach(slot -> {
                    if(slot.getInventoryProperty(SlotIndex.class).isPresent()
                            && slot.getInventoryProperty(SlotIndex.class).get().getValue() <= 7)
                    {
                        slot.peek().ifPresent(stackList::add);
                    }
                });
        return stackList;
    }
    private void reset(Inventory inventory){
        inventory .query(QueryOperationTypes.INVENTORY_PROPERTY.of(
                SlotIndex.greaterThanOrEqual(SlotIndex.of(8))
        )).clear();
        InventoryTransactionResult set = inventory.query(QueryOperationTypes.INVENTORY_PROPERTY.of(SlotIndex.of(8)))
                .set(CraftService.SEPARATOR.createStack());
    }
}
