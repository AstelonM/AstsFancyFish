package com.astelon.astsfancyfish.managers;

import com.astelon.astsfancyfish.fish.BaseFish;
import com.astelon.astsfancyfish.fish.conditions.Weather;
import com.astelon.astsfancyfish.utils.WeightedList;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class BiomeManager {

    private final Random random;
    private HashMap<Biome, HashMap<BaseFish, Integer>> biomes;

    public BiomeManager() {
        random = new Random();
    }

    public boolean hasDifferentFish(Biome biome) {
        return biomes.containsKey(biome);
    }

    public BaseFish getFish(Biome biome, int hour, Weather weather, boolean surface) {
        HashMap<BaseFish, Integer> fishMap = biomes.get(biome);
        if (fishMap == null)
            return null;
        //TODO cache the list
        WeightedList<BaseFish> fishWeights = new WeightedList<>();
        for (Map.Entry<BaseFish, Integer> fishWeightEntry: fishMap.entrySet()) {
            if (fishWeightEntry.getKey().canAppear(hour, weather, surface))
                fishWeights.put(fishWeightEntry.getValue(), fishWeightEntry.getKey());
        }
        if (fishWeights.isEmpty())
            return null;
        return fishWeights.getRandom(random);
    }

    public void setBiomes(HashMap<Biome, HashMap<BaseFish, Integer>> biomes) {
        this.biomes = biomes;
    }
}
