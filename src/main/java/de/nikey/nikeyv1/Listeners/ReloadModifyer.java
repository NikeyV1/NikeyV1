package de.nikey.nikeyv1.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

@SuppressWarnings("ALL")
public class ReloadModifyer implements Listener {
    @EventHandler(ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        final Player p = event.getPlayer();
        final String cmd = event.getMessage();
        final String[] args = cmd.split(" ");
        if (cmd.startsWith("/rl") ||cmd.startsWith("/reload")||cmd.startsWith("rl")){
            if (args.length == 1 && p.isOp()){
                event.setCancelled(true);
                for (Player players : Bukkit.getOnlinePlayers()){
                    players.kickPlayer("§cServer reload");
                }
                p.getServer().reload();
            }
        }
    }
}
