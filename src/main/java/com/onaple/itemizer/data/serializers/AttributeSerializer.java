package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.AttributeBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

public class AttributeSerializer implements TypeSerializer<AttributeBean> {
    @Override
    public AttributeBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        int operation = value.getNode("operation").getInt();
        Float amount = value.getNode("amount").getFloat();
        String name = value.getNode("name").getString();
        String slot = value.getNode("slot").getString();

        return new AttributeBean(name,slot,amount,operation);
    }

    @Override
    public void serialize(TypeToken<?> type, AttributeBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
