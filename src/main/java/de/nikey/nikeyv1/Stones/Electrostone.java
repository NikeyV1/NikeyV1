package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.effect.TornadoEffect;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LightningStrike;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Electrostone implements Listener {
    private static ArrayList<Entity> stunned = new ArrayList<>();
    public static HashMap<Player, Integer> cooldown = new HashMap<>();
    public static HashMap<Player, Integer> ability = new HashMap<>();
    private int timer;
    int c;
    int a;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eElektro Stein")&&
                event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".elektro",null);
            config.set(p.getName()+".stone","Elektro");
            config.set(p.getName()+".level",i);
            NikeyV1.plugin.saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i ==10 || i == 11){
                    if (!cooldown.containsKey(p)){
                        cooldown.put(p,0);
                        c = 0;
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                c++;
                                if (!p.isOnline()){
                                    config.set(p.getName()+"."+stone+"."+"cooldown1.time",c);
                                    config.set(p.getName()+"."+stone+"."+"cooldown1.timer",true);
                                    NikeyV1.getPlugin().saveConfig();
                                    cancel();
                                }
                                if (cooldown.get(p) < 100){
                                    cooldown.replace(p,c);
                                }else {
                                    c=0;
                                    cooldown.remove(p);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0L,20);
                        timer = 20;
                        World world = p.getWorld();
                        BukkitRunnable runnable = new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (timer == 0){
                                    cancel();
                                    return;
                                }else {
                                    int x = (int) (p.getLocation().getX());
                                    int z = (int) (p.getLocation().getZ());
                                    int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
                                    int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
                                    int randomY = world.getHighestBlockYAt(randomX,randomZ);
                                    Location location = new Location(world,randomX,randomY+1,randomZ);
                                    LightningStrike lightningStrike = world.strikeLightning(location);
                                    for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
                                        if (e instanceof LivingEntity){
                                            LivingEntity entity = (LivingEntity) e;
                                            if (entity != p){
                                                entity.damage(8,lightningStrike);
                                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*2,2));
                                            }
                                        }
                                    }
                                    TornadoEffect effect = new TornadoEffect(NikeyV1.em);
                                    effect.setLocation(location);
                                    effect.maxTornadoRadius = 3F;
                                    effect.tornadoParticle = Particle.ELECTRIC_SPARK;
                                    effect.start();
                                }
                                timer--;
                            }
                        };
                        runnable.runTaskTimer(NikeyV1.getPlugin(),20,20);
                    }

                } else if (i >= 12) {
                    if (!cooldown.containsKey(p)){
                        cooldown.put(p,0);
                        c = 0;
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                c++;
                                if (!p.isOnline()){
                                    config.set(p.getName()+"."+stone+"."+"cooldown1.time",c);
                                    config.set(p.getName()+"."+stone+"."+"cooldown1.timer",true);
                                    NikeyV1.getPlugin().saveConfig();
                                    cancel();
                                }
                                if (cooldown.get(p) < 100){
                                    cooldown.replace(p,c);
                                }else {
                                    c=0;
                                    cooldown.remove(p);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0L,20);
                        timer = 20;
                        World world = p.getWorld();
                        BukkitRunnable runnable = new BukkitRunnable() {

                            @Override
                            public void run() {
                                if (timer == 0){
                                    cancel();
                                    return;
                                }else {
                                    int x = (int) (p.getLocation().getX());
                                    int z = (int) (p.getLocation().getZ());
                                    int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
                                    int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
                                    int randomY = world.getHighestBlockYAt(randomX,randomZ);
                                    Location location = new Location(world,randomX,randomY+1,randomZ);
                                    LightningStrike lightningStrike = world.strikeLightning(location);
                                    for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
                                        if (e instanceof LivingEntity){
                                            LivingEntity entity = (LivingEntity) e;
                                            if (entity != p){
                                                entity.damage(10,lightningStrike);
                                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,2));
                                            }
                                        }
                                    }
                                    TornadoEffect effect = new TornadoEffect(NikeyV1.em);
                                    effect.setLocation(location);
                                    effect.maxTornadoRadius = 3F;
                                    effect.tornadoParticle = Particle.ELECTRIC_SPARK;
                                    effect.start();
                                }
                                timer--;
                            }
                        };
                        runnable.runTaskTimer(NikeyV1.getPlugin(),20,20);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§eElektro Stein")){
                Entity entity = event.getEntity();
                String[] arr = p.getInventory().getItemInMainHand().getLore().get(1).split(":");
                int i = Integer.parseInt(arr[1]);
                FileConfiguration config = NikeyV1.plugin.getConfig();
                if (i >= 15){
                    String stone = config.getString(p.getName() + ".stone");
                    if (!ability.containsKey(p)){
                        ability.put(p,0);
                        a = 0;
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                a++;
                                if (!p.isOnline()){
                                    config.set(p.getName()+"."+stone+"."+"cooldown2.time",a);
                                    config.set(p.getName()+"."+stone+"."+"cooldown2.timer",true);
                                    NikeyV1.getPlugin().saveConfig();
                                    cancel();
                                }
                                if (ability.get(p) < 180){
                                    ability.replace(p,a);
                                }else {
                                    a=0;
                                    ability.remove(p);
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0L,20);
                        stunned.add(entity);
                        entity.getWorld().strikeLightning(entity.getLocation());
                        CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                        effect.setEntity(entity);
                        effect.duration = 5000;
                        effect.particles = 80;
                        effect.particle = Particle.FLASH;
                        effect.start();
                        for (Entity e : entity.getNearbyEntities(1,1,1)){
                            if (e != p){
                                e.getWorld().strikeLightningEffect(e.getLocation());
                                stunned.add(e);
                                if (entity instanceof LivingEntity) {
                                    ((org.bukkit.entity.LivingEntity)entity).damage(5,p);
                                }
                                BukkitRunnable runnable = new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        stunned.clear();
                                    }
                                };
                                runnable.runTaskLater(NikeyV1.getPlugin(),20*5);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityMove(EntityMoveEvent event) {
        LivingEntity entity = event.getEntity();
        if (stunned.contains(entity)){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player entity = event.getPlayer();
        if (stunned.contains(entity)){
            Location from = event.getFrom();
            Location to = event.getTo();
            double x = from.getX();
            double z = from.getZ();
            double xx = to.getX();
            double zz = to.getZ();
            if (x != xx||z !=zz){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTeleport(EntityTeleportEvent event) {
        if (stunned.contains(event.getEntity())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (stunned.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
