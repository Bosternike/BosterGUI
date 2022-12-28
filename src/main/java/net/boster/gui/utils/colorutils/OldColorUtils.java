package net.boster.gui.utils.colorutils;

import net.md_5.bungee.api.ChatColor;
import org.jetbrains.annotations.NotNull;

public class OldColorUtils implements ColorUtils {

    @Override
    public @NotNull String toColor(@NotNull String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
