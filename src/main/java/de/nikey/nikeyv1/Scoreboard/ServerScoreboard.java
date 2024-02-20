package de.nikey.nikeyv1.Scoreboard;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Stones.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;

@SuppressWarnings("ALL")
public class ServerScoreboard extends ScoreboardBuilder {
    public ServerScoreboard(Player player) {
        super(player, "  §8>> §6§l§nStone SMP§r §8<<  ");
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        Integer level = config.getInt(player.getName() + ".level");
        stone();
        update();
        run();
        ability2();
        if (level >=20) {
            masterability();
        }else {
            hideMasterability();
        }
    }


    @Override
    public void update() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                setScore("§7Level: "+ChatColor.AQUA+config.getString(player.getName()+".level"),5);
            }
        };
        runnable.runTaskTimer(NikeyV1.getPlugin(),120,120);
    }

    @Override
    public void createScoreboard() {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String stone = config.getString(player.getName() + ".stone");
        setScore("§7Level: "+ChatColor.AQUA+config.getString(player.getName()+".level"),5);
        setScore("§7Ability 1: §aReady",4);
        setScore("§7Ability 2: §aReady",3);
        setScore("§7Master Ability: §aReady",2);
        setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
        setScore("§7Online Players: "+ChatColor.DARK_PURPLE+ Bukkit.getOnlinePlayers().size(),0);
    }

    private void hideMasterability() {
        setScore("--------------",2);
    }


    private void masterability() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                long remainingTime3 = 0;
                if (Arrays.asList("Electric", "Fire", "Water", "Frozen", "Undead", "Holy").contains(stone)) {
                    remainingTime3 = getMasterCooldown(player,stone);
                }
                int a = (int) ((remainingTime3 - System.currentTimeMillis()) / 1000);
                setScore("§7Master Ability: " + (a <= 0 ? "§aReady" : "§c" + a + "/300"), 2);
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 10, 10);
    }

    private void ability2() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (Arrays.asList("Electric", "Fire", "Water", "Frozen", "Undead", "Holy").contains(stone)) {
                    long remainingTime2 = getAbilityCooldown(player, stone);
                    int a = (int) ((remainingTime2 - System.currentTimeMillis()) / 1000);
                    if (a <= 0) {
                        setScore("§7Ability 2: §aReady", 3);
                    } else {
                        setScore("§7Ability 2: §c" + a + "/180", 3);
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 10, 10);
    }
    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (Arrays.asList("Electric", "Fire", "Water", "Frozen", "Undead", "Holy").contains(stone)) {
                    long remainingTime = getCooldown(player, stone);
                    int i = (int) ((remainingTime - System.currentTimeMillis()) / 1000);
                    if (i <= 0) {
                        setScore("§7Ability 1: §aReady", 4);
                    } else {
                        setScore("§7Ability 1: §c" + i + "/100", 4);
                    }
                }
                setScore("§7Ping: " + ChatColor.DARK_PURPLE + player.getPing(), 1);
                setScore("§7Online Players: " + ChatColor.DARK_PURPLE + Bukkit.getOnlinePlayers().size(), 0);
            }
        }.runTaskTimer(NikeyV1.getPlugin(),10,10);
    }

    private long getAbilityCooldown(Player player, String stone) {
        switch (stone.toLowerCase()) {
            case "electric":
                return Electrostone.ability.getOrDefault(player.getUniqueId(), 0L);
            case "fire":
                return Firestone.ability.getOrDefault(player.getUniqueId(), 0L);
            case "water":
                return Waterstone.ability.getOrDefault(player.getUniqueId(), 0L);
            case "frozen":
                return Frozenstone.ability.getOrDefault(player.getUniqueId(), 0L);
            case "undead":
                return Undeadstone.ability.getOrDefault(player.getUniqueId(), 0L);
            case "holy":
                return Holystone.ability.getOrDefault(player.getUniqueId(), 0L);
            default:
                return 0L;
        }
    }

    private long getMasterCooldown(Player player, String stone) {
        switch (stone.toLowerCase()) {
            case "fire":
                return Firestone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            case "water":
                return Waterstone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            case "electric":
                return Electrostone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            case "frozen":
                return Frozenstone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            case "undead":
                return Undeadstone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            case "holy":
                return Holystone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            default:
                return 0L;
        }
    }

    private long getCooldown(Player player, String stone) {
        switch (stone.toLowerCase()) {
            case "electric":
                return Electrostone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "fire":
                return Firestone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "water":
                return Waterstone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "frozen":
                return Frozenstone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "undead":
                return Undeadstone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "holy":
                return Holystone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            default:
                return 0L;
        }
    }


    private void stone() {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                String stone = config.getString(player.getName() + ".stone");
                net.md_5.bungee.api.ChatColor color;
                switch (stone.toLowerCase()) {
                    case "fire":
                        color = ChatColor.RED.asBungee();
                        break;
                    case "electric":
                        color = ChatColor.YELLOW.asBungee();
                        break;
                    case "water":
                        color = ChatColor.BLUE.asBungee();
                        break;
                    case "frozen":
                        color = ChatColor.DARK_AQUA.asBungee();
                        break;
                    case "undead":
                        color = net.md_5.bungee.api.ChatColor.of("#100613");
                        break;
                    case "holy":
                        color = net.md_5.bungee.api.ChatColor.of("#47d147");
                        break;
                    default:
                        color = ChatColor.WHITE.asBungee();
                        break;
                }
                setScore("§7Stone: " + color + stone, 6);
            }
        }.runTaskTimer(NikeyV1.getPlugin(),0,120);
    }
}
