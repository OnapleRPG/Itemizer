package com.onaple.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.ConfigUtils;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.Crafts;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemsRoot;
import com.onaple.itemizer.data.beans.MinerBean;
import com.onaple.itemizer.data.beans.Mining;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.beans.PoolBeanRoot;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import com.onaple.itemizer.data.serializers.PoolSerializer;
import com.onaple.itemizer.utils.MinerUtil;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.onaple.itemizer.Itemizer.getLogger;

@Singleton
@NoArgsConstructor
public class ConfigurationHandler {

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;


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
     */
    public int readItemsConfiguration() {
        Path path =Paths.get(configDir + "/itemizer/", "items.conf");
        getLogger().info("path to file", path);
        initDefaultConfig(path);
        ItemsRoot itemRoot = ConfigUtils.load(ItemsRoot.class, path);
        if(itemRoot != null) {
            itemList.addAll(itemRoot.getItems());
            getLogger().info("{} items loaded from configuration.", itemList.size());
        } else {
            getLogger().warn("Empty config file");
        }

        return itemList.size();
    }

    /**
     * Read miners configuration and interpret it
     */
    public int readMinerConfiguration() {
        Path path =Paths.get(configDir + "/itemizer/", "miners.conf");
        initDefaultConfig(path);

        Mining mining = ConfigUtils.load(Mining.class, path);
        if(mining != null) {
            minerList.addAll(new MinerUtil(mining.getMiners()).getExpandedMiners());
            getLogger().info("{} miners loaded from configuration.", minerList.size());
        }else {
            getLogger().warn("Miner config file is empty");
        }
        return minerList.size();
    }

    /**
     * Read Craft configuration and interpret it
     */
    public int readCraftConfiguration() throws ObjectMappingException {
        Path path =Paths.get(configDir + "/itemizer/", "crafts.conf");
        initDefaultConfig(path);
        Crafts crafts = ConfigUtils.load(Crafts.class, path);
        if(crafts != null) {
            craftList.addAll(crafts.getCraftingRecipes());
            getLogger().info("{} crafting recipes loaded from configuration.", craftList.size());
        } else {
            getLogger().warn("Crafing config is empty");
        }
        return craftList.size();
    }

    /**
     * Read pools configuration and interpret it. Must be the last config file read.
     */
    public int readPoolsConfiguration() throws ObjectMappingException {
        Path path =Paths.get(configDir + "/itemizer/", "pools.conf");
        initDefaultConfig(path);
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(PoolBean.class), new PoolSerializer());
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ItemStack.class), new ItemBeanRefOrItemIdAdapter());
        PoolBeanRoot pools = ConfigUtils.load(PoolBeanRoot.class, path);
        if (pools != null) {
            poolList.addAll(poolList);
        }
        getLogger().info("{} pools loaded from configuration. [{}]", poolList.size(), poolList);
        return poolList.size();
    }

    public GlobalConfig readGlobalConfig() {
        Path path =Paths.get(configDir + "/itemizer/", "global.conf");
        initDefaultConfig(path);
        return ConfigUtils.load(GlobalConfig.class,path );
    }

    public void saveGlobalConfiguration() {
        String filename = configDir + "/itemizer/global.conf";
        ConfigurationLoader<CommentedConfigurationNode> config = HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
        final TypeToken<GlobalConfig> token = new TypeToken<GlobalConfig>() {
        };
        try {
            CommentedConfigurationNode node = config.load();
            node.setValue(token, Itemizer.getItemizer().getGlobalConfig());
            config.save(node);
        } catch (Exception e) {
            getLogger().error("can't save global config", e);
        }
    }


    public void saveItemConfig() {
        String filename = configDir + "/itemizer/items.conf";
        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
        final TypeToken<List<ItemBean>> token = new TypeToken<List<ItemBean>>() {
        };
        try {
            CommentedConfigurationNode root = configLoader.load();
            getLogger().info("{} items to save in configuration : [{}]", itemList.size(), itemList);
            root.getNode("items").setValue(token, itemList);
            configLoader.save(root);
        } catch (IOException | ObjectMappingException e) {
            getLogger().error("Error while save item in config", e);
        }
    }

    private void initDefaultConfig(Path path) {
        if (!path.toFile().exists()) {
            PluginContainer pluginInstance = Itemizer.getInstance();
            if (pluginInstance != null) {
                Optional<Asset> itemsDefaultConfigFile = pluginInstance.getAsset(path.getFileName().toString());
                getLogger().info("No config file set for {} default config will be loaded",path);
                if (itemsDefaultConfigFile.isPresent()) {
                    try {
                        itemsDefaultConfigFile.get().copyToFile(path);
                    } catch (IOException e) {
                        getLogger().error("Error while setting default configuration : {}", e.getMessage());
                    }
                } else {
                    getLogger().warn("Default config not found");
                }
            }
        }
    }
}
