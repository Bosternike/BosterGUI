package net.boster.gui.item.owner;

import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public interface OwningPlayerProvider {

    void setOwner(@NotNull SkullMeta meta, @NotNull String player);
}
