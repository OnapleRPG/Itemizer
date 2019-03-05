package com.onaple.itemizer.data.manipulator;

import com.onaple.itemizer.data.OnaKeys;
import com.onaple.itemizer.data.beans.AttributeParams;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableListData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractListData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class AttributeModifiersData extends AbstractListData<AttributeParams, AttributeModifiersData, AttributeModifiersData.Immutable> {


    protected AttributeModifiersData() {
        this(new ArrayList<>());
    }

    protected AttributeModifiersData(List<AttributeParams> value) {
        super(value, OnaKeys.ATTRIBUTE_MODIFIER);
    }

    @Override
    public Optional<AttributeModifiersData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<AttributeModifiersData> otherData_ = dataHolder.get(AttributeModifiersData.class);
        if (otherData_.isPresent()) {
            AttributeModifiersData otherData = otherData_.get();
            AttributeModifiersData finalData = overlap.merge(this, otherData);
            finalData.setValue(otherData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<AttributeModifiersData> from(DataContainer container) {
        return from((DataView) container);
    }


    Optional<AttributeModifiersData> from(DataView view) {
        if (view.contains(OnaKeys.ATTRIBUTE_MODIFIER)) {
            List<Map> stringListMap = new ArrayList<>();
            view.getList(OnaKeys.ATTRIBUTE_MODIFIER.getQuery()).ifPresent(objects -> stringListMap.addAll((List<Map>) objects));
            List<AttributeParams> attributeList = new ArrayList<>();
            stringListMap.forEach(w -> attributeList.add(new AttributeParams(w)));
            setValue(attributeList);
            return Optional.of(this);
        }
        return Optional.empty();
    }

    @Override
    public AttributeModifiersData copy() {
        return new AttributeModifiersData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    @Override
    public int getContentVersion() {
        return 1;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(OnaKeys.ATTRIBUTE_MODIFIER, getValue());
    }

    public class Immutable extends AbstractImmutableListData<AttributeParams, Immutable, AttributeModifiersData> {


        protected Immutable(List<AttributeParams> value) {
            super(value, OnaKeys.ATTRIBUTE_MODIFIER);
        }


        @Override
        public AttributeModifiersData asMutable() {
            return new AttributeModifiersData(getValue());
        }

        @Override
        public int getContentVersion() {
            return 1;
        }

        @Override
        public DataContainer toContainer() {
            return super.toContainer().set(OnaKeys.ATTRIBUTE_MODIFIER, getValue());
        }
    }

    public static class AttributeDataBuilder extends AbstractDataBuilder<AttributeModifiersData> implements DataManipulatorBuilder<AttributeModifiersData, Immutable> {

        public AttributeDataBuilder() {
            super(AttributeModifiersData.class, 1);
        }

        @Override
        public AttributeModifiersData create() {
            return new AttributeModifiersData(new ArrayList<>());
        }

        @Override
        public Optional<AttributeModifiersData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<AttributeModifiersData> buildContent(DataView container) throws InvalidDataException {
            return create().from(container);
        }
    }
}
