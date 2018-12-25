package com.onaple.itemizer.data.serializers;

import com.google.common.reflect.TypeToken;
import com.onaple.itemizer.ICraftRecipes;
import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.access.ItemDAO;
import com.onaple.itemizer.data.beans.CraftingRecipeRegister;
import com.onaple.itemizer.data.beans.ItemBean;
import com.onaple.itemizer.data.beans.ShapedCrafting;
import com.onaple.itemizer.data.beans.SmeltingRecipeRegister;
import com.onaple.itemizer.utils.ItemBuilder;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import ninja.leaping.configurate.objectmapping.serialize.TypeSerializer;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.Ingredient;

import java.util.*;

public class CraftingSerializer implements TypeSerializer<ICraftRecipes> {
    @Override
    public ICraftRecipes deserialize(TypeToken<?> type, ConfigurationNode value) throws ObjectMappingException {

        String id = value.getNode("id").getString();

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

                        ingredients.put(key.charAt(0),Ingredient.of(getItemStack(value.getNode("ingredients",key))));
                    }
                    return new ShapedCrafting(id,shape,ingredients,resultIngredient);
            }

       throw new ObjectMappingException();
    }

    public ItemStack getItemStack(ConfigurationNode node) throws ObjectMappingException {
        String ref = node.getNode("ref").getString();
        if(ref != null && !ref.equals("")) {
            Optional<ItemBean> itemBeanOptional = Itemizer.getItemDAO().getItem(ref);
            ItemStack result;
            if (itemBeanOptional.isPresent()) {
                Optional<ItemStack> itemStackOptional = new ItemBuilder().buildItemStack(itemBeanOptional.get());
                if (itemStackOptional.isPresent()) {
                    return itemStackOptional.get();
                }
            } else {
                throw new ObjectMappingException("Item reference not found");
            }
        } else {
            String name = node.getNode("name").getString();
            if(name != null){
                Optional<ItemStack> itemStackOptional = new ItemBuilder().buildItemStack(name);
                  if(itemStackOptional.isPresent()){
                      return itemStackOptional.get();
                  }else{
                      Itemizer.getLogger().error("item named " + name + "is not registered");
                      throw new ObjectMappingException("item named " + name + "is not registered");
                  }
            }

        }
        throw new ObjectMappingException("unknown error");
    }

    @Override
    public void serialize(TypeToken<?> type, ICraftRecipes obj, ConfigurationNode value) throws ObjectMappingException {

    }
}
