package com.onaple.itemizer.data.access;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;

import java.util.List;
import java.util.Optional;

public class PoolDAO {
    /**
     * Call the configuration handler to retrieve a pool from an id
     * @param id ID of the pool
     * @return Optional of the pool data
     */
    public static Optional<PoolBean> getPool(String id) {
        List<PoolBean> pools = Itemizer.getConfigurationHandler().getPoolList();
        for(PoolBean pool: pools) {
            if (pool.getId().equals(id)) {
                return Optional.of(pool);
            }
        }
        return Optional.empty();
    }
}
