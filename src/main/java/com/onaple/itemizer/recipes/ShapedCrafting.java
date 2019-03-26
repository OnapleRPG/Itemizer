package com.onaple.itemizer.recipes;

import static org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapedBuilder;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.serializers.IngredientMapAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.List;
import java.util.Map;

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
                build("craft" + id, Itemizer.getInstance());
        event.register(r);
    }
}
