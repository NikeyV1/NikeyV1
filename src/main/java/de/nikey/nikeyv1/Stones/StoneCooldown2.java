package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StoneCooldown2 {
    private int time;
    private int stopTime;

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    private BukkitRunnable task;
    public void start(Player player){
        FileConfiguration config = NikeyV1.plugin.getConfig();
        String stone = config.getString(player.getName() + ".stone");
        if (!config.getBoolean(player.getName()+"."+stone+".cooldown2.timer")){
            config.set(player.getName()+"."+stone+".cooldown2"+".stoptime",getStopTime());
            NikeyV1.plugin.saveConfig();
            task = new BukkitRunnable() {
                @Override
                public void run() {
                    if (Bukkit.getOnlinePlayers().contains(player)){
                        if (getStopTime() > getTime()){
                            config.set(player.getName()+"."+stone+".cooldown2"+".time",getTime());
                            config.set(player.getName()+"."+stone+".cooldown2"+".timer",true);
                            NikeyV1.getPlugin().saveConfig();
                            setTime(getTime()+1);
                        }else {
                            config.set(player.getName()+"."+stone+".cooldown2"+".timer",false);
                            config.set(player.getName()+"."+stone+".cooldown2"+".time",0);
                            config.set(player.getName()+"."+stone+".cooldown2"+".stoptime",0);
                            NikeyV1.getPlugin().saveConfig();
                            cancel();
                        }
                    }else {
                        cancel();
                        NikeyV1.getPlugin().getLogger().info(player.getName()+" left while cooldown");
                    }
                }
            };
            task.runTaskTimer(NikeyV1.getPlugin(),20,20);
        }else {
            player.sendMessage("§cYou are on cooldown");
        }
    }    
}
