package com.ylinor.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStack;

public class CraftingSerializer implements TypeSerializer<ICraftRecipes> {
    @Override
    public ICraftRecipes deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {
        String craftingType = value.getNode("type").getString();

        ConfigurationNode resultname = value.getNode("result");

        try {
            resultname.getInt();
        } catch (Exception e){

        }

        int reference = value.getNode("ref").getInt();
        String itemName;
        switch (craftingType){
            case "CraftingRecipeRegister" :
                itemName = value.getNode("ingredient").getString();
                ItemStack itemStack = ItemBuilder.buildItemStack( );
                break;
            case "SmeltingRecipeRegister" :
                itemName = value.getNode("ingredient").getString();
                break;
            case "ShapedCrafting" :

        }

        return null;
    }

    @Override
    public void serialize(TypeToken<?> type, ICraftRecipes obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
