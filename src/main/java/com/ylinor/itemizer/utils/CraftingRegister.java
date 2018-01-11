package com.ylinor.itemizer.utils;

import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.CraftingRecipeRegister;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

public class CraftingRegister {
    public static void register(CraftingRecipeRegister craft){
        switch (craft.getType()){
            case "CraftingRecipeRegister" :
                ShapelessCraftingRecipe craftingRecipe = org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapelessBuilder().addIngredient(Ingredient.builder().with(craft.getContent()).build()).
                        result(craft.getResult()).build("craft", Itemizer.getInstance());

                Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register(craftingRecipe);
                break;
        }
    }
}
