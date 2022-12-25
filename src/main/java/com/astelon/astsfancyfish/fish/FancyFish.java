package com.astelon.astsfancyfish.fish;

import com.astelon.astsfancyfish.fish.appearance.FishAppearance;
import com.astelon.astsfancyfish.fish.conditions.FishConditions;
import com.astelon.astsfancyfish.fish.conditions.Weather;
import org.bukkit.inventory.ItemStack;

public class FancyFish extends BaseFish {

    private final FishAppearance appearance;
    private final FishConditions conditions;

    public FancyFish(String name, FishAppearance appearance, FishConditions conditions) {
        super(name);
        this.appearance = appearance;
        this.conditions = conditions;
    }

    @Override
    public ItemStack getItemStack() {
        return appearance.getFishItemStack();
    }

    @Override
    public boolean canAppear(int hour, Weather currentWeather, boolean isOnSurface) {
        return conditions.match(hour, currentWeather, isOnSurface);
    }
}
