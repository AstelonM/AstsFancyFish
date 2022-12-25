package com.astelon.astsfancyfish.fish.appearance;

import net.kyori.adventure.text.Component;
import org.bukkit.DyeColor;
import org.bukkit.entity.TropicalFish;

import java.util.List;

public class TropicalFishAppearance extends FishAppearance {

    private final TropicalFish.Pattern pattern;
    private final DyeColor bodyColour;
    private final DyeColor patternColour;

    public TropicalFishAppearance(Component displayName, List<Component> description,
                                  TropicalFish.Pattern pattern, DyeColor bodyColour, DyeColor patternColour) {
        super(displayName, description, FishType.TROPICAL_FISH);
        this.pattern = pattern;
        this.bodyColour = bodyColour;
        this.patternColour = patternColour;
    }
}
