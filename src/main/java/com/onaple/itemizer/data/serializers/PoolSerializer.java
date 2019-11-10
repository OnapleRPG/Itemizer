package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.beans.PoolItemBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.List;

public class PoolSerializer implements TypeSerializer<PoolBean> {

    @Override
    public PoolBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String id = value.getNode("id").getString();
        // Pool items
        List<PoolItemBean> itemList = value.getNode("items").getList(TypeToken.of(PoolItemBean.class));
        return new PoolBean(id, itemList );
    }

    @Override
    public void serialize(TypeToken<?> type, PoolBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}