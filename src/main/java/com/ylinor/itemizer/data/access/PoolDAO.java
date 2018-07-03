package com.ylinor.itemizer.data.access;

import com.ylinor.itemizer.data.beans.PoolBean;
import com.ylinor.itemizer.data.handlers.ConfigurationHandler;

import java.util.List;
import java.util.Optional;

public class PoolDAO {
    /**
     * Call the configuration handler to retrieve a pool from an id
     * @param id ID of the pool
     * @return Optional of the pool data
     */
    public static Optional<PoolBean> getPool(int id) {
        List<PoolBean> pools = ConfigurationHandler.getPoolList();
        for(PoolBean pool: pools) {
            if (pool.getId() == id) {
                return Optional.of(pool);
            }
        }
        return Optional.empty();
    }
}
