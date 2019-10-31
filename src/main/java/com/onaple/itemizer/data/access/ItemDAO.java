package com.onaple.itemizer.data.access;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Singleton
public class ItemDAO {

    @Inject
    private ConfigurationHandler configurationHandler;

    /**
     * Call the configuration handler to retrieve an item from an id
     * @param id ID of the item
     * @return Optional of the item data
     */
    public Optional<ItemBean> getItem(String id) {
        List<ItemBean> items = Itemizer.getConfigurationHandler().getItemList();
        for(ItemBean item: items) {
            if (item.getId().equals(id)) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public ItemBean save(ItemBean item){
        List<ItemBean> items = configurationHandler.getItemList();
        if(items.stream().noneMatch(itemBean -> itemBean.getId().equals(item.getId()))){
            items.add(item);
            Itemizer.getConfigurationHandler().saveItemConfig();
        }
        return item;
    }

    public Map<String, ItemBean> getMap(){
        List<ItemBean> items = Itemizer.getConfigurationHandler().getItemList();
        return items.stream().collect(Collectors.toMap(ItemBean::getId, Function.identity()));
    }
}
