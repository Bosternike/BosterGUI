package net.boster.gui.multipage;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface MultiPageFunctionalEntry {

    @Nullable ItemStack item(@NotNull Player p, int page, int slot);

    void onClick(@NotNull MultiPageGUI gui, @NotNull InventoryClickEvent event);
}
