package com.onaple.itemizer.data.manipulators;

import com.onaple.itemizer.data.OnaKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractSingleData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.SetValue;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.annotation.NonnullByDefault;

import java.util.Optional;

public class HiddenFlagsData extends AbstractSingleData<Integer,HiddenFlagsData,HiddenFlagsData.Immutable> {


    public HiddenFlagsData(int flag) {
        super(flag, OnaKeys.HIDDEN_FLAGS);
    }

    public HiddenFlagsData() {
        this(0);
    }


    @Override
    public int getContentVersion() {
        return Builder.CURRENT_VERSION;
    }

    @Override
    protected Value<?> getValueGetter() {
        return null;
    }

    @Override
    public Optional<HiddenFlagsData> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<HiddenFlagsData> dataOptional = dataHolder.get(HiddenFlagsData.class);
        if (dataOptional.isPresent()) {
            HiddenFlagsData otherData = dataOptional.get();
            HiddenFlagsData finalData = overlap.merge(this, otherData);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }

    @Override
    public Optional<HiddenFlagsData> from(DataContainer container) {
        if (container.contains(OnaKeys.HIDDEN_FLAGS)) {
            container.get(OnaKeys.HIDDEN_FLAGS.getQuery()).ifPresent(o -> setValue((int) o));
            return Optional.of(this);
        }
        return Optional.empty();
    }

    @Override
    public DataContainer toContainer() {
        DataContainer dataContainer = super.toContainer();
        dataContainer.set(OnaKeys.HIDDEN_FLAGS, getValue());
        return dataContainer;
    }

    @Override
    public HiddenFlagsData copy() {
        return new HiddenFlagsData(getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue());
    }

    public static class Builder extends AbstractDataBuilder<HiddenFlagsData> implements
            DataManipulatorBuilder<HiddenFlagsData,HiddenFlagsData.Immutable>{
        public static final int CURRENT_VERSION = 1;

        public Builder(){
            super(HiddenFlagsData.class,CURRENT_VERSION);
        }
        @Override
        public HiddenFlagsData create(){
            return new HiddenFlagsData();
        }
        @Override
        public Optional<HiddenFlagsData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<HiddenFlagsData> buildContent(DataView container) throws InvalidDataException {
            return create().from((DataContainer) container);
        }
    }

    public class Immutable extends AbstractImmutableSingleData<Integer, Immutable, HiddenFlagsData> {


        public Immutable(Integer value) {
            super(value, OnaKeys.HIDDEN_FLAGS);
        }

        public Immutable() {
            this(0);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(OnaKeys.HIDDEN_FLAGS, getValue()).asImmutable();
        }

        @Override
        public HiddenFlagsData asMutable() {
            return new HiddenFlagsData(getValue());
        }

        @Override
        public int getContentVersion() {
            return Builder.CURRENT_VERSION;
        }

        @Override
        public DataContainer toContainer() {
            DataContainer dataContainer = super.toContainer();
            dataContainer.set(OnaKeys.HIDDEN_FLAGS, getValue());
            return dataContainer;
        }
    }
}
