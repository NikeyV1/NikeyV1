package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.NotNull;

public class Update implements CommandExecutor {
    @Override
    public boolean onCommand( CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("reload") && sender.isOp()){
            sender.getServer().reload();
            sender.getServer().reloadData();
            sender.getServer().reloadCommandAliases();
            sender.getServer().reloadPermissions();
            sender.getServer().reloadWhitelist();
            BukkitRunnable runnable = new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player players : Bukkit.getOnlinePlayers()){
                        players.kick();
                    }
                }
            };
            runnable.runTaskLater(NikeyV1.getPlugin(),20);
        }
        return true;
    }
}
