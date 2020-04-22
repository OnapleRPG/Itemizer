package com.onaple.itemizer.data.beans.recipes;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.beans.ItemBean;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Map;
import java.util.Optional;

@ConfigSerializable
public class RowCraft extends AbstractCraftingRecipe {

    public RowCraft() {
    }

    @Setting("ingredients")
    private Map<String,Integer> ingredients;

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

    @Override
    public void register(GameRegistryEvent.Register event) {
        Itemizer.getConfigurationHandler().getRowCraftList().add(this);
    }

    @Override
    public String toString() {
        return "RowCraft{" +
                "ingredients=" + ingredients +
                ", id=" + id +
                ", result=" + result +
                '}';
    }
}
