package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class Stone_Swap implements Listener {
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack mainHand = player.getInventory().getItemInMainHand();
        ItemStack offHand = player.getInventory().getItemInOffHand();
        if (event.getAction() == Action.RIGHT_CLICK_AIR) {
            // Überprüfen, ob der Spieler Efficiency-Verzauberung auf dem Papier in der Haupt-Hand und Feuerwerksstern in der Neben-Hand hat
            if (mainHand.getType() == Material.PAPER && offHand.getType() == Material.FIREWORK_STAR && mainHand.getItemMeta().getDisplayName().equalsIgnoreCase("§3Stone Switcher") && offHand.getItemMeta().hasLore()) {
                // Papier um eins reduzieren
                if (mainHand.getAmount() > 1) {
                    mainHand.setAmount(mainHand.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }


                // Feuerwerksstern entfernen
                player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));

                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                String stone = config.getString(player.getName() + ".stone");
                Integer level = config.getInt(player.getName() + ".level");
                swap(player,config,level, stone);
                player.getWorld().playSound(player.getLocation(),Sound.BLOCK_BEACON_POWER_SELECT,1,1);
            }
        }
    }

    private void swap(Player p, FileConfiguration config,Integer level,String stone) {
        Random random = new Random();
        int i = random.nextInt(6);
        if (i == 0 ){
            if (!stone.equalsIgnoreCase("fire")) {
                fireStone(p, level);
            }else {
                swap(p, config, level, stone);
            }
        }else if (i == 1){
            if (!stone.equalsIgnoreCase("electric")) {
                electricStone(p, level);
            }else {
                swap(p, config, level, stone);
            }
        }else if (i == 2){
            if (!stone.equalsIgnoreCase("water")) {
                waterStone(p, level);
            }else {
                swap(p, config, level, stone);
            }
        }else if (i == 3){
            if (!stone.equalsIgnoreCase("frozen")) {
                frozenStone(p,level);
            }else {
                swap(p, config, level, stone);
            }
        }else if (i == 4){
            if (!stone.equalsIgnoreCase("undead")) {
                undeadStone(p, level);
            }else {
                swap(p, config, level, stone);
            }
        }else if (i == 5){
            if (!stone.equalsIgnoreCase("holy")) {
                holyStone(p, level);
            }else {
                swap(p, config, level, stone);
            }
        }
    }

    private void frozenStone(Player p ,Integer level) {
        Items.Frozenstone(p,level);
        p.sendTitle("§d§kR§r§3Frozen Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Frozen");
        NikeyV1.getPlugin().saveConfig();
    }
    private void fireStone(Player p ,Integer level) {
        Items.Firestone(p,level);
        p.sendTitle("§d§kR§r§cFire Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Fire");
        NikeyV1.getPlugin().saveConfig();
    }
    private void electricStone(Player p ,Integer level) {
        Items.Electrostone(p,level);
        p.sendTitle("§d§kR§r§eElectric Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Electric");
        NikeyV1.getPlugin().saveConfig();
    }
    private void holyStone(Player p ,Integer level) {
        Items.Holystone(p,level);
        p.sendTitle("§d§kR§r§aHoly Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Holy");
        NikeyV1.getPlugin().saveConfig();
    }
    private void undeadStone(Player p ,Integer level) {
        Items.Undeadstone(p,level);
        p.sendTitle("§d§kR§r§0Undead Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Undead");
        NikeyV1.getPlugin().saveConfig();
    }
    private void waterStone(Player p ,Integer level) {
        Items.Waterstone(p,level);
        p.sendTitle("§d§kR§r§9Water Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Water");
        NikeyV1.getPlugin().saveConfig();
    }
}
