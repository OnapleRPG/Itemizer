package com.onaple.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemsRoot;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.beans.PoolsRoot;
import com.onaple.itemizer.data.beans.affix.AffixBean;
import com.onaple.itemizer.data.beans.affix.AffixRoot;
import com.onaple.itemizer.data.beans.crafts.CraftsRoot;
import com.onaple.itemizer.data.beans.crafts.ICraftRecipes;
import com.onaple.itemizer.data.beans.recipes.RowCraft;
import com.onaple.itemizer.utils.ConfigUtils;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.plugin.PluginContainer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.onaple.itemizer.Itemizer.getLogger;

@Singleton
public class ConfigurationHandler {

    public ConfigurationHandler() {
    }

    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;


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

    private final List<AffixBean> affixBeans = new ArrayList<>();

    public List<AffixBean> getAffixBeans() {
        return affixBeans;
    }

    public List<RowCraft> getRowCraftList() {
        return rowCraftList;
    }

    private final  List<RowCraft> rowCraftList = new ArrayList<>();



    public void createItemizerDirectory() {
        Path configPath = Paths.get(configDir + "/itemizer/");
        if (!configPath.toFile().exists()) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                Itemizer.getLogger().error("Can't create config dir", e);
            }
        }
    }

    private <T> List<T> readConfiguration(String file, Class<T> clazz) throws IOException, ObjectMappingException {
        Path path = Paths.get(configDir + "/itemizer/", file + ".conf");
        Path folderPath = Paths.get(configDir + "/itemizer/" + file + "/");
        List<T> rootList = new ArrayList<>();
        if (!folderPath.toFile().exists()) {
            initDefaultConfig(path);
            rootList.add(ConfigUtils.load(clazz, path));
        } else {
            rootList = ConfigUtils.loadMultiple(clazz, folderPath);
        }
        rootList.removeIf(root -> root == null);
        return rootList;
    }

    /**
     * Read items configuration and interpret it
     */
    public int readItemsConfiguration() throws IOException, ObjectMappingException {
        itemList.clear();
        readConfiguration("items", ItemsRoot.class).forEach(itemRoot -> itemList.addAll(itemRoot.getItems()));
        return itemList.size();
    }

    public int readAffixConfiguration() throws IOException, ObjectMappingException {
        affixBeans.clear();
        readConfiguration("affix", AffixRoot.class).forEach(affixRoot -> affixBeans.addAll(affixRoot.getAffixes()));
        return affixBeans.size();
    }

    /**
     * Read Craft configuration and interpret it
     */
    public int readCraftConfiguration() throws ObjectMappingException, IOException {
        craftList.clear();
        readConfiguration("crafts", CraftsRoot.class).forEach(craftRoot -> craftList.addAll(craftRoot.getCraftingRecipes()));
        return craftList.size();
    }

    /**
     * Read pools configuration and interpret it. Must be the last config file read.
     */
    public int readPoolsConfiguration() throws ObjectMappingException, IOException {
        poolList.clear();
        readConfiguration("pools", PoolsRoot.class).forEach(poolRoot -> poolList.addAll(poolRoot.getPoolList()));
        return poolList.size();
    }

    public GlobalConfig readGlobalConfig() throws IOException, ObjectMappingException {
        Path path = Paths.get(configDir + "/itemizer/", "global.conf");
        initDefaultConfig(path);
        return ConfigUtils.load(GlobalConfig.class, path);
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
                getLogger().info("No config file set for {}. default config will be loaded", path);
                if (itemsDefaultConfigFile.isPresent()) {
                    try {
                        itemsDefaultConfigFile.get().copyToFile(path);
                    } catch (IOException e) {
                        getLogger().error("Error while setting default configuration : ", e);
                    }
                } else {
                    getLogger().warn("Default config not found");
                }
            }
        }
    }
}

