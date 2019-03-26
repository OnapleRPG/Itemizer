package com.onaple.itemizer.utils;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemEnchant;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class ItemDeconstruct {
    private ItemBean itemRegistred;
    private ItemStack itemToRegister;
    public ItemDeconstruct(ItemStack itemStack) {
        this.itemToRegister = itemStack;
        this.itemRegistred = new ItemBean();
    }

    public ItemBean register(String itemId) {
        this.itemRegistred.setId(itemId);
        //item type
        itemRegistred.setType(itemToRegister.getType());
        //item name
        itemToRegister.get(Keys.DISPLAY_NAME).ifPresent(text -> itemRegistred.setName(text.toPlain()));
        //item lore
        List<Text> lore = new ArrayList<>();
        itemToRegister.get(Keys.ITEM_LORE).ifPresent(lore::addAll);
        if (!lore.isEmpty()) {
            List<String> loreString = lore.stream().map(Text::toPlain).collect(Collectors.toList());
            itemRegistred.setLore(String.join("\n", loreString));
        }
        //item unbrekable
        itemToRegister.get(Keys.UNBREAKABLE).ifPresent(itemRegistred::setUnbreakable);
        //item durability
        itemToRegister.get(Keys.ITEM_DURABILITY).ifPresent(itemRegistred::setMaxDurability);
        //item enchants
        Map<String, ItemEnchant> enchants = new HashMap<>();
        itemToRegister.get(Keys.ITEM_ENCHANTMENTS).ifPresent(enchantments -> enchantments
                .forEach(enchantment -> enchants.put(enchantment.getType().getId(), new ItemEnchant().setLevel(enchantment.getLevel()))));
        itemRegistred.setEnchants(enchants);
        getAttribute();
        return itemRegistred;
    }
    private void getAttribute() {
        List atributelist = new ArrayList<>();
        Optional<Object> atributeOptional = getCustomData(itemToRegister, "AttributeModifiers");
        if (atributeOptional.isPresent()) {
            Object atributes = atributeOptional.get();
            if (atributes instanceof List) {
                atributelist = (List) atributes;
                List<AttributeBean> attributeBeanList = new ArrayList<>();
                for (Object o :
                        atributelist) {
                    if(o instanceof DataContainer) {

                        DataContainer container = (DataContainer) o;
                        AttributeBean attributeBean = new AttributeBean();
                        container.get(DataQuery.of("AttributeName"))
                                .ifPresent(o1 ->{ if(o1 instanceof String){ attributeBean.setName((String) o1);}
                                });
                        container.get(DataQuery.of("Amount"))
                                .ifPresent(o1 ->{ if(o1 instanceof Float ){ attributeBean.setAmount((float) o1);}
                                else if(o1 instanceof Integer){ attributeBean.setAmount(((Integer) o1).floatValue());}
                                });
                        container.get(DataQuery.of("Operation"))
                                .ifPresent(o1 ->{ if(o1 instanceof Integer){ attributeBean.setOperation((Integer) o1);}
                                });
                        container.get(DataQuery.of("Slot"))
                                .ifPresent(o1 ->{ if(o1 instanceof String){ attributeBean.setSlot((String) o1);}
                                });
                        attributeBeanList.add(attributeBean);
                    }

                    else {
                        Itemizer.getLogger().info(" {} is not instance of DataContainer", o.getClass().getName());

                    }

                }
                itemRegistred.setAttributeList(attributeBeanList);
            } else {
                Itemizer.getLogger().info("{} is not instance of List", atributes.getClass().getName());
            }
        }
    }
    private Optional<Object> getCustomData(ItemStack target, String queryPath) {
        List<String> queryList ;

        if(queryPath.contains(".")) {
            String[] queries = queryPath.split(".");
            queryList = Arrays.stream(queries).collect(Collectors.toList());
        }
        else {
            queryList = new ArrayList<>();
            queryList.add(queryPath);
        }
        queryList.add(0, "UnsafeData");
        DataQuery dt = DataQuery.of(queryList);
        return target.toContainer().get(dt);
    }
}
