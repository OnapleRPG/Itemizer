package com.ylinor.itemizer;

import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.HashMap;

public class ShapedCrafting implements ICraftRecipes {
    /** ID **/
    private int id;
    /** Schema **/
    private String[] schema;
    /** content **/
    private HashMap<Character,Ingredient> content;
    /** result **/
    private ItemStack result;

    public ShapedCrafting() {
    }

    public HashMap<Character, Ingredient> getContent() {
        return content;
    }

    public int getId() {

        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String[] getSchema() {
        return schema;
    }

    public void setSchema(String[] schema) {
        this.schema = schema;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public ShapedCrafting(int id, String[] schema, HashMap<Character, Ingredient> content, ItemStack result) {
        this.id = id;
        this.schema = schema;
        this.content = content;
        this.result = result;
    }


    @Override
    public Recipe register() {
       return org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapedBuilder().
                aisle(this.schema).
                where(content).
                result(result).
                build("craft", Itemizer.getInstance());
    }
}
