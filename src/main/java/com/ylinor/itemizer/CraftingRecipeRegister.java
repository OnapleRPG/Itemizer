package com.ylinor.itemizer;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

public class CraftingRecipeRegister implements ICraftRecipes {
    /** ID **/
    private int id;
    /** Type **/
    private String type;
    /** content **/
    private ItemStack content;
    /** result **/
    private ItemStack result;

    public CraftingRecipeRegister() {
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public CraftingRecipeRegister(int id, String type, ItemStack content, ItemStack result) {
        this.id = id;
        this.type = type;
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
    public Recipe register() {
        return  org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapelessBuilder().addIngredient(Ingredient.builder().with(this.getContent()).build()).
                result(this.getResult()).build("craft", Itemizer.getInstance());
    }
}
