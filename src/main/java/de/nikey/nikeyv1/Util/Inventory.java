package de.nikey.nikeyv1.Util;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static org.bukkit.Material.ANVIL;

public class Inventory {
    public static void anvilinv(org.bukkit.inventory.Inventory inv){

        //Items
        ItemStack glass = new ItemStack(Material.PURPLE_STAINED_GLASS_PANE);
        ItemStack a = new ItemStack(Material.ANVIL);
        ItemMeta m = a.getItemMeta();
        m.setDisplayName("§dUpgrade");
        a.setItemMeta(m);
        //Invsetup
        inv.clear();
        inv.setItem(3,glass);
        inv.setItem(4,glass);
        inv.setItem(5,glass);
        inv.setItem(12,glass);
        inv.setItem(14,glass);
        inv.setItem(15,a);
        inv.setItem(21,glass);
        inv.setItem(22,glass);
        inv.setItem(23,glass);

    }
}
