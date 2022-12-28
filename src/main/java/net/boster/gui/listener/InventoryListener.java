package net.boster.gui.listener;

import net.boster.gui.CustomGUI;
import net.boster.gui.multipage.MultiPageGUI;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class InventoryListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClose(InventoryCloseEvent e) {
        CustomGUI gui = CustomGUI.get(e.getInventory());
        if(gui != null) {
            gui.onClose(e);
            return;
        }

        MultiPageGUI mpg = MultiPageGUI.get(e.getInventory());
        if(mpg != null) {
            mpg.onClose(e);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onClick(InventoryClickEvent e) {
        CustomGUI gui = CustomGUI.get(e.getInventory());
        if(gui != null && !gui.isClosed()) {
            gui.onClick(e);
            return;
        }

        MultiPageGUI mpg = MultiPageGUI.get(e.getInventory());
        if(mpg != null && !mpg.isClosed()) {
            mpg.onClick(e);
        }
    }
}
