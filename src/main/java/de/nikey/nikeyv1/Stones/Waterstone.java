package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.Util.Tornado;
import de.slikey.effectlib.effect.EquationEffect;
import de.slikey.effectlib.effect.FountainEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Waterstone implements Listener {
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;
    @EventHandler
    public void onEntityAirChange(EntityAirChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player p = (Player) entity;
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            if (config.getString(p.getName()+".stone").equalsIgnoreCase("Water") && config.getInt(p.getName()+".level") >=3 && p.isInWater()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Water Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Water");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction().isRightClick()) {
                if (i >= 10) {
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 100);
                        //cooldown-ability
                        if (i ==10 || i == 11){
                            timer = 15;
                            Location location = p.getLocation();
                            FountainEffect effect = new FountainEffect(NikeyV1.em);
                            effect.setLocation(location);
                            effect.duration = 20000;
                            effect.particlesStrand = 50;
                            effect.particlesSpout = 70;
                            effect.start();
                            //Ability
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }
                                    for (Entity e : location.getNearbyEntities(10,8,10)){
                                        if (e == p){
                                            if (p.getHealth() < 18)p.setHealth(p.getHealth()+2);
                                        } else {
                                            if (e instanceof LivingEntity){
                                                LivingEntity entity = (LivingEntity) e;
                                                entity.damage(2);
                                            }
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(), 30,30);
                        }else if (i >= 12) {
                            timer = 15;
                            Location location = p.getLocation();
                            FountainEffect effect = new FountainEffect(NikeyV1.em);
                            effect.setLocation(location);
                            effect.duration = 20000;
                            effect.particlesStrand = 50;
                            effect.particlesSpout = 70;
                            effect.start();
                            //Ability
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }
                                    for (Entity e : location.getNearbyEntities(10,8,10)){
                                        if (e == p){
                                            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400,0,false));
                                            if (p.getHealth() < 18)p.setHealth(p.getHealth()+2);
                                        } else {
                                            if (e instanceof LivingEntity){
                                                LivingEntity entity = (LivingEntity) e;
                                                entity.damage(4);
                                            }
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(), 30,30);
                        }
                    }
                }
            }else if (event.getAction()==Action.LEFT_CLICK_AIR){
                if (p.isSneaking()) {
                    if (i == 15||i == 16||i == 17){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {

                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 40);
                            e.getLocation().getWorld().createExplosion(e.getLocation(),2F);
                            if (e instanceof LivingEntity){
                                LivingEntity entity = (LivingEntity) e;
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400,0));
                                double maxHealth = entity.getMaxHealth();
                                double damage = 0.25 * maxHealth; // 25% der maximalen Herzen als Schaden
                                entity.damage(damage);
                                entity.setVisualFire(false);
                            }
                        }
                    }else if (i >=18){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {

                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 50);
                            if (e != null) {
                                e.getLocation().getWorld().createExplosion(e.getLocation(),2F);
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 450,0));
                                    double maxHealth = entity.getMaxHealth();
                                    double damage = 0.35 * maxHealth; // 35% der maximalen Herzen als Schaden
                                    entity.damage(damage);
                                    entity.setVisualFire(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§9Water Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Water");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (!p.isSneaking()) {
                if (i== 20){
                    if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown2.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
                        //Cooldown-Ability
                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f);
                        Location BlocksAway = p.getLocation().add(p.getLocation().getDirection().multiply(4));
                        Tornado.spawnTornado(NikeyV1.getPlugin(),BlocksAway,p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getType(),p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getData(),p.getLocation().getDirection().multiply(4),0.4,200,20*20,true,p,false);
                    }
                } else if (i == 21) {
                    if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown2.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
                        //Cooldown-Ability
                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f);
                        Location BlocksAway = p.getLocation().add(p.getLocation().getDirection().multiply(4));
                        Tornado.spawnTornado(NikeyV1.getPlugin(),BlocksAway,p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getType(),p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getData(),p.getLocation().getDirection().multiply(4.5),0.4,275,20*30,true,p,false);
                    }
                }
            }
        }
    }
}
