package com.onaple.itemizer.data.translators;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.AttributeBean;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.persistence.DataTranslator;
import org.spongepowered.api.data.persistence.InvalidDataException;

import java.util.UUID;

public class AttributeTranslator implements DataTranslator<AttributeBean> {

    @Override
    public TypeToken<AttributeBean> getToken() {
        return TypeToken.of(AttributeBean.class);
    }

    @Override
    public AttributeBean translate(DataView view) throws InvalidDataException {
        AttributeBean attributeBean = new AttributeBean();
        view.getString(DataQuery.of("AttributeName")).ifPresent(attributeBean::setName);
        view.getInt(DataQuery.of("Operation")).ifPresent(attributeBean::setOperation);
        view.getString(DataQuery.of("Slot")).ifPresent(attributeBean::setSlot);
        view.getInt(DataQuery.of("Amount")).ifPresent(attributeBean::setAmount);
        return attributeBean;
    }

    @Override
    public DataContainer translate(AttributeBean obj) throws InvalidDataException {
        UUID uuid = UUID.randomUUID();
        DataContainer container = DataContainer.createNew();
        container.set(DataQuery.of("AttributeName"),obj.getName());
        container.set(DataQuery.of("Name"),obj.getName());
        container.set(DataQuery.of("Amount"),obj.getAmount());
        container.set(DataQuery.of("Operation"),obj.getOperation());
        container.set(DataQuery.of("Slot"),obj.getSlot());
        container.set(DataQuery.of("UUIDMost"),uuid.getMostSignificantBits());
        container.set(DataQuery.of("UUIDLeast"),uuid.getLeastSignificantBits());

        return container;
    }

    @Override
    public String getId() {
        return "attributetranslator";
    }

    @Override
    public String getName() {
        return "Attribute translator";
    }
}
