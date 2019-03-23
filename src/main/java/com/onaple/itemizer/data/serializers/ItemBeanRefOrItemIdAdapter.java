package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;

/**
 * Created by NeumimTo on 23.3.2019.
 */
public class ItemBeanRefAdapter implements TypeSerializer<ItemBean> {

    @Override
    public ItemBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String idvalue = value.getValue(TypeToken.of(String.class));
        return ItemDAO.getItem(idvalue).orElseThrow(() ->
                new ObjectMappingException("Could not find item with id " + idvalue));
    }

    @Override
    public void serialize(TypeToken<?> type, @Nullable ItemBean obj, ConfigurationNode value) throws ObjectMappingException {
        value.setValue(obj.getId());
    }
}
