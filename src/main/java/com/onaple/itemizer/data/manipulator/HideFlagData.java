package com.onaple.itemizer.data.manipulator;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.OnaKeys;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataHolder;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.manipulator.DataManipulatorBuilder;
import org.spongepowered.api.data.manipulator.immutable.common.AbstractImmutableSingleData;
import org.spongepowered.api.data.manipulator.mutable.common.AbstractIntData;
import org.spongepowered.api.data.merge.MergeFunction;
import org.spongepowered.api.data.persistence.AbstractDataBuilder;
import org.spongepowered.api.data.persistence.InvalidDataException;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class HideFlagData extends AbstractIntData<HideFlagData, HideFlagData.Immutable> {

    public HideFlagData() {
        this(0,OnaKeys.HIDDEN_FLAGS);
    }

    protected HideFlagData(int value, Key<? extends BaseValue<Integer>> usedKey) {
        super(value, usedKey);
    }


    @Override
    public Optional<HideFlagData> fill(DataHolder dataHolder, MergeFunction overlap) {
        HideFlagData mergedData = overlap.merge(this, dataHolder.get(HideFlagData.class).orElse(null));
        setValue(mergedData.getValue());
        return Optional.of(this);
    }

    @Override
    public Optional<HideFlagData> from(DataContainer container) {
        Itemizer.getLogger().info("from conteneur , {}" ,container );
        if(container.contains(OnaKeys.HIDDEN_FLAGS)){
            int friend = container.getInt(OnaKeys.HIDDEN_FLAGS.getQuery()).orElse(0);
            return Optional.of(setValue(friend));
        }
        return Optional.empty();
    }

    @Override
    public HideFlagData copy() {
        return new HideFlagData(getValue(), OnaKeys.HIDDEN_FLAGS);
    }

    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(OnaKeys.HIDDEN_FLAGS, getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(getValue(),OnaKeys.HIDDEN_FLAGS);
    }

    @Override
    public int getContentVersion() {
        return 0;
    }

    public class Immutable extends AbstractImmutableSingleData<Integer,HideFlagData.Immutable,HideFlagData>{

        protected Immutable(Integer value, Key<? extends BaseValue<Integer>> usedKey) {
            super(value, usedKey);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(OnaKeys.HIDDEN_FLAGS, getValue()).asImmutable();
        }

        @Override
        public HideFlagData asMutable() {
            return new HideFlagData(getValue(),OnaKeys.HIDDEN_FLAGS);
        }

        @Override
        public int getContentVersion() {
            return 0;
        }
    }
    public static class Builder extends AbstractDataBuilder<HideFlagData> implements DataManipulatorBuilder<HideFlagData, Immutable> {
        public Builder() {
            super(HideFlagData.class, 1);
        }

        @Override
        public HideFlagData create() {
            return new HideFlagData();
        }

        @Override
        public Optional<HideFlagData> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<HideFlagData> buildContent(DataView container) throws InvalidDataException {
            return Optional.of(create());
        }
    }
}
