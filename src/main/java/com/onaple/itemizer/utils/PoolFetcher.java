package com.onaple.itemizer.utils;

import com.onaple.itemizer.data.access.PoolDAO;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.beans.PoolItemBean;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Singleton;
import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

@Singleton
public class PoolFetcher {

    private static final Random RANDOM = new Random();

    /**
     * Retrieve an optional random item from a pool. if the pool does not exist you will get
     * an empty Optional. and if the pool return nothing you will get an empty ItemStack.
     *
     * @param poolId Id of the pool you want to fetch
     * @return Random ItemStack based on configuration probability. Might be Optional.empty()
     * if the pool don't exist or ItemStack.empty() if the pool return nothing
     */
    public Optional<ItemStack> fetchItemFromPool(String poolId) {
        Optional<ItemStack> itemStackOptional = Optional.empty();
        ItemStack item = ItemStack.empty();
        Optional<PoolBean> optionalPool = PoolDAO.getPool(poolId);
        if (optionalPool.isPresent()) {
            double randomValue = RANDOM.nextDouble();
            Iterator<PoolItemBean> iterator = optionalPool.get().getItems().iterator();
            double cumulatedProbabilities = 0;
            while (item.isEmpty() && iterator.hasNext()) {
                PoolItemBean poolItem = iterator.next();
                cumulatedProbabilities += poolItem.getProbability();
                if (randomValue <= cumulatedProbabilities) {
                    ItemStack stack = poolItem.getItem();
                    int quantity = 1;
                    if (poolItem.getQuantity() > 1) {
                        quantity = RANDOM.nextInt(poolItem.getQuantity()) + 1;
                    }
                    stack.setQuantity(quantity);
                    itemStackOptional = Optional.of(stack);
                }
            }
        }
        return itemStackOptional;
    }
}
