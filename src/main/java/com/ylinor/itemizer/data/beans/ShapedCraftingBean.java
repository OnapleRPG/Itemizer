package com.ylinor.itemizer.data.beans;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;

public class ShapedCraftingBean {
    /** ID **/
    private int id;
    /** Type **/
    private String type;
    /** Schema **/
    private String[] schema;
    /** content **/
    private HashMap<String,ItemStack> content;
    /** result **/
    private ItemStack result;

    public ShapedCraftingBean() {
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

    public String[] getSchema() {
        return schema;
    }

    public void setSchema(String[] schema) {
        this.schema = schema;
    }

    public HashMap<String, ItemStack> getContent() {
        return content;
    }

    public void setContent(HashMap<String, ItemStack> content) {
        this.content = content;
    }

    public ItemStack getResult() {
        return result;
    }

    public void setResult(ItemStack result) {
        this.result = result;
    }

    public ShapedCraftingBean(int id, String type, String[] schema, HashMap<String, ItemStack> content, ItemStack result) {
        this.id = id;
        this.type = type;
        this.schema = schema;
        this.content = content;
        this.result = result;
    }
}
