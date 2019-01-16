package com.onaple.itemizer.data.beans;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataSerializable;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.util.ResettableBuilder;

import java.util.Optional;
import java.util.UUID;

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

    /**
     *
     * @param name
     * @param slot
     * @param amount
     * @param operation
     */
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
