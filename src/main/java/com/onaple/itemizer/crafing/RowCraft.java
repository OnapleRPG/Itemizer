package com.onaple.itemizer.crafing;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Map;

public class RowCraft {

    private Map<String,Integer> ingredients;

    private ItemStack result;

    public RowCraft(Map<String, Integer> ingredients, ItemStack result) {
        this.ingredients = ingredients;
        this.result = result;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<String, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }
}
