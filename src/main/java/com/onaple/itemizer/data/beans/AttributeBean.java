package com.onaple.itemizer.data.beans;

public class AttributeBean {
    /**
     * Name of the Attribute
     */
    private String name;
    /**
    * Slot of the item
     */
    private String slot;
    /**
     * amount of the modifier
     */
    private float amount;
    /**
     * operation : 0 = addition ; 1 = % additive ; 2 = % multiplicative
     */
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
