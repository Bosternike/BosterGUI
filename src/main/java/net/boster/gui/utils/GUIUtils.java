package net.boster.gui.utils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.Nullable;

public class GUIUtils {

    @Contract("!null -> !null")
    public static String toColor(@Nullable String s) {
        if(s == null) return null;

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static int menuPages(int values, int from, int to) {
        return menuPages(values, to - from);
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

    public static int getGUIFirstSlotOfLastRow(int slots) {
        if(slots < 9) {
            return 0;
        } else {
            return slots - 9;
        }
    }

    public static int[] createBorder(int slots) {
        if(slots == 27) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26};
        } else if(slots == 36) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 28, 29, 30, 31, 32, 33, 34, 35};
        } else if(slots == 45) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44};
        } else if(slots == 54) {
            return new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 17, 18, 26, 27, 35, 36, 44, 45, 46, 47, 48, 49, 50, 51, 52, 53};
        }

        return new int[]{};
    }
}
