package com.onaple.itemizer;

import com.onaple.itemizer.commands.*;

import com.onaple.itemizer.commands.globalConfiguration.ConfigureColorCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureEnchantCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureModifierCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureRewriteCommand;
import com.onaple.itemizer.data.OnaKeys;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import com.onaple.itemizer.data.manipulator.AttributeModifiersData;
import com.onaple.itemizer.data.manipulator.HideFlagData;
import com.onaple.itemizer.events.ItemizerPreLoadEvent;
import com.onaple.itemizer.service.ItemService;
import com.onaple.itemizer.service.IItemService;
import com.onaple.itemizer.utils.CraftRegister;

import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogType;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Plugin(id = "itemizer", name = "Itemizer improuved items", version = "1.5.2",
        description = "Plugin to manage custom items and crafts",
        url = "http://onaple.fr",
        authors = {"Zessirb", "Selki"})
public class Itemizer {

    private static Itemizer itemizer;

    public static Itemizer getItemizer() {
        return itemizer;
    }


    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;

    private static GlobalConfig globalConfig;

    @Inject
    private void setGlobalConfig(GlobalConfig globalConfig){
        this.globalConfig = globalConfig;
    }

    public static GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    private static Logger logger;

    @Inject
    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    public static Logger getLogger() {
        return logger;
    }

    @Inject
    private CraftRegister craftRegister;


    private  static  ItemDAO itemDAO;
    @Inject
    private void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    public static ItemDAO getItemDAO(){
        return itemDAO;
    }

    @Inject
    PluginContainer container;

    private static ConfigurationHandler configurationHandler;

    @Inject
    public void setConfigurationHandler(ConfigurationHandler configurationHandler) {
        this.configurationHandler = configurationHandler;
    }

