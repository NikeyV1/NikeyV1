package de.nikey.nikeyv1.api;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.entity.Player;

public class Stone {
    public static int getStoneLevel(Player player) {
        return NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
    }

    public static String getStoneName(Player player) {
        return NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
    }

    public static boolean isUpgrading(Player player) {
        return NikeyV1.getPlugin().getConfig().getBoolean(player.getName()+".time");
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

    public static void setStoneLevel(Player player, int level) {
        NikeyV1.getPlugin().getConfig().set(player.getName() + ".level",level);
    }
}
