package com.onaple.itemizer.crafing;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.crafing.event.RowCraftEvent;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.type.DyeColors;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CraftService {

    @Inject
    private PluginContainer pluginContainer;

    @Inject
    private ConfigurationHandler configurationHandler;

    public static final ItemStackSnapshot SEPARATOR = ItemStack.builder().itemType(ItemTypes.STAINED_GLASS_PANE)
            .add(Keys.DISPLAY_NAME, Text.of(""))
            .add(Keys.DYE_COLOR, DyeColors.GRAY).build().createSnapshot();

    private Map<String, Integer> convertList(List<ItemStack> itemStacks) {
        Map<String,Integer> craftList = new HashMap<>();
        itemStacks.forEach(itemStack ->
                {
                    String itemId = itemStack.get(ItemizerKeys.ITEM_ID).orElse(itemStack.getType().getName());
                    int quantity = itemStack.getQuantity();
                    if(craftList.get(itemId)!= null){
                        Integer qte = craftList.get(itemId);
                        quantity += qte;
                    }
                    craftList.put(itemId,quantity);
                }
        );
        return craftList;
    }

    private Optional<ItemStack> craft(List<ItemStack> ingredients){
        Itemizer.getLogger().info("attempt a craft with [{}]",ingredients);
        Map<String, Integer> stringIntegerMap = convertList(ingredients);
        Itemizer.getLogger().info("craftList [{}]",configurationHandler.getRowCraftList());
        Optional<RowCraft> craftOptional = configurationHandler.getRowCraftList().stream()
                .filter(rowCraft -> rowCraft.getIngredients().equals(stringIntegerMap))
                .findFirst();
        Itemizer.getLogger().info("find maching craft {}",craftOptional);
        return craftOptional.map(rowCraft -> rowCraft.getResult());
    }

    public void craft(Player player,List<ItemStack> craftList){

        Optional<ItemStack> optionalCraftResult = craft(craftList);
        emmitCraftAttempt(player,craftList,optionalCraftResult);
    }

    private void emmitCraftAttempt(Player player, List<ItemStack> ingredients, Optional<ItemStack> result){
        EventContext eventContext = EventContext.builder().add(EventContextKeys.PLUGIN, pluginContainer).build();
        RowCraftEvent event = new RowCraftEvent(Cause.of(eventContext,player, pluginContainer),player, ingredients,result);
        Sponge.getEventManager().post(event);
    }

}
