package com.onaple.itemizer;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;
import org.spongepowered.api.util.TypeTokens;

public class ItemizerKeys {

    private ItemizerKeys() {}
    public static final Key<Value<String>> ITEM_ID;

    static {
        ITEM_ID = Key.builder()
                .id("item.id")
                .name("Item ID")
                .type(TypeTokens.STRING_VALUE_TOKEN)
                .query(DataQuery.of(".","id"))
                .build();
    }

}
