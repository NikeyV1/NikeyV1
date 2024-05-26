package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;

import java.util.List;

@SuppressWarnings("ALL")
public class Ghoststone implements Listener {
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        String stone = Stone.getStoneName(event.getPlayer());
        if (stone.equalsIgnoreCase("Ghost")) {
            Player player = event.getPlayer();
            int level = Stone.getStoneLevel(player);
            String attacking = Stone.getAttacking(player);
            if (level == 3) {
                for (Entity e : player.getNearbyEntities(4,4,4)) {
                    if (!(e instanceof LivingEntity)) continue;
                    if (e == player) continue;
                    LivingEntity entity = (LivingEntity) e;
                    if (attacking.equalsIgnoreCase("all")) {
                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                        if (!playersInSameTeam.contains(entity)) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                            entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                        }
                    } else if (attacking.equalsIgnoreCase("players")) {
                        if (entity instanceof Player) {
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                            }
                        }
                    }else if (attacking.equalsIgnoreCase("monsters")) {
                        if (entity instanceof Monster) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                            entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                        }
                    }else if (attacking.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player || entity instanceof Monster) {
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                            }
                        }
                    }
                }
            }else if (level == 4) {
                for (Entity e : player.getNearbyEntities(6,6,6)) {
                    if (!(e instanceof LivingEntity)) continue;
                    if (e == player) continue;
                    LivingEntity entity = (LivingEntity) e;
                    if (attacking.equalsIgnoreCase("all")) {
                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                        if (!playersInSameTeam.contains(entity)) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                            entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                        }
                    } else if (attacking.equalsIgnoreCase("players")) {
                        if (entity instanceof Player) {
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                            }
                        }
                    }else if (attacking.equalsIgnoreCase("monsters")) {
                        if (entity instanceof Monster) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                            entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                        }
                    }else if (attacking.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player || entity instanceof Monster) {
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                            }
                        }
                    }
                }
            }else if (level >= 5) {
                for (Entity e : player.getNearbyEntities(8,8,8)) {
                    if (!(e instanceof LivingEntity)) continue;
                    if (e == player) continue;
                    LivingEntity entity = (LivingEntity) e;
                    if (attacking.equalsIgnoreCase("all")) {
                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                        if (!playersInSameTeam.contains(entity)) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                            entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                        }
                    } else if (attacking.equalsIgnoreCase("players")) {
                        if (entity instanceof Player) {
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                            }
                        }
                    }else if (attacking.equalsIgnoreCase("monsters")) {
                        if (entity instanceof Monster) {
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                            entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                        }
                    }else if (attacking.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player || entity instanceof Monster) {
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*3,0,true,false,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*3,0,true,false,false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE,entity.getLocation().add(0,0.5F,0),0,0,0,0);
                            }
                        }
                    }
                }
            }
        }
    }



    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player shooter = (Player) event.getEntity().getShooter();
        String stoneName = Stone.getStoneName(shooter);
        int stoneLevel = Stone.getStoneLevel(shooter);
        if (stoneName.equalsIgnoreCase("Ghost") && stoneLevel >= 6) {
            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if (!arrow.isCritical()) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
