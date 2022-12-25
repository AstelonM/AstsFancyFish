package com.astelon.astsfancyfish.fish.appearance;

import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class FishAppearance {

    protected final Component displayName;
    protected final List<Component> description;
    protected final FishType fishType;

    protected final ItemStack itemStack;

    public FishAppearance(Component displayName, List<Component> description, FishType fishType) {
        this.displayName = displayName;
        this.description = description;
        this.fishType = fishType;
        this.itemStack = createItemStack();
    }

    protected ItemStack createItemStack() {
        ItemStack result = new ItemStack(getFishMaterial());
        ItemMeta meta = result.getItemMeta();
        meta.displayName(displayName);
        meta.lore(description);
        result.setItemMeta(meta);
        return result;
    }

    public ItemStack getFishItemStack() {
        return new ItemStack(itemStack);
    }

    private Material getFishMaterial() {
        return switch (fishType) {
            case COD -> Material.COD;
            case SALMON -> Material.SALMON;
            case PUFFERFISH -> Material.PUFFERFISH;
            case TROPICAL_FISH -> Material.TROPICAL_FISH;
        };
    }
}
