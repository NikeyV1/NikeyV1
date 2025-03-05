package de.nikey.nikeyv1.Scoreboard;

import de.nikey.nikeyv1.Listeners.GhostStoneDamageAbility;
import de.nikey.nikeyv1.Listeners.InfernoBlade;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Stones.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.Random;

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
                if (Arrays.asList("Electric", "Fire", "Water", "Frozen", "Undead", "Holy", "Ghost", "Air").contains(stone)) {
                    boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
                    if (!buffed) {
                        remainingTime3 = getMasterCooldown(player,stone);
                        int a = (int) ((remainingTime3 - System.currentTimeMillis()) / 1000);
                        setScore("§7Master Ability: " + (a <= 0 ? "§aReady" : "§c" + a + "/300"), 2);
                    }else {
                        remainingTime3 = getElementalUltimate(player);
                        int a = (int) ((remainingTime3 - System.currentTimeMillis()) / 1000);
                        setScore("§7Master Ability: " + (a <= 0 ? "§aReady" : "§c" + a + "/150"), 2);
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 10, 10);
    }

    private void ability2() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (Arrays.asList("Electric", "Fire", "Water", "Frozen", "Undead", "Holy", "Ghost", "Air").contains(stone)) {
                    boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
                    if (!buffed) {
                        long remainingTime2 = getAbilityCooldown(player, stone);
                        int a = (int) ((remainingTime2 - System.currentTimeMillis()) / 1000+1);
                        if (a <= 0) {
                            setScore("§7Ability 2: §aReady", 3);
                        } else {
                            setScore("§7Ability 2: §c" + a + "/180", 3);
                        }
                    }else {
                        long remainingTime2 = getElementalAbility(player);
                        int a = (int) ((remainingTime2 - System.currentTimeMillis()) / 1000 +1);
                        if (InfernoBlade.red) {
                            if (a <= 0) {
                                setScore("§7Ability 2: §aReady", 3);
                            } else {
                                setScore("§7Ability 2: §c" + a + "/45", 3);
                            }
                        }else {
                            if (a <= 0) {
                                setScore("§7Ability 2: §aReady", 3);
                            } else {
                                setScore("§7Ability 2: §c" + a + "/6", 3);
                            }
                        }
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
                if (Arrays.asList("Electric", "Fire", "Water", "Frozen", "Undead", "Holy", "Ghost", "Air").contains(stone)) {
                    boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
                    if (!buffed) {
                        long remainingTime = getCooldown(player, stone);
                        int i = (int) ((remainingTime - System.currentTimeMillis()) / 1000);
                        if (i <= 0) {
                            setScore("§7Ability 1: §aReady", 4);
                        } else {
                            setScore("§7Ability 1: §c" + i + "/100", 4);
                        }
                    }else {
                        long remainingTime = getElementalCooldown(player);
                        int i = (int) ((remainingTime - System.currentTimeMillis()) / 1000);
                        if (i <= 0) {
                            setScore("§7Ability 1: §aReady", 4);
                        } else {
                            setScore("§7Ability 1: §c" + i + "/120", 4);
                        }
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
            case "ghost":
                return GhostStoneDamageAbility.ability.getOrDefault(player.getUniqueId(), 0L);
            case "air":
                return Airstone.ability.getOrDefault(player.getUniqueId(), 0L);
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
            case "ghost":
                return Ghoststone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            case "air":
                return Airstone.cooldown2.getOrDefault(player.getUniqueId(), 0L);
            default:
                return 0L;
        }
    }

    private long getElementalCooldown(Player player) {
        boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
        if (buffed) {
            return Elementalstone.cooldown.getOrDefault(player.getUniqueId(),0L);
        }else {
            return 0L;
        }
    }

    private long getElementalAbility(Player player) {
        boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
        if (buffed) {
            return InfernoBlade.ability.getOrDefault(player.getUniqueId(),0L);
        }else {
            return 0L;
        }
    }

    private long getElementalUltimate(Player player) {
        boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
        if (buffed) {
            return Elementalstone.cooldown2.getOrDefault(player.getUniqueId(),0L);
        }else {
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
            case "ghost":
                return Ghoststone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "air":
                return Airstone.cooldown.getOrDefault(player.getUniqueId(), 0L);
            case "nature":
                return Naturestone.cooldown.getOrDefault(player.getUniqueId(),0L);
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
                boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
                if (!buffed) {
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
                        case "ghost":
                            color = net.md_5.bungee.api.ChatColor.of("#dddddd");
                            break;
                        case "air":
                            color = net.md_5.bungee.api.ChatColor.of("#b4d4ce");
                            break;
                        case "nature":
                            color = net.md_5.bungee.api.ChatColor.of("#228B22");
                            break;
                        default:
                            color = ChatColor.WHITE.asBungee();
                            break;
                    }
                    setScore("§7Stone: " + color + stone, 6);
                }else {
                    if (InfernoBlade.red) {
                        setScore("§7Stone: " +ChatColor.RED + "Elemental Stone", 6);
                    }else {
                        setScore("§7Stone: " +ChatColor.AQUA + "Elemental Stone", 6);
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),0,120);
    }
}
