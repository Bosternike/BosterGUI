package net.boster.gui.item.unbreakable;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public interface UnbreakableProvider {

    void setUnbreakable(@NotNull ItemMeta meta, boolean b);
}
