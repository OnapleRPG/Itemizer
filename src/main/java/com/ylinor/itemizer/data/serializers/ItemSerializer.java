package com.ylinor.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.data.beans.ItemBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.HashMap;
import java.util.Map;

public class ItemSerializer implements TypeSerializer<ItemBean> {

    @Override
    public ItemBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        int id = value.getNode("id").getInt();
        String itemType = value.getNode("type").getString();
        String name = value.getNode("name").getString();
        String lore = value.getNode("lore").getString();
        boolean unbreakable = value.getNode("unbreakable").getBoolean();
        Map<String, Integer> enchants = new HashMap<>();
        Map<Object, ?> enchantsNode = value.getNode("enchants").getChildrenMap();
        for (Map.Entry<Object, ?> entry : enchantsNode.entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof ConfigurationNode) {
                enchants.put((String)entry.getKey(), ((ConfigurationNode) entry.getValue()).getNode("level").getInt());
            }
        }
        ItemBean item = new ItemBean(id, itemType, name, lore, unbreakable, enchants);
        return item;
    }

    @Override
    public void serialize(TypeToken<?> type, ItemBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}