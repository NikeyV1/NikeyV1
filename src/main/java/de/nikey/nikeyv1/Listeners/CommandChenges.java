package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Util.Items;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandChenges implements Listener {
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
                } else if (args[1].equalsIgnoreCase("Pack")) player.setResourcePack("https://download.mc-packs.net/pack/4c8d208dff3e41fe8f7b4236035b2b7b1a0cacd2.zip");
             }
        }else if (cmd.startsWith("/give") ||cmd.startsWith("7give")) {
            if(player.isOp()) {
                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("Soulofstrenght") && player.isOp()){
                        Items.SoulofStrenght(player);
                        event.setCancelled(true);
                    } else if (args[2].equalsIgnoreCase("EnchantedAnvil") && player.isOp()) {
                        Items.GiveEnchantedAnvil(player);
                        event.setCancelled(true);
                    }else if (args[2].equalsIgnoreCase("InfernoBlade") && player.isOp()) {
                        Items.GiveInfernoBlade(player);
                        event.setCancelled(true);
                    }
            }
            }
        }
    }
}
