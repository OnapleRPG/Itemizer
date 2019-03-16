package com.onaple.itemizer.data.beans;

import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
public class AttributeBean {
    /**
     * Name of the Attribute
     */
    @Setting("name")
    private String name;
    /**
    * Slot of the item
     */
    @Setting("slot")
    private String slot;
    /**
     * amount of the modifier
     */
    @Setting("amount")
    private float amount;
    /**
     * operation : 0 = addition ; 1 = % additive ; 2 = % multiplicative
     */
    @Setting("operation")
    private int operation;

    public AttributeBean(String name, String slot, float amount, int operation) {
        this.name = name;
        this.slot = slot;
        this.amount = amount;
        this.operation = operation;
    }

    public AttributeBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }
}
