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
                }
            }
        }
    }
}
