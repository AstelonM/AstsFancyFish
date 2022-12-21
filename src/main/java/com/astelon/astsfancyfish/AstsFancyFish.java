package com.astelon.astsfancyfish;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public class AstsFancyFish extends JavaPlugin {

    private Config config;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        reloadConfig();
        loadConfig();
        getServer().getPluginManager().registerEvents(new FishingListener(this), this);
        Objects.requireNonNull(getCommand("fancyfish")).setExecutor(new FishCommand(this));
    }

    public void reload() {
        reloadConfig();
        loadConfig();
    }

    private void loadConfig() {
        FileConfiguration configuration = getConfig();
        boolean itemsDirectlyToInventory = configuration.getBoolean("itemsDirectlyToInventory");
        boolean announceFishedItems = configuration.getBoolean("announceFishedItems");
        String chatPrefix = configuration.getString("chatPrefix", "");
        config = new Config(itemsDirectlyToInventory, announceFishedItems, chatPrefix);
    }

    public Config getConfiguration() {
        return config;
    }
}
