package com.onaple.itemizer.utils;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.beans.AttributeBean;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.MinerBean;
import com.onaple.itemizer.data.handlers.ConfigurationHandler;
import com.sun.jmx.remote.internal.ArrayQueue;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.block.BlockType;
import org.spongepowered.api.data.DataContainer;
import org.spongepowered.api.data.DataQuery;
import org.spongepowered.api.data.DataTransactionResult;
import org.spongepowered.api.data.Transaction;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.manipulator.mutable.item.BreakableData;
import org.spongepowered.api.data.manipulator.mutable.item.EnchantmentData;
import org.spongepowered.api.data.manipulator.mutable.item.LoreData;
import org.spongepowered.api.data.value.mutable.MutableBoundedValue;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.enchantment.Enchantment;
import org.spongepowered.api.item.enchantment.EnchantmentType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.format.*;
import javax.inject.Singleton;
import java.util.*;

@Singleton
public class ItemBuilder {

    private ItemStack item;
    private List<Text> lore;
    public ItemBuilder() {
        lore = new ArrayList<>();
    }


    /**
     * Build an itemstack from an ItemBean
     * @param itemBean Data of the item to build
     * @return Optional of the itemstack
     */
    public Optional<ItemStack> buildItemStack(ItemBean itemBean) {
        Optional<ItemType> optionalType = Sponge.getRegistry().getType(ItemType.class, itemBean.getType());
        if (optionalType.isPresent()) {
               this.item = ItemStack.builder().itemType(optionalType.get()).build();
               defineItemStack(itemBean);
               enchantItemStack(itemBean);
               grantMining(itemBean);
               setAttribute(itemBean);
            if(Itemizer.getItemizer().getGlobalConfig().isDescriptionRewrite()) {
                this.item = ItemStack.builder()
                        .fromContainer(item.toContainer().set(DataQuery.of("UnsafeData","HideFlags"),31))
                        .build();
                addLore();
            } else{
                if (itemBean.getLore() != null) {
                    List<Text> loreData = new ArrayList<>();
                    for (String loreLine : itemBean.getLore().split("\n")) {
                        loreData.add(Text.builder(loreLine).color(TextColors.GRAY).build());
                    }
                    item.offer(Keys.ITEM_LORE,loreData);
                }

            }
            return Optional.ofNullable(this.item);
        } else {
            Itemizer.getLogger().warn("Unknown item type : " + itemBean.getType());
        }
        return Optional.empty();
    }
    /**
     * Build an itemstack from this name
     * @param name Data of the item to build
     * @return Optional of the itemstack
     */
    public Optional<ItemStack> buildItemStack(String name) {
        Optional<ItemType> optionalType = Sponge.getRegistry().getType(ItemType.class,name);
        if (optionalType.isPresent()) {
            ItemStack itemStack = ItemStack.builder().itemType(optionalType.get()).build();
            return Optional.of(itemStack);
        } else {
            Itemizer.getLogger().warn("Unknown item type : " + name);
        }
        return Optional.empty();
    }

    /**
     * Define the characteristics of an ItemStack from an ItemBean
     * @param itemBean Data of the item to define
     * @return ItemStack edited
     */
    private void defineItemStack(ItemBean itemBean) {
        // Item name
        if (itemBean.getName() != null && !itemBean.getName().isEmpty()) {
            item.offer(Keys.DISPLAY_NAME, Text.builder(itemBean.getName()).style(TextStyles.BOLD).build());
        }
        // Item lore
        if (itemBean.getLore() != null) {

            for (String loreLine : itemBean.getLore().split("\n")) {
                lore.add(Text.builder(loreLine).color(TextColors.GRAY).build());
            }

        }
        // Item attributes
        item.offer(Keys.UNBREAKABLE, itemBean.isUnbreakable());
        if(itemBean.isUnbreakable()) {
            lore.add(Text.builder("Unbreakable").color(TextColors.DARK_GRAY).style(TextStyles.ITALIC).build());
        }
        if(itemBean.getDurability() > 0){
            item.offer(Keys.ITEM_DURABILITY, itemBean.getDurability());
        }


        if(itemBean.getToolLevel() !=0) {
            DataContainer container = this.item.toContainer();
            container.set(DataQuery.of("UnsafeData", "ToolLevel"), itemBean.getToolLevel());
            this.item = ItemStack.builder().fromContainer(container).build();
        }

    }

