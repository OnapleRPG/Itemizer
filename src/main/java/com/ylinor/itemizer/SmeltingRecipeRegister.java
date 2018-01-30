package com.ylinor.itemizer;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;

public class SmeltingRecipeRegister implements ICraftRecipes {
    public ItemStack ingredient;
    public ItemStack result;

    public ItemStack getIngredient() {
        return ingredient;
    }

    public void setIngredient(ItemStack ingredient) {
        this.ingredient = ingredient;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public SmeltingRecipeRegister(ItemStack ingredient, ItemStack result) {
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public Recipe register() {
        return org.spongepowered.api.item.recipe.smelting.SmeltingRecipe.builder().
                ingredient(ingredient).
                result(result).
                build();
    }


}
