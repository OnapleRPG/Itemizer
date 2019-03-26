package com.onaple.itemizer;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;
import org.spongepowered.api.item.recipe.Recipe;

@ConfigSerializable
public interface ICraftRecipes {
    void register(GameRegistryEvent.Register event);
}
