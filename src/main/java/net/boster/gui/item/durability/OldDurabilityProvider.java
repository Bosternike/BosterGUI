package net.boster.gui.item.durability;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class OldDurabilityProvider implements DurabilityProvider {

    @Override
    public void setDurability(@NotNull ItemStack itemStack, @NotNull ItemMeta meta, int damage) {
        itemStack.setDurability((short) damage);
    }
}
