package net.boster.gui.item.durability;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public interface DurabilityProvider {

    void setDurability(@NotNull ItemStack itemStack, @NotNull ItemMeta meta, int damage);
}
