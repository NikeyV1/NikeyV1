package de.nikey.nikeyv1.Timer;

import de.nikey.nikeyv1.NikeyV1;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class Timer {
    private boolean running;
    private int time;

    public Timer(boolean running,int time) {
        this.running = running;
        this.time = time;

        run();
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
    public void sendActionBar(Player player){
        if (!isRunning()){
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.RED+"Timer ended"));
        }else {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent(ChatColor.GOLD.toString()+ChatColor.BOLD+getTime()));
        }
    }
    private void run(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isRunning()){
                    return;
                }
                setTime(getTime() +1);
            }
        }.runTaskTimer(NikeyV1.plugin,20,20);
    }
}
