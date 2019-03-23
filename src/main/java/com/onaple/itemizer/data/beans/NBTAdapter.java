package com.onaple.itemizer.data.beans;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class NBTAdapter implements TypeSerializer<Map<String,Object>> {

    @Nullable
    @Override
    public Map<String,Object> deserialize(@NonNull TypeToken type, @NonNull ConfigurationNode value) {
        Map<String,Object> nbtList = new HashMap<>();
        for (Map.Entry<Object,?> entry : value.getChildrenMap().entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof ConfigurationNode) {
                nbtList.put((String)entry.getKey(),((ConfigurationNode) entry.getValue()).getValue());
            }
        }
        return nbtList;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Map<String, Object> obj, @NonNull ConfigurationNode value) {
        if (obj != null) {
            value.setValue(obj);
        }
    }
}
