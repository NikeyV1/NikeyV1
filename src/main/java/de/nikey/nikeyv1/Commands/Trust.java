package de.nikey.nikeyv1.Commands;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Trust implements CommandExecutor , TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        FileConfiguration config = NikeyV1.getPlugin().getConfig();

        if (args.length == 0) {
            player.sendMessage("Use /trust <add/remove/list> [player]");
            return true;
        }

        switch (args[0].toLowerCase()) {
            case "add":
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        String playerPath = player.getName() + ".trust";
                        List<String> trustedList = config.getStringList(playerPath);

                        if (trustedList.contains(target.getName())) {
                            player.sendMessage(target.getName() + " is already on your trust list.");
                        } else {
                            trustedList.add(target.getName());
                            config.set(playerPath, trustedList);
                            NikeyV1.getPlugin().saveConfig();
                            player.sendMessage(target.getName() + " has been §aadded§r to your trust list.");
                        }
                    } else {
                        player.sendMessage("Player not found.");
                    }
                } else {
                    player.sendMessage("Usage: /trust add [player]");
                }
                break;

            case "remove":
                if (args.length == 2) {
                    Player target = Bukkit.getPlayer(args[1]);
                    if (target != null) {
                        String playerPath = player.getName() + ".trust";
                        List<String> trustedList = config.getStringList(playerPath);

                        if (trustedList.remove(target.getName())) {
                            config.set(playerPath, trustedList);
                            NikeyV1.getPlugin().saveConfig();
                            player.sendMessage(target.getName() + " has been §cremoved§r from your trust list.");
                        } else {
                            player.sendMessage(target.getName() + " is not on your trust list.");
                        }
                    } else {
                        player.sendMessage("Player not found.");
                    }
                } else {
                    player.sendMessage("Usage: /trust remove [player]");
                }
                break;

            case "list":
                String playerPath = player.getName() + ".trust";
                List<String> trustedList = config.getStringList(playerPath);
                if (trustedList.isEmpty()) {
                    player.sendMessage("You have no trusted players.");
                } else {
                    player.sendMessage("Trusted players:");
                    for (String name : trustedList) {
                        player.sendMessage("- " + name);
                    }
                }
                break;

            default:
                player.sendMessage("Invalid command. Use /trust <add/remove/list> [player]");
                break;
        }
        return true;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command,  String label,  String[] args) {
        if(args.length == 0){
            final List<String> completions = new ArrayList<>();
            List<String> commands = new ArrayList<>();
            commands.add("add");
            commands.add("remove");
            commands.add("list");
            StringUtil.copyPartialMatches(args[0], commands, completions);
            return completions;
        }else{
            final List<String> completions = new ArrayList<>();
            List<String> commands = new ArrayList<>();
            for (Player player : Bukkit.getOnlinePlayers()) {
                commands.add(player.getName());
            }

            StringUtil.copyPartialMatches(args[0], commands, completions);
            return completions;
        }
    }
}
