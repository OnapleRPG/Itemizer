package com.onaple.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.ItemizerKeys;
import com.onaple.itemizer.data.beans.affix.AffixBean;
import com.onaple.itemizer.data.beans.affix.AffixRoot;
import com.onaple.itemizer.data.beans.crafts.CraftsRoot;
import com.onaple.itemizer.data.beans.crafts.ICraftRecipes;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemsRoot;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.beans.PoolsRoot;
import com.onaple.itemizer.data.manipulators.IdDataManipulator;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import com.onaple.itemizer.utils.ConfigUtils;
import lombok.NoArgsConstructor;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.plugin.PluginContainer;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.onaple.itemizer.Itemizer.getLogger;

@Singleton
@NoArgsConstructor
public class ConfigurationHandler {

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

    public void createItemizerDirectory() {
        Path configPath = Paths.get(configDir + "/itemizer/");
        if (!Files.exists(configPath)) {
            try {
                Files.createDirectories(configPath);
            } catch (IOException e) {
                Itemizer.getLogger().error("Can't create config dir", e);
            }
        }
    }

    private <T> T readConfiguration(String file, Class<T> clazz) throws IOException, ObjectMappingException {
        Path path = Paths.get(configDir + "/itemizer/", file);
        getLogger().info("path to file", path);
        initDefaultConfig(path);
        T root = ConfigUtils.load(clazz, path);
        if (root != null) {
            return root;
        } else {
            getLogger().warn("Empty config file");
            throw new IOException();
        }
    }

    /**
     * Read items configuration and interpret it
     */
    public int readItemsConfiguration() throws IOException, ObjectMappingException {
        itemList.clear();
        itemList.addAll(setIdtoItems(readConfiguration("items.conf", ItemsRoot.class).getItems()));
        saveItemConfig();
        return itemList.size();
    }

    public int readAffixConfiguration() throws IOException, ObjectMappingException {
        affixBeans.clear();
        affixBeans.addAll(readConfiguration("affix.conf", AffixRoot.class).getAffixes());
        return affixBeans.size();
    }

    /**
     * Read Craft configuration and interpret it
     */
    public int readCraftConfiguration() throws ObjectMappingException, IOException {
        craftList.clear();
        craftList.addAll(readConfiguration("craft.conf", CraftsRoot.class).getCraftingRecipes());
        return craftList.size();
    }

    /**
     * Read pools configuration and interpret it. Must be the last config file read.
     */
    public int readPoolsConfiguration() throws ObjectMappingException, IOException {
        poolList.clear();
        poolList.addAll(readConfiguration("pools.conf", PoolsRoot.class).getPoolList());
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
                getLogger().info("No config file set for {} default config will be loaded", path);
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
    private List<ItemBean> setIdtoItems(List<ItemBean> itemBeanList)
    {
        List<ItemBean> items = new ArrayList<>();
        itemBeanList.forEach(itemBean -> {
            ItemStack stack = itemBean.getItemStackSnapshot().createStack();
            if (!stack.get(ItemizerKeys.ITEM_ID).isPresent()) {
                IdDataManipulator id = new IdDataManipulator(ItemizerKeys.ITEM_ID, itemBean.getId());
                stack.offer(id);
                itemBean.setItemStackSnapshot(stack.createSnapshot());
            }
            items.add(itemBean);
        });
        return items;
    }
}
