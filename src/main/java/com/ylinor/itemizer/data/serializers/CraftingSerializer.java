package com.ylinor.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.CraftingRecipeRegister;
import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.data.access.ItemDAO;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.entity.Item;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.HashMap;
import java.util.Optional;

public class CraftingSerializer implements TypeSerializer<ICraftRecipes> {
    @Override
    public ICraftRecipes deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        String craftingType = value.getNode("type").getString();

        ConfigurationNode resultNode = value.getNode("result");
        Optional<ItemStack> itemStackOptional=getItemStack(resultNode);

        ItemStack resultIngredient;

        if(itemStackOptional.isPresent()){
            resultIngredient = itemStackOptional.get();
        }

        ItemStack singleIngredient;
        HashMap<Character,ItemStack> itemStackHashMap = new HashMap<>();
        switch (craftingType) {
            case "CraftingRecipeRegister":
                ConfigurationNode configurationNode =  value.getNode("recipe");

                Optional<ItemStack> RecepiceOptional=getItemStack(configurationNode);
                if(RecepiceOptional.isPresent()){
                    singleIngredient = RecepiceOptional.get();

                }
                break;
            case "SmeltingRecipeRegister":
                break;
            case "ShapedCrafting":
        }
        return null;
    }

    public Optional<ItemStack> getItemStack(ConfigurationNode node){
        int ref = node.getNode("ref").getInt();
        if(ref>0) {
            Optional<ItemBean> itemBeanOptional = ItemDAO.getItem(ref);
            ItemStack result;
            if (itemBeanOptional.isPresent()) {
                Optional<ItemStack> itemStackOptional = ItemBuilder.buildItemStack(itemBeanOptional.get());
                if (itemBeanOptional.isPresent()) {
                    return itemStackOptional;
                }
            }
        } else {
            String name = node.getNode("name").getString();
            if(name != null){
                 return ItemBuilder.buildItemStack(name);
            }

        }
        return Optional.empty();
    }

    @Override
    public void serialize(TypeToken<?> type, ICraftRecipes obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
