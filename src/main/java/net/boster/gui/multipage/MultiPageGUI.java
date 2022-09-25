package net.boster.gui.multipage;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.BosterGUI;
import net.boster.gui.GUI;
import net.boster.gui.InventoryCreator;
import net.boster.gui.button.GUIButton;
import net.boster.gui.craft.CraftSizedGUI;
import net.boster.gui.craft.CraftTypedGUI;
import net.boster.gui.utils.GUIUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;

public class MultiPageGUI implements GUI {

    private static final HashMap<Player, MultiPageGUI> user = new HashMap<>();

    @Getter @Setter @NotNull private List<MultiPageButton> nextButtons = new ArrayList<>();
    @Getter @Setter @NotNull private List<MultiPageButton> previousButtons = new ArrayList<>();

    @Getter @NotNull private final Player player;
    @Getter @Setter @NotNull private InventoryCreator creator;
    @Getter @NotNull private Inventory inventory;

    @Getter @Setter private int pages;
    @Getter private int pageNumber = 1;
    @Getter private int actualFrom = 0;
    @Getter @Setter @NotNull private List<Integer> slots = new ArrayList<>();

    @Getter @Setter @NotNull private Map<Integer, GUIButton> buttons = new HashMap<>();

    @Getter @Setter @NotNull private List<MultiPageFunctionalEntry> items = new ArrayList<>();
    @Getter @Setter @NotNull private Map<Integer, MultiPageFunctionalEntry> currentItems = new LinkedHashMap<>();

    @Getter @Setter @NotNull private Map<String, Object> data = new HashMap<>();

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    private BukkitTask closedTask;
    @Getter private boolean closed = false;

    public MultiPageGUI(@NotNull Player player) {
        this.player = player;
        this.creator = new CraftSizedGUI(null, 9);
        this.inventory = creator.getGUI(this);

        user.put(player, this);
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

    public void setType(@NotNull InventoryType type) {
        creator = new CraftTypedGUI(getTitle(), type);
    }

    public static @Nullable MultiPageGUI get(Player p) {
        return user.get(p);
    }

    public boolean newPage() {
        if(pageNumber < pages) {
            pageNumber = pageNumber + 1;
            actualFrom = actualFrom + slots.size();
            return true;
        } else {
            return false;
        }
    }

    public boolean pastPage() {
        if(pageNumber > 1) {
            pageNumber = pageNumber - 1;
            actualFrom = actualFrom - slots.size();
            return true;
        } else {
            return false;
        }
    }

    public void prepare() {
        for(GUIButton b : buttons.values()) {
            ItemStack i = b.prepareItem(player);
            if(i != null) {
                inventory.setItem(b.getSlot(), i);
            }
        }
    }

    public void addSwitchers() {
        for(MultiPageButton i : nextButtons) {
            if(i.getAppearance().checkAppear(this, true)) {
                ItemStack item = i.item(player);
                if(item != null) {
                    inventory.setItem(i.getSlot(), item);
                }
            }
        }
        for(MultiPageButton i : previousButtons) {
            if(i.getAppearance().checkAppear(this, false)) {
                ItemStack item = i.item(player);
                if(item != null) {
                    inventory.setItem(i.getSlot(), item);
                }
            }
        }
    }

    public void loadItems() {
        for(int i = 0; i < slots.size(); i++) {
            int slot = slots.get(i);
            if(slot >= inventory.getSize()) continue;
            if(actualFrom + i >= items.size()) break;

            MultiPageFunctionalEntry e = items.get(actualFrom + i);
            inventory.setItem(slot, e.item(player, pageNumber, slot));
            currentItems.put(slot, e);
        }
    }

    public void open() {
        currentItems.clear();

        inventory = creator.getGUI(this, prepareTitle());
        prepare();
        addSwitchers();
        loadItems();

        setClosed(1);
        player.openInventory(inventory);
    }

    public @Nullable String prepareTitle() {
        return getTitle();
    }

    public @Nullable MultiPageButton isSwitchSlot(@NotNull List<MultiPageButton> buttons, int slot) {
        for(MultiPageButton b : buttons) {
            if(b.getSlot() == slot) {
                return b;
            }
        }

        return null;
    }

    public void setClosed(boolean b) {
        closed = b;
    }

    public void setClosed(int ticks) {
        closed = true;

        if(closedTask != null) {
            closedTask.cancel();
        }

        Bukkit.getScheduler().runTaskLater(BosterGUI.getProvider(), () -> {
            closed = false;
            closedTask = null;
            if(!player.isOnline()) {
                clear();
            }
        }, ticks);
    }

    public void clear() {
        user.remove(player);
    }

    public boolean checkAccess(int slot) {
        if(accessibleSlots == null) return false;

        return accessibleSlots.contains(slot);
    }

    public void onClick(InventoryClickEvent e) {
        if(e.getClickedInventory().getHolder() instanceof Player) {
            e.setCancelled(!accessPlayerInventory);
            return;
        }

        if(closed) {
            e.setCancelled(true);
            return;
        }

        e.setCancelled(!checkAccess(e.getSlot()));

        MultiPageButton mb = isSwitchSlot(nextButtons, e.getSlot());
        if(mb == null) {
            mb = isSwitchSlot(previousButtons, e.getSlot());
        }
        if(mb != null) {
            mb.performPage(this);
            mb.onClick(this, e);
            return;
        }

        MultiPageFunctionalEntry me = currentItems.get(e.getSlot());
        if(me != null) {
            mb.onClick(this, e);
            return;
        }

        GUIButton gb = buttons.get(e.getSlot());
        if(gb != null) {
            gb.onClick(this, e);
        }
    }

    public void onClose(InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(BosterGUI.getProvider(), () -> {
            if(!closed && inventory != player.getOpenInventory().getTopInventory()) {
                clear();
            }
        }, 1);
    }

    @Override
    public void log(@NotNull String s, @NotNull Level log) {
        Bukkit.getLogger().log(log, GUIUtils.toColor("[BosterGUI] (MultiPageGUI): " + s));
    }
}
