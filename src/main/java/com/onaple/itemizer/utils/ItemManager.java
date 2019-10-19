package com.onaple.itemizer.utils;

import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.ItemBean;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Singleton
public class ItemManager {

        private ItemStack setCustomData(ItemStack item,String queryPath, Object value) {
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

    public ItemStack setId(ItemStack item,String id){
            return setCustomData(item,"id",id );
    }
    public String getId(ItemStack item){
         return (String) getCustomData(item,"id").orElse("");
    }


    public ItemBean getItemBean(ItemStack item){
        return ItemDAO.getItem(getId(item)).orElse(null);
    }

    public ItemBean register(String itemId,ItemStack itemStack) {
        itemStack = setId(itemStack,itemId);
        ItemBean itemBean = new ItemBean();
        itemBean.setId(itemId);
        //item type
        itemBean.setItemStackSnapshot(itemStack.createSnapshot());
        return itemBean;
    }
}
