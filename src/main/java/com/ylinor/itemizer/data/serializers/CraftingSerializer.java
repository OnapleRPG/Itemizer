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
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.*;

public class CraftingSerializer implements TypeSerializer<ICraftRecipes> {
    @Override
    public ICraftRecipes deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        int id = value.getNode("id").getInt();

        String craftingType = value.getNode("type").getString();

        ConfigurationNode resultNode = value.getNode("result");
        ItemStack itemStackOptional=getItemStack(resultNode);

        ItemStack resultIngredient;


            resultIngredient = itemStackOptional;

            ItemStack singleIngredient;
            HashMap<Character,ItemStack> itemStackHashMap = new HashMap<>();
            switch (craftingType) {
                case "ShapelessCrafting":
                    ConfigurationNode shaplessIngredient =  value.getNode("recipe");
                    ItemStack Recipice=getItemStack(shaplessIngredient);
                    singleIngredient = Recipice;
                    return new CraftingRecipeRegister(id, singleIngredient,resultIngredient);

                case "Smelting":
                    ConfigurationNode configurationNode =  value.getNode("recipe");
                    ItemStack smeltingIngrediant=getItemStack(configurationNode);
                    singleIngredient = smeltingIngrediant;
                    return new SmeltingRecipeRegister(id, singleIngredient,resultIngredient);
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
                        ingredients.put(key.charAt(0),Ingredient.of(getItemStack(value.getNode("ingredients",key))));
                    }
                    return new ShapedCrafting(id,shape,ingredients,resultIngredient);
            }

       throw new ObjectMappingException();
    }

    public ItemStack getItemStack(ConfigurationNode node) throws ObjectMappingException{
        int ref = node.getNode("ref").getInt();
        if(ref>0) {
            Optional<ItemBean> itemBeanOptional = ItemDAO.getItem(ref);
            ItemStack result;
            if (itemBeanOptional.isPresent()) {
                Optional<ItemStack> itemStackOptional = ItemBuilder.buildItemStack(itemBeanOptional.get());
                if (itemBeanOptional.isPresent()) {
                    return itemStackOptional.get();
                }
            }
        } else {
            String name = node.getNode("name").getString();
            if(name != null){

                  if(ItemBuilder.buildItemStack(name).isPresent()){
                      return ItemBuilder.buildItemStack(name).get();
                  }
            }

        }
        throw new ObjectMappingException();
    }

    @Override
    public void serialize(TypeToken<?> type, ICraftRecipes obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
