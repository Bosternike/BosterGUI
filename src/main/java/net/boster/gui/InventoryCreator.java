package net.boster.gui;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@AllArgsConstructor
public abstract class InventoryCreator {

    @Getter @Setter @Nullable protected String title;

    @NotNull public abstract Inventory getGUI(@NotNull GUI holder);
    @NotNull public abstract Inventory getGUI(@NotNull GUI holder, @Nullable String title);

    public abstract int getSize();
}
