package com.onaple.itemizer.crafing.event;

import org.spongepowered.api.event.Cancellable;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.impl.AbstractEvent;
import org.spongepowered.api.item.inventory.ItemStack;

public class CraftSuccessfulEvent extends AbstractEvent implements Cancellable
{

    private final Cause cause;



    private ItemStack craftResult;
    private boolean cancelled = false;

    public CraftSuccessfulEvent(Cause cause, ItemStack craftResult) {
        this.cause = cause;
        this.craftResult = craftResult;
    }

    @Override
    public Cause getCause() {
        return this.cause;
    }

    public ItemStack getCraftResult() {
        return craftResult;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        this.cancelled = cancel;
    }
    public void setCraftResult(ItemStack craftResult) {
        this.craftResult = craftResult;
    }

}
