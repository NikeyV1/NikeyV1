package de.nikey.nikeyv1.api;

import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;
import java.util.Objects;

@SuppressWarnings("ALL")
public class StoneHandler {
    public static int getStoneLevel(Player player) {
        return NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
    }

    public static String getStoneName(Player player) {
        String name = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
        if (name.isEmpty() && NikeyV1.getPlugin().getConfig().getBoolean("autoKick")) {
            player.kick();
            return "null";
        }

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
        if (!item.getEnchantments().containsKey(Enchantment.SHARPNESS)) return false;

        Map<Enchantment, Integer> enchantments = item.getEnchantments();

        ItemMeta meta = item.getItemMeta();
        return meta.hasCustomModelData() && enchantments.get(Enchantment.SHARPNESS) == 7 && meta.isUnbreakable() && meta.getLore().contains("§7What will you do?") && item.getType() == Material.NETHERITE_SWORD;
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
            }else if (displayName.equalsIgnoreCase(ChatColor.of("#b4d4ce")+"Air Stone")) {
                return "Air";
            }else if (displayName.equalsIgnoreCase(ChatColor.of("#228B22") + "Nature Stone")) {
                return "Nature";
            }else {
                return "";
            }
        }else {
            return "";
        }
    }

    public static boolean shouldHaveStone(Player player, ItemStack item) {
        return getStoneName(player).equalsIgnoreCase(whatStone(item));
    }


    public static boolean isUndeadMaster(LivingEntity entity) {
        if (!(entity instanceof Zombie)) return false;
        if (Objects.requireNonNull(entity.getAttribute(Attribute.SCALE)).getValue() == 3.5F) {
            if (Objects.requireNonNull(entity.getAttribute(Attribute.MAX_HEALTH)).getValue() == 500) {
                return entity.hasMetadata("master");
            }
        }

        return false;
    }


    public static boolean isSummoned(Player summoner, LivingEntity entity) {
        if (entity.getCustomName() == null) return false;
        if (summoner.getName().isEmpty()) return false;
        return entity.getCustomName().startsWith(summoner.getName() + "'s " + entity.getType().getName());
    }

    public static boolean isSummoned(LivingEntity entity) {
        if (entity.getCustomName() == null) return false;
        return entity.getCustomName().contains("'s "+ entity.getType().name());
    }

    public static Player getSummoner(LivingEntity entity) {
        String[] arr = entity.getCustomName().split("'");
        Player p = Bukkit.getPlayer(arr[0]);
        return p;
    }

    public static boolean isBuffed(Player player) {
        return NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
    }
}