package com.ylinor.itemizer;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

public class CraftingRecipeRegister implements ICraftRecipes {


    /** content **/
    private ItemStack content;
    /** result **/
    private ItemStack result;

    public CraftingRecipeRegister() {
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public CraftingRecipeRegister(ItemStack content, ItemStack result) {

        this.content = content;
        this.result = result;
    }

    public ItemStack getContent() {
        return content;
    }

    public void setContent(ItemStack content) {
        this.content = content;
    }

    @Override
    public ShapelessCraftingRecipe register() {
        return  org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapelessBuilder().addIngredient(Ingredient.builder().with(this.getContent()).build()).
                result(this.getResult()).build("craft", Itemizer.getInstance());
    }
}
