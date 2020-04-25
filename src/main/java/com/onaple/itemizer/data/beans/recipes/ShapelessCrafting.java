package com.onaple.itemizer.data.beans.recipes;

import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.function.Predicate;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public class ShapelessCrafting extends AbstractCraftingRecipe {

    @Setting("recipe")

    protected String content;

    @Override
    public void register(GameRegistryEvent.Register event) {
        Recipe r = CraftingRecipe.shapelessBuilder()
                .addIngredient(Ingredient.builder().with(isMatchingId(content)).build())
                .result(result)
                .id("craft_itemizer%" + id)
                .group("itemizer")
                .build();
        event.register(r);
    }


}
