package com.onaple.itemizer.data;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.beans.AttributeBean;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.mutable.Value;

import java.util.List;

public class OnaKeys {
    public static Key<Value<Integer>> HIDDEN_FLAGS = null;
    public static Key<Value<List<AttributeBean>>> ATTRIBUTE_MODIFIER = null;

    public OnaKeys() {
        HIDDEN_FLAGS = Key.builder()
                .id("hiddenflags")
                .name("Hidden flags")
                .query(DataQuery.of(".","HideFlags"))
                .type(new TypeToken<Value<Integer>>(){})
                .build();
        ATTRIBUTE_MODIFIER = Key.builder()
                .id("attributemodifier")
                .name("Attribute modifier")
                .query(DataQuery.of("UnsafeData","AttributeModifier"))
                .type(new TypeToken<Value<List<AttributeBean>>>() {})
                .build();
    }
}
