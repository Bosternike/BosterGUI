package net.boster.gui.craft;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.GUI;
import net.boster.gui.InventoryCreator;
import net.boster.gui.button.GUIButton;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class CraftCustomGUI {

    private final HashMap<Integer, GUIButton> buttons = new HashMap<>();

    @Getter @Setter @NotNull private InventoryCreator creator;

    @Getter @Setter @Nullable private CraftGUIActions actions;

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    public CraftCustomGUI(int size, @Nullable String title) {
        if(!setSize(size)) {
            this.creator = new CraftSizedGUI(title, 9);
        }
    }

    public CraftCustomGUI(@NotNull InventoryType type, @Nullable String title) {
        this.creator = new CraftTypedGUI(title, type);
    }

    public CraftCustomGUI(@NotNull InventoryType type) {
        this(type, null);
    }

    public CraftCustomGUI(int size) {
        this(size, null);
    }

    public CraftCustomGUI(@Nullable String title) {
        this(9, title);
    }

    public CraftCustomGUI() {
        this(9, null);
    }

    public @NotNull Inventory getGUI(@NotNull GUI holder, @Nullable String title) {
        return creator.getGUI(holder, title);
    }

    public @NotNull Inventory getGUI(@NotNull GUI holder) {
        return creator.getGUI(holder);
    }

    public @Nullable GUIButton getButton(int i) {
        return buttons.get(i);
    }

    public void addButton(GUIButton button) {
        buttons.put(button.getSlot(), button);
    }

    public void removeButton(int i) {
        buttons.remove(i);
    }

    public void setClosed(Player p) {
        onClose(p);
    }

    public void onClose(Player p) {
        if(actions != null) {
            actions.onClose(p);
        }
    }

    public void onOpen(Player p) {
        if(actions != null) {
            actions.onOpen(p);
        }
    }

    public int getSize() {
        return creator.getSize();
    }

    public @Nullable String getTitle() {
        return creator.getTitle();
    }

    public boolean setSize(int i) {
        try {
            Bukkit.createInventory(null, i);
            this.creator = new CraftSizedGUI(getTitle(), i);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public void setType(@NotNull InventoryType type) {
        creator = new CraftTypedGUI(getTitle(), type);
    }

    public Map<Integer, GUIButton> getButtonMap() {
        return buttons;
    }

    public Collection<GUIButton> getButtons() {
        return buttons.values();
    }

    public boolean checkAccess(int slot) {
        if(accessibleSlots == null) return false;

        return accessibleSlots.contains(slot);
    }
}
