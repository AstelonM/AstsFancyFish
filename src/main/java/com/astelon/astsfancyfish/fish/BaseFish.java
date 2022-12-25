package com.astelon.astsfancyfish.fish;

import com.astelon.astsfancyfish.fish.conditions.Weather;
import org.bukkit.inventory.ItemStack;

public abstract class BaseFish {

    protected final String name;

    public BaseFish(String name) {
        this.name = name;
    }

    public abstract ItemStack getItemStack();

    public abstract boolean canAppear(int hour, Weather currentWeather, boolean isOnSurface);
}
