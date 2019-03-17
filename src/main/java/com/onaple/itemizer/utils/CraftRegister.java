package com.onaple.itemizer.utils;

import com.onaple.itemizer.ICraftRecipes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipe;

import java.util.List;

import javax.inject.Singleton;


@Singleton
public class CraftRegister {
    public CraftRegister() {
    }

    public void register(List<ICraftRecipes> craftRecipesList){
         for (ICraftRecipes recipeRegister: craftRecipesList) {

        Recipe r= recipeRegister.register();


        if(r instanceof ShapelessCraftingRecipe){
            Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register((ShapelessCraftingRecipe)r);
        }
        if(r instanceof ShapedCraftingRecipe){
            Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register((ShapedCraftingRecipe)r);
        }
        if(r instanceof SmeltingRecipe){
            Sponge.getGame().getRegistry().getSmeltingRecipeRegistry().register((org.spongepowered.api.item.recipe.smelting.SmeltingRecipe) r);
        }

    }
}
}
