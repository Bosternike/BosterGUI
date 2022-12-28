package net.boster.gui.utils;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface PlaceholdersProvider {

    @NotNull String applyPlaceholders(@NotNull Player player, @NotNull String s);
}
