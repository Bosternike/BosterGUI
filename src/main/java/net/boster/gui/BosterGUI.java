package net.boster.gui;

import lombok.Getter;
import net.boster.gui.listener.InventoryListener;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BosterGUI {

    @Getter private static Plugin provider;

    public static void setup(@NotNull Plugin plugin) {
        provider = plugin;
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
    }
}
