package com.onaple.itemizer.data.handlers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.ConfigUtils;
import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.Items;
import com.onaple.itemizer.data.beans.MinerBean;
import com.onaple.itemizer.data.beans.Mining;
import com.onaple.itemizer.data.beans.PoolBean;
import com.onaple.itemizer.data.serializers.CraftingSerializer;
import com.onaple.itemizer.data.serializers.GlobalConfigurationSerializer;
import com.onaple.itemizer.data.serializers.PoolSerializer;
import com.onaple.itemizer.utils.MinerUtil;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

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
     * @param path File path
     */
    public int readItemsConfiguration(Path path) {
        Items itemRoot = ConfigUtils.load(Items.class, path);
        itemList = itemRoot.getItems();
        Itemizer.getLogger().info(itemList.size() + " items loaded from configuration.");
        return itemList.size();
    }

    /**
     * Read miners configuration and interpret it
     * @param path path to file
     */
    public int readMinerConfiguration(Path path) {
        Mining load = ConfigUtils.load(Mining.class, path);
        minerList = new MinerUtil(load.getMiners()).getExpandedMiners();
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
     * @param configurationNode ConfigurationNode to read from
     */
    public int readPoolsConfiguration(CommentedConfigurationNode configurationNode) throws ObjectMappingException {
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

    private ConfigurationLoader<CommentedConfigurationNode> getConfigurationLoader(String filename){
       return HoconConfigurationLoader.builder().setPath(Paths.get(filename)).build();
    }

    public GlobalConfig readGlobalConfiguration(CommentedConfigurationNode configurationNode) {
    /*    boolean DescriptionRewrite = configurationNode.getNode("DescriptionRewrite").getBoolean();
        Map<String,Boolean> hiddenFlags = new HashMap<>();
        configurationNode.getNode("RewriteParts").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof Boolean){
                hiddenFlags.put((String) o,(Boolean) o2.getValue());
            }
        });
        Map<EnchantmentType,String> enchantMap = new HashMap<>();
        configurationNode.getNode("EnchantRewrite").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof String){
                Optional<EnchantmentType> enchant =  Sponge.getRegistry().getType(EnchantmentType.class,(String) o);
                enchant.ifPresent(enchantmentType ->   enchantMap.put(enchantmentType,(String) o2.getValue()));
            }
        });
        Map<String,String> modifierMap = new HashMap<>();
        configurationNode.getNode("ModifierRewrite").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof String){
                modifierMap.put((String) o,(String) o2.getValue());
            }
        });

        String unbreakable = configurationNode.getNode("UnbreakableRewrite").getString();

        String canMineRewrite = configurationNode.getNode("CanMineRewrite").getString();

        Map<String,TextColor> colors = new HashMap<>();

        configurationNode.getNode("DefaultColor").getChildrenMap().forEach((o, o2) -> {
            if(o instanceof String  && o2.getValue() instanceof String){
                Optional<TextColor> colorOptional = Sponge.getRegistry().getType(TextColor.class, o2.getString());

                colorOptional.ifPresent(textColor -> {
                    Itemizer.getLogger().info(textColor.toString());
                    colors.put((String)o,textColor);
                });

            }
        });*/

        TypeSerializers.getDefaultSerializers().registerType(TypeToken.of(GlobalConfig.class), new GlobalConfigurationSerializer());
        try {
            return configurationNode.getValue(TypeToken.of(GlobalConfig.class));
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error(e.toString());
        }
        return null;
    }

    public void saveGlobalConfiguration(String filename){
        ConfigurationLoader<CommentedConfigurationNode> config = getConfigurationLoader(filename);
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
        ConfigurationLoader<CommentedConfigurationNode> configLoader = getConfigurationLoader(filename);
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
