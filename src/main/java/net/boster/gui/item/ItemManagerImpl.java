package net.boster.gui.item;

import net.boster.gui.item.durability.DurabilityProvider;
import net.boster.gui.item.durability.NewDurabilityProvider;
import net.boster.gui.item.durability.OldDurabilityProvider;
import net.boster.gui.utils.Version;
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
        if(Version.getCurrentVersion().getVersionInteger() < 9) {
            this.creator = new OldItemCreator();
        } else {
            this.creator = new NewItemCreator();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 10) {
            this.customModelDataProvider = new OldCustomModelDataProvider();
        } else {
            this.customModelDataProvider = new NewCustomModelDataProvider();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 8) {
            this.owningPlayerProvider = new OldOwningPlayerProvider();
        } else {
            this.owningPlayerProvider = new NewOwningPlayerProvider();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 7) {
            this.unbreakableProvider = new OldUnbreakableProvider();
        } else {
            this.unbreakableProvider = new NewUnbreakableProvider();
        }

        if(Version.getCurrentVersion().getVersionInteger() < 9) {
            this.durabilityProvider = new OldDurabilityProvider();
        } else {
            this.durabilityProvider = new NewDurabilityProvider();
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
