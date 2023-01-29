package net.boster.gui.button;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.boster.gui.GUI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Getter
@Setter
@RequiredArgsConstructor
public class ClickableButton extends SimpleButtonItem implements GUIButton {

    private final int slot;

    @Nullable private Consumer<Player> clickAction;
    @Nullable private Consumer<ButtonClickData> initialClickAction;

    public ClickableButton(@NotNull ConfigurationSection section) {
        super(section);

        slot = section.getInt("slot");
    }

    public void onClick(@NotNull Player p) {
        if(clickAction != null) {
            clickAction.accept(p);
        }
    }

    public void onClick(@NotNull GUI gui, @NotNull InventoryClickEvent event) {
        if(initialClickAction != null) {
            initialClickAction.accept(new ButtonClickData(gui, event));
        } else {
            onClick((Player) event.getWhoClicked());
        }
    }

    @Data
    public static class ButtonClickData {
        @NotNull private final GUI gui;
        @NotNull private final InventoryClickEvent event;
    }
}
