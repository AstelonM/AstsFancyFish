package com.astelon.astsfancyfish.listeners;

import com.astelon.astsfancyfish.AstsFancyFish;
import com.astelon.astsfancyfish.Config;
import com.astelon.astsfancyfish.fish.BaseFish;
import com.astelon.astsfancyfish.fish.conditions.Weather;
import com.astelon.astsfancyfish.managers.BiomeManager;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.HashMap;

public class FishingListener implements Listener {

    private final AstsFancyFish plugin;
    private final MiniMessage miniMessage;
    private final BiomeManager biomeManager;

    public FishingListener(AstsFancyFish plugin, BiomeManager biomeManager) {
        this.plugin = plugin;
        this.biomeManager = biomeManager;
        miniMessage = MiniMessage.miniMessage();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Entity caught = event.getCaught();
            if (caught == null)
                return;
            Player player = event.getPlayer();
            Item item = (Item) caught;
            ItemStack itemStack = item.getItemStack();
            if (isFish(itemStack)) {
                Location location = event.getHook().getLocation();
                //TODO find the proper method
                Biome biome = location.getBlock().getBiome();
                if (!biomeManager.hasDifferentFish(biome))
                    return;
                World world = player.getWorld();
                int hour = (int) ((world.getTime() / 1000) + 6) % 24;
                Weather weather = getWeather(world);
                int highestBlock = world.getHighestBlockYAt(location);
                boolean surface = location.getBlockY() >= highestBlock;
                BaseFish fish = biomeManager.getFish(biome, hour, weather, surface);
                if (fish != null) {
                    itemStack = fish.getItemStack();
                    item.setItemStack(itemStack);
                }
            }
            Config config = plugin.getConfiguration();
            if (config.itemsDirectlyToInventory()) {
                PlayerInventory inventory = player.getInventory();
                HashMap<Integer, ItemStack> notAdded = inventory.addItem(itemStack);
                if (notAdded.isEmpty())
                    item.remove();
            }
            if (config.announceFishedItems()) {
                player.sendMessage(miniMessage.deserialize("<prefix>You caught a <item>!",
                        Placeholder.parsed("prefix", config.chatPrefix()),
                        Placeholder.component("item", itemStack.displayName().hoverEvent(itemStack.asHoverEvent()))));
            }

        }
    }

    private boolean isFish(ItemStack itemStack) {
        Material material = itemStack.getType();
        return material == Material.COD || material == Material.SALMON || material == Material.TROPICAL_FISH ||
                material == Material.PUFFERFISH;
    }

    private Weather getWeather(World world) {
        boolean rain = world.hasStorm();
        boolean thunderstorm = world.isThundering();
        if (!rain)
            return Weather.SUN;
        else if (!thunderstorm)
            return Weather.RAIN;
        return Weather.STORM;
    }
}
