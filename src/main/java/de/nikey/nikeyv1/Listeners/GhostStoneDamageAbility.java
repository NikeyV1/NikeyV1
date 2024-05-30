package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.LineEffect;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EvokerFangs;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GhostStoneDamageAbility implements Listener {
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static long remainingTime2;

    private final Set<Player> blockedPlayers = new HashSet<>();

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
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                double distance = player.getLocation().distance(victim.getLocation());
                                LineEffect effect = new LineEffect(NikeyV1.em);
                                effect.setEntity(player);
                                effect.particle = Particle.ASH;
                                effect.particles = 10;
                                effect.setTarget(victim.getLocation());
                                effect.length = distance;
                                effect.iterations = 10;
                                effect.start();
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,10);
                    }
                }
            }
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

        damager.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*20,1,true));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING,20*20,1,true));
        victim.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*20,1,true));
        damager.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,20*20,2,true));
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
        }.runTaskLater(NikeyV1.getPlugin(), 20 * 20);
    }
}
