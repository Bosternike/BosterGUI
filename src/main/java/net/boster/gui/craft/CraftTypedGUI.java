package net.boster.gui.craft;

import lombok.Getter;
import net.boster.gui.GUI;
import net.boster.gui.InventoryCreator;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CraftTypedGUI extends InventoryCreator {

    private final InventoryType type;
    @Getter private final int size;

    public CraftTypedGUI(@Nullable String title, @NotNull InventoryType type) {
        super(title);
        this.type = type;
        this.size = Bukkit.createInventory(null, type).getSize();
    }

    @Override
    public @NotNull Inventory getGUI(@NotNull GUI holder) {
        return getGUI(holder, title);
    }

    @Override
    public @NotNull Inventory getGUI(@NotNull GUI holder, @Nullable String title) {
        if(title != null) {
            return Bukkit.createInventory(holder, type, title);
        } else {
            return Bukkit.createInventory(holder, type);
        }
    }
}
