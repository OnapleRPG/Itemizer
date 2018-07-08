package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.Recipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.Arrays;
import java.util.Map;

import static org.spongepowered.api.item.recipe.crafting.CraftingRecipe.shapedBuilder;

public class ShapedCrafting implements ICraftRecipes {
    /** ID **/
    private int id;
    /** Schema **/
    private String[] schema;
    /** content **/
    private Map<Character,Ingredient> content;
    /** result **/
    private ItemStack result;

    public ShapedCrafting() {
    }

    public Map<Character, Ingredient> getContent() {
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

    public ShapedCrafting(int id, String[] schema, Map<Character, Ingredient> content, ItemStack result) {
        this.id = id;
        this.schema = schema;
        this.content = content;
        this.result = result;
    }

    @Override
    public String toString() {
        return "ShapedCrafting{" +
                "id=" + id +
                ", schema=" + Arrays.toString(schema) +
                ", content=" + content +
                ", result=" + result +
                '}';
    }

    @Override
    public Recipe register() {
        this.toString();
       return shapedBuilder().
                aisle(this.schema).
                where(this.content).
                result(this.result).
                build("craft"+ id, Itemizer.getInstance());
    }
}
