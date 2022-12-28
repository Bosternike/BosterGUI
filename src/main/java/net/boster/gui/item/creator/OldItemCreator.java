package net.boster.gui.item.creator;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class OldItemCreator implements ItemCreator {

    @Override
    public @NotNull ItemStack createItem(@NotNull String s) {
        try {
            String[] ss = s.split(":");
            return new ItemStack(Material.valueOf(ss[0]), 1, (short) Integer.parseInt(ss[1]));
        } catch (IndexOutOfBoundsException e) {
            return new ItemStack(Material.valueOf(s));
        }
    }
}
