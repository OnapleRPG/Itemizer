package com.onaple.itemizer.recipes;

import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipe;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public class Smelting extends AbstractCraftingRecipe {

    @Setting("recipe")
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    private ItemStack ingredient;

    @Override
    public void register(GameRegistryEvent.Register event) {
        Recipe r = SmeltingRecipe.builder().
                ingredient(ingredient).
                result(result).
                id("craft_itemizer%" + id).
                build();
        event.register(r);
    }
}
