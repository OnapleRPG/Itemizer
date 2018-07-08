package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.MinerBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.util.ArrayList;
import java.util.List;

public class MinerSerializer implements TypeSerializer<MinerBean> {

    @Override
    public MinerBean deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        int id = value.getNode("id").getInt();
        //  Types of blocks that can be mined
        List<String> mineTypes = new ArrayList<>();
        List<? extends ConfigurationNode> minerNodeList = value.getNode("mine_types").getChildrenList();
        for (ConfigurationNode minerNode : minerNodeList) {
            String minerType = minerNode.getString();
            if (!minerType.isEmpty()) {
                mineTypes.add(minerType);
            }
        }
        // IDs of the other miners inherited from
        List<Integer> inheritances = new ArrayList<>();
        List<? extends ConfigurationNode> inheritanceList = value.getNode("inherit").getChildrenList();
        for (ConfigurationNode inheritanceNode : inheritanceList) {
            int inheritance = inheritanceNode.getInt();
            if (inheritance > 0) {
                inheritances.add(inheritance);
            }
        }
        return new MinerBean(id, mineTypes, inheritances);
    }

    @Override
    public void serialize(TypeToken<?> type, MinerBean obj, ConfigurationNode value) throws ObjectMappingException {

    }
}