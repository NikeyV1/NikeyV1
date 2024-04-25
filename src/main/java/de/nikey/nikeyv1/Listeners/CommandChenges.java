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
                } else if (args[1].equalsIgnoreCase("Pack")){
                    event.setCancelled(true);
                    event.getPlayer().setResourcePack("https://download.mc-packs.net/pack/1ae3a52c4b0096559c567b176d433b59af81a588.zip","1ae3a52c4b0096559c567b176d433b59af81a588");
                }
             }
        }else if (cmd.startsWith("/give")) {
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
                    }else if (args[2].equalsIgnoreCase("PowerBeacon") && player.isOp()) {
                        Items.GivePowerBeacon(player);
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
