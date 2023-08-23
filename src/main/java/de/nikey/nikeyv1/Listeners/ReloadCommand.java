package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Stones.StoneCooldown1;
import de.nikey.nikeyv1.Timer.TimerBuild;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ReloadCommand implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final Player player = event.getPlayer();
        final String cmd = event.getMessage();
        final String[] args = cmd.split(" ");

        if (cmd.startsWith("/rl") ||cmd.startsWith("/reload")){
             if (args.length == 2) {
                if (args[1].equalsIgnoreCase("scoreboard")) {
                    event.setCancelled(true);
                    player.sendMessage("§aYou reloaded your scoreboard");
                    new ServerScoreboard(player);
                    FileConfiguration config = NikeyV1.plugin.getConfig();
                    String stone = config.getString(player.getName() + ".stone");
                    TimerBuild timerBuild = new TimerBuild();
                    if (config.getBoolean(player.getName()+".time")){
                        BukkitRunnable runnable = new BukkitRunnable() {
                            @Override
                            public void run() {
                                config.set(player.getName()+".time",false);
                                if (!config.getBoolean(player.getName()+".time")){
                                    timerBuild.setLevel(config.getInt(player.getName()+".level"));
                                    timerBuild.setTime(config.getInt(player.getName()+".timer"));
                                    timerBuild.setStopTime(config.getInt(player.getName()+".stoptime"));
                                    timerBuild.start(player);
                                    player.sendMessage("§aYour upgrade continues!");
                                }else {
                                    player.sendMessage("§cPlugin Error: "+event.getEventName()+".continue.upgrade");
                                }
                            }
                        };
                        runnable.runTaskLater(NikeyV1.getPlugin(),20);
                    }
                    if (config.getBoolean(player.getName()+"."+stone+".cooldown1"+".timer")){
                        BukkitRunnable runnable = new BukkitRunnable() {

                            @Override
                            public void run() {
                                config.set(player.getName()+"."+stone+".cooldown1"+".timer",false);
                                if (!config.getBoolean(player.getName()+"."+stone+".cooldown1"+".timer")){
                                    StoneCooldown1 stoneCooldown1 = new StoneCooldown1();
                                    stoneCooldown1.setTime(config.getInt(player.getName()+"."+stone+".cooldown1"+".time"));
                                    stoneCooldown1.setStopTime(config.getInt(player.getName()+"."+stone+".cooldown1"+".stoptime"));
                                    stoneCooldown1.start(player);
                                }else {
                                    player.sendMessage("§cPlugin Error: "+event.getEventName()+".continue.cooldown");
                                }
                            }
                        };
                        runnable.runTaskLater(NikeyV1.getPlugin(),20);
                    }
                }
            }
        }
    }
}
