package com.ylinor.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.data.beans.AttributeBean;
import com.ylinor.itemizer.data.beans.ItemBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemSerializer implements TypeSerializer<ItemBean> {

    @Override
    public ItemBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        // Item characteristics
        int id = value.getNode("id").getInt();
        String itemType = value.getNode("type").getString();
        String name = value.getNode("name").getString();
        String lore = value.getNode("lore").getString();
        int durability = value.getNode("durability").getInt();
        boolean unbreakable = value.getNode("unbreakable").getBoolean();
        // Item enchantments
        Map<String, Integer> enchants = new HashMap<>();
        Map<Object, ?> enchantsNode = value.getNode("enchants").getChildrenMap();
        for (Map.Entry<Object, ?> entry : enchantsNode.entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof ConfigurationNode) {
                enchants.put((String)entry.getKey(), ((ConfigurationNode) entry.getValue()).getNode("level").getInt());
            }
        }
        // IDs of the miners abilities
        List<Integer> miners = new ArrayList<>();
        List<? extends ConfigurationNode> minerList = value.getNode("miners").getChildrenList();
        for (ConfigurationNode minerNode : minerList) {
            int miner = minerNode.getInt();
            if (miner > 0) {
                miners.add(miner);
            }
        }

        List<AttributeBean> attributes = value.getNode("attributes").getList(TypeToken.of(AttributeBean.class));
        ItemBean item = new ItemBean(id, itemType, name, lore,durability, unbreakable, enchants, miners,attributes);

        return item;
    }

    @Override
    public void serialize(TypeToken<?> type, ItemBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}