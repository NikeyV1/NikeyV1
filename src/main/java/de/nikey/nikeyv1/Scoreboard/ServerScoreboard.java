package de.nikey.nikeyv1.Scoreboard;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Stones.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;

@SuppressWarnings("ALL")
public class ServerScoreboard extends ScoreboardBuilder {
    public ServerScoreboard(Player player) {
        super(player, "  §8>> §6§l§nNikey§r §8<<  ");
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        Integer level = config.getInt(player.getName() + ".level");
        stone();
        update();
        run();
        ability2();
        if (level >=20) {
            masterability();
        }else {
            hideMasterability();
        }
    }


    @Override
    public void update() {
        BukkitRunnable runnable = new BukkitRunnable() {
            @Override
            public void run() {
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                setScore("§7Level: "+ChatColor.AQUA+config.getString(player.getName()+".level"),5);
            }
        };
        runnable.runTaskTimer(NikeyV1.getPlugin(),120,120);
    }

    @Override
    public void createScoreboard() {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String stone = config.getString(player.getName() + ".stone");
        setScore("§7Level: "+ChatColor.AQUA+config.getString(player.getName()+".level"),5);
        setScore("§7Ability 1: §aReady",4);
        setScore("§7Ability 2: §aReady",3);
        setScore("§7Master Ability: §aReady",2);
        setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
        setScore("§7Online Players: "+ChatColor.DARK_PURPLE+ Bukkit.getOnlinePlayers().size(),0);
    }

    private void hideMasterability() {
        setScore("§7§kMaster Ability: Unusable",2);
    }


    private void masterability() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (stone.equalsIgnoreCase("Water")){
                    //Ability 2
                    long remainingTime3 = Waterstone.cooldown2.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime3/1000);
                    //
                    if (a == 0){
                        setScore("§7Master Ability: §aReady",2);
                    }else {
                        setScore("§7Master Ability: §c"+a +"/300",2);
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),10,10);
    }

    private void ability2() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (stone.equalsIgnoreCase("Electric")){
                    //Ability 2
                    long remainingTime2 = Electrostone.ability.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime2/1000);
                    //
                    if (a == 0){
                        setScore("§7Ability 2: §aReady",3);
                    }else {
                        setScore("§7Ability 2: §c"+a +"/180",3);
                    }
                }else if (stone.equalsIgnoreCase("Fire")){
                    //Ability 2
                    long remainingTime2 = Firestone.ability.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime2/1000);
                    //
                    if (a == 0){
                        setScore("§7Ability 2: §aReady",3);
                    }else {
                        setScore("§7Ability 2: §c"+a +"/180",3);
                    }
                }else if (stone.equalsIgnoreCase("Water")){
                    //Ability 2
                    long remainingTime2 = Waterstone.ability.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime2/1000);
                    //
                    if (a == 0){
                        setScore("§7Ability 2: §aReady",3);
                    }else {
                        setScore("§7Ability 2: §c"+a +"/180",3);
                    }
                }else if (stone.equalsIgnoreCase("Frozen")){
                    //Ability 2
                    long remainingTime2 = Frozenstone.ability.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime2/1000);
                    //
                    if (a == 0){
                        setScore("§7Ability 2: §aReady",3);
                    }else {
                        setScore("§7Ability 2: §c"+a +"/180",3);
                    }
                }else if (stone.equalsIgnoreCase("Undead")){
                    long remainingTime2 = Undeadstone.ability.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime2/1000);
                    //
                    if (a == 0){
                        setScore("§7Ability 2: §aReady",3);
                    }else {
                        setScore("§7Ability 2: §c"+a +"/180",3);
                    }
                }else if (stone.equalsIgnoreCase("Holy")){
                    long remainingTime2 = Holystone.ability.get(player.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remainingTime2/1000);
                    if (a == 0){
                        setScore("§7Ability 2: §aReady",3);
                    }else {
                        setScore("§7Ability 2: §c"+a +"/180",3);
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),10,10);
    }
    private void run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
                if (stone.equalsIgnoreCase("Electric")){
                    long remainingTime = Electrostone.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                    int i = (int) (remainingTime/1000);
                    if (i == 0){
                        setScore("§7Ability 1: §aReady",4);
                    }else {
                        setScore("§7Ability 1: §c"+ i+"/100",4);
                    }
                }else if (stone.equalsIgnoreCase("Fire")){
                    long remainingTime = Firestone.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                    int i = (int) (remainingTime/1000);
                    if (i == 0){
                        setScore("§7Ability 1: §aReady",4);
                    }else {
                        setScore("§7Ability 1: §c"+ i+"/100",4);
                    }
                }else if (stone.equalsIgnoreCase("Water")){
                    long remainingTime = Waterstone.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                    int i = (int) (remainingTime/1000);
                    if (i == 0){
                        setScore("§7Ability 1: §aReady",4);
                    }else {
                        setScore("§7Ability 1: §c"+ i+"/100",4);
                    }
                }else if (stone.equalsIgnoreCase("Frozen")){
                    long remainingTime = Frozenstone.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                    int i = (int) (remainingTime/1000);
                    if (i == 0){
                        setScore("§7Ability 1: §aReady",4);
                    }else {
                        setScore("§7Ability 1: §c"+ i+"/100",4);
                    }
                }else if (stone.equalsIgnoreCase("Undead")){
                    long remainingTime = Undeadstone.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                    int i = (int) (remainingTime/1000);
                    if (i == 0){
                        setScore("§7Ability 1: §aReady",4);
                    }else {
                        setScore("§7Ability 1: §c"+ i+"/100",4);
                    }
                }else if (stone.equalsIgnoreCase("Holy")){
                    long remainingTime = Holystone.cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                    int i = (int) (remainingTime/1000);
                    if (i == 0){
                        setScore("§7Ability 1: §aReady",4);
                    }else {
                        setScore("§7Ability 1: §c"+ i+"/100",4);
                    }
                }
                setScore("§7Ping: "+ChatColor.DARK_PURPLE+ player.getPing(),1);
                setScore("§7Online Players: "+ChatColor.DARK_PURPLE+ Bukkit.getOnlinePlayers().size(),0);
            }
        }.runTaskTimer(NikeyV1.getPlugin(),10,10);
    }


    private void stone() {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("Fire")){
            setScore("§7Stone: "+ChatColor.RED +config.getString(player.getName()+".stone"),6);
        }else if (stone.equalsIgnoreCase("Electric")){
            setScore("§7Stone: "+ChatColor.YELLOW +config.getString(player.getName()+".stone"),6);
        }else if (stone.equalsIgnoreCase("Water")){
            setScore("§7Stone: "+ChatColor.BLUE +config.getString(player.getName()+".stone"),6);
        }else if (stone.equalsIgnoreCase("Frozen")){
            setScore("§7Stone: "+ChatColor.DARK_AQUA +config.getString(player.getName()+".stone"),6);
        }else if (stone.equalsIgnoreCase("Undead")){
            setScore("§7Stone: "+ net.md_5.bungee.api.ChatColor.of("#100613")+config.getString(player.getName()+".stone"),6);
        }else if (stone.equalsIgnoreCase("Holy")){
            setScore("§7Stone: "+ net.md_5.bungee.api.ChatColor.of("#47d147")+config.getString(player.getName()+".stone"),6);
        }
    }
}
