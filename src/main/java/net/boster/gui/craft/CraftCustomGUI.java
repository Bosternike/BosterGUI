package net.boster.gui.craft;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.InventoryCreator;
import net.boster.gui.button.GUIButton;
import org.bukkit.Bukkit;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

@Getter
@Setter
public class CraftCustomGUI {

    private final HashMap<Integer, GUIButton> buttons = new HashMap<>();

    @NotNull private InventoryCreator creator;

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    public CraftCustomGUI(int size, @Nullable String title) {
        if(!setCreator(title, size)) {
            this.creator = new CraftSizedGUI(title, 54);
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
        this(54, title);
    }

    public CraftCustomGUI() {
        this(54, null);
    }

    public @NotNull Inventory getGUI(@Nullable String title) {
        return creator.getGUI(title);
    }

    public @NotNull Inventory getGUI() {
        return creator.getGUI();
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

    public int getSize() {
        return creator.getSize();
    }

    public void setTitle(@Nullable String title) {
        creator.setTitle(title);
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

    private boolean setCreator(String title, int size) {
        try {
            Bukkit.createInventory(null, size);
            this.creator = new CraftSizedGUI(title, size);
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
