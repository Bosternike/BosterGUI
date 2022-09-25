package net.boster.gui.multipage;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MultiPageEntry extends MultiPageFunctionalEntry {

    @Nullable ItemStack item(@NotNull Player p);

    default @Nullable ItemStack item(@NotNull Player p, int page, int slot) {
        return item(p);
    }

    @SuppressWarnings("PMD.UncommentedEmptyMethodBody")
    default void onClick(@NotNull MultiPageGUI gui, @NotNull Player p) {
    }

    default void onClick(@NotNull MultiPageGUI gui, @NotNull InventoryClickEvent event) {
        onClick(gui, (Player) event.getWhoClicked());
    }

    default void onLeftClick(@NotNull MultiPageGUI gui, @NotNull Player p) {
        onClick(gui, p);
    }

    default void onRightClick(@NotNull MultiPageGUI gui, @NotNull Player p) {
        onClick(gui, p);
    }
}
