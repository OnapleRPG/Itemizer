package com.ylinor.itemizer.data.beans;

import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.Itemizer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

public class CraftingRecipeRegister implements ICraftRecipes {
    /** index **/
    private int id;
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

    public CraftingRecipeRegister(int id ,ItemStack content, ItemStack result) {
        this.id = id;
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
                result(this.getResult()).build("craft"+ id, Itemizer.getInstance());
    }
}
