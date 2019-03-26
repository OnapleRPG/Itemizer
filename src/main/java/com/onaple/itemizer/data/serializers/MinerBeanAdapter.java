package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.MinerBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class MinerBeanAdapter implements TypeSerializer<List<MinerBean>> {

    @Override
    public List<MinerBean> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        List<String> clist = value.getList(TypeToken.of(String.class));

        return Itemizer.getConfigurationHandler().getMinerList()
                .stream()
                .filter(minerBean -> clist.contains(minerBean.getId()))
                .collect(Collectors.toList());

    }

    @Override
    public void serialize(TypeToken<?> type, List<MinerBean> obj, ConfigurationNode value) throws ObjectMappingException {
        if (obj == null)
            return;
        value.setValue(obj.stream().map(MinerBean::getId).collect(Collectors.toList()));
    }
}