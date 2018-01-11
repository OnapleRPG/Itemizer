package com.ylinor.itemizer.data.beans;

import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;

public class CraftingBean {
    /** ID **/
    private int id;
    /** Type **/
    private String type;
    /** content **/
    private ItemStack content;
    /** result **/
    private ItemStack result;

    public CraftingBean() {
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

    public CraftingBean(int id, String type, ItemStack content, ItemStack result) {
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
}
