package com.onaple.itemizer.data.beans;

import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;

@ConfigSerializable
@Data
@NoArgsConstructor
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

