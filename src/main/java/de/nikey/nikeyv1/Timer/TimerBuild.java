package de.nikey.nikeyv1.Timer;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Util.Items;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@SuppressWarnings("ALL")
public class TimerBuild  {
    private int time;
    private int stopTime;
    private boolean running;
    private int level;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    private String getStone(Player player) {
        String string = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
        return string;
    }

    private BukkitRunnable task;
    public void start(Player player){
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (!isRunning()){
            if (!config.getBoolean(player.getName()+".time")){
                setRunning(true);
                NikeyV1.getPlugin().getConfig().set(player.getName()+".stoptime",getStopTime());
                NikeyV1.getPlugin().saveConfig();
                task = new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (Bukkit.getOnlinePlayers().contains(player)){
                            if (getStopTime() >getTime()){
                                NikeyV1.getPlugin().getConfig().set(player.getName()+".timer",getTime());
                                config.set(player.getName()+".time",true);
                                NikeyV1.getPlugin().saveConfig();
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.GOLD.toString()+ChatColor.BOLD+getTime()+"/"+getStopTime()));
                                setTime(getTime()+1);
                            }else {
                                config.set(player.getName()+".time",false);
                                NikeyV1.getPlugin().saveConfig();
                                setRunning(false);
                                player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.RED+"Time is up!"));
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP,2,1);
                                new ServerScoreboard(player);
                                if (getStone(player).equalsIgnoreCase("Fire")){
                                    Items.Firestone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Electric")){
                                    Items.Electrostone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Water")){
                                    Items.Waterstone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Frozen")){
                                    Items.Frozenstone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Undead")){
                                    Items.Undeadstone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Holy")){
                                    Items.Holystone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Ghost")){
                                    Items.Ghoststone(player,getLevel());
                                }else if (getStone(player).equalsIgnoreCase("Air")){
                                    Items.Airstone(player,getLevel());
                                }
                                cancel();
                            }
                        }else {
                            NikeyV1.getPlugin().getLogger().info(player.getName()+" left while upgrading!");
                            NikeyV1.getPlugin().getLogger().info("Was eine b*tch");
                            cancel();
                            return;
                        }
                    }
                };
                task.runTaskTimer(NikeyV1.getPlugin(),20,20);
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
