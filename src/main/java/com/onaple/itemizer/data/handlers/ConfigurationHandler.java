package com.onaple.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.*;
import com.onaple.itemizer.data.serializers.*;
import com.onaple.itemizer.utils.MinerUtil;

import cz.neumimto.config.blackjack.and.hookers.NotSoStupidObjectMapper;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMapper;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.text.format.TextColor;
import org.spongepowered.api.text.format.TextColors;

import javax.inject.Singleton;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Singleton
public class ConfigurationHandler {
    public ConfigurationHandler() {}

    private  List<MinerBean> minerList = new ArrayList<>();
    public  List<MinerBean> getMinerList(){
        return minerList;
    }

    private  List<ItemBean> itemList= new ArrayList<>();
    public  List<ItemBean> getItemList(){
        return itemList;
    }

    private  List<PoolBean> poolList= new ArrayList<>();
    public  List<PoolBean> getPoolList(){
        return poolList;
    }

    private  List<ICraftRecipes> craftList= new ArrayList<>();
    public  List<ICraftRecipes> getCraftList(){
        return craftList;
    }



    /**
     * Read items configuration and interpret it
     * @param path Path to the file to read configuration from
     */
    public int readItemsConfiguration(String path) throws ObjectMappingException {
        itemList = new ArrayList<>();
        HoconConfigurationLoader.Builder builder = HoconConfigurationLoader.builder().setPath(Paths.get(path));
        HoconConfigurationLoader build = builder.build();
        ObjectMapper<ItemsRootConfig> c = NotSoStupidObjectMapper.forClass(ItemsRootConfig.class);
        try {
            itemList = c.bind(new ItemsRootConfig()).populate(build.load()).getItems();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Itemizer.getLogger().info(itemList.size() + " items loaded from configuration.");
        return itemList.size();
    }

    /**
     * Read miners configuration and interpret it
     * * @param path Path to the file to read configuration from
     */
    public int readMinerConfiguration(String path) throws ObjectMappingException, IOException {
        minerList = new ArrayList<>();

        HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(Paths.get(path)).build();
        ObjectMapper<MinerBeanRoot> c = NotSoStupidObjectMapper.forClass(MinerBeanRoot.class);
        MinerBeanRoot res = c.bind(new MinerBeanRoot()).populate(hcl.load());
        minerList = res.getMiners();

        MinerUtil minerUtil = new MinerUtil(minerList);
        minerList = minerUtil.getExpandedMiners();
        Itemizer.getLogger().info(minerList.size() + " miners loaded from configuration.");
        return minerList.size();
    }

    /**
     * Read Craft configuration and interpret it
     * @param configurationNode ConfigurationNode to read from
     */
    public int readCraftConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
        craftList = new ArrayList<>();
        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(ICraftRecipes.class), new CraftingSerializer());
        craftList = configurationNode.getNode("crafts").getList(TypeToken.of(ICraftRecipes.class));
        Itemizer.getLogger().info( craftList.size() + " craft(s) loaded from configuration.");
        return craftList.size();
    }

    /**
     * Read pools configuration and interpret it. Must be the last config file read.
     * @param path
     */
    public int readPoolsConfiguration(String path) throws ObjectMappingException, IOException {
        poolList = new ArrayList<>();

        HoconConfigurationLoader hcl = HoconConfigurationLoader.builder().setPath(Paths.get(path)).build();
        ObjectMapper<PoolBeanRoot> c = NotSoStupidObjectMapper.forClass(PoolBeanRoot.class);
        PoolBeanRoot res = c.bind(new PoolBeanRoot()).populate(hcl.load());
        poolList = res.getPoolList();

        Itemizer.getLogger().info(poolList.size() + " pools loaded from configuration.");
        return poolList.size();
    }


    public GlobalConfig readGlobalConfiguration(String path) {
        try {
            ObjectMapper<GlobalConfig> globalConfigObjectMapper = NotSoStupidObjectMapper.forClass(GlobalConfig.class);
            HoconConfigurationLoader build = HoconConfigurationLoader.builder().setPath(Paths.get(path)).build();
            return globalConfigObjectMapper.bind(new GlobalConfig()).populate(build.load());
        } catch (Exception e) {
            Itemizer.getLogger().error("Could not read " + path, e.toString());
            throw new RuntimeException(e);
        }
    }

    public void saveGlobalConfiguration(String filename){
        ConfigurationLoader<CommentedConfigurationNode> config = HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
        final TypeToken<GlobalConfig> token = new TypeToken<GlobalConfig>() {};
        try {

                  CommentedConfigurationNode node = config.load();
                  node.setValue(token,Itemizer.getItemizer().getGlobalConfig());
                  config.save(node);
        } catch (Exception e) {
            Itemizer.getLogger().error(e.toString());
        }
    }


    public void saveItemConfig(String filename){
        ConfigurationLoader<CommentedConfigurationNode> configLoader = HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
        final TypeToken<List<ItemBean>> token = new TypeToken<List<ItemBean>>() {};
        try {
            CommentedConfigurationNode root =  configLoader.load();
            Itemizer.getLogger().info(itemList.size()+ " items");
                    root.getNode("items").setValue(token, itemList);
            configLoader.save(root);
        } catch (IOException | ObjectMappingException e) {
            Itemizer.getLogger().error(e.toString());
        }
    }


}
