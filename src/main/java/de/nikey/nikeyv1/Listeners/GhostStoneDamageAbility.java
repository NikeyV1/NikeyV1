package de.nikey.nikeyv1.Listeners;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.api.Stone;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@SuppressWarnings("ALL")
public class GhostStoneDamageAbility implements Listener {
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static long remainingTime2;

    private final Set<Player> blockedPlayers = new HashSet<>();
    public static HashMap<Player, Integer> timer = new HashMap<>();
    public static HashMap<Player, Integer> stealtimer = new HashMap<>();

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (!(event.getDamager() instanceof Player && event.getEntity() instanceof Player))return;
        Player player = (Player) event.getDamager();
        Player victim = (Player) event.getEntity();

        if (player.getInventory().getItemInMainHand().getItemMeta() == null)return;
        if (Stone.whatStone(player.getInventory().getItemInMainHand()).equalsIgnoreCase("Ghost")){
            int l = Stone.getStoneLevelFromItem(player.getInventory().getItemInMainHand());
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(player.getName()+".stone","Ghost");
            config.set(player.getName()+".level",l);
            NikeyV1.getPlugin().saveConfig();
            if (l >= 15) {
                if (!(ability.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                    ability.put(player.getUniqueId(),System.currentTimeMillis() + (180*1000));

                    if (l == 15) {
                        setVisibility(victim,player);
                        timer.put(player,80);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                timer.replace(player,timer.get(player)-1);
                                if (timer.get(player) == 0){
                                    cancel();
                                }

                                Location loc = player.getLocation().add(0, 1, 0);
                                Location target = victim.getLocation().add(0, 1, 0);
                                double distance = loc.distance(target);
                                double step = 0.2;
                                for (double d = 0; d < distance; d += step) {
                                    double t = d / distance;
                                    double x = loc.getX() + (target.getX() - loc.getX()) * t;
                                    double y = loc.getY() + (target.getY() - loc.getY()) * t;
                                    double z = loc.getZ() + (target.getZ() - loc.getZ()) * t;
                                    player.getWorld().spawnParticle(Particle.ASH, new Location(player.getWorld(), x, y, z), 2);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,5);
                    } else if (l == 16 ) {
                        setVisibility(victim,player);
                        timer.put(player,120);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                timer.replace(player,timer.get(player)-1);
                                if (timer.get(player) == 0){
                                    cancel();
                                }

                                Location loc = player.getLocation().add(0, 1, 0);
                                Location target = victim.getLocation().add(0, 1, 0);
                                double distance = loc.distance(target);
                                double step = 0.2;
                                for (double d = 0; d < distance; d += step) {
                                    double t = d / distance;
                                    double x = loc.getX() + (target.getX() - loc.getX()) * t;
                                    double y = loc.getY() + (target.getY() - loc.getY()) * t;
                                    double z = loc.getZ() + (target.getZ() - loc.getZ()) * t;
                                    player.getWorld().spawnParticle(Particle.ASH, new Location(player.getWorld(), x, y, z), 2);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,5);
                    }else if (l == 17 || l == 18) {
                        startHealthSteal(player,victim);
                        setVisibility(victim,player);
                        timer.put(player,120);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                timer.replace(player,timer.get(player)-1);
                                if (timer.get(player) == 0){
                                    cancel();
                                }

                                Location loc = player.getLocation().add(0, 1, 0);
                                Location target = victim.getLocation().add(0, 1, 0);
                                double distance = loc.distance(target);
                                double step = 0.2;
                                for (double d = 0; d < distance; d += step) {
                                    double t = d / distance;
                                    double x = loc.getX() + (target.getX() - loc.getX()) * t;
                                    double y = loc.getY() + (target.getY() - loc.getY()) * t;
                                    double z = loc.getZ() + (target.getZ() - loc.getZ()) * t;
                                    player.getWorld().spawnParticle(Particle.ASH, new Location(player.getWorld(), x, y, z), 2);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,5);
                    }else if (l >= 19) {
                        startHealthSteal(player,victim);
                        setVisibility(victim,player);
                        timer.put(player,160);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                timer.replace(player,timer.get(player)-1);
                                if (timer.get(player) == 0){
                                    cancel();
                                }

                                Location loc = player.getLocation().add(0, 1, 0);
                                Location target = victim.getLocation().add(0, 1, 0);
                                double distance = loc.distance(target);
                                double step = 0.2;
                                for (double d = 0; d < distance; d += step) {
                                    double t = d / distance;
                                    double x = loc.getX() + (target.getX() - loc.getX()) * t;
                                    double y = loc.getY() + (target.getY() - loc.getY()) * t;
                                    double z = loc.getZ() + (target.getZ() - loc.getZ()) * t;
                                    player.getWorld().spawnParticle(Particle.ASH, new Location(player.getWorld(), x, y, z), 2);
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,5);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (!(event.getEntity() instanceof Player))return;

        Player player = (Player) event.getEntity();
        if (blockedPlayers.contains(player) && event.isGliding()) {
            player.setGliding(false);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerElytraBoost(PlayerElytraBoostEvent event) {
        Player player = event.getPlayer();
        if (blockedPlayers.contains(player)) {
            event.setCancelled(true);
        }
    }


    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (blockedPlayers.contains(player)) {
            event.setCancelled(true);
        }
    }


    public void startHealthSteal(Player user , Player victim) {
        int level = Stone.getStoneLevel(user);
        if (level <= 18) {
            stealtimer.put(user,30);
        }else if (level >= 19) {
            stealtimer.put(user,40);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                stealtimer.replace(user,stealtimer.get(user)-1);
                if (stealtimer.get(user) == 0 || !victim.isValid() || !user.isValid()) cancel();

                if (level == 15 || level == 16) {
                    if (victim.getAbsorptionAmount() == 0) {
                        if (victim.getHealth()-0.5F >= 0.5F) {
                            victim.setHealth(victim.getHealth()-0.5F);
                            victim.playHurtAnimation(0);
                        }else {
                            victim.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                        }
                    }else {
                        if (victim.getAbsorptionAmount()-0.5F >= 0.5F) {
                            victim.setAbsorptionAmount(victim.getAbsorptionAmount()-0.5F);
                            victim.playHurtAnimation(0);
                        }else {
                            victim.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                        }
                    }
                    if (user.getHealth() < 20) {
                        user.setHealth(user.getHealth()+0.5F);
                    }
                } else if (level >= 17) {
                    if (victim.getAbsorptionAmount() == 0) {
                        if (victim.getHealth()-1 >= 1) {
                            victim.setHealth(victim.getHealth()-1);
                            victim.playHurtAnimation(0);
                        }else {
                            victim.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                        }
                    }else {
                        if (victim.getAbsorptionAmount()-1 >= 1) {
                            victim.setAbsorptionAmount(victim.getAbsorptionAmount()-1);
                            victim.playHurtAnimation(0);
                        }
                    }
                    if (user.getHealth() <= 19) {
                        user.setHealth(user.getHealth()+1);
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),0,20);
    }

    public void setVisibility(Player victim, Player damager) {
        int level = Stone.getStoneLevel(damager);
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player != victim && player != damager) {
                player.hidePlayer(NikeyV1.getPlugin(), victim);
                player.hidePlayer(NikeyV1.getPlugin(), damager);
                // Hide all other players from victim and damager
                victim.hidePlayer(NikeyV1.getPlugin(), player);
                damager.hidePlayer(NikeyV1.getPlugin(), player);
            }

        }
        victim.showPlayer(NikeyV1.getPlugin(), damager);
        damager.showPlayer(NikeyV1.getPlugin(), victim);

        victim.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("§8§lLocked!"));
        damager.spigot().sendMessage(ChatMessageType.ACTION_BAR,new TextComponent("§8§lLocked!"));

        //Effects
        if (level == 15) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*20,2,true));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,20*20,1,true));
        }else if (level == 16 ||level == 17) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*30,2,true));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,20*30,1,true));
        }else if (level == 18) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*30,2,true));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,20*30,1,true));
            damager.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,20*30,1,true));
        }else if (level >= 19) {
            victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*40,2,true));
            victim.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE,20*40,1,true));
            damager.addPotionEffect(new PotionEffect(PotionEffectType.STRENGTH,20*40,1,true));
        }
        damager.setHealth(damager.getMaxHealth());

        // Add victim and damager to blockedPlayers set to prevent teleportation
        blockedPlayers.add(victim);
        blockedPlayers.add(damager);

        if (level == 15) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(NikeyV1.getPlugin(), victim);
                        player.showPlayer(NikeyV1.getPlugin(), damager);
                        // Show all other players to victim and damager
                        victim.showPlayer(NikeyV1.getPlugin(), player);
                        damager.showPlayer(NikeyV1.getPlugin(), player);
                    }

                    // Remove victim and damager from blockedPlayers set
                    blockedPlayers.remove(victim);
                    blockedPlayers.remove(damager);
                }
            }.runTaskLater(NikeyV1.getPlugin(), 20 * 20);
        }else if (level == 16 || level == 17 || level == 18) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(NikeyV1.getPlugin(), victim);
                        player.showPlayer(NikeyV1.getPlugin(), damager);
                        // Show all other players to victim and damager
                        victim.showPlayer(NikeyV1.getPlugin(), player);
                        damager.showPlayer(NikeyV1.getPlugin(), player);
                    }

                    // Remove victim and damager from blockedPlayers set
                    blockedPlayers.remove(victim);
                    blockedPlayers.remove(damager);
                }
            }.runTaskLater(NikeyV1.getPlugin(), 20 * 30);
        } else if (level >= 19) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.showPlayer(NikeyV1.getPlugin(), victim);
                        player.showPlayer(NikeyV1.getPlugin(), damager);
                        // Show all other players to victim and damager
                        victim.showPlayer(NikeyV1.getPlugin(), player);
                        damager.showPlayer(NikeyV1.getPlugin(), player);
                    }

                    // Remove victim and damager from blockedPlayers set
                    blockedPlayers.remove(victim);
                    blockedPlayers.remove(damager);
                }
            }.runTaskLater(NikeyV1.getPlugin(), 20 * 40);
        }
    }
}
