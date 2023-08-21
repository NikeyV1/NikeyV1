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
    }


    @Override
    public void update() {

    }

    @Override
    public void createScoreboard() {
        FileConfiguration config = NikeyV1.plugin.getConfig();
        setScore("§7Stone: "+ChatColor.RED +config.getString(player.getName()+".stone"),5);
        setScore("§7Level: "+ChatColor.AQUA+config.getString(player.getName()+".level"),4);
        setScore("----------",3);
        setScore("§7Plugin version:"+ChatColor.DARK_PURPLE+NikeyV1.getPlugin().getPluginMeta().getVersion(),2);
        setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
        setScore("§7Online Players: "+ChatColor.DARK_PURPLE+ Bukkit.getOnlinePlayers().size(),0);
    }
    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
            }
        }.runTaskTimer(NikeyV1.plugin,20,20);
    }
}
