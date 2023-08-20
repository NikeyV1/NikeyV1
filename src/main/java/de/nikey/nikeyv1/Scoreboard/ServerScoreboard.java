package de.nikey.nikeyv1.Scoreboard;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.eclipse.aether.impl.RepositoryEventDispatcher;

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
        setScore("§fStone:",6);
        setScore(ChatColor.RED +config.getString(player.getName()+".stone"),5);
        setScore(ChatColor.AQUA+"Level: "+config.getString(player.getName()+".level"),4);
        setScore("----------",3);
        setScore("§7Server stats:",2);
        setScore("§fIP: "+ player.getAddress(),1);
        setScore("§fOnline Players: "+ Bukkit.getOnlinePlayers().size(),0);
    }
    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(NikeyV1.plugin,20,20);
    }
}