    /**
     * Enchant an ItemStack with an ItemBean data
     * @param itemBean Data of the item to enchant
     * @return Enchanted (or not) ItemStack
     */
    private void enchantItemStack(ItemBean itemBean) {
        Map<String, Integer> enchants = itemBean.getEnchants();
        if (!enchants.isEmpty()) {
            EnchantmentData enchantmentData = item.getOrCreate(EnchantmentData.class).get();
            for (Map.Entry<String, Integer> enchant : enchants.entrySet()) {
                Optional<EnchantmentType> optionalEnchant = Sponge.getRegistry().getType(EnchantmentType.class, enchant.getKey());
                if (optionalEnchant.isPresent()) {
                    enchantmentData.set(enchantmentData.enchantments().add(Enchantment.builder().
                            type(optionalEnchant.get()).
                            level(enchant.getValue()).build()));
                    lore.add(Text
                            .builder(enchant.getKey() + " " + enchant.getValue())
                            .style(TextStyles.ITALIC)
                            .color(TextColors.DARK_PURPLE)
                    .build());
                } else {
                    Itemizer.getLogger().warn("Unknown enchant : " + enchant.getKey());
                }
            }
            item.offer(enchantmentData);
        }
    }

    /**
     * Grant mining capabilities
     * @param itemBean Data of the item
     * @return Item with mining powers
     */
    private void grantMining(ItemBean itemBean) {
        BreakableData breakableData = item.getOrCreate(BreakableData.class).get();
        List<MinerBean> minerList = Itemizer.getConfigurationHandler().getMinerList();
        List<String> minerNames = new ArrayList<>();
        Text.Builder miningText = Text.builder("Can mine :").color(TextColors.BLUE).style(TextStyles.UNDERLINE);
        for (String minerId : itemBean.getMiners()) {
            for (MinerBean minerBean : minerList) {
                if (minerBean.getId().equals(minerId)) {
                     minerBean.getMineTypes().forEach((blockName, blockType) -> {
                         miningText.append(Text.builder(" " + blockName + " ").color(TextColors.BLUE).style(TextStyles.RESET).build());
                         Optional<BlockType> optionalBlockType = Sponge.getRegistry().getType(BlockType.class, blockType);
                         optionalBlockType.ifPresent(blockType1 -> breakableData.set(breakableData.breakable().add(blockType1)));
                     });

                    }
                }
        }
        lore.add(miningText.build());
        item.offer(breakableData);
    }

    /**
     * Set attributes to an item
     * @param itemBean Data of the item
     * @return Item with attributes set
     */
    private void setAttribute(ItemBean itemBean){
        List<DataContainer> containers = new ArrayList();
        Text.Builder attributeTextbuilder = Text.builder();
        for(AttributeBean att : itemBean.getAttributeList()){
            DataContainer dc = createAttributeModifier(att);
            containers.add(dc);

            if(att.getOperation()==0){
                attributeTextbuilder.append(Text.builder("+ " +Float.toString(att.getAmount())).color(TextColors.GOLD).build());
            } else if(att.getOperation()==1) {

                attributeTextbuilder.append(Text.builder("+ " + Float.toString(att.getAmount()*100)+ "%").color(TextColors.GOLD).build());
            } else {

                attributeTextbuilder.append(Text
                        .builder("x "+Float.toString(att.getAmount()*100)+ "%")
                        .color(TextColors.GOLD)
                        .build());
            }
            attributeTextbuilder.append(Text.builder(displayAtribute(att.getName())).color(TextColors.GOLD).build());
        }
        lore.add(attributeTextbuilder.build());
        DataContainer container = this.item.toContainer();
        container.set(DataQuery.of("UnsafeData","AttributeModifiers"),containers);
    }


    /**
     * Create the datacontainer for an attribute's data
     * @param attribute Data of the attribute
     * @return DataContainer from which the item will be recreated
     */
    private DataContainer createAttributeModifier(AttributeBean attribute){
        UUID uuid = UUID.randomUUID();
        DataContainer dataContainer = DataContainer.createNew();
        dataContainer.set(DataQuery.of("AttributeName"),attribute.getName());
        dataContainer.set(DataQuery.of("Name"),attribute.getName());
        dataContainer.set(DataQuery.of("Amount"),attribute.getAmount());
        dataContainer.set(DataQuery.of("Operation"),attribute.getOperation());
        dataContainer.set(DataQuery.of("Slot"),attribute.getSlot());
        dataContainer.set(DataQuery.of("UUIDMost"),uuid.getMostSignificantBits());
        dataContainer.set(DataQuery.of("UUIDLeast"),uuid.getLeastSignificantBits());
        return dataContainer;
    }

    private void addLore() {
        item.offer(Keys.ITEM_LORE,lore);
    }

    private String displayAtribute(String genericName){
        switch (genericName){
            case "generic.attackDamage":
                return " Damage";
            case "generic.maxHealth":
                return " Life";
            case "generic.movementSpeed":
                return " Speed";
            case "generic.attackSpeed":
                return " Attack speed";
            default:
                return genericName;
        }
    }
}
