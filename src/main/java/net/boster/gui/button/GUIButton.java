package net.boster.gui.button;

import net.boster.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface GUIButton {

    int getSlot();

    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    default void onClick(@NotNull Player p) {
    }

    default void onClick(@NotNull GUI gui, @NotNull InventoryClickEvent event) {
        onClick((Player) event.getWhoClicked());
    }

    @Nullable ItemStack prepareItem(@NotNull Player p);
}
