package com.onaple.itemizer.utils;

import com.google.common.base.Functions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.MemoryDataContainer;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;

import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

public class ItemDeconstructor {


    public ItemDeconstructor() {
    }

    public ItemBean register(ItemStack itemStack) {
        ItemBean itemRegistred = new ItemBean();
        //item type
        itemRegistred.setType(itemStack.getType().getName());
        //item  id
        getCustomData(itemStack, "id").ifPresent(o -> {
            if (o instanceof String) {
                itemRegistred.setId((String) o);
            }
        });
        //item name
        itemStack.get(Keys.DISPLAY_NAME).ifPresent(text -> itemRegistred.setName(text.toPlain()));
        //item lore
        List<Text> lore = new ArrayList<>();
        itemStack.get(Keys.ITEM_LORE).ifPresent(lore::addAll);
        if (!lore.isEmpty()) {
            List<String> loreString = lore.stream().map(Text::toPlain).collect(Collectors.toList());
            itemRegistred.setLore(String.join("\n", loreString));
        }
        //item unbrekable
        itemStack.get(Keys.UNBREAKABLE).ifPresent(itemRegistred::setUnbreakable);
        //item durability
        itemStack.get(Keys.ITEM_DURABILITY).ifPresent(itemRegistred::setMaxDurability);
        //item enchants
        Map<String, Integer> enchants = new HashMap<>();
        itemStack.get(Keys.ITEM_ENCHANTMENTS).ifPresent(enchantments -> enchantments
                .forEach(enchantment -> enchants.put(enchantment.getType().getId(), enchantment.getLevel())));
        itemRegistred.setEnchants(enchants);
        getAttribute(itemStack,itemRegistred);
        return itemRegistred;
    }

    private void getAttribute(ItemStack item,ItemBean itemRegistred) {
        List atributelist = new ArrayList<>();
        Optional<Object> atributeOptional = getCustomData(item, "AttributeModifiers");
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
                                .ifPresent(o1 ->{ if(o1 instanceof Float){ attributeBean.setAmount((Float) o1);}
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
                        Itemizer.getLogger().info(o.getClass().getName() + "is not instance of DataContainer");

                    }

                }
                itemRegistred.setAttributeList(attributeBeanList);
            }else {
                Itemizer.getLogger().info(atributes.getClass().getName() + "is not instance of List");
            }
        }
    }
    private Optional<Object> getCustomData(ItemStack target, String queryPath) {
        List<String> queryList ;
        if(queryPath.contains(".")) {
            String[] queries = queryPath.split(".");
            Itemizer.getLogger().info(("length" + queries.length));
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



