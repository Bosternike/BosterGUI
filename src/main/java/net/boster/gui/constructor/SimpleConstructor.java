package net.boster.gui.constructor;

import net.boster.gui.InventoryCreator;
import net.boster.gui.button.GUIButton;
import net.boster.gui.button.SimpleButtonItem;
import net.boster.gui.craft.CraftCustomGUI;
import net.boster.gui.craft.CraftSizedGUI;
import net.boster.gui.craft.CraftTypedGUI;
import net.boster.gui.utils.ButtonUtils;
import net.boster.gui.utils.GUIUtils;
import net.boster.gui.utils.PlaceholdersProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SimpleConstructor {

    @Nullable protected String title;
    @NotNull protected InventoryCreator inventoryCreator;
    @NotNull protected final CraftCustomGUI craftGUI;

    public SimpleConstructor(@NotNull ConfigurationSection section) {
        title = GUIUtils.toColor(section.getString("Title"));
        if(section.get("Size") != null) {
            int si;
            try {
                si = section.getInt("Size");
                Bukkit.createInventory(null, si);
            } catch (Exception e) {
                si = 45;
            }
            inventoryCreator = new CraftSizedGUI(title, section.getInt("Size"));
        } else if(section.get("Type") != null) {
            try {
                inventoryCreator = new CraftTypedGUI(title, InventoryType.valueOf(section.getString("Type")));
            } catch (Exception ignored) {
                inventoryCreator = new CraftSizedGUI(title, 54);
            }
        } else {
            inventoryCreator = new CraftSizedGUI(title, 54);
        }

        craftGUI = new CraftCustomGUI();
        craftGUI.setCreator(inventoryCreator);

        ConfigurationSection items = section.getConfigurationSection("Items");
        if(items != null) {
            loadItems(items, (PlaceholdersProvider) null);
        }
    }

    public <T extends GUIButton> void loadItems(@NotNull ConfigurationSection items, @NotNull Class<T> clazz) {
        for(T t : ButtonUtils.loadAll(items, clazz)) {
            craftGUI.addButton(t);
        }
    }

    public void loadItems(@NotNull ConfigurationSection items, @Nullable PlaceholdersProvider replacer) {
        for(String s : items.getKeys(false)) {
            ConfigurationSection c = items.getConfigurationSection(s);
            if(c == null) continue;

            SimpleButtonItem i = new SimpleButtonItem(c);
            if(replacer != null) {
                i.setPlaceholdersProvider(p -> replacer);
            }

            for(Integer slot : ButtonUtils.getSlots(c)) {
                craftGUI.addButton(new GUIButton() {
                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    @Override
                    public ItemStack prepareItem(@NotNull Player player) {
                        return i.prepareItem(player);
                    }
                });
            }
        }
    }
}