    public static ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    @Listener
    public void preInit(GamePreInitializationEvent event) {
        logger.info("Initalisation");
        new OnaKeys();
        DataRegistration.builder()
                .dataName("Hidden flags")
                .manipulatorId("hidden_flags") // prefix is added for you and you can't add it yourself
                .dataClass(HideFlagData.class)
                .immutableClass(HideFlagData.Immutable.class)
                .builder(new HideFlagData.Builder())
                .buildAndRegister(container);

        DataRegistration.builder()
                .dataName("Attribute modifiers")
                .manipulatorId("attribute_modifier")
                .dataClass(AttributeModifiersData.class)
                .immutableClass(AttributeModifiersData.Immutable.class)
                .builder(new AttributeModifiersData.AttributeDataBuilder())
                .buildAndRegister(container);

       // Sponge.getDataManager().registerTranslator(AttributeBean.class,new AttributeTranslator());

        loadGlobalConfig();
        Sponge.getEventManager().post(new ItemizerPreLoadEvent());
        try {
            loadItems();
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error("Error while reading configuration 'items' : " + e.getMessage());
        } catch (Exception e) {
            Itemizer.getLogger().error(e.getMessage());
        }
        try {
            loadMiners();
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error("Error while reading configuration 'miners' : " + e.getMessage());
        } catch (Exception e) {
            Itemizer.getLogger().error(e.getMessage());
        }

        try {
            loadPools();
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error("Error while reading configuration 'pools' : " + e.getMessage());
        } catch (Exception e) {
            Itemizer.getLogger().error(e.getMessage());
        }
        try {
            loadCrafts();
        } catch (ObjectMappingException e) {
            Itemizer.getLogger().error("Error while reading configuration 'crafts' : " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }
        craftRegister.register(configurationHandler.getCraftList());

        itemizer = this;
        Sponge.getServiceManager().setProvider(getInstance(), IItemService.class, new ItemService());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws Exception {



        CommandSpec retrieve = CommandSpec.builder()
                .description(Text.of("Retrieve an item from a configuration file with its id."))
                .arguments(GenericArguments.onlyOne(GenericArguments.choices(Text.of("id"),itemDAO.getMap())),
                        GenericArguments.optional(GenericArguments.integer(Text.of("quantity"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .permission("itemizer.command.retrieve")
                .executor(new RetrieveCommand()).build();
        Sponge.getCommandManager().register(this, retrieve, "retrieve");

        CommandSpec fetch = CommandSpec.builder()
                .permission("itemizer.get")
                .description(Text.of("Try to retrieve an item from a pool describes in a configuration file with its id."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("quantity"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .permission("itemizer.command.fetch")
                .executor(new FetchCommand()).build();
        Sponge.getCommandManager().register(this, fetch, "fetch");

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reaload Itemizer configuration from files."))
                .permission("itemizer.command.reload")
                .executor(new ReloadCommand()).build();
        Sponge.getCommandManager().register(this, reload, "reload-itemizer");

        CommandSpec getInfo = CommandSpec.builder()
                .description(Text.of("get information about item in main hand"))
                .permission("itemizer.command.analyse")
                .executor(new GetItemInfos()).build();
        Sponge.getCommandManager().register(this, getInfo, "analyse");

        CommandSpec register = CommandSpec.builder()
                .description(Text.of("Register an new itemizer item from main hand."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
                .permission("itemizer.command.register")
                .executor(new RegisterCommand()).build();
        Sponge.getCommandManager().register(this, register, "register");


        CommandSpec rewrite = CommandSpec.builder()
                .description(Text.of("Update global configuration."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("Key")/*,
                                globalConfig.getRewriteChoice()::keySet, globalConfig.getRewriteChoice()::get)*/)),
                        GenericArguments.optional(GenericArguments.string(Text.of("Name")))
                )
                .permission("itemizer.command.rewrite")
                .executor(new ConfigureRewriteCommand()).build();

        CommandSpec enchantRewrite = CommandSpec.builder()
                .description(Text.of("Update global enchant configuration."))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.catalogedElement(Text.of("Enchant"), CatalogTypes.ENCHANTMENT_TYPE)),
                        GenericArguments.optional(GenericArguments.string(Text.of("Name"))
                        )
                )
                .permission("itemizer.command.rewrite")
                .executor(new ConfigureEnchantCommand()).build();

        CommandSpec colorRewrite = CommandSpec.builder()
                .description(Text.of("Update global color configuration."))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.enumValue(Text.of("Key"),GlobalConfig.RewriteFlagColorList.class)),
                        GenericArguments.optional(
                                GenericArguments.catalogedElement(Text.of("Color"), CatalogTypes.TEXT_COLOR))
                ).permission("itemizer.command.rewrite")
                .executor(new ConfigureColorCommand()).build();


        CommandSpec modifierRewrite = CommandSpec.builder()
                .description(Text.of("Update global color configuration."))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.string(Text.of("Modifier"))),
                        GenericArguments.optional(
                                GenericArguments.string(Text.of("Name")))
                )
                .permission("itemizer.command.rewrite")
                .executor(new ConfigureModifierCommand()).build();

        CommandSpec configurationUpdate = CommandSpec.builder()
                .child(rewrite,"description" )
                .child(enchantRewrite,"enchantment")
                .child(modifierRewrite, "modifier")
                .child(colorRewrite,"color")
                .permission("itemizer.command.rewrite")
                .build();
        Sponge.getCommandManager().register(this, configurationUpdate, "configure");

        logger.info("ITEMIZER initialized.");
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        getConfigurationHandler().saveItemConfig(configDir + "/itemizer/items.conf");
    }

    public void saveGlobalConfig() {
        getConfigurationHandler().saveGlobalConfiguration(configDir + "/itemizer/global.conf");
    }

    public static PluginContainer getInstance() {
        return Sponge.getPluginManager().getPlugin("itemizer").orElse(null);
    }

    private void loadGlobalConfig() {
        initDefaultConfig("global.conf");
        try {
            setGlobalConfig(configurationHandler.readGlobalConfiguration(
                    configurationHandler.loadConfiguration(configDir + "/itemizer/global.conf")));
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }


    public int loadItems() throws Exception {
        initDefaultConfig("items.conf");
        return configurationHandler.readItemsConfiguration(
                configurationHandler.loadConfiguration(configDir + "/itemizer/items.conf"));
    }

    public int loadMiners() throws Exception {
        initDefaultConfig("miners.conf");
        return configurationHandler.readMinerConfiguration(configurationHandler.loadConfiguration(configDir + "/itemizer/miners.conf"));
    }

    public int loadPools() throws Exception {
        initDefaultConfig("pools.conf");
        return configurationHandler.readPoolsConfiguration(configurationHandler.loadConfiguration(configDir + "/itemizer/pools.conf"));
    }

    public int loadCrafts() throws Exception {
        initDefaultConfig("crafts.conf");
        return configurationHandler.readCraftConfiguration(configurationHandler.loadConfiguration(configDir + "/itemizer/crafts.conf"));
    }

    private void initDefaultConfig(String path) {
        if (Files.notExists(Paths.get(configDir + "/itemizer/" + path))) {
            PluginContainer pluginInstance = getInstance();
            if (pluginInstance != null) {
                Optional<Asset> itemsDefaultConfigFile = pluginInstance.getAsset(path);
                getLogger().info("No config file set for " + path + " default config will be loaded");
                if (itemsDefaultConfigFile.isPresent()) {
                    try {
                        itemsDefaultConfigFile.get().copyToDirectory(Paths.get(configDir + "/itemizer/"));
                    } catch (IOException e) {
                        Itemizer.getLogger().error("Error while setting default configuration : " + e.getMessage());
                    }
                } else {
                    logger.warn("Item default config not found");
                }
            }
        }
    }
}
