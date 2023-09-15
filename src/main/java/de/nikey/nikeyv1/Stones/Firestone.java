package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Scoreboard.ServerScoreboard;
import de.slikey.effectlib.effect.*;
import de.slikey.effectlib.util.ParticleEffect;
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
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerToggleSprintEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;

public class Firestone implements Listener {
    private ArrayList<Entity> entities = new ArrayList<>();
    private int timer;
    public static HashMap<Player, Integer> cooldown = new HashMap<>();
    int c;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null){
            return;
        }
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#e66b63")+"Lava Stein")&&
                event.getItem().getType() == Material.FIREWORK_STAR){
            Player p = event.getPlayer();
            ItemStack item = event.getItem();
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".stone","Fire");
            config.set(p.getName()+".fire",null);
            config.set(p.getName()+".level",i);
            String stone = NikeyV1.getPlugin().getConfig().getString(p.getName() + ".stone");
            NikeyV1.plugin.saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                if (i == 3){
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0,true,false));
                } else if (i == 10 || i == 11) {
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
                        SphereEffect effect = new SphereEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.duration = 20000;
                        effect.particles = 120;
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
                    }
                }else if (i >= 12) {
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
                                for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(),30,30,30)){
                                    if (e instanceof LivingEntity){
                                        LivingEntity entity = (LivingEntity) e;
                                        entities.add(entity);
                                        if (entity != p){
                                            e.setVisualFire(true);
                                            entity.damage(3);
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
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (i <= 4) {

                }else if (i <= 9){

                }else if (i <= 14){

                }else if (i <= 19){

                }
            }

        }
    }
}