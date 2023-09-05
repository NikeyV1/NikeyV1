package de.nikey.nikeyv1.Commands;

import de.nikey.nikeyv1.Util.Items;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class stone implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        org.bukkit.entity.Player p = (Player) sender;
        if (command.getName().equalsIgnoreCase("stone") && p.isOp()){
            if (args.length == 2){
                String stone = args[0];
                if (stone.equalsIgnoreCase("Fire")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Firestone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }else if (stone.equalsIgnoreCase("Electro") ||stone.equalsIgnoreCase("Elektro")){
                    try {
                        int level = Integer.parseInt(args[1]);
                        Items.Electrostone(p,level);
                    }catch (NumberFormatException e){
                        p.sendMessage("§cTake a Number");
                    }
                }
            }else {
                p.sendMessage("§cThat doesnt work like so!");
            }
        }
        return true;
    }
}