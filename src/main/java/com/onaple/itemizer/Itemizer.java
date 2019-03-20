package com.onaple.itemizer;

import com.onaple.itemizer.commands.FetchCommand;
import com.onaple.itemizer.commands.GetItemInfos;
import com.onaple.itemizer.commands.RegisterCommand;
import com.onaple.itemizer.commands.ReloadCommand;
import com.onaple.itemizer.commands.RetrieveCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureColorCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureEnchantCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureModifierCommand;
import com.onaple.itemizer.commands.globalConfiguration.ConfigureRewriteCommand;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import com.onaple.itemizer.service.IItemService;
import com.onaple.itemizer.service.ItemService;
import com.onaple.itemizer.utils.CraftRegister;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.asset.Asset;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import javax.inject.Inject;

@Plugin(id = "itemizer", name = "Itemizer improuved items", version = "1.5",
        description = "Plugin to manage custom items and crafts",
        url = "http://onaple.fr",
        authors = {"Zessirb", "Selki"})
public class Itemizer {

    private static Itemizer itemizer;
    private static Logger logger;
    private static ConfigurationHandler configurationHandler;
    @Inject
    @ConfigDir(sharedRoot = true)
    private Path configDir;
    private GlobalConfig globalConfig;
    @Inject
    private CraftRegister craftRegister;

    public static Itemizer getItemizer() {
        return itemizer;
    }

    public static Logger getLogger() {
        return logger;
    }

    @Inject
    private void setLogger(Logger logger) {
        this.logger = logger;
    }

    public static ConfigurationHandler getConfigurationHandler() {
        return configurationHandler;
    }

    @Inject
    public void setConfigurationHandler(ConfigurationHandler configurationHandler) {
        this.configurationHandler = configurationHandler;
    }

    public static PluginContainer getInstance() {
        return Sponge.getPluginManager().getPlugin("itemizer").orElse(null);
    }

    public GlobalConfig getGlobalConfig() {
        return globalConfig;
    }

    @Listener
    public void gameConstruct(GameConstructionEvent event) {
        itemizer = this;
        Sponge.getServiceManager().setProvider(getInstance(), IItemService.class, new ItemService());
    }

    @Listener
    public void preInit(GamePostInitializationEvent event) {
        logger.info("Initalisation");
        loadGlobalConfig();
        try {
            loadMiners();
        } catch (Exception e) {
            Itemizer.getLogger().error("Error while reading configuration 'miners' : " + e.getMessage());
        }
        try {
            loadItems();
        } catch (Exception e) {
            Itemizer.getLogger().error("Error while reading configuration 'items' : " + e.getMessage());
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
            Itemizer.getLogger().error(e.getMessage());
        }
        craftRegister.register(configurationHandler.getCraftList());
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) throws Exception {

        CommandSpec retrieve = CommandSpec.builder()
                .description(Text.of("Retrieve an item from a configuration file with its id."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .permission("itemizer.command.retrieve")
                .executor(new RetrieveCommand()).build();
        Sponge.getCommandManager().register(this, retrieve, "retrieve");

        CommandSpec fetch = CommandSpec.builder()
                .permission("itemizer.get")
                .description(Text.of("Try to retrieve an item from a pool describes in a configuration file with its id."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
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
                        GenericArguments.onlyOne(GenericArguments.choices(Text.of("Key"),
                                globalConfig.getRewriteChoice()::keySet, globalConfig.getRewriteChoice()::get)),
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
                                GenericArguments.enumValue(Text.of("Key"), GlobalConfig.RewriteFlagColorList.class)),
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
                .child(rewrite, "description")
                .child(enchantRewrite, "enchantment")
                .child(modifierRewrite, "modifier")
                .child(colorRewrite, "color")
                .permission("itemizer.command.rewrite")
                .build();
        Sponge.getCommandManager().register(this, configurationUpdate, "configure");

        logger.info("ITEMIZER initialized.");
    }

    @Listener
    public void onServerStop(GameStoppedServerEvent event) {
        Itemizer.getConfigurationHandler().saveItemConfig(configDir + "/itemizer/items.conf");
    }

    public void saveGlobalConfig() {
        getConfigurationHandler().saveGlobalConfiguration(configDir + "/itemizer/global.conf");
    }

    private void loadGlobalConfig() {
        initDefaultConfig("global.conf");
        try {
            this.globalConfig = configurationHandler.readGlobalConfiguration(
                    configurationHandler.loadConfiguration(configDir + "/itemizer/global.conf"));
        } catch (Exception e) {
            logger.error(e.toString());
        }

    }


    public int loadItems(){
        initDefaultConfig("items.conf");
        return configurationHandler.readItemsConfiguration(
                Paths.get(configDir + "/itemizer/", "items.conf"));
    }

    public int loadMiners() {
        initDefaultConfig("miners.conf");
        return configurationHandler.readMinerConfiguration(
                Paths.get(configDir + "/itemizer/", "miners.conf"));
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
