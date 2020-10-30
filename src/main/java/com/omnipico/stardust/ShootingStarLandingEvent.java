package com.omnipico.stardust;

import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ShootingStarLandingEvent extends Event {
    private static final HandlerList HANDLERS = new HandlerList();
    private StarType starType;
    private Location location;

    public ShootingStarLandingEvent(StarType starType, Location location) {
        this.starType = starType;
        this.location = location;
    }

    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public StarType getStarType() {
        return starType;
    }

    public Location getLocation() {
        return location;
    }
}
