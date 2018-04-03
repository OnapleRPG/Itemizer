package com.ylinor.itemizer.utils;

import com.ylinor.itemizer.data.access.PoolDAO;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.data.beans.PoolBean;

import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

public class PoolFetcher {
    /**
     * Retrieve an optional random item from a pool
     * @param poolId Id of the pool
     * @return Random item, or nothing
     */
    public static Optional<ItemBean> fetchItemFromPool(int poolId) {
        Optional<ItemBean> item = Optional.empty();
        Optional<PoolBean> optionalPool = PoolDAO.getPool(poolId);
        if (optionalPool.isPresent()) {
            Random random = new Random();
            double randomValue = random.nextDouble();
            Map<Double, ItemBean> poolItems = optionalPool.get().getItems();
            Iterator iterator = poolItems.entrySet().iterator();
            double cumulatedProbabilities = 0;
            while (!item.isPresent() && iterator.hasNext()) {
                Map.Entry pair = (Map.Entry)iterator.next();
                cumulatedProbabilities += (Double) pair.getKey();
                if (randomValue <= cumulatedProbabilities) {
                    item = Optional.ofNullable((ItemBean) pair.getValue());
                }
            }
        }
        return item;
    }
}