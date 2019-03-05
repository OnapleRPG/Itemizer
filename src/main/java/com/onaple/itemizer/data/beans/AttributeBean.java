package com.onaple.itemizer.data.beans;

import com.google.common.reflect.TypeToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
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

@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
@Getter
@Setter
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
}
