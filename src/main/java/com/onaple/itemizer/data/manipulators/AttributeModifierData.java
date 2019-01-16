package com.onaple.itemizer.data.manipulators;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.OnaKeys;
import com.onaple.itemizer.data.beans.AttributeBean;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.ImmutableDataManipulator;
import org.spongepowered.api.data.manipulator.immutable.ImmutableListData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractListData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableListValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;


import java.util.*;

public class AttributeModifierData extends AbstractListData<AttributeBean, AttributeModifierData, AttributeModifierData.Immutable> {

    public AttributeModifierData() {
        this(new ArrayList<>());
    }

    public AttributeModifierData(List<AttributeBean> value) {
        super(value, OnaKeys.ATTRIBUTE_MODIFIER);
    }

    @Override
    public Optional<AttributeModifierData> fill(DataHolder dataHolder, MergeFunction overlap) {
        return null;
    }

    @Override
    public Optional<AttributeModifierData> from(DataContainer container) {

        return from((DataView) container);
    }

    public Optional<AttributeModifierData> from(DataView view) {
        if (view.contains(OnaKeys.ATTRIBUTE_MODIFIER.getQuery())) {
            Map<String, Map> stringMapMap = new HashMap<>();
            view.getMap(OnaKeys.ATTRIBUTE_MODIFIER.getQuery()).ifPresent(o -> stringMapMap.putAll((Map<String, Map>) o));
            stringMapMap.forEach((s, map) -> Itemizer.getLogger().info(s + " "+  map.toString() + "\n"));
        }
        return null;
    }


    @Override
    public AttributeModifierData copy() {
        return null;
    }

    @Override
    public Immutable asImmutable() {
        return null;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

public class Immutable implements ImmutableListData<AttributeBean, Immutable, AttributeModifierData> {


    @Override
    public ImmutableListValue<AttributeBean> getListValue() {
        return null;
    }

    @Override
    public List<AttributeBean> asList() {
        return null;
    }

    @Override
    public AttributeModifierData asMutable() {
        return null;
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    @Override
    public DataContainer toContainer() {
        return null;
    }

    @Override
    public <E> Optional<E> get(Key<? extends BaseValue<E>> key) {
        return null;
    }

    @Override
    public <E, V extends BaseValue<E>> Optional<V> getValue(Key<V> key) {
        return null;
    }

    @Override
    public boolean supports(Key<?> key) {
        return false;
    }

    @Override
    public Set<Key<?>> getKeys() {
        return null;
    }

    @Override
    public Set<ImmutableValue<?>> getValues() {
        return null;
    }
}
}
