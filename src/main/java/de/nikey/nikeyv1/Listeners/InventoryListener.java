package de.nikey.nikeyv1.Listeners;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

public class InventoryListener implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory top = event.getView().getTopInventory();
        Inventory bottom = event.getView().getBottomInventory();
        InventoryAction action = event.getAction();
        if (action ==InventoryAction.HOTBAR_SWAP||action == InventoryAction.HOTBAR_MOVE_AND_READD ) {
            if (event.getCurrentItem() == null || event.getCurrentItem().getType() == Material.AIR)event.setCancelled(true);
        }
        if (event.getCurrentItem().getType() == Material.FIREWORK_STAR && event.getCurrentItem().getItemMeta().hasLore()) {
            String[] arr = event.getCurrentItem().getLore().get(1).split(":");
            String a = arr[0];
            if (a.equalsIgnoreCase(ChatColor.of("#00FFAA")+"Level") && !(top.getType() == InventoryType.CRAFTING)){
                if (!event.getView().getTitle().equalsIgnoreCase("Enchanted Anvil")) {
                    event.setCancelled(true);
                }
                if (event.getInventory().getType() == InventoryType.ENDER_CHEST) {
                    event.getCurrentItem().setAmount(0);
                }
            }
        }
    }
}
