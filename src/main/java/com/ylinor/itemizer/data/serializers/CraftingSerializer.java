package com.ylinor.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
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

        ConfigurationNode resultname = value.getNode("result");


        int reference = resultname.getNode("ref").getInt();



        ItemStack singleIngredient;
        HashMap<Character,ItemStack> itemStackHashMap = new HashMap<>();
        switch (craftingType){
            case "CraftingRecipeRegister" :
                String itemName = value.getNode("ingredient").getNode("name").getString();
                Optional<ItemStack> itemStack = ItemBuilder.buildItemStack(itemName);
                if(itemStack.isPresent()){
                    singleIngredient = itemStack.get();
                }


                break;
            case "SmeltingRecipeRegister" :

                break;
            case "ShapedCrafting" :

        }

        return null;
    }


    public Optional<ItemStack> getItemStack(int id){
        Optional<ItemBean> itemBeanOptional =  ItemDAO.getItem(id);
        ItemStack result;
        if (itemBeanOptional.isPresent()){

            Optional<ItemStack> itemStackOptional = ItemBuilder.buildItemStack(itemBeanOptional.get());
            if(itemBeanOptional.isPresent()){
                result = itemStackOptional.get();
            }
        }
        return Optional.empty();
    }

    @Override
    public void serialize(TypeToken<?> type, ICraftRecipes obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
