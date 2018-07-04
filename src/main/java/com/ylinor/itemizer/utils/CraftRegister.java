package com.ylinor.itemizer.utils;

import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.Itemizer;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipe;

import javax.inject.Singleton;
import java.util.List;

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
