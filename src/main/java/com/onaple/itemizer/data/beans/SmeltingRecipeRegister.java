package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.ICraftRecipes;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;

public class SmeltingRecipeRegister implements ICraftRecipes {
    private String id;
    private ItemStack ingredient;
    private ItemStack result;


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

    public SmeltingRecipeRegister(String id, ItemStack ingredient, ItemStack result) {
        this.id = id;
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
