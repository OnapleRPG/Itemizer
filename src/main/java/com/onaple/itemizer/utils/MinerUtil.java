package com.onaple.itemizer.utils;

import com.onaple.itemizer.data.beans.MinerBean;

import java.util.*;

public class MinerUtil {
    /** Map of miners **/
    private Map<String, MinerBean> miners = new HashMap<>();
    /** List of miners keys already processed **/
    private List<String> keysProcessed = new ArrayList<>();

    /**
     * Utility to resolve miners inheritance
     * @param minersList Miners with inherit relations
     */
    public MinerUtil(List<MinerBean> minersList) {
        for (MinerBean miner : minersList) {
            miners.put(miner.getId(), miner);
        }
    }

    /**
     * Resolve miners inheritance
     * @return List of exhaustive miners
     */
    public List<MinerBean> getExpandedMiners() {
        for (Map.Entry<String, MinerBean> miner : miners.entrySet()) {
            resolveDependencies(miner.getKey());
        }
        return new ArrayList<>(miners.values());
    }

    /**
     * Resolve dependencies of a miner, recursively
     * @param minerKey Key of the miner to expand
     */
    private void resolveDependencies(String minerKey) {
        keysProcessed.add(minerKey);
        MinerBean miner = miners.get(minerKey);
        for (String inheritKey : miner.getInheritances()) {
            if (!keysProcessed.contains(inheritKey)) {
                resolveDependencies(inheritKey);
            }
            Set<String> inheritValues = new HashSet<>(miner.getMineTypes());
            inheritValues.addAll(miners.get(inheritKey).getMineTypes());
            miner.setMineTypes(new ArrayList<>(inheritValues));
        }
        miners.put(minerKey, miner);
    }
}
