package com.onaple.itemizer;

import com.onaple.itemizer.commands.FetchCommand;
import com.onaple.itemizer.commands.GetItemInfos;
import com.onaple.itemizer.commands.HasItemCommand;
import com.onaple.itemizer.commands.RegisterCommand;
import com.onaple.itemizer.commands.ReloadCommand;
import com.onaple.itemizer.commands.RetrieveCommand;
import com.onaple.itemizer.commands.elements.IdElement;
import com.onaple.itemizer.commands.elements.PoolIdElement;
import com.onaple.itemizer.commands.global.ConfigureColorCommand;
import com.onaple.itemizer.commands.global.ConfigureEnchantCommand;
import com.onaple.itemizer.commands.global.ConfigureModifierCommand;
import com.onaple.itemizer.commands.global.ConfigureRewriteCommand;
import com.onaple.itemizer.commands.manager.LoreManagerCommand;
import com.onaple.itemizer.crafing.CraftCommand;
import com.onaple.itemizer.crafing.CraftEventListener;
import com.onaple.itemizer.crafing.event.CraftSuccessfulEvent;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.access.PoolDAO;
import com.onaple.itemizer.data.beans.crafts.ICraftRecipes;
import com.onaple.itemizer.data.beans.recipes.RowCraft;
import com.onaple.itemizer.data.beans.recipes.Smelting;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import com.onaple.itemizer.data.manipulators.BaseNameDataManipulator;
import com.onaple.itemizer.data.manipulators.IdDataManipulator;
import com.onaple.itemizer.service.IItemService;
import com.onaple.itemizer.service.ItemService;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.slf4j.Logger;
import org.spongepowered.api.CatalogTypes;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.data.DataManager;
import org.spongepowered.api.data.DataRegistration;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.Order;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;
import org.spongepowered.api.event.cause.EventContextKeys;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.event.game.state.GameConstructionEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.smelting.SmeltingRecipe;
import org.spongepowered.api.plugin.Plugin;
import org.spongepowered.api.plugin.PluginContainer;
import org.spongepowered.api.text.Text;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Optional;

@Plugin(id = "itemizer", name = "Itemizer", version = "3.5.0",
        description = "Custom item generation with crafting and pool system",
        url = "http://onaple.fr",
        authors = {"Zessirb", "Selki"})
public class Itemizer {

    private static final String RETRIEVE_PERMISSION = "itemizer.command.retrieve";
    private static final String FETCH_PERMISSION = "itemizer.command.fetch";
    private static final String ANALYSE_PERMISSION = "itemizer.command.analyse";
    private static final String RELOAD_PERMISSION = "itemizer.command.reload";
    private static final String REGISTER_PERMISSION = "itemizer.command.register";
    private static final String REWRITE_PERMISSION = "itemizer.command.rewrite";
    private static final String HAS_ITEM_PERMISSION = "itemizer.command.hasitem";


    private static Itemizer itemizer;

    private static Logger logger;

    private static ConfigurationHandler configurationHandler;

    private static ItemService itemService;

    private static ItemDAO itemDAO;

    public static ItemDAO getItemDAO() {
        return itemDAO;
    }

    @Inject
    public  void setItemDAO(ItemDAO itemDAO) {
        this.itemDAO = itemDAO;
    }

    @Inject
    private void setItemService(ItemService itemService){
        this.itemService = itemService;
    }

    public static ItemService getItemService(){
        return itemService;
    }

    private static GlobalConfig globalConfig;


    public static GlobalConfig getGlobalConfig(){
        return globalConfig;
    }

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

    @Inject
    private PoolDAO poolDAO;

    @Inject
    private CraftCommand craftCommand;

    @Inject RegisterCommand registerCommand;

    private DataRegistration<IdDataManipulator, IdDataManipulator.Immutable> idDataRegistration;
    private DataRegistration<BaseNameDataManipulator, BaseNameDataManipulator.Immutable> basenameDataRegistration;


    public static PluginContainer getInstance() {
        return Sponge.getPluginManager().getPlugin("itemizer").orElse(null);
    }

