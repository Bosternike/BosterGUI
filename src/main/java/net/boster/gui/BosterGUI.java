package net.boster.gui;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.listener.InventoryListener;
import net.boster.gui.item.ItemManager;
import net.boster.gui.item.ItemManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BosterGUI {

    @Getter private static Plugin provider;
    @Getter @Setter @NotNull private static ItemManager itemManager = new ItemManagerImpl();

    public static void setup(@NotNull Plugin plugin) {
        if(provider != null) return;

        provider = plugin;
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), plugin);
    }
}
