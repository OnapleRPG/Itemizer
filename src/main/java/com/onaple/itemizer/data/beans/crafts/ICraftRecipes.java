package com.onaple.itemizer.data.beans.crafts;

import ninja.leaping.configurate.objectmapping.serialize.ConfigSerializable;
import org.spongepowered.api.event.game.GameRegistryEvent;

@ConfigSerializable
public interface ICraftRecipes {
    void register(GameRegistryEvent.Register event);
}
