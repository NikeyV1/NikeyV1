package de.nikey.nikeyv1.Scoreboard;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ServerScoreboard extends ScoreboardBuilder {
    public ServerScoreboard(Player player) {
        super(player, "  §8>> §6§l§nNikey§r §8<<  ");
    }


    @Override
    public void update() {

    }

    @Override
    public void createScoreboard() {
        setScore("§7Dein §8Stein",7);
        setScore("Stein",6);
        setScore("§7",7);
        setScore("§7",7);
    }
    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {

            }
        }.runTaskTimer(NikeyV1.plugin,20,20);
    }
}
