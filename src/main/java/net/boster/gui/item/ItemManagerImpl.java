package net.boster.gui.item;

import net.boster.gui.item.durability.DurabilityProvider;
import net.boster.gui.item.durability.NewDurabilityProvider;
import net.boster.gui.item.durability.OldDurabilityProvider;
import net.boster.gui.item.creator.ItemCreator;
import net.boster.gui.item.creator.NewItemCreator;
import net.boster.gui.item.creator.OldItemCreator;
import net.boster.gui.item.custommodeldata.CustomModelDataProvider;
import net.boster.gui.item.custommodeldata.NewCustomModelDataProvider;
import net.boster.gui.item.custommodeldata.OldCustomModelDataProvider;
import net.boster.gui.item.owner.NewOwningPlayerProvider;
import net.boster.gui.item.owner.OldOwningPlayerProvider;
import net.boster.gui.item.owner.OwningPlayerProvider;
import net.boster.gui.item.unbreakable.NewUnbreakableProvider;
import net.boster.gui.item.unbreakable.OldUnbreakableProvider;
import net.boster.gui.item.unbreakable.UnbreakableProvider;
import net.boster.gui.utils.Version;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.NotNull;

public class ItemManagerImpl implements ItemManager {

    private final ItemCreator creator;
    private final CustomModelDataProvider customModelDataProvider;
    private final OwningPlayerProvider owningPlayerProvider;
    private final UnbreakableProvider unbreakableProvider;
    private final DurabilityProvider durabilityProvider;

    public ItemManagerImpl() {
        if(Version.v1_13_R2.isCurrentUpToDate()) {
            this.creator = new NewItemCreator();
        } else {
            this.creator = new OldItemCreator();
        }

        if(Version.v1_14_R1.isCurrentUpToDate()) {
            this.customModelDataProvider = new NewCustomModelDataProvider();
        } else {
            this.customModelDataProvider = new OldCustomModelDataProvider();
        }

        if(Version.v1_12_R1.isCurrentUpToDate()) {
            this.owningPlayerProvider = new NewOwningPlayerProvider();
        } else {
            this.owningPlayerProvider = new OldOwningPlayerProvider();
        }

        if(Version.v1_11_R1.isCurrentUpToDate()) {
            this.unbreakableProvider = new NewUnbreakableProvider();
        } else {
            this.unbreakableProvider = new OldUnbreakableProvider();
        }

        if(Version.v1_13_R2.isCurrentUpToDate()) {
            this.durabilityProvider = new NewDurabilityProvider();
        } else {
            this.durabilityProvider = new OldDurabilityProvider();
        }
    }

    @Override
    public @NotNull ItemStack createItem(@NotNull String s) {
        return creator.createItem(s);
    }

    @Override
    public void setCustomModelData(@NotNull ItemMeta meta, int i) {
        customModelDataProvider.setCustomModelData(meta, i);
    }

    @Override
    public void setOwner(@NotNull SkullMeta meta, @NotNull String player) {
        owningPlayerProvider.setOwner(meta, player);
    }

    @Override
    public void setUnbreakable(@NotNull ItemMeta meta, boolean b) {
        unbreakableProvider.setUnbreakable(meta, b);
    }

    @Override
    public void setDurability(@NotNull ItemStack itemStack, @NotNull ItemMeta meta, int damage) {
        durabilityProvider.setDurability(itemStack, meta, damage);
    }
}
