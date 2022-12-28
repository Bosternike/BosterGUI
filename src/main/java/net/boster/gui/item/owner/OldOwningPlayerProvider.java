package net.boster.gui.item.owner;

import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class OldOwningPlayerProvider implements OwningPlayerProvider {

    @Override
    public void setOwner(@NotNull SkullMeta meta, @NotNull String player) {
        meta.setOwner(player);
    }
}
