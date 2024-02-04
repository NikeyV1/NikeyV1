package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Firestone implements Listener {
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> player = new ArrayList<>();
    private int timer;
    private int time;
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static long remainingTime1;
    public static long remainingTime2;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#e66b63")+"Fire Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            Player p = event.getPlayer();
            ItemStack item = event.getItem();
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Fire");
            config.set(p.getName()+".fire",null);
            config.set(p.getName()+".level",i);
            String stone = NikeyV1.getPlugin().getConfig().getString(p.getName() + ".stone");
            NikeyV1.getPlugin().saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                if (i >= 10) {
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
                        if (i == 10 || i == 11) {
                            timer = 20;
                            SphereEffect effect = new SphereEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.duration = 20000;
                            effect.particles = 100;
                            effect.particle = Particle.FLAME;
                            effect.radius = 20.0;
                            effect.start();
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(),20,20,20)){
                                        if (e instanceof LivingEntity){
                                            LivingEntity entity = (LivingEntity) e;
                                            entities.add(entity);
                                            if (entity != p){
                                                e.setVisualFire(true);
                                                entity.damage(2);
                                            }
                                            if (timer == 0){
                                                entity.setVisualFire(false);
                                                entities.forEach(entity1 -> entity1.setVisualFire(false));
                                                entities.clear();
                                                cancel();
                                                return;
                                            }
                                        }
                                    }
                                    timer--;
                                }
                            };
                            runnable.runTaskTimer(NikeyV1.getPlugin(),0,20);
                        } else if (i >= 12) {
                            timer = 20;
                            SphereEffect effect = new SphereEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.duration = 20000;
                            effect.particles = 200;
                            effect.particle = Particle.FLAME;
                            effect.radius = 30.0;
                            effect.start();
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 30, 30, 30)) {
                                        if (e instanceof LivingEntity) {
                                            LivingEntity entity = (LivingEntity) e;
                                            entities.add(entity);
                                            if (entity != p) {
                                                e.setVisualFire(true);
                                                entity.damage(3);
                                            }
                                            if (timer == 0) {
                                                entity.setVisualFire(false);
                                                entities.forEach(entity1 -> entity1.setVisualFire(false));
                                                entities.clear();
                                                cancel();
                                                return;
                                            }
                                        }
                                    }
                                    timer--;
                                }
                            };
                            runnable.runTaskTimer(NikeyV1.getPlugin(), 0, 20);
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (i == 15||i == 16||i == 17){
                    if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                        event.setCancelled(true);
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
                        time = 10;
                        AnimatedBallEffect effect = new AnimatedBallEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.particle = Particle.FLAME;
                        effect.duration = 20000;
                        effect.visibleRange = 100;
                        effect.start();
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (timer == 0){
                                    cancel();
                                    return;
                                }else {
                                    double health = p.getHealth();
                                    p.setHealth(health+0.8);
                                }
                                time--;
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),40,40);
                    }
                }else if (i >=18){
                    if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                        event.setCancelled(true);
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
                        time = 10;
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (timer == 0){
                                    cancel();
                                    return;
                                }else {
                                    double health = p.getHealth();
                                    p.setHealth(health+1.2);
                                }
                                time--;
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),40,40);
                        AnimatedBallEffect effect = new AnimatedBallEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.particle = Particle.FLAME;
                        effect.duration = 20000;
                        effect.visibleRange = 100;
                        effect.start();
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        Entity d = event.getDamager();
        Entity entity = event.getEntity();
        if (entity instanceof Player && d instanceof LivingEntity){
            Player p = (Player) entity;
            LivingEntity damager = (LivingEntity) d;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (ability.containsKey(p.getUniqueId())){
                if (i == 15||i == 16||i == 17){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.3);
                        damager.damage(damage*0.2,entity);
                    }
                }else if (i >= 18){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.75);
                        damager.damage(damage*0.4,entity);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) entity;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (ability.containsKey(p.getUniqueId())){
                if (i == 15||i == 16||i == 17){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.30);
                    }
                }else if (i >= 18){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.5);
                    }
                }
            }
        }
    }
}