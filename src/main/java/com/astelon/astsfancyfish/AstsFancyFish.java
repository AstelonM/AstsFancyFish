package com.astelon.astsfancyfish;

import com.astelon.astsfancyfish.commands.FishCommand;
import com.astelon.astsfancyfish.fish.BaseFish;
import com.astelon.astsfancyfish.fish.FancyFish;
import com.astelon.astsfancyfish.fish.SimpleFish;
import com.astelon.astsfancyfish.fish.appearance.TropicalFishAppearance;
import com.astelon.astsfancyfish.fish.appearance.FishType;
import com.astelon.astsfancyfish.fish.appearance.FishAppearance;
import com.astelon.astsfancyfish.fish.conditions.FishConditions;
import com.astelon.astsfancyfish.fish.conditions.TimePeriod;
import com.astelon.astsfancyfish.fish.conditions.Weather;
import com.astelon.astsfancyfish.listeners.FishingListener;
import com.astelon.astsfancyfish.managers.BiomeManager;
import com.astelon.astsfancyfish.managers.FishManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import org.bukkit.DyeColor;
import org.bukkit.block.Biome;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.TropicalFish;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;

public class AstsFancyFish extends JavaPlugin {

    private Config config;
    private MiniMessage fishMiniMessage;
    private FishManager fishManager;
    private BiomeManager biomeManager;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        loadConfig();
        initMiniMessage();
        fishManager = new FishManager();
        loadFish();
        biomeManager = new BiomeManager();
        loadBiomes();
        getServer().getPluginManager().registerEvents(new FishingListener(this, biomeManager), this);
        Objects.requireNonNull(getCommand("fancyfish")).setExecutor(new FishCommand(this));
    }

    public void reload() {
        reloadConfig();
        loadConfig();
        loadFish();
        loadBiomes();
    }

    private void loadConfig() {
        FileConfiguration configuration = getConfig();
        boolean itemsDirectlyToInventory = configuration.getBoolean("itemsDirectlyToInventory");
        boolean announceFishedItems = configuration.getBoolean("announceFishedItems");
        String chatPrefix = configuration.getString("chatPrefix", "");
        config = new Config(itemsDirectlyToInventory, announceFishedItems, chatPrefix);
    }

    private void loadFish() {
        getLogger().info("Loading fish list.");
        HashMap<String, BaseFish> fish = new HashMap<>();
        File fishFile = new File(getDataFolder(), "fish.yml");
        if (!fishFile.exists()) {
            getLogger().info("Could not find fish.yml, creating a default one.");
            saveResource("fish.yml", true);
        }
        YamlConfiguration fishConfig = YamlConfiguration.loadConfiguration(fishFile);
        for (String fishId: fishConfig.getKeys(false)) {
            String name = fishConfig.getString(fishId + ".name");
            Component nameComponent = null;
            if (name != null)
                nameComponent = fishMiniMessage.deserialize(name);
            List<String> description = fishConfig.getStringList(fishId + ".description");
            List<Component> descriptionComponents = description.stream().map(line -> fishMiniMessage.deserialize(line)).toList();
            FishType fishType;
            try {
                String fishTypeName = fishConfig.getString(fishId + ".appearance.fishType");
                fishType = FishType.valueOf(fishTypeName);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Fish with id " + fishId + " has invalid appearance type.");
                continue;
            }
            FishAppearance fishAppearance;
            if (fishType == FishType.TROPICAL_FISH) {
                try {
                    String patternName = fishConfig.getString(fishId + ".appearance.pattern");
                    TropicalFish.Pattern pattern = TropicalFish.Pattern.valueOf(patternName);
                    String bodyColourName = fishConfig.getString(fishId + ".appearance.bodyColour");
                    DyeColor bodyColour = DyeColor.valueOf(bodyColourName);
                    String patternColourName = fishConfig.getString(fishId + ".appearance.patternColour");
                    DyeColor patternColour = DyeColor.valueOf(patternColourName);
                    fishAppearance = new TropicalFishAppearance(nameComponent, descriptionComponents, pattern, bodyColour, patternColour);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Fish with id " + fishId + " has invalid appearance.");
                    continue;
                }
            } else {
                fishAppearance = new FishAppearance(nameComponent, descriptionComponents, fishType);
            }
            List<String> unformattedTimePeriods = fishConfig.getStringList(fishId + ".conditions.timePeriods");
            ArrayList<TimePeriod> timePeriods = new ArrayList<>(unformattedTimePeriods.size());
            for (String unformattedTimePeriod: unformattedTimePeriods) {
                try {
                    TimePeriod timePeriod = TimePeriod.fromText(unformattedTimePeriod);
                    timePeriods.add(timePeriod);
                } catch (IllegalArgumentException e) {
                    getLogger().warning("Fish with id " + fishId + " has an invalid time period " + unformattedTimePeriod + ".");
                }
            }
            if (timePeriods.size() == 0)
                getLogger().warning("Fish with id " + fishId + " has no valid time periods. It will not appear anywhere.");
            String weatherName = fishConfig.getString(fishId + ".conditions.weather");
            Weather weather = null;
            try {
                weather = Weather.valueOf(weatherName);
            } catch (IllegalArgumentException e) {
                getLogger().warning("Fish with id " + fishId + " has invalid weather. It will not appear anywhere.");
            }
            boolean surface = fishConfig.getBoolean(fishId + ".conditions.surface");
            FishConditions fishConditions = new FishConditions(timePeriods, weather, surface);
            FancyFish fancyFish = new FancyFish(fishId, fishAppearance, fishConditions);
            fish.put(fishId, fancyFish);
        }
        getLogger().info("Loaded " + fish.size() + " fancy fish.");
        fish.put("cod", new SimpleFish("cod", FishType.COD));
        fish.put("salmon", new SimpleFish("salmon", FishType.SALMON));
        fish.put("tropicalFish", new SimpleFish("tropicalFish", FishType.TROPICAL_FISH));
        fish.put("pufferfish", new SimpleFish("pufferfish", FishType.PUFFERFISH));
        fishManager.setFish(fish);
    }

    //TODO biome classes support
    private void loadBiomes() {
        getLogger().info("Loading biomes list.");
        HashMap<Biome, HashMap<BaseFish, Integer>> biomes = new HashMap<>();
        File biomesFile = new File(getDataFolder(), "biomes.yml");
        if (!biomesFile.exists()) {
            getLogger().info("Could not find biomes.yml, creating a default one.");
            saveResource("biomes.yml", true);
        }
        YamlConfiguration biomesConfig = YamlConfiguration.loadConfiguration(biomesFile);
        for (String biomeName: biomesConfig.getKeys(false)) {
            Biome biome;
            try {
                biome = Biome.valueOf(biomeName.toUpperCase(Locale.ROOT));
            } catch (IllegalArgumentException e) {
                getLogger().warning("There is no biome with the name " + biomeName + ".");
                continue;
            }
            HashMap<BaseFish, Integer> fishWeights = new HashMap<>();
            ConfigurationSection biomeSection = Objects.requireNonNull(biomesConfig.getConfigurationSection(biomeName));
            for (String fishName: biomeSection.getKeys(false)) {
                BaseFish fish = fishManager.getFish(fishName);
                if (fish == null) {
                    getLogger().warning("Fish with id " + fishName + " added in biome " + biomeName + " is invalid.");
                    continue;
                }
                int weight = biomesConfig.getInt(biomeName + "." + fishName);
                if (weight <= 0) {
                    getLogger().info("Weight of fish " + fishName + " added in biome " + biomeName + " is invalid.");
                    continue;
                }
                fishWeights.put(fish, weight);
            }
            if (fishWeights.isEmpty()) {
                getLogger().warning("Biome " + biomeName + " has no valid fish added. Fish caught in it will not be changed.");
                continue;
            }
            biomes.put(biome, fishWeights);
        }
        getLogger().info("Loaded " + biomes.size() + " biomes.");
        biomeManager.setBiomes(biomes);
    }

    private void initMiniMessage() {
        fishMiniMessage = MiniMessage.builder().tags(TagResolver.builder()
                .resolver(StandardTags.color())
                .resolver(StandardTags.decorations())
                .resolver(StandardTags.gradient())
                .resolver(StandardTags.rainbow())
                .build()
        ).build();
    }

    public Config getConfiguration() {
        return config;
    }
}
