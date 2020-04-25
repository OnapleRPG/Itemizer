package com.onaple.itemizer.data.beans.recipes;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.HashMap;
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
    private Map<String, String> ingredients;


    @Override
    public void register(GameRegistryEvent.Register event) {
        CraftingRecipe r = shapedBuilder().
                aisle(this.pattern.stream().toArray(String[]::new)).
                where(this.getIngredients()).
                result(this.result).
                id("craft_itemizer%" + id).
                build();
        event.register(r);
    }

    private Map<Character, Ingredient> getIngredients() {
        Map<Character, Ingredient> characterIngredientMap = new HashMap<>();
        ingredients.forEach((character, name) -> {
                    if (!character.trim().isEmpty()) {
                        characterIngredientMap.put(character.charAt(0), Ingredient.builder().with(isMatchingId(name)).build());
                    }
                }
        );
        return characterIngredientMap;
    }
}
