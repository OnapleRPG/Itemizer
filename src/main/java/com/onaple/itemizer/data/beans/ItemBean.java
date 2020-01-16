package com.onaple.itemizer.data.beans;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.manipulators.IdDataManipulator;
import lombok.Data;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.inventory.ItemStackSnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@ConfigSerializable
@Data
@NoArgsConstructor
public class ItemBean {

    /** ID of the item in config **/
    @Setting("id")
    private String id;

    @Setting("item")
    private ItemStackSnapshot itemStackSnapshot;

    @Setting("thirdParties")
    private Set<ItemNbtFactory> thirdParties = new TreeSet<>();

    private static ItemStack setCustomData(ItemStack item, String queryPath, Object value) {
        List<String> queryList;
        if (queryPath.contains(".")) {
            String[] queries = queryPath.split(".");
            queryList = Arrays.stream(queries).collect(Collectors.toList());
        } else {
            queryList = new ArrayList<>();
            queryList.add(queryPath);
        }
        queryList.add(0, "UnsafeData");
        DataQuery dt = DataQuery.of(queryList);
        item = ItemStack.builder()
                .fromContainer(item.toContainer().set(dt, value))
                .build();
        return item;
    }
    private static Optional<Object> getCustomData(ItemStack target, String queryPath) {
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

    public List<AttributeBean> getItemAttribute(){

            Optional<Object> atributeOptional = getCustomData(this.itemStackSnapshot.createStack(), "AttributeModifiers");
            if (atributeOptional.isPresent()) {
                Object atributes = atributeOptional.get();
                if (atributes instanceof List) {
                    List atributelist = (List) atributes;
                    List<AttributeBean> attributeBeanList = new ArrayList<>();
                    for (Object o :
                            atributelist) {
                        if (o instanceof DataContainer) {

                            DataContainer container = (DataContainer) o;
                            AttributeBean attributeBean = new AttributeBean();
                            container.get(DataQuery.of("AttributeName"))
                                    .ifPresent(o1 -> {
                                        if (o1 instanceof String) {
                                            attributeBean.setName((String) o1);
                                        }
                                    });
                            container.get(DataQuery.of("Amount"))
                                    .ifPresent(o1 -> {
                                        if (o1 instanceof Float) {
                                            attributeBean.setAmount((float) o1);
                                        } else if (o1 instanceof Integer) {
                                            attributeBean.setAmount(((Integer) o1).floatValue());
                                        }
                                    });
                            container.get(DataQuery.of("Operation"))
                                    .ifPresent(o1 -> {
                                        if (o1 instanceof Integer) {
                                            attributeBean.setOperation((Integer) o1);
                                        }
                                    });
                            container.get(DataQuery.of("Slot"))
                                    .ifPresent(o1 -> {
                                        if (o1 instanceof String) {
                                            attributeBean.setSlot((String) o1);
                                        }
                                    });
                            attributeBeanList.add(attributeBean);
                        } else {
                            Itemizer.getLogger().info(" {} is not instance of DataContainer", o.getClass().getName());

                        }

                    }
                    return attributeBeanList;
                } else {
                    Itemizer.getLogger().info("{} is not instance of List", atributes.getClass().getName());
                }
            }
            return new ArrayList<>();
        }


    public static ItemStack setId(ItemStack item,String id){
        return setCustomData(item,"id",id );
    }
    public static String getId(ItemStack item){
        return (String) getCustomData(item,"id").orElse("");
    }

    public static ItemBean from(String itemId, ItemStack itemStack) {

        ItemBean itemBean = new ItemBean();
        //itemStack = setId(itemStack,itemId);
        itemBean.setId(itemId);
        itemStack.offer(itemStack.getOrCreate(IdDataManipulator.class).get());
        itemStack.offer(ItemizerKeys.ITEM_ID, itemId);
        //item type
        itemBean.setItemStackSnapshot(itemStack.createSnapshot());
        return itemBean;
    }

}

