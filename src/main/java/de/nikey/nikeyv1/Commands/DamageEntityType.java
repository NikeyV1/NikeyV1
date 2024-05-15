package de.nikey.nikeyv1.Commands;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DamageEntityType implements CommandExecutor , TabCompleter {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        if (!(sender instanceof Player)) {
            return false;
        }
        Player player = (Player) sender;

        if (args.length != 1) {
            player.sendMessage("§cThat doesn't work like that: " + "/§adamageentitytype [Players,Monsters,Monsters-Players,All]");
            return false;
        }

        String arg = args[0];

        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (arg.equalsIgnoreCase("Players")) {
            config.set(player.getName()+".det","players");
            NikeyV1.getPlugin().saveConfig();
            player.sendMessage("§aYour stone now only attacks players");
        } else if (arg.equalsIgnoreCase("Monsters")) {
            config.set(player.getName()+".det","monsters");
            NikeyV1.getPlugin().saveConfig();
            player.sendMessage("§aYour stone now only attacks monsters");
        }else if (arg.equalsIgnoreCase("Monsters-Players")) {
            config.set(player.getName()+".det","monsters-player");
            NikeyV1.getPlugin().saveConfig();
            player.sendMessage("§aYour stone now attacks monsters and players");
        }else if (arg.equalsIgnoreCase("All")) {
            config.set(player.getName()+".det","all");
            NikeyV1.getPlugin().saveConfig();
            player.sendMessage("§aYour stone now attacks everything");
        }else {
            player.sendMessage("§cThat doesn't work like that: " + "§a/damageentitytype [Players,Monsters,Monsters-Players,All]");
            return false;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        final List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        commands.add("Players");
        commands.add("Monsters");
        commands.add("Monsters-Players");
        commands.add("All");
        StringUtil.copyPartialMatches(args[0], commands, completions);
        return completions;
    }
}
