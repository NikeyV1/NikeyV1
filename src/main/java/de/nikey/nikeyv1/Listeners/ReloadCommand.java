package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

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
        }else if (cmd.startsWith("/give") ||cmd.startsWith("7give")) {
            if (args.length == 3) {
                if (args[2].equalsIgnoreCase("Soulofstrenght") && player.isOp()){
                    Items.SoulofStrenght(player);
                    event.setCancelled(true);
                }
            }
        }
    }
}
