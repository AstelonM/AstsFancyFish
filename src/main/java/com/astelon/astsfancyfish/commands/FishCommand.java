package com.astelon.astsfancyfish.commands;

import com.astelon.astsfancyfish.AstsFancyFish;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.stream.Collectors;

public class FishCommand implements TabExecutor {

    private final AstsFancyFish plugin;
    private final List<String> subcommands;

    public FishCommand(AstsFancyFish plugin) {
        this.plugin = plugin;
        subcommands = List.of("reload");
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Component.text("Available subcommands: reload."));
            return true;
        }
        String subcommand = args[0];
        if (subcommand.equalsIgnoreCase("reload")) {
            plugin.reload();
            sender.sendMessage(Component.text("Ast's Fancy Fish reloaded!", NamedTextColor.GOLD));
            return true;
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (args.length == 1) {
            String text = args[0].toLowerCase();
            return subcommands.stream().filter(subcommand -> subcommand.startsWith(text)).collect(Collectors.toList());
        }
        return null;
    }
}
