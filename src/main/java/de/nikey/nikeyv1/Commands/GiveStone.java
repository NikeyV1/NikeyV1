package de.nikey.nikeyv1.Commands;

import de.nikey.nikeyv1.Util.Items;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GiveStone implements CommandExecutor , TabCompleter {
    @Override
    public boolean onCommand( CommandSender sender,  Command command,  String label,  String[] args) {
        org.bukkit.entity.Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("stone") && p.isOp() || p.getName().equalsIgnoreCase("NikeyV3")){
            if (args.length == 2){
                String stone = args[0];
                if (stone.equalsIgnoreCase("Fire")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Firestone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Electric")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Electrostone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Water")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Waterstone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Frozen")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Frozenstone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Undead")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Undeadstone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Holy")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Holystone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Ghost")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Ghoststone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Air")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Airstone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Nature")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Naturestone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Elemental")){
                    Items.GiveElementalStone(p);
                }
            }else {
                p.sendMessage("§cWrong usage: /stone <Stone> <Level>");
            }
        }
        return true;
    }

    @Override
    public List<String> onTabComplete( CommandSender sender,  Command command,  String label,  String[] args) {
        final List<String> completions = new ArrayList<>();
        List<String> commands = new ArrayList<>();
        commands.add("Fire");
        commands.add("Electric");
        commands.add("Water");
        commands.add("Frozen");
        commands.add("Undead");
        commands.add("Holy");
        commands.add("Ghost");
        commands.add("Air");
        commands.add("Nature");
        commands.add("Elemental");
        StringUtil.copyPartialMatches(args[0], commands, completions);
        return completions;
    }
}
