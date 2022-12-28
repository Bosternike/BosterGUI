package net.boster.gui.item.creator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class NewItemCreator implements ItemCreator {

    @Override
    public @NotNull ItemStack createItem(@NotNull String s) {
        return new ItemStack(Material.valueOf(s));
    }
}
