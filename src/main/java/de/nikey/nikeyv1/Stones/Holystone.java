package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.CircleEffect;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Holystone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;


    @EventHandler
    public void onEntityRegainHealth(EntityRegainHealthEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        if (event.getEntity() instanceof Player){
            Player p = (Player) event.getEntity();
            String stone = config.getString(p.getName() + ".stone");
            if (stone.equalsIgnoreCase("Holy")){
                if (config.getInt(p.getName()+".level") == 3||config.getInt(p.getName()+".level") == 4){
                    event.setAmount(event.getAmount()+0.4);
                }else if (config.getInt(p.getName()+".level") >= 5){
                    event.setAmount(event.getAmount()+0.6);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§aHoly Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Holy");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i >= 10){
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        event.setCancelled(true);
                        p.updateInventory();
                        remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*100);
                        if (i ==10 || i == 11){
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(20, 20, 20).size();
                            if (players >18){
                                players = 18;
                            }
                            p.setMaxHealth(21+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,1,0);
                            p.spawnParticle(Particle.HEART,location,3);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*40);
                        }else if (i >= 12) {
                            World world = p.getWorld();
                            int players = p.getNearbyEntities(20, 20, 20).size();
                            if (players >18)players = 18;
                            p.removePotionEffect(PotionEffectType.WEAKNESS);
                            p.removePotionEffect(PotionEffectType.POISON);
                            p.removePotionEffect(PotionEffectType.DARKNESS);
                            p.removePotionEffect(PotionEffectType.LEVITATION);
                            p.setMaxHealth(21+players);
                            p.setHealth(20);
                            Location location = p.getLocation().add(0,2,0);
                            CircleEffect effect = new CircleEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.start();
                            p.spawnParticle(Particle.HEART,location,3);
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    p.setMaxHealth(20);
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*40);
                        }
                    }            
                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR){
                if (i == 15||i == 16||i == 17){
                    if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ability.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*180);
                        //Cooldown-Ability
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                       for (Entity e : p.getNearbyEntities(20,15,20)){
                           if (e instanceof Player) {
                               Player player =(Player) e;
                               double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                               armor = armor*1.5;
                               int players = p.getNearbyEntities(15, 15, 15).size();
                               if (players == 1 || players == 2){
                                   p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                               } else if (players  == 3||players == 4 || players == 5) {
                                   p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                               }else {
                                   p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                               }
                               player.damage(armor+8,p);
                           }
                           if (e instanceof LivingEntity){
                               LivingEntity entity = (LivingEntity) e;
                               entity.damage(8,p);
                           }
                       }
                    }
                } else if (i >= 18) {
                    if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ability.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*180);
                        //Cooldown-Ability
                        p.getWorld().playSound(p.getLocation(), Sound.BLOCK_BEACON_ACTIVATE,1,1);
                        for (Entity e : p.getNearbyEntities(20,20,20)){
                            if (e instanceof Player) {
                                Player player =(Player) e;
                                double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                armor = armor*1.65;
                                int players = p.getNearbyEntities(20, 20, 20).size();
                                if (players == 1 || players == 2){
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,0));
                                } else if (players  == 3||players == 4 || players == 5) {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,1));
                                }else {
                                    p.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,20*20,2));
                                }
                                player.damage(armor+12,p);
                            }
                            if (e instanceof LivingEntity){
                                LivingEntity entity = (LivingEntity) e;
                                entity.damage(12,p);
                            }
                        }
                    }
                }
            }
        }    
    }    
}
