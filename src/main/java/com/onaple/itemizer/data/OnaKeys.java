package com.onaple.itemizer.data;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.AttributeParams;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

import java.util.List;

public class OnaKeys {
    public static Key<Value<Integer>> HIDDEN_FLAGS;
    public static Key<Value<List<AttributeParams>>> ATTRIBUTE_MODIFIER = null;
    static Key<Value<String>> ATTRIBUTE_NAME;

    public OnaKeys() {
        HIDDEN_FLAGS = Key.builder()
                .id("itemizer:hidden_flags")
                .name("Hidden flags")
                .query(DataQuery.of(".","HideFlags"))
                .type(TypeTokens.INTEGER_VALUE_TOKEN)
                .build();
        ATTRIBUTE_MODIFIER = Key.builder()
                .id("itemizer:attribute_modifier")
                .name("Attribute modifier")
                .query(DataQuery.of(".","AttributeModifier"))
                .type(new TypeToken<Value<List<AttributeParams>>>() {})
                .build();

    }
}
