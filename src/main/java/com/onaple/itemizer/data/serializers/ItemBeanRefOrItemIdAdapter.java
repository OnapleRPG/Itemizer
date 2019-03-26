package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Optional;

/**
 * Created by NeumimTo on 23.3.2019.
 */
public class ItemBeanRefOrItemIdAdapter implements TypeSerializer<ItemStack> {

    @Override
    public ItemStack deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        ItemConfig value1 = value.getValue(TypeToken.of(ItemConfig.class));
        return value1.toItemStack();
    }

    @Override
    public void serialize(TypeToken<?> type, @Nullable ItemStack obj, ConfigurationNode value) throws ObjectMappingException {
        //todo
    }

    @ConfigSerializable
    private static class ItemConfig {

        @Setting("ref")
        String ref;

        @Setting("name")
        ItemType type;

        ItemStack toItemStack() {
            if (type != null) {
                return ItemStack.of(type);
            }
            Optional<ItemBean> item = ItemDAO.getItem(ref);
            if (item.isPresent()) {
                return new ItemBuilder().buildItemStack(item.get()).get();
            }
            return null;
        }
    }
}
