package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.affix.AffixBean;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class AffixNameAdapter implements TypeSerializer<AffixBean> {

    @Nullable
    @Override
    public AffixBean deserialize(@NonNull TypeToken<?> type, @NonNull ConfigurationNode value) throws ObjectMappingException {
        String groupName = value.getString();
        return Itemizer.getConfigurationHandler().getAffixBeans()
                .stream()
                .filter(affixBean -> affixBean.getGroupName().equals(groupName))
                .findFirst()
                .orElseThrow(() -> new ObjectMappingException(String.format("No group name called [%s].",groupName)));
    }

    @Override
    public void serialize(@NonNull TypeToken<?> type, @Nullable AffixBean obj, @NonNull ConfigurationNode value) throws ObjectMappingException {
        if(obj != null) {
            value.setValue(obj.getGroupName());
        }
    }
}
