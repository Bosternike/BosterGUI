package net.boster.gui;

import lombok.Getter;
import net.boster.gui.button.GUIButton;
import net.boster.gui.craft.CraftCustomGUI;
import net.boster.gui.utils.GUIUtils;
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
import java.util.logging.Level;

public class CustomGUI implements GUI {

    public static final HashMap<Player, CustomGUI> openedMap = new HashMap<>();

    @Getter @NotNull protected final Player player;
    @Getter @NotNull protected Inventory inventory;
    @Getter @NotNull protected final CraftCustomGUI gui;

    protected BukkitTask closedTask;
    @Getter protected boolean closed = false;

    public CustomGUI(@NotNull Player p, @NotNull CraftCustomGUI gui) {
        openedMap.put(p, this);
        this.player = p;
        this.gui = gui;
        this.inventory = gui.getGUI(this);
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

    public static CustomGUI get(Player p) {
        return openedMap.get(p);
    }

    public void open() {
        open(gui.getTitle());
    }

    public void open(@Nullable String title) {
        setClosed(1);
        inventory = gui.getGUI(this, title);
        fillGUI();
        player.openInventory(inventory);
        gui.onOpen(player);
    }

    public void fillGUI() {
        for(GUIButton i : gui.getButtons()) {
            inventory.setItem(i.getSlot(), i.prepareItem(player));
        }
    }

    public void setClosed(boolean b) {
        closed = b;
    }

    public void setClosed(int ticks) {
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

    public void onClick(InventoryClickEvent e) {
        if(closed) return;

        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory().getHolder() instanceof Player) {
            e.setCancelled(!gui.accessPlayerInventory);
            return;
        }

        e.setCancelled(!gui.checkAccess(e.getSlot()));
        GUIButton b = gui.getButton(e.getSlot());

        if(b != null) {
            if(e.isRightClick()) {
                b.onRightClick(p);
            } else {
                b.onLeftClick(p);
            }
        }
    }

    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        Bukkit.getScheduler().runTaskLater(BosterGUI.getProvider(), () -> {
            if(!closed && inventory != p.getOpenInventory().getTopInventory()) {
                gui.onClose(p);
                clear();
            }
        }, 1);
    }

    public void clear() {
        openedMap.remove(player);
    }

    @Override
    public void log(@NotNull String s, @NotNull Level log) {
        Bukkit.getLogger().log(log, GUIUtils.toColor("[BosterGUI] (CustomGUI): " + s));
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
