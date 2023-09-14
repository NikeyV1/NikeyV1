package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class StoneCooldown1 {
    private int coolDown;
    private int stopTime;
    private int taskId;
    private Player player;


    public ArrayList<UUID> getTask() {
        return task;
    }


    public ArrayList<UUID> task = new ArrayList<>();

    public int getStopTime() {
        return stopTime;
    }

    public void setStopTime(int stopTime) {
        this.stopTime = stopTime;
    }

    public int getCoolDown() {
        return coolDown;
    }

    public void start(){
        if (!task.contains(player.getUniqueId())) {
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            String stone = config.getString(player.getName() + ".stone");
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    if (!player.isOnline()){
                        NikeyV1.getPlugin().getLogger().info(player.getName()+" left while cooldown 1 with time "+getCoolDown());
                        config.set(player.getName()+"."+stone+".cooldown1"+".timer",true);
                        config.set(player.getName()+"."+stone+".cooldown1"+".time",getCoolDown());
                        config.set(player.getName()+"."+stone+".cooldown1"+".stoptime",getStopTime());
                        NikeyV1.getPlugin().saveConfig();
                        cancel();
                        return;
                    }else {
                        player.sendMessage(String.valueOf(getCoolDown()));
                    }
                    if (getCoolDown() == getStopTime()) {
                        player.sendMessage("Cooldown is done!, you can use it now!");
                        task.remove(player.getUniqueId());
                        config.set(player.getName()+"."+stone+".cooldown1"+".timer",false);
                        config.set(player.getName()+"."+stone+".cooldown1"+".time",0);
                        config.set(player.getName()+"."+stone+".cooldown1"+".stoptime",0);
                        NikeyV1.getPlugin().saveConfig();
                        cancel();
                    };
                    setCoolDown(getCoolDown()+1);
                }
            };
            runnable.runTaskTimer(NikeyV1.getPlugin(),20,20);
            setTaskId(runnable.getTaskId());
        }
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public void setCoolDown(int coolDown) {
        this.coolDown = coolDown;
    }
}
