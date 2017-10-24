package com.ylinor.itemizer.data;

import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.data.handlers.ConfigurationHandler;

import java.util.List;
import java.util.Optional;

public class ItemDAO {
    /**
     * Call the configuration handler to retrieve an item from an id
     * @param id ID of the item
     * @return Optional of the item data
     */
    public static Optional<ItemBean> getItem(int id) {
        List<ItemBean> items = ConfigurationHandler.getItemList();
        for(ItemBean item: items) {
            if (item.getId() == id) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }
}
