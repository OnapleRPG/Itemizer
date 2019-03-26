package com.onaple.itemizer.data.beans;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.HashMap;
import java.util.Map;

public class EnchantsAdapter implements TypeSerializer<Map<String,Integer>> {

    @Nullable
    @Override
    public Map<String,Integer> deserialize(@NonNull TypeToken type, @NonNull ConfigurationNode value) {
        Map<String, Integer> i = new HashMap<>();
        for (Map.Entry<Object, ?> entry : value.getChildrenMap().entrySet()) {
            if (entry.getKey() instanceof String && entry.getValue() instanceof ConfigurationNode) {
                i.put((String)entry.getKey(), ((ConfigurationNode) entry.getValue()).getNode("level").getInt());
            }
        }
        return i;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Map<String, Integer> obj, @NonNull ConfigurationNode value) {
        if (obj != null) {
            value.setValue(obj);
        }
    }
}
