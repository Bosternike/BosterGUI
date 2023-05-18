package net.boster.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;
import java.util.function.Function;

@Getter
@Setter
public class InventoryActions {

    @Nullable private Consumer<InventoryClickEvent> outOfInventoryClick;
    @Nullable private Consumer<InventoryClickEvent> onPlayerInventoryClick;
    @Nullable private Function<InventoryCloseEvent, Boolean> onClose;
}
