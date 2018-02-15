package com.ylinor.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.ylinor.itemizer.Itemizer;
import com.ylinor.itemizer.data.beans.CraftingRecipeRegister;
import com.ylinor.itemizer.ICraftRecipes;
import com.ylinor.itemizer.data.beans.ShapedCrafting;
import com.ylinor.itemizer.data.beans.SmeltingRecipeRegister;
import com.ylinor.itemizer.data.access.ItemDAO;
import com.ylinor.itemizer.data.beans.ItemBean;
import com.ylinor.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class CraftingSerializer implements TypeSerializer<ICraftRecipes> {
    @Override
    public ICraftRecipes deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        int id = value.getNode("id").getInt();

        String craftingType = value.getNode("type").getString();

        ConfigurationNode resultNode = value.getNode("result");
        Optional<ItemStack> itemStackOptional=getItemStack(resultNode);

        ItemStack resultIngredient;

        if(itemStackOptional.isPresent()){
            resultIngredient = itemStackOptional.get();

            ItemStack singleIngredient;
            HashMap<Character,ItemStack> itemStackHashMap = new HashMap<>();
            switch (craftingType) {
                case "CraftingRecipeRegister":
                    ConfigurationNode shaplessIngredient =  value.getNode("recipe");

                    Optional<ItemStack> RecipiceOptional=getItemStack(shaplessIngredient);
                    if(RecipiceOptional.isPresent()){
                        singleIngredient = RecipiceOptional.get();
                        return new CraftingRecipeRegister(id, singleIngredient,resultIngredient);
                    }
                    break;
                case "SmeltingRecipeRegister":
                    ConfigurationNode configurationNode =  value.getNode("recipe");

                    Optional<ItemStack> smeltingIngrediant=getItemStack(configurationNode);
                    if(smeltingIngrediant.isPresent()){
                        singleIngredient = smeltingIngrediant.get();
                        return new SmeltingRecipeRegister(id, singleIngredient,resultIngredient);
                    }
                    break;
                case "ShapedCrafting":
                    String[] shape = Arrays.copyOf(value.getNode("pattern").getList(TypeToken.of(String.class)).toArray(),
                            value.getNode("pattern").getList(TypeToken.of(String.class)).toArray().length, String[].class);
                    Map<Character,Ingredient> ingredients = new HashMap<>();
                    List<Character> keys = new ArrayList<>();
                   Map<Object,? extends ConfigurationNode> b = value.getNode("ingredients").getChildrenMap();
                    for(Object prekey: b.keySet()){
                        String key = (String) prekey;
                        Itemizer.getLogger().info("key : " + key);
                        Itemizer.getLogger().info(value.getNode("ingredients", key).getString());
                        ingredients.put(key.charAt(0),Ingredient.of(getItemStack(value.getNode("ingredients",key)).get()));
                    }

                    return new ShapedCrafting(id,shape,ingredients,resultIngredient);

            }
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
