package net.boster.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Getter
@Setter
public class InventoryClickActions {

    @Nullable private Consumer<InventoryClickEvent> outOfInventoryClick;
    @Nullable private Consumer<InventoryClickEvent> onPlayerInventoryClick;
}
