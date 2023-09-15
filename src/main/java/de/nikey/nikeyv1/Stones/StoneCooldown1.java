package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class StoneCooldown1 {
    private int time;
    private int stopTime;
    public HashMap<String,Integer> cooldown = new HashMap<>();
    private final HashMap<Player,Integer> taskID = new HashMap<>();

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
    public void start(Player p){
        if(!cooldown.containsKey(p.getName())){
            cooldown.put(p.getName(),getTime());
            p.sendMessage(cooldown.toString());
            p.sendMessage(String.valueOf(cooldown.get(p.getName())));
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!taskID.containsKey(p)){
                        taskID.put(p,this.getTaskId());
                    }
                    time++;
                    p.sendMessage(String.valueOf(cooldown.get(p.getName())));
                    if (cooldown.get(p.getName()) == getStopTime()){
                        cooldown.replace(p.getName(),getTime());

                    }else {
                        cooldown.remove(p.getName());
                        cancel();

                    }
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0L,20L);
        }
    }
    public void stop(Player p){
        if (taskID.containsKey(p)){
            Bukkit.getScheduler().cancelTask(taskID.get(p));
        }
    }
}