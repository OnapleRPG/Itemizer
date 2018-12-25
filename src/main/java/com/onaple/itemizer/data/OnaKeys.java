package com.onaple.itemizer.data;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class OnaKeys {
    public static Key<Value<Integer>> HIDDEN_FLAGS = null;


    public OnaKeys() {
        HIDDEN_FLAGS = Key.builder()
                .id("onaple:hiddenflags")
                .name("Hidden flags")
                .query(DataQuery.of(".","HideFlags"))
                .type(new TypeToken<Value<Integer>>(){})
                .build();
    }
}
