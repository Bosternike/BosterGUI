package net.boster.gui;

import net.boster.gui.button.GUIButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.logging.Level;

public interface GUI {

    void log(@NotNull String s, @NotNull Level log);
    @NotNull Map<Integer, GUIButton> getButtons();
    int getSize();
    void onClick(@NotNull InventoryClickEvent e);
    void onClose(@NotNull InventoryCloseEvent e);
}
