package com.astelon.astsfancyfish.fish;

import com.astelon.astsfancyfish.fish.appearance.FishType;
import com.astelon.astsfancyfish.fish.conditions.Weather;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class SimpleFish extends BaseFish {

    private final ItemStack itemStack;

    public SimpleFish(String name, FishType type) {
        super(name);
        itemStack = switch (type) {
            case COD -> new ItemStack(Material.COD);
            case SALMON -> new ItemStack(Material.SALMON);
            case TROPICAL_FISH -> new ItemStack(Material.TROPICAL_FISH);
            case PUFFERFISH -> new ItemStack(Material.PUFFERFISH);
        };
    }

    @Override
    public ItemStack getItemStack() {
        return new ItemStack(itemStack);
    }

    @Override
    public boolean canAppear(int hour, Weather currentWeather, boolean isOnSurface) {
        return true;
    }
}
