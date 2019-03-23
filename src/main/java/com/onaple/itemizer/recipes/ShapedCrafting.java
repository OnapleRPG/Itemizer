package com.onaple.itemizer.recipes;

import static org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapedBuilder;

import com.onaple.itemizer.Itemizer;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;

import java.util.Map;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public class ShapedCrafting extends AbstractCraftingRecipe {


    @Setting("pattern")
    private String[] pattern;

    @Setting("ingredients")
    private Map<Character,Ingredient> ingredient;

    @Override
    public void register() {
        ShapedCraftingRecipe r = shapedBuilder().
                aisle(this.pattern).
                where(this.ingredient).
                result(this.result).
                build("craft" + id, Itemizer.getInstance());
        Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register(r);
    }
}
