package de.nikey.nikeyv1.api;

import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Stone {
    public static int getStoneLevel(Player player) {
        return NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
    }

    public static String getStoneName(Player player) {
        return NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
    }

    public static int getUpgradeTime(Player player) {
        return NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".timer");
    }

    public static int getUpgradeStopTime(Player player) {
        return NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".stoptime");
    }

    public static String getAttacking(Player player) {
        return NikeyV1.getPlugin().getConfig().getString(player.getName() + ".det");
    }


    public static int getStoneLevelFromItem(ItemStack item) {
        if (isStone(item)) {
            String[] arr = item.getLore().get(1).split(":");
            return Integer.parseInt(arr[1]);
        }else {
            return 0;
        }
    }


    public static void setStoneLevel(Player player, int level) {
        NikeyV1.getPlugin().getConfig().set(player.getName() + ".level",level);
    }

    public static void setStoneName(Player player, String stone) {
        NikeyV1.getPlugin().getConfig().set(player.getName() + ".stone",stone);
    }


    public static boolean isUpgrading(Player player) {
        return NikeyV1.getPlugin().getConfig().getBoolean(player.getName()+".time");
    }


    public static boolean isStone(ItemStack item) {
        if (!item.hasItemMeta()) return false;

        return item.getType() == Material.FIREWORK_STAR && item.getItemMeta().hasLore();
    }

    public static boolean isInfernoBlade(ItemStack item) {
        if (!item.hasItemMeta())return false;
        if (!item.getItemMeta().hasLore())return false;

        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && meta.isUnbreakable() && meta.getLore().contains("§7What will you do?") && item.getType() == Material.NETHERITE_SWORD;
    }

    public static String whatStone(ItemStack item) {
        if (isStone(item)) {
            String displayName = item.getItemMeta().getDisplayName();
            if (displayName.equalsIgnoreCase(ChatColor.of("#dddddd")+"Ghost Stone")) {
                return "Ghost";
            } else if (displayName.equalsIgnoreCase("§aHoly Stone")) {
                return "Holy";
            }else if (displayName.equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")) {
                return "Undead";
            }else if (displayName.equalsIgnoreCase("§3Frozen Stone")) {
                return "Frozen";
            }else if (displayName.equalsIgnoreCase("§9Water Stone")) {
                return "Water";
            }else if (displayName.equalsIgnoreCase("§eElectric Stone")) {
                return "Electric";
            }else if (displayName.equalsIgnoreCase(ChatColor.of("#e66b63")+"Fire Stone")) {
                return "Fire";
            }else {
                return "Error: No Stone";
            }
        }else {
            return "Error: No Stone";
        }
    }

    public static boolean shouldHaveStone(Player player, ItemStack item) {
        return getStoneName(player).equalsIgnoreCase(whatStone(item));
    }
}
