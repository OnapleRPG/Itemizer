package com.ylinor.itemizer.utils;

import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.data.beans.CraftingBean;
import jdk.internal.dynalink.beans.StaticClass;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapedCraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipe;

public class CraftingRegister {
    public static void register(CraftingBean craft){
        switch (craft.getType()){
            case "CraftingRecipe" :
                ShapelessCraftingRecipe craftingRecipe = CraftingRecipe.shapelessBuilder().addIngredient(Ingredient.builder().with(craft.getContent()).build()).
                        result(craft.getResult()).build("craft", Itemizer.getInstance());

                Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register(craftingRecipe);
                break;
        }
    }
}