    @Listener
    public void gameConstruct(GameConstructionEvent event) {
        itemizer = this;
        Sponge.getServiceManager().setProvider(getInstance(), IItemService.class, itemService);
        ItemizerKeys.dummy();
        this.idDataRegistration = DataRegistration.builder()
                .name("Itemizer id")
                .id("item.id") // prefix is added for you and you can't add it yourself
                .dataClass(IdDataManipulator.class)
                .immutableClass(IdDataManipulator.Immutable.class)
                .builder(new IdDataManipulator.Builder())
                .buildAndRegister(getInstance());
        this.basenameDataRegistration = DataRegistration.builder()
                .name("item base name")
                .id("item.basename")
                .dataClass(BaseNameDataManipulator.class)
                .immutableClass(BaseNameDataManipulator.Immutable.class)
                .builder(new BaseNameDataManipulator.Builder())
                .buildAndRegister(getInstance());
        loadConfiguration();
    }

    @Listener
    public void onCraftRegistration(GameRegistryEvent.Register<CraftingRecipe> event) throws InterruptedException {
        getLogger().info("register crafting");
        int i = 0;
        for (ICraftRecipes recipeRegister : configurationHandler.getCraftList()) {
            if (!(recipeRegister instanceof Smelting)) {
                recipeRegister.register(event);
                i++;
            }
        }
        getLogger().info("registered {} crafting recipes",i);
    }
    @Listener
    public void onSmeltingRegistration(GameRegistryEvent.Register<SmeltingRecipe> event) {
        getLogger().info("register smelting");
        int i = 0;
        for (ICraftRecipes recipeRegister : configurationHandler.getCraftList()) {
            if (recipeRegister instanceof Smelting) {
                recipeRegister.register(event);
                i++;
            }
        }
        getLogger().info("registered {} smelting recipes",i);
    }

    public void loadConfiguration(){
        configurationHandler.createItemizerDirectory();
        loadGlobalConfig();
        try {
            int size = configurationHandler.readAffixConfiguration();
                getLogger().info("{} affix loaded from configuration.", size);
        } catch (ObjectMappingException | IOException e) {
            Itemizer.getLogger().warn("Error while reading configuration 'affix'.", e);
        }
        try {
            int size = configurationHandler.readItemsConfiguration();
             getLogger().info("{} items loaded from configuration.", size);
        } catch (ObjectMappingException | IOException e) {
            Itemizer.getLogger().warn("Error while reading configuration 'items'.", e);
        }
        try {
            int size = configurationHandler.readPoolsConfiguration();
                getLogger().info("{} pools loaded from configuration.", size);
        } catch (ObjectMappingException | IOException e) {
            Itemizer.getLogger().warn("Error while reading configuration 'pools'.", e);
        }
        try {
            int size = configurationHandler.readCraftConfiguration();
            getLogger().info("{} crafting recipes loaded from configuration.", size);
            initRowCraft();
        } catch (ObjectMappingException | IOException e) {
            Itemizer.getLogger().warn("Error while reading configuration 'crafts'.", e);
        }
    }


    @Listener
    public void preInit(GamePreInitializationEvent event) {
        logger.warn("This version use a new config file format for items.");
        Sponge.getEventManager().registerListeners(this, new CraftEventListener());
    }

    private void initRowCraft(){
        for(ICraftRecipes recipes : getConfigurationHandler().getCraftList()){
            if(recipes instanceof RowCraft){
                recipes.register(null);
            }
        }
    }

