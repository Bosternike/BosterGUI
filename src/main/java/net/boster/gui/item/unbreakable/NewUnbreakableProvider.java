package net.boster.gui.item.unbreakable;

import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NewUnbreakableProvider implements UnbreakableProvider {

    @Override
    public void setUnbreakable(@NotNull ItemMeta meta, boolean b) {
        meta.setUnbreakable(true);
    }
}
