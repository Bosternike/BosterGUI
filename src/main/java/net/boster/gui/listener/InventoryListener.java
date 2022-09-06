package net.boster.gui.listener;

import net.boster.gui.GUI;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        Player p = (Player) e.getPlayer();

        if(e.getInventory() == null) return;

        if(e.getInventory().getHolder() instanceof GUI) {
            ((GUI) e.getInventory().getHolder()).onClose(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();

        if(e.getClickedInventory() == null) return;

        if(e.getClickedInventory().getHolder() instanceof GUI) {
            ((GUI) e.getInventory().getHolder()).onClick(e);
        }
    }
}
