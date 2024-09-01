package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
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
            if (mainHand.getType() == Material.PAPER && offHand.getType() == Material.FIREWORK_STAR && mainHand.getItemMeta().getDisplayName().equalsIgnoreCase("§3Stone Switcher") && offHand.getItemMeta().hasLore()) {
                if (mainHand.getAmount() > 1) {
                    mainHand.setAmount(mainHand.getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
                boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
                if (!buffed) {
                    player.getInventory().setItemInOffHand(new ItemStack(Material.AIR));
                    FileConfiguration config = NikeyV1.getPlugin().getConfig();
                    String stone = config.getString(player.getName() + ".stone");
                    Integer level = config.getInt(player.getName() + ".level");
                    player.getActivePotionEffects().clear();
                    swap(player,level, stone);
                    player.getWorld().playSound(player.getLocation(),Sound.BLOCK_BEACON_POWER_SELECT,1,1);
                }else {
                    player.sendMessage("§cDon't even try!");
                }
            }
        }
    }

    public static void swap(Player p,Integer level,String stone) {
        Random random = new Random();
        int i = random.nextInt(8);
        if (i == 0 ){
            if (!stone.equalsIgnoreCase("fire")) {
                fireStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }else if (i == 1){
            if (!stone.equalsIgnoreCase("electric")) {
                electricStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }else if (i == 2){
            if (!stone.equalsIgnoreCase("water")) {
                waterStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }else if (i == 3){
            if (!stone.equalsIgnoreCase("frozen")) {
                frozenStone(p,level);
            }else {
                swap(p, level, stone);
            }
        }else if (i == 4){
            if (!stone.equalsIgnoreCase("undead")) {
                undeadStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }else if (i == 5){
            if (!stone.equalsIgnoreCase("holy")) {
                holyStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }else if (i == 6){
            if (!stone.equalsIgnoreCase("ghost")) {
                ghostStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }else {
            if (!stone.equalsIgnoreCase("air")) {
                airStone(p, level);
            }else {
                swap(p, level, stone);
            }
        }
    }

    private static void frozenStone(Player p, Integer level) {
        Items.Frozenstone(p,level);
        p.sendTitle("§d§kR§r§3Frozen Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Frozen");
        NikeyV1.getPlugin().saveConfig();
    }
    private static void fireStone(Player p, Integer level) {
        Items.Firestone(p,level);
        p.sendTitle("§d§kR§r§cFire Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Fire");
        NikeyV1.getPlugin().saveConfig();
    }
    private static void electricStone(Player p, Integer level) {
        Items.Electrostone(p,level);
        p.sendTitle("§d§kR§r§eElectric Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Electric");
        NikeyV1.getPlugin().saveConfig();
    }
    private static void holyStone(Player p, Integer level) {
        Items.Holystone(p,level);
        p.sendTitle("§d§kR§r§aHoly Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Holy");
        NikeyV1.getPlugin().saveConfig();
    }
    private static void undeadStone(Player p, Integer level) {
        Items.Undeadstone(p,level);
        p.sendTitle("§d§kR§r§0Undead Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Undead");
        NikeyV1.getPlugin().saveConfig();
    }
    private static void waterStone(Player p, Integer level) {
        Items.Waterstone(p,level);
        p.sendTitle("§d§kR§r§9Water Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Water");
        NikeyV1.getPlugin().saveConfig();
    }

    private static void ghostStone(Player p, Integer level) {
        Items.Ghoststone(p,level);
        p.sendTitle("§d§kR§r§fGhost Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Ghost");
        NikeyV1.getPlugin().saveConfig();
    }

    private static void airStone(Player p, Integer level) {
        Items.Airstone(p,level);
        p.sendTitle("§d§kR§r"+ Color.fromRGB(180, 212, 206) +"Air Stone§r§d§kR","");
        NikeyV1.getPlugin().getConfig().set(p.getName()+".stone", "Air");
        NikeyV1.getPlugin().saveConfig();
    }
}
