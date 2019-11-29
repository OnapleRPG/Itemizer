package com.onaple.itemizer;

import com.google.common.reflect.TypeToken;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.value.mutable.Value;

public class ItemizerKeys {

    public static Key<Value<String>> ITEM_ID;

    public ItemizerKeys() {
        ITEM_ID = Key.builder()
                .id("itemid")
                .name("Item ID")
                .type(new TypeToken<Value<String>>() {
                })
                .query(DataQuery.of("id"))
                .build();
    }

}
