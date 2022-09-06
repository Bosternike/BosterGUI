package net.boster.gui;

import net.boster.gui.button.GUIButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Level;

public interface GUI extends InventoryHolder {

    void log(@NotNull String s, @NotNull Level log);
    @NotNull Map<Integer, GUIButton> getButtons();
    int getSize();
    void onClick(InventoryClickEvent e);
    void onClose(InventoryCloseEvent e);
}
