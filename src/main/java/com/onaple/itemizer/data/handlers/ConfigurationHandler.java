package com.onaple.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.ConfigUtils;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.Crafts;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.Items;
import com.onaple.itemizer.data.beans.MinerBean;
import com.onaple.itemizer.data.beans.Mining;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import com.onaple.itemizer.data.serializers.PoolSerializer;
import com.onaple.itemizer.utils.MinerUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.item.inventory.ItemStack;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Singleton
public class ConfigurationHandler {
    public ConfigurationHandler() {
    }

    private final List<MinerBean> minerList = new ArrayList<>();

    public List<MinerBean> getMinerList() {
        return minerList;
    }

    private final List<ItemBean> itemList = new ArrayList<>();

    public List<ItemBean> getItemList() {
        return itemList;
    }

    private final List<PoolBean> poolList = new ArrayList<>();

    public List<PoolBean> getPoolList() {
        return poolList;
    }

    private final List<ICraftRecipes> craftList = new ArrayList<>();

    public List<ICraftRecipes> getCraftList() {
        return craftList;
    }


    /**
     * Read items configuration and interpret it
     *
     * @param path File path
     */
    public int readItemsConfiguration(Path path) {
        Items itemRoot = ConfigUtils.load(Items.class, path);
        if(itemRoot != null) {
            itemList.addAll(itemRoot.getItems());
            Itemizer.getLogger().info("{} items loaded from configuration.", itemList.size());
        } else {
            Itemizer.getLogger().warn("Empty config file");
        }

        return itemList.size();
    }

    /**
     * Read miners configuration and interpret it
     *
     * @param path path to file
     */
    public int readMinerConfiguration(Path path) {
        Mining mining = ConfigUtils.load(Mining.class, path);
        if(mining != null) {
            minerList.addAll(new MinerUtil(mining.getMiners()).getExpandedMiners());
            Itemizer.getLogger().info("{} miners loaded from configuration.", minerList.size());
        }else {
            Itemizer.getLogger().warn("Miner config file is empty");
        }
        return minerList.size();
    }

    /**
     * Read Craft configuration and interpret it
     *
     * @param path path to file
     */
    public int readCraftConfiguration(Path path) throws ObjectMappingException {
        Crafts crafts = ConfigUtils.load(Crafts.class, path);
        if(crafts != null) {
            craftList.addAll(crafts.getCraftingRecipes());
            Itemizer.getLogger().info("{} crafting recipes loaded from configuration.", craftList.size());
        } else {
            Itemizer.getLogger().warn("Crafing config is empty");
        }
        return craftList.size();
    }

    /**
     * Read pools configuration and interpret it. Must be the last config file read.
     *
     * @param configurationNode ConfigurationNode to read from
     */
    public int readPoolsConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(PoolBean.class), new PoolSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ItemStack.class), new ItemBeanRefOrItemIdAdapter());

        poolList.addAll(configurationNode.getNode("pools").getList(TypeToken.of(PoolBean.class)));
        poolList.forEach(poolBean -> poolBean.getItems().forEach(poolItemBean ->
                Itemizer.getLogger().info("pool item bean loaded = {},{},{}", poolItemBean.getItem(), poolItemBean.getProbability(), poolItemBean.getQuantity())
        ));
        Itemizer.getLogger().info("{} pools loaded from configuration.", poolList.size());
        return poolList.size();
    }

    /**
     * Load configuration from file
     *
     * @param configName Name of the configuration in the configuration folder
     * @return Configuration ready to be used
     */
    public CommentedConfigurationNode loadConfiguration(String configName) throws Exception {
        ConfigurationLoader<CommentedConfigurationNode> configLoader = getConfigurationLoader(configName);
        CommentedConfigurationNode configNode = null;
        try {
            configNode = configLoader.load();
        } catch (IOException e) {
            throw new Exception("Error while loading configuration '" + configName + "' : " + e.getMessage());
        }
        return configNode;
    }

    private ConfigurationLoader<CommentedConfigurationNode> getConfigurationLoader(String filename) {
        return HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
    }

    public void saveGlobalConfiguration(String filename) {
        ConfigurationLoader<CommentedConfigurationNode> config = HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
        final TypeToken<GlobalConfig> token = new TypeToken<GlobalConfig>() {
        };
        try {

            CommentedConfigurationNode node = config.load();
            node.setValue(token, Itemizer.getItemizer().getGlobalConfig());
            config.save(node);
        } catch (Exception e) {
            Itemizer.getLogger().error("{}", e.toString());
        }
    }


    public void saveItemConfig(String filename) {
        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
        final TypeToken<List<ItemBean>> token = new TypeToken<List<ItemBean>>() {
        };
        try {
            CommentedConfigurationNode root = configLoader.load();
            Itemizer.getLogger().info("{} items to save in configuration : [{}]", itemList.size(), itemList);
            root.getNode("items").setValue(token, itemList);
            configLoader.save(root);
        } catch (IOException | ObjectMappingException e) {
            Itemizer.getLogger().error("Error while save item in config {}", e);
        }
    }


    public GlobalConfig readGlobalConfig(Path path) {
        return ConfigUtils.load(GlobalConfig.class, path);
    }
}
