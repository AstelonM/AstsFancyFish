package com.astelon.astsfancyfish.listeners;

import com.astelon.astsfancyfish.AstsFancyFish;
import com.astelon.astsfancyfish.Config;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
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

    public FishingListener(AstsFancyFish plugin) {
        this.plugin = plugin;
        miniMessage = MiniMessage.miniMessage();
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerFish(PlayerFishEvent event) {
        if (event.getState() == PlayerFishEvent.State.CAUGHT_FISH) {
            Entity caught = event.getCaught();
            if (caught == null)
                return;
            Item item = (Item) caught;
            ItemStack itemStack = item.getItemStack();
            Config config = plugin.getConfiguration();
            Player player = event.getPlayer();
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
}
