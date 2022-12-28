package net.boster.gui.item.creator;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface ItemCreator {

    @NotNull ItemStack createItem(@NotNull String s);
}
