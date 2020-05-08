package com.onaple.itemizer.data.beans.recipes;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

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
