package com.onaple.itemizer.data.beans.affix;

import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataView;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@ConfigSerializable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AffixTier extends AbstractAffix {

    @Setting("attributes")
    private List<AttributeBean> attributes;

    @Override
    public ItemStack apply(ItemStack itemStack) {
        super.apply(itemStack);
        return setAttribute(itemStack);
    }

    private ItemStack setAttribute(ItemStack itemStack) {
        List<DataView> containers = itemStack.toContainer().getViewList(DataQuery.of("UnsafeData", "AttributeModifiers")).orElse(new ArrayList<>());

        if (getAttributes().isEmpty()) {
            return itemStack;
        }
        for (AttributeBean att : getAttributes()) {
            DataContainer dc = createAttributeModifier(att);
            containers.add(dc);
        }

        DataContainer container = itemStack.toContainer();
        container.set(DataQuery.of("UnsafeData", "AttributeModifiers"), containers);
        itemStack = ItemStack.builder()
                .fromContainer(container)
                .build();
        return itemStack;
    }

    /**
     * Create the datacontainer for an attribute's data
     *
     * @param attribute Data of the attribute
     * @return DataContainer from which the item will be recreated
     */
    private DataContainer createAttributeModifier(AttributeBean attribute) {
        UUID uuid = UUID.randomUUID();
        DataContainer dataContainer = DataContainer.createNew();
        dataContainer.set(DataQuery.of("AttributeName"), attribute.getName());
        dataContainer.set(DataQuery.of("Name"), attribute.getName());
        dataContainer.set(DataQuery.of("Amount"), attribute.getAmount());
        dataContainer.set(DataQuery.of("Operation"), attribute.getOperation());
        dataContainer.set(DataQuery.of("Slot"), attribute.getSlot());
        dataContainer.set(DataQuery.of("UUIDMost"), uuid.getMostSignificantBits());
        dataContainer.set(DataQuery.of("UUIDLeast"), uuid.getLeastSignificantBits());
        return dataContainer;
    }
}
