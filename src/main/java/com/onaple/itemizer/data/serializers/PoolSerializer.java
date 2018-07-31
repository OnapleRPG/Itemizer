package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.PoolBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.*;

public class PoolSerializer implements TypeSerializer<PoolBean> {

    @Override
    public PoolBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String id = value.getNode("id").getString();
        // Pool items
        Map<Double, ItemBean> items = new HashMap<>();
        List<? extends ConfigurationNode> itemList = value.getNode("items").getChildrenList();
        for (ConfigurationNode itemNode : itemList) {
            double probability = itemNode.getNode("probability").getDouble();
            String reference = itemNode.getNode("ref").getString();
            String itemType = itemNode.getNode("type").getString();
            Optional<ItemBean> item = Optional.empty();
            if (reference != null && !reference.equals("")) {
                item = ItemDAO.getItem(reference);
            }
            if (!item.isPresent() && itemType != null){
                item = Optional.of(new ItemBean(itemType));
            }
            if (probability > 0 && item.isPresent()) {
                items.put(probability, item.get());
            }
        }
        PoolBean pool = new PoolBean(id, items);
        return pool;
    }

    @Override
    public void serialize(TypeToken<?> type, PoolBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}