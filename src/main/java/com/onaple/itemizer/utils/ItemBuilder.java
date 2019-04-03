package com.onaple.itemizer.utils;

import com.onaple.itemizer.GlobalConfig;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ItemEnchant;
import com.onaple.itemizer.data.beans.ItemLoreWriter;
import com.onaple.itemizer.data.beans.ItemNbtFactory;
import com.onaple.itemizer.data.beans.MinerBean;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockState;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.block.trait.BlockTrait;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.BreakableData;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.TextStyles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class ItemBuilder {

    private GlobalConfig config;
    private ItemStack item;
    private List<Text> lore;
    private Set<Key> usedKeys;

    public ItemBuilder() {
        lore = new ArrayList<>();
        usedKeys =  new LinkedHashSet<>();
        config = Itemizer.getItemizer().getGlobalConfig();
    }

    /**
     * Add block traits to a future block
     *
     * @param blockType Type of the block
     * @param traits    Map containing all the traits
     * @return BlockState of the future block
     */
    private static BlockState addTraits(BlockType blockType, Map<String, String> traits) {
        BlockState blockState = blockType.getDefaultState();
        for (Map.Entry<String, String> trait : traits.entrySet()) {
            Optional<BlockTrait<?>> optTrait = blockState.getTrait(trait.getKey());
            if (optTrait.isPresent()) {
                Optional<BlockState> newBlockState = blockState.withTrait(optTrait.get(), trait.getValue());
                if (newBlockState.isPresent()) {
                    blockState = newBlockState.get();
                }
            }
        }
        return blockState;
    }

    /**
     * Build an itemstack from an ItemBean
     *
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public Optional<ItemStack> buildItemStack(ItemBean itemBean) {

        Optional<BlockType> potentialBlock = itemBean.getType().getBlock();
        if (potentialBlock.isPresent()) {
            BlockState blockState = addTraits(potentialBlock.get(), itemBean.getBlockTrait());
            this.item = ItemStack.builder().fromBlockState(blockState).build();
        } else {
            this.item = ItemStack.builder().itemType(itemBean.getType()).build();
        }
        defineItemStack(itemBean, config.getHiddenFlags().get("Unbreakable"));
        enchantItemStack(itemBean, config.getHiddenFlags().get("Enchantments"));
        grantMining(itemBean, config.getHiddenFlags().get("CanDestroy"));
        setAttribute(itemBean, config.getHiddenFlags().get("Attributes_modifiers"));
        addLore();
        setCustomDatamanipulators(itemBean);
        setNbt(itemBean);
        applyCustomLoreAppender();
        return Optional.ofNullable(this.item);
    }

    private void applyCustomLoreAppender() {

        Set<ItemLoreWriter> appenders = Itemizer.getItemizer().getItemService().getItemLoreAppenders(usedKeys);

        for (ItemLoreWriter appender : appenders) {
            appender.apply(item, lore);
        }

    }


    private void setCustomDatamanipulators(ItemBean itemBean) {
        List<ItemNbtFactory> thirdpartyConfigs = itemBean.getNbt();
        for (ItemNbtFactory cfg : thirdpartyConfigs) {
            cfg.apply(item);
            usedKeys.add(cfg.getKey());
        }
    }

    /**
     * Build an itemstack from this name
     *
     * @param name Data of the item to build
     * @return Optional of the itemstack
     */
    public Optional<ItemStack> buildItemStack(String name) {
        Optional<ItemType> optionalType = Sponge.getRegistry().getType(ItemType.class, name);
        if (optionalType.isPresent()) {
            ItemStack itemStack = ItemStack.builder().itemType(optionalType.get()).build();
            return Optional.of(itemStack);
        } else {
            Itemizer.getLogger().warn("Unknown item type : {} " , name);
        }
        return Optional.empty();
    }

    /**
     * Define the characteristics of an ItemStack from an ItemBean
     *
     * @param itemBean Data of the item to define
     * @return ItemStack edited
     */
    private void defineItemStack(ItemBean itemBean, boolean rewrite) {
        //item Id
        if (itemBean.getId() != null && !itemBean.getId().isEmpty()) {
            setCustomData("id", itemBean.getId());
        }

        // Item name
        if (itemBean.getName() != null && !itemBean.getName().isEmpty()) {
            item.offer(Keys.DISPLAY_NAME, Text.builder(itemBean.getName()).style(TextStyles.BOLD).build());
        }
        // Item lore
        if (itemBean.getLore() != null) {

            for (String loreLine : itemBean.getLore().split("\n")) {
                lore.add(Text.builder(loreLine).color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.lore)).build());
            }

        }

        // Item attributes
        item.offer(Keys.UNBREAKABLE, itemBean.isUnbreakable());
        item.offer(Keys.HIDE_UNBREAKABLE,rewrite);
        if (itemBean.isUnbreakable()) {
            if (rewrite && config.getUnbreakableRewrite() != null) {

                lore.add(Text.builder(config.getUnbreakableRewrite()).color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.unbreakable))
                        .style(TextStyles.ITALIC).build());
            }
        }
        if (itemBean.getDurability() > 0) {
            item.offer(Keys.ITEM_DURABILITY, itemBean.getDurability());
        }


        if (itemBean.getToolLevel() != 0) {
            DataContainer container = this.item.toContainer();
            container.set(DataQuery.of("UnsafeData", "ToolLevel"), itemBean.getToolLevel());
            this.item = ItemStack.builder().fromContainer(container).build();
        }

    }

    /**
     * Enchant an ItemStack with an ItemBean data
     *
     * @param itemBean Data of the item to enchant
     * @return Enchanted (or not) ItemStack
     */
    private void enchantItemStack(ItemBean itemBean, boolean rewrite) {
        Map<String, ItemEnchant> enchants = itemBean.getEnchants();
        if (!enchants.isEmpty()) {
            EnchantmentData enchantmentData = item.getOrCreate(EnchantmentData.class).get();
            for (Map.Entry<String, ItemEnchant> enchant : enchants.entrySet()) {
                Optional<EnchantmentType> optionalEnchant = Sponge.getRegistry().getType(EnchantmentType.class, enchant.getKey());
                if (optionalEnchant.isPresent()) {
                    enchantmentData.set(enchantmentData.enchantments().add(Enchantment.builder().
                            type(optionalEnchant.get()).
                            level(enchant.getValue().getLevel()).build()));
                    if (rewrite) {
                        if (config.getEnchantRewrite().size() > 0) {
                            lore.add(Text
                                    .builder(config.getEnchantRewrite().get(optionalEnchant.get()) + " " + enchant.getValue())
                                    .style(TextStyles.ITALIC)
                                    .color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.enchantments))
                                    .build());
                        }
                    }
                } else {
                    Itemizer.getLogger().warn("Unknown enchant : {}", enchant.getKey());
                }
            }

            item.offer(enchantmentData);
            item.offer(Keys.HIDE_ENCHANTMENTS,rewrite);

        }
    }

    /**
     * Grant mining capabilities
     *
     * @param itemBean Data of the item
     * @return Item with mining powers
     */
    private void grantMining(ItemBean itemBean, boolean rewrite) {
        BreakableData breakableData = item.getOrCreate(BreakableData.class).get();
        if (!itemBean.getMiners().isEmpty()) {
            Text.Builder miningText = Text.builder(config.getCanMineRewrite().isEmpty() ? "" : config.getCanMineRewrite())
                    .color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.canDestroyMention))
                    .style(TextStyles.UNDERLINE);
            for (MinerBean minerBean : itemBean.getMiners()) {
                minerBean.getMineTypes().forEach((blockName, blockType) -> {
                    miningText.append(Text.builder(" " + blockName + " ")
                            .color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.canDestroyMention)).style(TextStyles.RESET)
                            .build());
                    breakableData.set(breakableData.breakable().add(blockType));
                });


            }
            if (rewrite) {
                if (!config.getCanMineRewrite().isEmpty() && config.getCanMineRewrite() != null) {
                    lore.add(miningText.build());
                }

            }
        }
        item.offer(breakableData);
        item.offer(Keys.HIDE_CAN_DESTROY,rewrite);
    }

    /**
     * Set attributes to an item
     *
     * @param itemBean Data of the item
     * @return Item with attributes set
     */
    private void setAttribute(ItemBean itemBean, Boolean rewrite) {
        List<DataContainer> containers = new ArrayList<>();
        if(itemBean.getAttributeList().isEmpty()){
            return;
        }
        for (AttributeBean att : itemBean.getAttributeList()) {
            DataContainer dc = createAttributeModifier(att);
            containers.add(dc);
            Text.Builder attributText;
            if (att.getOperation() == 0) {
                attributText = Text.builder(String.format("%.1f", att.getAmount()) + " ");

            } else if (att.getOperation() == 1) {

                attributText = Text.builder(String.format("%.1f", att.getAmount() * 100) + "% ");
            } else {
                attributText = Text.builder(String.format("%.1f", att.getAmount() * 100) + "% ");
            }
            String name = config.getModifierRewrite().get(att.getName());
            if (name == null) {
                name = att.getName();
            }
            attributText.append(Text.builder(name).build());
            if (att.getAmount() > 0) {
                attributText.color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.attributesModifiersPositive));
            } else {
                attributText.color(config.getColorMap().get(GlobalConfig.RewriteFlagColorList.attributesModifiersNegavite));
            }
            if (rewrite) {
                lore.add(attributText.build());
            }
        }

        DataContainer container = this.item.toContainer();
        container.set(DataQuery.of("UnsafeData", "AttributeModifiers"), containers);
        this.item = ItemStack.builder()
                .fromContainer(container)
                .build();
        this.item.offer(Keys.HIDE_ATTRIBUTES,rewrite);
    }

    /**
     * Create the datacontainer for an attribute's data
     *
     * @param attribute Data of the attribute
     * @return DataContainer from which the item will be recreated
     */
    private DataContainer createAttributeModifier(AttributeBean attribute) {
        UUID uuid = UUID.randomUUID();
        DataContainer dataContainer = DataContainer.createNew();
        dataContainer.set(DataQuery.of("AttributeName"), attribute.getName());
        dataContainer.set(DataQuery.of("Name"), attribute.getName());
        dataContainer.set(DataQuery.of("Amount"), attribute.getAmount());
        dataContainer.set(DataQuery.of("Operation"), attribute.getOperation());
        dataContainer.set(DataQuery.of("Slot"), attribute.getSlot());
        dataContainer.set(DataQuery.of("UUIDMost"), uuid.getMostSignificantBits());
        dataContainer.set(DataQuery.of("UUIDLeast"), uuid.getLeastSignificantBits());
        return dataContainer;
    }

    private void addLore() {
        item.offer(Keys.ITEM_LORE, lore);
    }

    private void setNbt(ItemBean itemBean) {
        for (ItemNbtFactory itemNbtFactory : itemBean.getNbt()) {
            itemNbtFactory.apply(this.item);
        }
    }

    private void setCustomData(String queryPath, Object value) {
        List<String> queryList;
        if (queryPath.contains(".")) {
            String[] queries = queryPath.split(".");
            queryList = Arrays.stream(queries).collect(Collectors.toList());
        } else {
            queryList = new ArrayList<>();
            queryList.add(queryPath);
        }
        queryList.add(0, "UnsafeData");
        DataQuery dt = DataQuery.of(queryList);
        this.item = ItemStack.builder()
                .fromContainer(item.toContainer().set(dt, value))
                .build();
    }
}
