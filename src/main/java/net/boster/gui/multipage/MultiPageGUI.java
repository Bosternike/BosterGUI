package net.boster.gui.multipage;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.BosterGUI;
import net.boster.gui.GUI;
import net.boster.gui.InventoryClickActions;
import net.boster.gui.InventoryCreator;
import net.boster.gui.button.GUIButton;
import net.boster.gui.craft.CraftCustomGUI;
import net.boster.gui.craft.CraftSizedGUI;
import net.boster.gui.craft.CraftTypedGUI;
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

@Getter
@Setter
public class MultiPageGUI implements GUI {

    private static final HashMap<Player, MultiPageGUI> viewers = new HashMap<>();

    @NotNull private List<MultiPageButton> nextButtons = new ArrayList<>();
    @NotNull private List<MultiPageButton> previousButtons = new ArrayList<>();

    @NotNull private final Player player;
    @NotNull private InventoryCreator creator;
    @NotNull private Inventory inventory;

    private int pages;
    private int pageNumber = 1;
    private int actualFrom = 0;
    @NotNull private List<Integer> slots = new ArrayList<>();

    @NotNull private Map<Integer, GUIButton> buttons = new HashMap<>();

    @NotNull private List<MultiPageFunctionalEntry> items = new ArrayList<>();
    @NotNull private Map<Integer, MultiPageFunctionalEntry> currentItems = new LinkedHashMap<>();

    @NotNull private Map<String, Object> data = new HashMap<>();

    public boolean accessPlayerInventory = false;
    public List<Integer> accessibleSlots = new ArrayList<>();

    private BukkitTask closedTask;
    private boolean closed = false;

    @NotNull private InventoryClickActions clickActions = new InventoryClickActions();

    public MultiPageGUI(@NotNull Player player) {
        this.player = player;
        this.creator = new CraftSizedGUI(null, 9);
        this.inventory = creator.getGUI();

        viewers.put(player, this);
    }

    public MultiPageGUI(@NotNull Player player, @NotNull CraftCustomGUI gui) {
        this.player = player;
        this.creator = gui.getCreator();
        this.inventory = creator.getGUI();

        buttons.putAll(gui.getButtonMap());
        accessPlayerInventory = gui.accessPlayerInventory;
        accessibleSlots.addAll(gui.accessibleSlots);

        viewers.put(player, this);
    }

    public static @Nullable MultiPageGUI get(@NotNull Player p) {
        return viewers.get(p);
    }

    public static @Nullable MultiPageGUI get(@NotNull Inventory inv) {
        for(MultiPageGUI g : viewers.values()) {
            if(g.inventory == inv) return g;
        }

        return null;
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

    public boolean newPage() {
        if(pageNumber < pages) {
            pageNumber = pageNumber + 1;
            actualFrom = actualFrom + currentItems.size();
            return true;
        } else {
            return false;
        }
    }

    public boolean pastPage() {
        if(pageNumber > 1) {
            pageNumber = pageNumber - 1;
            actualFrom = actualFrom - currentItems.size();
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
            if(i.getAppearance().shouldAppear(this, true)) {
                ItemStack item = i.item(player);
                if(item != null) {
                    inventory.setItem(i.getSlot(), item);
                }
            }
        }
        for(MultiPageButton i : previousButtons) {
            if(i.getAppearance().shouldAppear(this, false)) {
                ItemStack item = i.item(player);
                if(item != null) {
                    inventory.setItem(i.getSlot(), item);
                }
            }
        }
    }

    public void loadItems() {
        int d = 0;
        for (int slot : slots) {
            if (slot >= inventory.getSize()) continue;
            if (actualFrom + d >= items.size()) break;

            MultiPageFunctionalEntry e = items.get(actualFrom + d);
            inventory.setItem(slot, e.item(player, pageNumber, slot));
            currentItems.put(slot, e);
            d++;
        }
    }

    public void open() {
        currentItems.clear();

        inventory = creator.getGUI(prepareTitle());
        prepare();
        addSwitchers();
        loadItems();

        setClosed(1);
        player.openInventory(inventory);
    }

    public void update() {
        prepare();
        addSwitchers();
        loadItems();
    }

    public @Nullable String prepareTitle() {
        return getTitle();
    }

    public @Nullable MultiPageButton getSwitchSlot(@NotNull List<MultiPageButton> buttons, int slot) {
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

        closedTask = Bukkit.getScheduler().runTaskLater(BosterGUI.getProvider(), () -> {
            closed = false;
            closedTask = null;
            if(!player.isOnline()) {
                clear();
            }
        }, ticks);
    }

    public void clear() {
        viewers.remove(player);
    }

    public boolean checkAccess(int slot) {
        if(accessibleSlots == null) return false;

        return accessibleSlots.contains(slot);
    }

    public void onClick(@NotNull InventoryClickEvent e) {
        if(e.getClickedInventory() == null) {
            if(clickActions.getOutOfInventoryClick() != null) {
                clickActions.getOutOfInventoryClick().accept(e);
            }
            return;
        }

        if(e.getClickedInventory().getHolder() instanceof Player) {
            onPlayerInventoryClick(e);
            return;
        }

        e.setCancelled(!checkAccess(e.getSlot()));

        MultiPageButton mb = getSwitchSlot(nextButtons, e.getSlot());
        if(mb == null) {
            mb = getSwitchSlot(previousButtons, e.getSlot());
        }
        if(mb != null) {
            mb.performPage(this);
            mb.onClick(this, e);
            return;
        }

        MultiPageFunctionalEntry me = currentItems.get(e.getSlot());
        if(me != null) {
            me.onClick(this, e);
            return;
        }

        GUIButton gb = buttons.get(e.getSlot());
        if(gb != null) {
            gb.onClick(this, e);
        }
    }

    public void onPlayerInventoryClick(@NotNull InventoryClickEvent e) {
        if(clickActions.getOnPlayerInventoryClick() == null) {
            e.setCancelled(!accessPlayerInventory);
        } else {
            clickActions.getOnPlayerInventoryClick().accept(e);
        }
    }

    public void onClose(@NotNull InventoryCloseEvent e) {
        Bukkit.getScheduler().runTaskLater(BosterGUI.getProvider(), () -> {
            if(!closed && inventory != player.getOpenInventory().getTopInventory()) {
                clear();
            }
        }, 1);
    }
}
