package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
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
        String idvalue = String.valueOf(value.getNode("item").getValue());
        Optional<ItemBean> item = ItemDAO.getItem(idvalue);
        if (item.isPresent()) {
            ItemBean itemBean = item.get();
            return new ItemBuilder().buildItemStack(itemBean).get();
        } else {
            Optional<ItemType> type1 = Sponge.getRegistry().getType(ItemType.class, idvalue);
            if (type1.isPresent()) {
                return ItemStack.of(type1.get());
            }
        }
        throw new ObjectMappingException("Could not recognize itempattern pattern " + idvalue);
    }

    @Override
    public void serialize(TypeToken<?> type, @Nullable ItemStack obj, ConfigurationNode value) throws ObjectMappingException {
        //todo
    }
}
