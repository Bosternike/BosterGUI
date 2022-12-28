package net.boster.gui.item.durability;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class NewDurabilityProvider implements DurabilityProvider {

    @Override
    public void setDurability(@NotNull ItemStack itemStack, @NotNull ItemMeta meta, int damage) {
        if(meta instanceof Damageable) {
            ((Damageable) meta).setDamage(damage);
        }
    }
}
