package com.onaple.itemizer.utils;

import com.onaple.itemizer.ICraftRecipes;

import java.util.List;

import javax.inject.Singleton;


@Singleton
public class CraftRegister {

    public void register(List<ICraftRecipes> craftRecipesList) {
        for (ICraftRecipes recipeRegister : craftRecipesList) {
            recipeRegister.register();
        }
    }
}
