package de.nikey.nikeyv1.Listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

@SuppressWarnings("deprecation")
public class InventoryListener implements Listener {
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        InventoryAction action = event.getAction();
        if (event.getCurrentItem() != null) {
            if (event.getCurrentItem().getType() == Material.FIREWORK_STAR && event.getCurrentItem().getItemMeta().hasLore()) {
                if (!(top.getType() == InventoryType.CRAFTING)){
                    if (!event.getView().getTitle().equalsIgnoreCase("Enchanted Anvil")) {
                        event.setCancelled(true);
                    }
                    if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}