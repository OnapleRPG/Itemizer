package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

public class CraftingRecipeRegister implements ICraftRecipes {
    /** index **/
    private String id;
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

    public CraftingRecipeRegister(String id ,ItemStack content, ItemStack result) {
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
        return CraftingRecipe.shapelessBuilder()
                .addIngredient(Ingredient.builder().with(this.getContent()).build())
                .result(this.getResult()).build("craft"+ id, Itemizer.getInstance());
    }
}
