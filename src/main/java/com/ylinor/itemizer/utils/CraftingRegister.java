package com.ylinor.itemizer.utils;

import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.SmeltingRecipeRegister;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

import java.util.List;

public class CraftingRegister {
    public static List<ICraftRecipes> craftRecipes;
    public static void register(){
        for (ICraftRecipes recipe: craftRecipes
             ) {

            Recipe r= recipe.register();
            if(r instanceof ShapelessCraftingRecipe){
                Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register((ShapelessCraftingRecipe)r);
            }
            if(r instanceof ShapedCraftingRecipe){
                Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register((ShapedCraftingRecipe)r);
            }
            if(r instanceof SmeltingRecipeRegister){
                Sponge.getGame().getRegistry().getSmeltingRecipeRegistry().register((org.spongepowered.api.item.recipe.smelting.SmeltingRecipe) r);
            }

        }
    }

}
