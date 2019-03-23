package com.onaple.itemizer.recipes;

import com.onaple.itemizer.Itemizer;
import com.onaple.itemizer.data.serializers.ItemBeanRefOrItemIdAdapter;
import cz.neumimto.config.blackjack.and.hookers.annotations.CustomAdapter;
import ninja.leaping.configurate.objectmapping.Setting;
import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.Sponge;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.item.recipe.crafting.CraftingRecipe;
import org.spongepowered.api.item.recipe.crafting.Ingredient;
import org.spongepowered.api.item.recipe.crafting.ShapelessCraftingRecipe;

/**
 * Created by NeumimTo on 23.3.2019.
 */
@ConfigSerializable
public class ShapelessCrafting extends AbstractCraftingRecipe {

    @Setting("content")
    @CustomAdapter(ItemBeanRefOrItemIdAdapter.class)
    protected ItemStack content;

    @Override
    public void register() {
        ShapelessCraftingRecipe r = CraftingRecipe.shapelessBuilder()
                .addIngredient(Ingredient.builder().with(content).build())
                .result(result).build("craft" + id, Itemizer.getInstance());
        Sponge.getGame().getRegistry().getCraftingRecipeRegistry().register(r);
    }
}
