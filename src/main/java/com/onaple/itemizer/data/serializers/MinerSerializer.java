package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.MinerBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinerSerializer implements TypeSerializer<MinerBean> {

    @Override
    public MinerBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String id = value.getNode("id").getString();
        //  Types of blocks that can be mined
        Map<String,String> mineTypes = new HashMap<>();

        Map<Object, ?> minersTypeNode = value.getNode("mine_types").getChildrenMap();
        for (Map.Entry<Object, ?> entry : minersTypeNode.entrySet()) {
           // if (entry.getKey() instanceof String && entry.getValue() instanceof String) {

            if(entry.getValue() instanceof ConfigurationNode){
                ConfigurationNode configurationNode = (ConfigurationNode) entry.getValue();
                mineTypes.put((String)configurationNode.getKey(), configurationNode.getString((String)entry.getKey()));
            }
        }
        // IDs of the other miners inherited from
        List<String> inheritances = new ArrayList<>();
        List<? extends ConfigurationNode> inheritanceList = value.getNode("inherit").getChildrenList();
        for (ConfigurationNode inheritanceNode : inheritanceList) {
            String inheritance = inheritanceNode.getString();
            if (!inheritance.equals("")) {
                inheritances.add(inheritance);
            }
        }
        return new MinerBean(id, mineTypes, inheritances);
    }

    @Override
    public void serialize(TypeToken<?> type, MinerBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}