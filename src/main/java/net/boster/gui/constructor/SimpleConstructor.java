package net.boster.gui.constructor;

import net.boster.gui.GUI;
import net.boster.gui.InventoryCreator;
import net.boster.gui.button.ClickableButton;
import net.boster.gui.button.GUIButton;
import net.boster.gui.craft.CraftCustomGUI;
import net.boster.gui.craft.CraftSizedGUI;
import net.boster.gui.craft.CraftTypedGUI;
import net.boster.gui.utils.ButtonUtils;
import net.boster.gui.utils.GUIUtils;
import net.boster.gui.utils.PlaceholdersProvider;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.function.Function;

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
    }

    public <T extends GUIButton> void loadItems(@NotNull ConfigurationSection items, @NotNull Class<T> clazz) {
        loadItems(items, clazz, null);
    }

    public <T extends GUIButton> void loadItems(@NotNull ConfigurationSection items, @NotNull Class<T> clazz, @Nullable PlaceholdersProvider replacer) {
        loadItems(items, c -> {
            GUIButton i = ButtonUtils.load(c, clazz);

            if(replacer != null && i instanceof ClickableButton) {
                ((ClickableButton) i).setPlaceholdersProvider(p -> replacer);
            }

            return i;
        });
    }

    public void loadItems(@NotNull ConfigurationSection items, @NotNull Function<ConfigurationSection, GUIButton> function) {
        for(String s : items.getKeys(false)) {
            ConfigurationSection c = items.getConfigurationSection(s);
            if(c == null) continue;

            GUIButton i = Objects.requireNonNull(function.apply(c));

            for(Integer slot : ButtonUtils.getSlots(c)) {
                craftGUI.addButton(new GUIButton() {
                    @Override
                    public int getSlot() {
                        return slot;
                    }

                    public void onClick(@NotNull GUI gui, @NotNull InventoryClickEvent event) {
                        i.onClick(gui, event);
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
