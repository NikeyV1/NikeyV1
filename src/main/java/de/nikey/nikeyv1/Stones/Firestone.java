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

public class Firestone implements Listener {
    private ArrayList<Entity> entities = new ArrayList<>();
    private int timer;


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
            config.set(p.getName()+".fire",null);
            config.set(p.getName()+".level",i);
            NikeyV1.plugin.saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (!config.getBoolean(p.getName()+"."+stone+".cooldown1"+".timer")){
                new ServerScoreboard(p);
            }
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                StoneCooldown1 stoneCooldows = new StoneCooldown1();
                switch (i){
                    case 1:
                    case 2:
                    case 3:
                        p.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0,true,false));
                        break;
                    case 5:
                        stoneCooldows.setTime(1);
                        stoneCooldows.setStopTime(100);
                        stoneCooldows.start(p);
                        if (!config.getBoolean(p.getName()+"."+stone+".cooldown1"+".timer")){
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
                        break;
                    case 10:
                        stoneCooldows.setTime(1);
                        stoneCooldows.setStopTime(100);
                        stoneCooldows.start(p);
                        if (!config.getBoolean(p.getName()+"."+stone+".cooldown1"+".timer")){
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
