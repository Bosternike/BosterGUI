package net.boster.gui.craft;

import lombok.Getter;
import net.boster.gui.GUI;
import net.boster.gui.InventoryCreator;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftSizedGUI extends InventoryCreator {

    @Getter private final int size;

    public CraftSizedGUI(@Nullable String title, int size) {
        super(title);
        this.size = size;
    }

    @Override
    public @NotNull Inventory getGUI(@NotNull GUI holder) {
        return getGUI(holder, title);
    }

    @Override
    public @NotNull Inventory getGUI(@NotNull GUI holder, @Nullable String title) {
        if(title != null) {
            return Bukkit.createInventory(holder, size, title);
        } else {
            return Bukkit.createInventory(holder, size);
        }
    }
}
