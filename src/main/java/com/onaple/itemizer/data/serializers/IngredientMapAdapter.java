package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.Map;

/**
 * Created by NeumimTo on 24.3.2019.
 */
public class IngredientMapAdapter implements TypeSerializer<Map<Character,Ingredient>> {

    @Override
    public Map<Character, Ingredient> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        Map<Object, ? extends ConfigurationNode> childrenMap = value.getChildrenMap();
        //todo
        return null;
    }

    @Override
    public void serialize(TypeToken<?> type, @Nullable Map<Character, Ingredient> obj, ConfigurationNode value)
            throws ObjectMappingException {
    }
}
