package net.boster.gui;

import net.boster.gui.button.GUIButton;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public interface GUI {

    @NotNull Map<Integer, GUIButton> getButtons();
    int getSize();
    void onClick(@NotNull InventoryClickEvent e);
    void onClose(@NotNull InventoryCloseEvent e);
}
