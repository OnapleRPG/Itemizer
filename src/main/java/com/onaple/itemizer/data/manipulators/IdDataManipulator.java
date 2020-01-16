package com.onaple.itemizer.data.manipulators;

import com.onaple.itemizer.ItemizerKeys;
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
import org.spongepowered.api.data.value.immutable.ImmutableValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.Optional;

public class IdDataManipulator extends AbstractSingleData<String,IdDataManipulator, IdDataManipulator.Immutable> {

    public static int CONTENT_VERSION = 1;

    public IdDataManipulator(Key<? extends Value<String>> usedKey, String value) {
        super(usedKey, value);
    }

    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(ItemizerKeys.ITEM_ID, getValue());
    }

    @Override
    public Optional<IdDataManipulator> fill(DataHolder dataHolder, MergeFunction overlap) {
         Optional<IdDataManipulator> data_ = dataHolder.get(IdDataManipulator.class);
        if (data_.isPresent()) {
            IdDataManipulator data = data_.get();
            IdDataManipulator finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }


    @Override
    public Optional<IdDataManipulator> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<IdDataManipulator> from(DataView view) {
        if (view.contains(ItemizerKeys.ITEM_ID.getQuery())) {
            setValue(view.getString(ItemizerKeys.ITEM_ID.getQuery()).get());
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public IdDataManipulator copy() {
        return new IdDataManipulator(ItemizerKeys.ITEM_ID,getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(ItemizerKeys.ITEM_ID,getValue());
    }

    @Override
    public int getContentVersion() {
        return CONTENT_VERSION;
    }

    public static class Immutable extends AbstractImmutableSingleData<String, IdDataManipulator.Immutable, IdDataManipulator> {

        protected Immutable(Key<? extends Value<String>> usedKey, String value) {
            super(usedKey, value);
        }

        @Override
        protected ImmutableValue<?> getValueGetter() {
            return Sponge.getRegistry().getValueFactory().createValue(ItemizerKeys.ITEM_ID, getValue()).asImmutable();
        }

        @Override
        public IdDataManipulator asMutable() {
            return new IdDataManipulator(ItemizerKeys.ITEM_ID, getValue());
        }

        @Override
        public int getContentVersion() {
            return CONTENT_VERSION;
        }
    }
    public static class Builder extends AbstractDataBuilder<IdDataManipulator> implements DataManipulatorBuilder<IdDataManipulator, Immutable> {
        public Builder() {
            super(IdDataManipulator.class, 1);
        }

        @Override
        public IdDataManipulator create() {
            return new IdDataManipulator(ItemizerKeys.ITEM_ID,"no-id");
        }

        @Override
        public Optional<IdDataManipulator> createFrom(DataHolder dataHolder) {
            return create().fill(dataHolder);
        }

        @Override
        protected Optional<IdDataManipulator> buildContent(DataView container) throws InvalidDataException {
            return Optional.of(create());
        }
    }
}
