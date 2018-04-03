package com.ylinor.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.data.beans.MinerBean;
import com.ylinor.itemizer.data.beans.PoolBean;
import com.ylinor.itemizer.data.serializers.CraftingSerializer;
import com.ylinor.itemizer.data.serializers.ItemSerializer;
import com.ylinor.itemizer.data.serializers.MinerSerializer;
import com.ylinor.itemizer.data.serializers.PoolSerializer;
import com.ylinor.itemizer.utils.MinerUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ConfigurationHandler {
    private ConfigurationHandler() {}

    private static List<MinerBean> minerList;
    public static List<MinerBean> getMinerList(){
        return minerList;
    }

    private static List<ItemBean> itemList;
    public static List<ItemBean> getItemList(){
        return itemList;
    }

    private static List<PoolBean> poolList;
    public static List<PoolBean> getPoolList(){
        return poolList;
    }

    /**
     * Read items configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readItemsConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        itemList = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ItemBean.class), new ItemSerializer());
       // try {
            itemList = configurationNode.getNode("items").getList(TypeToken.of(ItemBean.class));
            Itemizer.getLogger().info(itemList.size() + " items loaded from configuration.");
            return itemList.size();
       // } catch (ObjectMappingException e) {
      //  }
    }

    /**
     * Read miners configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readMinerConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        minerList = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(MinerBean.class), new MinerSerializer());

            minerList = configurationNode.getNode("miners").getList(TypeToken.of(MinerBean.class));
            MinerUtil minerUtil = new MinerUtil(minerList);
            minerList = minerUtil.getExpandedMiners();
            for (MinerBean miner: minerList) {
                Itemizer.getLogger().debug("Miner from config : " + miner.getId() + " - " + miner.getMineTypes().size() + " blocks, " + miner.getInheritances().size() + " inheritances");
            }
            Itemizer.getLogger().info(minerList.size() + " miners loaded from configuration.");

            return minerList.size();

    }

    /**
     * Read Craft configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readCraftConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        List<ICraftRecipes> craftRecipes = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ICraftRecipes.class), new CraftingSerializer());

            craftRecipes = configurationNode.getNode("crafts").getList(TypeToken.of(ICraftRecipes.class));

            for (ICraftRecipes iCraftRecipes: craftRecipes) {
              //  Itemizer.getLogger().debug("Miner from config : " + craftRecipes + " - " + miner.getMineTypes().size() + " blocks, " + miner.getInheritances().size() + " inheritances");
                Itemizer.getCraftingDao().add(iCraftRecipes);
            }
            Itemizer.getLogger().info( Itemizer.getCraftingDao().getSize() + " craft(s) loaded from configuration.");
            return  Itemizer.getCraftingDao().getSize();
    }

    /**
     * Read pools configuration and interpret it. Must be the last config file read.
     * @param configurationNode ConfigurationNode to read from
     */
    public static int readPoolsConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        poolList = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(PoolBean.class), new PoolSerializer());

            poolList = configurationNode.getNode("pools").getList(TypeToken.of(PoolBean.class));
            Itemizer.getLogger().info(poolList.size() + " pools loaded from configuration.");
      return poolList.size();
    }

    /**
     * Load configuration from file
     * @param configName Name of the configuration in the configuration folder
     * @return Configuration ready to be used
     */
    public static CommentedConfigurationNode loadConfiguration(String configName) {
        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(Paths.get(configName)).build();
        CommentedConfigurationNode configNode = null;
        try {
            configNode = configLoader.load();
        } catch (IOException e) {
            Itemizer.getLogger().error("Error while loading configuration '" + configName + "' : " + e.getMessage());
        }
        return configNode;
    }
}
