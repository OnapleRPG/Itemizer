package com.ylinor.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.data.beans.MinerBean;
import com.ylinor.itemizer.data.serializers.ItemSerializer;
import com.ylinor.itemizer.data.serializers.MinerSerializer;
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

    /**
     * Read items configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static void readItemsConfiguration(CommentedConfigurationNode configurationNode){
        itemList = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ItemBean.class), new ItemSerializer());
        try {
            itemList = configurationNode.getNode("items").getList(TypeToken.of(ItemBean.class));
            for (ItemBean item: itemList) {
                Itemizer.getLogger().info("Item from config : " + item.getId() + "-" + item.getType() + "-" + item.getLore());
            }
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error("Error while reading configuration 'items' : " + e.getMessage());
        }
    }

    /**
     * Read miners configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public static void readMinerConfiguration(CommentedConfigurationNode configurationNode){
        minerList = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(MinerBean.class), new MinerSerializer());
        try {
            minerList = configurationNode.getNode("miners").getList(TypeToken.of(MinerBean.class));
            for (MinerBean miner: minerList) {
                Itemizer.getLogger().debug("Miner from config : " + miner.getId());
            }
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error("Error while reading configuration 'harvestables' : " + e.getMessage());
        }
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
