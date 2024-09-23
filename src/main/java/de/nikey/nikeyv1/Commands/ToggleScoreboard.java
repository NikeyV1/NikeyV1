package de.nikey.nikeyv1.Commands;

import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ToggleScoreboard implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args) {
        org.bukkit.entity.Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("board")) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("on")) {
                    new ServerScoreboard(player);
                } else if (args[0].equalsIgnoreCase("off")) {
                    player.setScoreboard(Bukkit.getScoreboardManager().getNewScoreboard());
                }
            }else {
                player.sendMessage(Component.text("Wrong usage: /scoreboard [on/off]"));
            }
        }
        return true;
    }
}
