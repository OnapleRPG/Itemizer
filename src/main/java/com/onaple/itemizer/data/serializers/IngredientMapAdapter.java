package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.Itemizer;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by NeumimTo on 24.3.2019.
 */
public class IngredientMapAdapter implements TypeSerializer<Map<Character, Ingredient>> {

    @Override
    public Map<Character, Ingredient> deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        Map<Object, ? extends ConfigurationNode> childrenMap = value.getChildrenMap();
        Map<Character, Ingredient> map = new HashMap<>();
        for (Map.Entry<Object, ? extends ConfigurationNode> entry : childrenMap.entrySet()) {
            String s = entry.getKey().toString();
            ConfigurationNode node = entry.getValue();
            ItemStack item = new ItemBeanRefOrItemIdAdapter().deserialize(type, node.getNode("item"));
            if (item == null) {
                Itemizer.getLogger().warn("Unknown item " + node.getNode("item").toString());
                continue;
            }
            map.put(s.charAt(0), Ingredient.builder().with(item).build());
        }
        return map;
    }

    @Override
    public void serialize(TypeToken<?> type, @Nullable Map<Character, Ingredient> obj, ConfigurationNode value)
            throws ObjectMappingException {
    }
}
