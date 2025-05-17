package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Util.Items;
import de.nikey.nikeyv1.api.Stone;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class CommandChanges implements Listener {
    @EventHandler
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
                    event.getPlayer().setResourcePack("https://download.mc-packs.net/pack/1ae3a52c4b0096559c567b176d433b59af81a588.zip","1ae3a52c4b0096559c567b176d433b59af81a588",false);
                    event.getPlayer().sendMessage("§aYou reloaded your Resource Pack");
                }
             }
        }else if (cmd.startsWith("/give")) {
            if(player.isOp()) {
                if (args.length == 3) {
                    if (args[2].equalsIgnoreCase("Soulofstrenght")){
                        Items.SoulofStrenght(player);
                        event.setCancelled(true);
                    } else if (args[2].equalsIgnoreCase("EnchantedAnvil")) {
                        Items.GiveEnchantedAnvil(player);
                        event.setCancelled(true);
                    }else if (args[2].equalsIgnoreCase("InfernoBlade")) {
                        Items.GiveInfernoBlade(player);
                        event.setCancelled(true);
                    }else if (args[2].equalsIgnoreCase("PowerBeacon")) {
                        Items.GivePowerBeacon(player);
                        event.setCancelled(true);
                    }else if (args[2].equalsIgnoreCase("UpgradeToken")) {
                        Items.GiveUpgradeToken(player);
                        event.setCancelled(true);
                    }else if (args[2].equalsIgnoreCase("StoneSwitcher")) {
                        Items.GiveSwitcher(player);
                        event.setCancelled(true);
                    }
                }
            }
        }else if (cmd.startsWith("/check")) {
            if (player.getName().equalsIgnoreCase("NikeyV1") || player.getName().equalsIgnoreCase("NikeyV3") || player.getName().equalsIgnoreCase("Raptor07111")) {
                if (args.length == 2) {
                    if (args[1].equalsIgnoreCase("isstone")) {
                        player.sendMessage(String.valueOf(Stone.isStone(player.getInventory().getItemInMainHand())));
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
