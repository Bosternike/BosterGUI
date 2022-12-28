package net.boster.gui.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.boster.gui.utils.colorutils.ColorUtils;
import net.boster.gui.utils.colorutils.NewColorUtils;
import net.boster.gui.utils.colorutils.OldColorUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.util.UUID;

public class GUIUtils {

    private static ColorUtils colorUtils;
    public static ItemStack SKULL;

    static {
        try {
            ChatColor.class.getMethod("of", String.class);
            colorUtils = new NewColorUtils();
        } catch (NoSuchMethodException e) {
            colorUtils = new OldColorUtils();
        }

        try {
            SKULL = new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        } catch (Exception e) {
            SKULL = new ItemStack(Material.valueOf("PLAYER_HEAD"));
        }
    }

    @Contract("!null -> !null")
    public static String toColor(@Nullable String s) {
        if(s == null) return null;

        return colorUtils.toColor(s);
    }

    public static int menuPages(int values, int slots) {
        int pages;
        if(values <= slots) {
            pages = 1;
        } else {
            int sum = values / slots;
            pages = sum;
            if(values > slots * sum) {
                pages++;
            }
        }
        return pages;
    }

    public static ItemStack getCustomSkull(String value) {
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), null);
        gameProfile.getProperties().put("textures", new Property("textures", value));

        ItemStack skull = SKULL.clone();
        SkullMeta skullMeta = (SkullMeta) skull.getItemMeta();
        Field profileField;
        try {
            profileField = skullMeta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(skullMeta, gameProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        skull.setItemMeta(skullMeta);
        return skull;
    }
}
