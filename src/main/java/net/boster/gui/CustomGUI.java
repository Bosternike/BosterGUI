package net.boster.gui;

import lombok.Getter;
import lombok.Setter;
import net.boster.gui.button.GUIButton;
import net.boster.gui.craft.CraftCustomGUI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class CustomGUI implements GUI {

    public static final HashMap<Player, CustomGUI> viewers = new HashMap<>();

    @NotNull protected final Player player;
    @NotNull protected Inventory inventory;
    @NotNull protected final CraftCustomGUI gui;

    @NotNull private Map<String, Object> data = new HashMap<>();

    protected BukkitTask closedTask;
    protected boolean closed = false;

    @NotNull private InventoryClickActions clickActions = new InventoryClickActions();

    public CustomGUI(@NotNull Player p, @NotNull CraftCustomGUI gui) {
        viewers.put(p, this);
        this.player = p;
        this.gui = gui;
        this.inventory = gui.getGUI();
    }

    public CustomGUI(@NotNull Player p, @NotNull String title, int size) {
        this(p, new CraftCustomGUI(size, title));
    }

    public CustomGUI(@NotNull Player p, @NotNull String title) {
        this(p, new CraftCustomGUI(title));
    }

    public CustomGUI(@NotNull Player p, int size) {
        this(p, new CraftCustomGUI(size));
    }

    public CustomGUI(@NotNull Player p) {
        this(p, new CraftCustomGUI());
    }

    public static CustomGUI get(@NotNull Player p) {
        return viewers.get(p);
    }

    public static @Nullable CustomGUI get(@NotNull Inventory inv) {
        for(CustomGUI g : viewers.values()) {
            if(g.inventory == inv) return g;
        }

        return null;
    }

    public void open() {
        open(gui.getTitle());
    }

    public void open(@Nullable String title) {
        setClosed(1);
        inventory = gui.getGUI(title);
        fillGUI();
        player.openInventory(inventory);
    }

    public void fillGUI() {
        for (GUIButton i : gui.getButtons()) {
            inventory.setItem(i.getSlot(), i.prepareItem(player));
        }
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

        e.setCancelled(!gui.checkAccess(e.getSlot()));
        GUIButton b = gui.getButton(e.getSlot());

        if(b != null) {
            b.onClick(this, e);
        }
    }

    public void onPlayerInventoryClick(@NotNull InventoryClickEvent e) {
        if(clickActions.getOnPlayerInventoryClick() == null) {
            e.setCancelled(!gui.accessPlayerInventory);
        } else {
            clickActions.getOnPlayerInventoryClick().accept(e);
        }
    }

    public void onClose(@NotNull InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        Bukkit.getScheduler().runTaskLater(BosterGUI.getProvider(), () -> {
            if(!closed && inventory != p.getOpenInventory().getTopInventory()) {
                clear();
            }
        }, 1);
    }

    public void clear() {
        viewers.remove(player);
    }

    @Override
    public @NotNull Map<Integer, GUIButton> getButtons() {
        return gui.getButtonMap();
    }

    @Override
    public int getSize() {
        return gui.getSize();
    }
}
