package com.ylinor.itemizer.data.access;

import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.SmeltingRecipeRegister;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipe;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class CraftingDao {
    private Map<Integer,ICraftRecipes> craftRecipesList = new HashMap<>();

    private int index =0;

    public void add(ICraftRecipes craftRecipes){
        craftRecipesList.put(index,craftRecipes);
        index++;
    }

    public int getSize(){
        return craftRecipesList.size();
    }
    public ICraftRecipes getCraft(int index){
        return  craftRecipesList.get(index);
    }

    public void register(){
        for (ICraftRecipes recipeRegister: craftRecipesList.values()
                ) {

            Recipe r= recipeRegister.register();
            Itemizer.getLogger().info(r.toString());

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
