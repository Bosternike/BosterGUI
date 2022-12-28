package net.boster.gui.item;

import net.boster.gui.item.creator.ItemCreator;
import net.boster.gui.item.custommodeldata.CustomModelDataProvider;
import net.boster.gui.item.durability.DurabilityProvider;
import net.boster.gui.item.owner.OwningPlayerProvider;
import net.boster.gui.item.unbreakable.UnbreakableProvider;

public interface ItemManager extends ItemCreator, CustomModelDataProvider, OwningPlayerProvider, UnbreakableProvider, DurabilityProvider {
}