    @Listener
    public void onServerStart(GameStartedServerEvent event) {

        CommandSpec retrieve = CommandSpec.builder()
                .description(Text.of("Retrieve an item from a configuration file with its id."))
                .arguments(new IdElement(Text.of("id")),
                        GenericArguments.optional(GenericArguments.integer(Text.of("quantity"))),
                        GenericArguments.optional(GenericArguments.player(Text.of("player")))
                )
                .permission(RETRIEVE_PERMISSION)
                .executor(new RetrieveCommand()).build();
        Sponge.getCommandManager().register(this, retrieve, "retrieve");
        CommandSpec fetch = CommandSpec.builder()
                .description(Text.of("Try to retrieve an item from a pool describes in a configuration file with its id."))
                .arguments(new PoolIdElement(Text.of("pool"),poolDAO),
                        GenericArguments.optional(GenericArguments.player(Text.of("player"))))
                .permission(FETCH_PERMISSION)
                .executor(new FetchCommand()).build();
        Sponge.getCommandManager().register(this, fetch, "fetch");

        CommandSpec reload = CommandSpec.builder()
                .description(Text.of("Reaload Itemizer configuration from files."))
                .permission(RELOAD_PERMISSION)
                .executor(new ReloadCommand()).build();
        Sponge.getCommandManager().register(this, reload, "reload-itemizer");

        CommandSpec craftSpec = CommandSpec.builder()
                .executor(craftCommand)
                .build();
        Sponge.getCommandManager().register(this,craftSpec,"craft");

        CommandSpec getInfo = CommandSpec.builder()
                .description(Text.of("get information about item in main hand"))
                .permission(ANALYSE_PERMISSION)
                .executor(new GetItemInfos()).build();
        Sponge.getCommandManager().register(this, getInfo, "analyse");

        CommandSpec changeLore = CommandSpec.builder()
                .description(Text.of("set or update Item lore"))
                .permission(ANALYSE_PERMISSION)
                .executor(new LoreManagerCommand()).build();
        Sponge.getCommandManager().register(this, changeLore, "set-lore");

        CommandSpec register = CommandSpec.builder()
                .description(Text.of("Register an new itemizer item from main hand."))
                .arguments(GenericArguments.onlyOne(GenericArguments.string(Text.of("id"))))
                .permission(REGISTER_PERMISSION)
                .executor(registerCommand).build();
        Sponge.getCommandManager().register(this, register, "register");


        CommandSpec rewrite = CommandSpec.builder()
                .description(Text.of("Update global configuration."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("Key")/*,
                                globalConfig.getRewriteChoice()::keySet, globalConfig.getRewriteChoice()::get)*/)),
                        GenericArguments.optional(GenericArguments.string(Text.of("Name")))
                )
                .permission(REWRITE_PERMISSION)
                .executor(new ConfigureRewriteCommand()).build();

        CommandSpec enchantRewrite = CommandSpec.builder()
                .description(Text.of("Update global enchant configuration."))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.catalogedElement(Text.of("Enchant"), CatalogTypes.ENCHANTMENT_TYPE)),
                        GenericArguments.optional(GenericArguments.string(Text.of("Name"))
                        )
                )
                .permission(REWRITE_PERMISSION)
                .executor(new ConfigureEnchantCommand()).build();

        CommandSpec colorRewrite = CommandSpec.builder()
                .description(Text.of("Update global color configuration."))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.enumValue(Text.of("Key"), GlobalConfig.RewriteFlag.class)),
                        GenericArguments.optional(
                                GenericArguments.catalogedElement(Text.of("Color"), CatalogTypes.TEXT_COLOR))
                ).permission(REWRITE_PERMISSION)
                .executor(new ConfigureColorCommand()).build();


        CommandSpec modifierRewrite = CommandSpec.builder()
                .description(Text.of("Update global color configuration."))
                .arguments(
                        GenericArguments.onlyOne(
                                GenericArguments.string(Text.of("Modifier"))),
                        GenericArguments.optional(
                                GenericArguments.string(Text.of("Name")))
                )
                .permission(REWRITE_PERMISSION)
                .executor(new ConfigureModifierCommand()).build();

        CommandSpec configurationUpdate = CommandSpec.builder()
                .child(rewrite, "description")
                .child(enchantRewrite, "enchantment")
                .child(modifierRewrite, "modifier")
                .child(colorRewrite, "color")
                .permission(REWRITE_PERMISSION)
                .build();
        Sponge.getCommandManager().register(this, configurationUpdate, "configure");

        CommandSpec hasItemCommand = CommandSpec.builder()
                .description(Text.of("Display if a player have an Item."))
                .arguments(
                        GenericArguments.onlyOne(GenericArguments.player(Text.of("player"))),
                        GenericArguments.onlyOne(GenericArguments.string(Text.of("reference"))),
                        GenericArguments.optional(GenericArguments.integer(Text.of("quantity")))
                )
                .permission(HAS_ITEM_PERMISSION)
                .executor(new HasItemCommand()).build();

        Sponge.getCommandManager().register(this, hasItemCommand, "hasitem","has-item");

        logger.info("ITEMIZER initialized.");
    }



    public void saveGlobalConfig() {
        getConfigurationHandler().saveGlobalConfiguration();
    }

    private void loadGlobalConfig() {
        try {
            globalConfig = configurationHandler.readGlobalConfig();

        } catch (Exception e) {
            logger.error(e.toString());
        }

    }

}
