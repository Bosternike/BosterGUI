package net.boster.gui.utils;

import net.boster.gui.button.GUIButton;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ButtonUtils {

    public static <T extends GUIButton> T load(@Nullable ConfigurationSection item, @NotNull Class<T> clazz) {
        if(item == null) return null;

        try {
            try {
                return clazz.getDeclaredConstructor(ConfigurationSection.class).newInstance(item);
            } catch (ReflectiveOperationException e) {
                return clazz.getDeclaredConstructor().newInstance();
            }
        } catch (Throwable e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T extends GUIButton> @NotNull List<T> loadAll(@NotNull ConfigurationSection section, @NotNull Class<T> clazz) {
        List<T> list = new ArrayList<>();

        for(String item : section.getKeys(false)) {
            ConfigurationSection b = section.getConfigurationSection(item);
            if(b != null) {
                list.add(load(b, clazz));
            }
        }

        return list;
    }

    public static @NotNull List<Integer> getSlots(@NotNull ConfigurationSection section) {
        List<Integer> slots = section.getIntegerList("slots");
        if(section.get("slot") != null) {
            slots.add(section.getInt("slot"));
        }
        return slots;
    }
}
