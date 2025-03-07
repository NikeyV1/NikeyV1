package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.nikey.nikeyv1.Util.Items;
import de.nikey.nikeyv1.api.ConfigAPI;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

@SuppressWarnings("ALL")
public class FirstJoin implements Listener {
    int level = ConfigAPI.getStartLevel();
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player p = event.getPlayer();
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (config.getString(p.getName()+".level") == null){
            Random random = new Random();
            int i = random.nextInt(9);
            if (i == 0){
                Items.Firestone(p,level);
                p.sendTitle("§d§kR§r§cFire Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Fire");
                config.set(p.getName()+".level", level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 1){
                Items.Electrostone(p,3);
                p.sendTitle("§d§kR§r§eElectric Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Electric");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 2){
                Items.Waterstone(p,level);
                p.sendTitle("§d§kR§r§9Water Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Water");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 3){
                Items.Frozenstone(p,level);
                p.sendTitle("§d§kR§r§3Frozen Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Frozen");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 4){
                Items.Undeadstone(p,level);
                p.sendTitle("§d§kR§r§0Undead Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Undead");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 5){
                Items.Holystone(p,level);
                p.sendTitle("§d§kR§r§aHoly Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Holy");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 6){
                Items.Ghoststone(p,level);
                p.sendTitle("§d§kR§r§fGhost Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Ghost");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 7){
                Items.Airstone(p,level);
                p.sendTitle("§d§kR§r"+ Color.fromRGB(180, 212, 206) +"Air Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Air");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }else if (i == 8){
                Items.Naturestone(p,level);
                p.sendTitle("§d§kR§r"+ Color.fromRGB(34, 139, 34) +"Nature Stone§r§d§kR","");
                config.set(p.getName()+".stone", "Nature");
                config.set(p.getName()+".level",level);
                NikeyV1.getPlugin().saveConfig();
            }
            config.set(p.getName()+".det","all");
            NikeyV1.getPlugin().saveConfig();
        }
        new ServerScoreboard(p);
    }
}
