package de.nikey.nikeyv1.Scoreboard;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.aether.impl.RepositoryEventDispatcher;

@SuppressWarnings("ALL")
public class ServerScoreboard extends ScoreboardBuilder {
    public ServerScoreboard(Player player) {
        super(player, "  §8>> §6§l§nNikey§r §8<<  ");
        run();
    }


    @Override
    public void update() {

    }

    @Override
    public void createScoreboard() {
        FileConfiguration config = NikeyV1.plugin.getConfig();
        String stone = config.getString(player.getName() + ".stone");
        setScore("§7Stone: "+ChatColor.RED +config.getString(player.getName()+".stone"),6);
        setScore("§7Level: "+ChatColor.AQUA+config.getString(player.getName()+".level"),5);
        setScore("§7Ability 1:"+config.getInt(player.getName()+"."+stone+".cooldown1"+".time") +"/"+config.getInt(player.getName()+"."+stone+".cooldown1"+".stoptime"),4);
        setScore("§7Ability 2:",3);
        setScore("----------",2);
        setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
        setScore("§7Online Players: "+ChatColor.DARK_PURPLE+ Bukkit.getOnlinePlayers().size(),0);
    }
    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (NikeyV1.getPlugin().getConfig().getInt(player.getName()+"."+stone+".cooldown1"+".time") == 0 &&NikeyV1.getPlugin().getConfig().getInt(player.getName()+"."+stone+".cooldown1"+".stoptime") == 0){
                    setScore("§7Ability 1: §aReady",4);
                }else {
                    setScore("§7Ability 1: §c"+NikeyV1.getPlugin().getConfig().getInt(player.getName()+"."+stone+".cooldown1"+".time") +"/"+NikeyV1.getPlugin().getConfig().getInt(player.getName()+"."+stone+".cooldown1"+".stoptime"),4);
                }
                setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
            }
        }.runTaskTimer(NikeyV1.plugin,20,20);
    }
}
