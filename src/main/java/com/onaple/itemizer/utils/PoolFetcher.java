package com.onaple.itemizer.utils;

import com.onaple.itemizer.data.access.PoolDAO;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.beans.PoolItemBean;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.Iterator;
import java.util.Optional;
import java.util.Random;

public class PoolFetcher {
    /**
     * Retrieve an optional random item from a pool
     *
     * @param poolId Id of the pool
     * @return Random item, or nothing
     */
    private static Random random = new Random();

    public static Optional<ItemStack> fetchItemFromPool(String poolId) {
        Optional<ItemStack> item = Optional.empty();
        Optional<PoolBean> optionalPool = PoolDAO.getPool(poolId);
        if (optionalPool.isPresent()) {
            double randomValue = random.nextDouble();
            Iterator<PoolItemBean> iterator = optionalPool.get().getItems().iterator();
            double cumulatedProbabilities = 0;
            while (!item.isPresent() && iterator.hasNext()) {
                PoolItemBean poolItem = iterator.next();
                cumulatedProbabilities += poolItem.getProbability();
                if (randomValue <= cumulatedProbabilities) {
                    ItemStack stack = poolItem.getItem();
                    int quantity = 1;
                    if (poolItem.getQuantity() > 1) {
                        quantity = random.nextInt(poolItem.getQuantity()) + 1;
                    }
                    stack.setQuantity(quantity);
                    item = Optional.of(stack);
                }
            }
        }
        return item;
    }
}
