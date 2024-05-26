package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.player.PlayerConnectionCloseEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftHumanEntity;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftPlayer;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Ghoststone implements Listener {

    private HashMap<UUID, Integer> playerHitCount = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player)) return;

    Player player = (Player) event.getEntity();
    String name = Stone.getStoneName(player);
    int level = Stone.getStoneLevel(player);

    if (name.equalsIgnoreCase("Ghost") && level >= 7) {
        int hitCount = playerHitCount.getOrDefault(player.getUniqueId(), 0);
        hitCount++;
        if (level == 7) {
            if (hitCount % 15 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),8,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        } else if (level == 8) {
            if (hitCount % 12 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),8,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        }else if (level >= 9) {
            if (hitCount % 9 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),8,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        }
        playerHitCount.put(player.getUniqueId(), hitCount);
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
                if (!arrow.isCritical() && arrow.canHitEntity(event.getEntity())) {
                    shooter.getWorld().spawnParticle(Particle.DUST_PLUME,shooter.getLocation().add(0,1,0),8,0.1,0.1,0.1);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
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
                        for (Entity e : player.getNearbyEntities(7,7,7)) {
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
                        for (Entity e : player.getNearbyEntities(10,10,10)) {
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
        }.runTaskTimer(NikeyV1.getPlugin(),5,2);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
    }
}