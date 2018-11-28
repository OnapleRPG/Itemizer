package com.onaple.itemizer.events;

import org.spongepowered.api.event.Event;
import org.spongepowered.api.event.cause.Cause;
import org.spongepowered.api.event.cause.EventContext;

public class ItemizerPreLoadEvent implements Event {
    @Override
    public Cause getCause() {
        return Cause.builder().build(EventContext.empty());
    }
}
