package com.onaple.itemizer.data.beans;


import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable

public class AttributeBean {

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

public String getCompiledAmount(){
    if(operation ==0){
        return "+" + amount;
    } else if( operation == 1){
        return"+"+ amount + "%";
    } else if(operation == 2){
        return  "x" + amount +"%";
    } else {
        return "";
    }
}
}

