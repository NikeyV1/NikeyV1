package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.BleedEffect;
import org.bukkit.BanList;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerChangedMainHandEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Random;

@SuppressWarnings("ALL")
public class InfernoBlade implements Listener {

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item.getType() == Material.FIREWORK_STAR && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // Getting the lore of the item
                List<String> lore = meta.getLore();
                // Checking if lore contains the keyword "Combined"
                if (lore != null ) {
                    String l = String.valueOf(lore);
                    if (l.equalsIgnoreCase("[§fThe combined power of all §8stones]")) {
                        ChatColor currentColor = ChatColor.getByChar(meta.getDisplayName().charAt(1));
                        ChatColor newColor = getRandomColor(currentColor);
                        meta.setDisplayName(newColor + "Elemental Stone");
                        item.setItemMeta(meta);
                        player.sendMessage("The sword's color has changed to " + newColor + "!");
                    }
                }
            }
        }
    }

    private ChatColor getRandomColor(ChatColor currentColor) {
        ChatColor[] colors = {ChatColor.RED, ChatColor.GREEN, ChatColor.BLUE, ChatColor.YELLOW, ChatColor.AQUA, ChatColor.LIGHT_PURPLE};
        Random random = new Random();
        ChatColor newColor = colors[random.nextInt(colors.length)];
        // Ensure the new color is different from the current color
        while (newColor == currentColor) {
            newColor = colors[random.nextInt(colors.length)];
        }
        return newColor;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity d = event.getDamager();
        if (d instanceof Player) {
            Player damager = (Player) d;
            if (damager.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD && damager.getInventory().getItemInMainHand().getItemMeta().hasLore()) {

            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (player.getKiller() != null ) {
            Player killer = player.getKiller();
            if (killer.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD && killer.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                BleedEffect effect = new BleedEffect(NikeyV1.em);
                effect.setLocation(player.getLocation());
                effect.iterations =0;
                effect.start();
                player.kickPlayer("§cYour are banned by "+killer.getName()+" useing the Inferno Blade");
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "§cYour stone is out of strength!",null,"Game");
            }
        }
    }
}
