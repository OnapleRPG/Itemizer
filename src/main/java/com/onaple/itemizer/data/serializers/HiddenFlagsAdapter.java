package com.onaple.itemizer.data.serializers;


import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.GlobalConfig;
import cz.neumimto.config.blackjack.and.hookers.annotations.EnableSetterInjection;
import cz.neumimto.config.blackjack.and.hookers.annotations.Setter;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Map;

@EnableSetterInjection
public class HiddenFlagsAdapter implements TypeSerializer<Map<String, Boolean>> {

    private GlobalConfig globalConfig;

    @Setter
    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Boolean> deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        return (Map<String, Boolean>) value.getValue(new TypeToken<Map<String, Boolean>>() {
        }.getType());
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Map<String, Boolean> obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }
}
