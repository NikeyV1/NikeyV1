package de.nikey.nikeyv1.Listeners;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerRiptideEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerVelocityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GhostStoneDamageAbility implements Listener {
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static long remainingTime2;

    private final Set<Player> blockedPlayers = new HashSet<>();
    public static HashMap<Player, Integer> timer = new HashMap<>();

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
                if (ability.containsKey(player.getUniqueId()) && ability.get(player.getUniqueId()) > System.currentTimeMillis()){
                    event.setCancelled(true);
                    remainingTime2 = ability.get(player.getUniqueId()) - System.currentTimeMillis();
                }else {
                    ability.put(player.getUniqueId(),System.currentTimeMillis() + (180*1000));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            ability.remove(player.getUniqueId());
                            cancel();
                        }
                    }.runTaskLater(NikeyV1.getPlugin(),20*180);

                    if (l == 15) {
                        setVisibility(victim,player);
                        timer.put(player,100);
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

    public void setVisibility(Player victim, Player damager) {
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

        //Effects
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*30,1,true));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*30,1,true));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*30,1,true));
        damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*30,2,true));
        victim.setHealth(20);

        // Add victim and damager to blockedPlayers set to prevent teleportation
        blockedPlayers.add(victim);
        blockedPlayers.add(damager);

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
    }
}
