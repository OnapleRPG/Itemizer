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
import org.spongepowered.api.text.Text;

import java.util.Optional;

public class BaseNameDataManipulator extends AbstractSingleData<Text, BaseNameDataManipulator, BaseNameDataManipulator.Immutable> {

    public static final int CONTENT_VERSION = 1;

    public BaseNameDataManipulator(Key<? extends Value<Text>> usedKey, Text value) {
        super(usedKey, value);
    }

    @Override
    protected Value<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(ItemizerKeys.BASE_NAME, getValue());
    }

    @Override
    public Optional<BaseNameDataManipulator> fill(DataHolder dataHolder, MergeFunction overlap) {
        Optional<BaseNameDataManipulator> data_ = dataHolder.get(BaseNameDataManipulator.class);
        if (data_.isPresent()) {
            BaseNameDataManipulator data = data_.get();
            BaseNameDataManipulator finalData = overlap.merge(this, data);
            setValue(finalData.getValue());
        }
        return Optional.of(this);
    }


    @Override
    public Optional<BaseNameDataManipulator> from(DataContainer container) {
        return from((DataView) container);
    }

    public Optional<BaseNameDataManipulator> from(DataView view) {
        if (view.contains(ItemizerKeys.BASE_NAME.getQuery())) {
            Optional<Text> idValue = view.getSerializable(ItemizerKeys.BASE_NAME.getQuery(),Text.class);
            idValue.ifPresent(this::setValue);
            return Optional.of(this);
        } else {
            return Optional.empty();
        }
    }

    @Override
    public BaseNameDataManipulator copy() {
        return new BaseNameDataManipulator(ItemizerKeys.BASE_NAME, getValue());
    }

    @Override
    public Immutable asImmutable() {
        return new Immutable(ItemizerKeys.BASE_NAME, getValue());
    }

    @Override
    public int getContentVersion() {
        return CONTENT_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(ItemizerKeys.BASE_NAME.getQuery(), getValue());

    }

public static class Immutable extends AbstractImmutableSingleData<Text, BaseNameDataManipulator.Immutable, BaseNameDataManipulator> {

    protected Immutable(Key<? extends Value<Text>> usedKey, Text value) {
        super(usedKey, value);
    }

    @Override
    protected ImmutableValue<?> getValueGetter() {
        return Sponge.getRegistry().getValueFactory().createValue(ItemizerKeys.BASE_NAME, getValue()).asImmutable();
    }

    @Override
    public BaseNameDataManipulator asMutable() {
        return new BaseNameDataManipulator(ItemizerKeys.BASE_NAME, getValue());
    }

    @Override
    public int getContentVersion() {
        return CONTENT_VERSION;
    }

    @Override
    public DataContainer toContainer() {
        return super.toContainer().set(ItemizerKeys.BASE_NAME.getQuery(), getValue());

    }
}

public static class Builder extends AbstractDataBuilder<BaseNameDataManipulator> implements DataManipulatorBuilder<BaseNameDataManipulator, Immutable> {
    public Builder() {
        super(BaseNameDataManipulator.class, 1);
    }

    @Override
    public BaseNameDataManipulator create() {
        return new BaseNameDataManipulator(ItemizerKeys.BASE_NAME, Text.EMPTY);
    }

    @Override
    public Optional<BaseNameDataManipulator> createFrom(DataHolder dataHolder) {
        return create().fill(dataHolder);
    }

    @Override
    protected Optional<BaseNameDataManipulator> buildContent(DataView container) throws InvalidDataException {
        return create().from(container);
    }
}
}
