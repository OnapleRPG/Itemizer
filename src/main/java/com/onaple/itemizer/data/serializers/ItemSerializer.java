package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
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
        String id = value.getNode("id").getString();
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
        Map<String,Object> nbtList = new HashMap<>();
        for (Map.Entry<Object,?> entry :value.getNode("nbt").getChildrenMap().entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof ConfigurationNode) {
                nbtList.put((String)entry.getKey(),((ConfigurationNode) entry.getValue()).getValue());
            }
        }
        // IDs of the miners abilities
        List<String> miners = new ArrayList<>();
        List<? extends ConfigurationNode> minerList = value.getNode("miners").getChildrenList();
        for (ConfigurationNode minerNode : minerList) {
            String miner = minerNode.getString();
            if (!miner.equals("")) {
                miners.add(miner);
            }
        }

        // tool
        String toolType = value.getNode("toolType").getString();
        int toolLevel = value.getNode("toolLevel").getInt();
        List<AttributeBean> attributes = value.getNode("attributes").getList(TypeToken.of(AttributeBean.class));
        ItemBean item = new ItemBean(id, itemType, name, lore, durability, unbreakable, enchants, miners, attributes,nbtList);
        item.setToolType(toolType);
        item.setToolLevel(toolLevel);
        return item;
    }

    @Override
    public void serialize(TypeToken<?> type, ItemBean obj, ConfigurationNode value) throws ObjectMappingException {
       value.getNode("id").setValue(obj.getId());
       value.getNode("type").setValue(obj.getType());
       value.getNode("name").setValue(obj.getName());
       value.getNode("lore").setValue(obj.getLore());
       value.getNode("durability").setValue(obj.getDurability());
       value.getNode("unbreakable").setValue(obj.isUnbreakable());
        value.getNode("toolType").setValue(obj.getToolLevel());
        value.getNode("toolLevel").getInt(obj.getToolLevel());
        value.getNode("attributes").setValue(obj.getAttributeList());
    }
}