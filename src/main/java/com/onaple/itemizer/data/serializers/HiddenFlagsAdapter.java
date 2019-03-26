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

import java.util.HashMap;
import java.util.Map;

@EnableSetterInjection
public class HiddenFlagsAdapter implements TypeSerializer<Map<String, Boolean>> {

    private GlobalConfig globalConfig;

    private final static HashMap flagsMap = new HashMap<GlobalConfig.Flags, Integer>() {{
        put(GlobalConfig.Flags.Enchantments, 1);
        put(GlobalConfig.Flags.Attributes_modifiers, 2);
        put(GlobalConfig.Flags.Unbreakable, 4);
        put(GlobalConfig.Flags.CanDestroy, 8);
        put(GlobalConfig.Flags.CanPlaceOn, 16);
        put(GlobalConfig.Flags.Others, 32);
    }};

    @Setter
    public void setGlobalConfig(GlobalConfig globalConfig) {
        this.globalConfig = globalConfig;
    }

    @Nullable
    @Override
    @SuppressWarnings("unchecked")
    public Map<String, Boolean> deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        Map<String, Boolean> value1 = (Map<String, Boolean>) value.getValue(new TypeToken<Map<String, Boolean>>() {}.getType());
        int flagsValue = 0;
        for (Map.Entry<String, Boolean> flag : value1.entrySet()) {

            if (flag.getValue()) {
                flagsValue += (int) flagsMap.get(GlobalConfig.Flags.valueOf(flag.getKey()));
            }
        }
        globalConfig.setHiddenFlagsValue(flagsValue);
        return value1;
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable Map<String, Boolean> obj, @NonNull ConfigurationNode value) throws ObjectMappingException {

    }
}
