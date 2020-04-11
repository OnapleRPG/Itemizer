package com.onaple.itemizer.crafing.event;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

public class RowCraftEvent extends AbstractEvent {



    private final Cause cause;
    private final Player owner;
    private final List<ItemStack> ingredients;
    private final Optional<ItemStack> result;


    @Override
    public Cause getCause() {
        return cause;
    }

    public RowCraftEvent(Cause cause, Player owner, List<ItemStack> ingredients, Optional<ItemStack> result) {
        this.cause = cause;
        this.owner = owner;
        this.ingredients = ingredients;
        this.result = result;
    }

    public Player getOwner() {
        return owner;
    }

    public List<ItemStack> getIngredients() {
        return ingredients;
    }

    public Optional<ItemStack> getResult() {
        return result;
    }
}
