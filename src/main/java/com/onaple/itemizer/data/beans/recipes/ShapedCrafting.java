package com.onaple.itemizer.data.beans.recipes;

import com.onaple.itemizer.data.serializers.IngredientMapAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.List;
import java.util.Map;

import static org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapedBuilder;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public class ShapedCrafting extends AbstractCraftingRecipe {

    @Setting("pattern")
    private List<String> pattern;

    @Setting("ingredients")
    @CustomAdapter(IngredientMapAdapter.class)
    private Map<Character,Ingredient> ingredient;


    @Override
    public void register(GameRegistryEvent.Register event) {
        CraftingRecipe r = shapedBuilder().
                aisle(this.pattern.stream().toArray(String[]::new)).
                where(this.ingredient).
                result(this.result).
                id("craft_itemizer%" + id).
                build();
        event.register(r);
    }
}
