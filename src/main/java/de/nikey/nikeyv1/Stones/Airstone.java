package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Airstone implements Listener {
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    public static HashMap<Player, Integer> timer = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (Stone.whatStone(item).equalsIgnoreCase("Air")) {
            int level = Stone.getStoneLevelFromItem(item);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(player.getName()+".stone","Air");
            config.set(player.getName()+".level",level);
            NikeyV1.getPlugin().saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (level >= 10){

                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!player.isSneaking() && level >= 15) {
                    if (!(ability.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                        ability.put(player.getUniqueId(), System.currentTimeMillis() + (180 * 1000));

                        if (level == 15) {
                            timer.put(player,6);
                        }else if (level == 16 || level == 17 || level == 18) {
                            timer.put(player,8);
                        }else {
                            timer.put(player,10);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (timer.get(player) == 0 || !player.isValid()) {
                                    cancel();
                                    return;
                                }

                                castKillerWail(player);
                                timer.replace(player,timer.get(player)-1);
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0,8);
                    }
                }
            }
        }
    }

    private void castKillerWail(Player player) {
        // Get the direction the player is looking at
        Vector direction = player.getEyeLocation().getDirection().normalize();
        Location startLocation = player.getEyeLocation().clone().add(direction.multiply(1.5)); // Offset to avoid starting inside the player

        int beamLength;
        if (Stone.getStoneLevel(player) >= 18) {
            beamLength = 30;
        }else {
            beamLength = 20;
        }
        double beamRadius = 1.5;
        double pullStrength = 2.5;
        double damage;
        if (Stone.getStoneLevel(player) >= 17) {
            damage = 2.5;
        }else {
            damage = 1.5;
        }
        int particlesPerCircle = 20;
        double beamAttackRange = 3.5;

        // Create a vector perpendicular to the direction the player is looking
        Vector perpVector1 = getPerpendicularVector(direction).normalize().multiply(beamRadius);
        Vector perpVector2 = perpVector1.clone().crossProduct(direction).normalize().multiply(beamRadius);

        for (int i = 0; i < beamLength; i++) {
            Location beamLocation = startLocation.clone().add(direction.clone().multiply(i));

            // Generate a circle of particles at each step along the beam
            for (int j = 0; j < particlesPerCircle; j++) {
                double angle = 2 * Math.PI * j / particlesPerCircle;
                double xOffset = perpVector1.getX() * Math.cos(angle) + perpVector2.getX() * Math.sin(angle);
                double yOffset = perpVector1.getY() * Math.cos(angle) + perpVector2.getY() * Math.sin(angle);
                double zOffset = perpVector1.getZ() * Math.cos(angle) + perpVector2.getZ() * Math.sin(angle);

                Location particleLocation = beamLocation.clone().add(xOffset, yOffset, zOffset);
                particleLocation.getWorld().spawnParticle(Particle.SMALL_GUST, particleLocation, 1, 0, 0, 0, 0);

                // Play sound effect at the center of the circle
                if (j == 0) {
                    beamLocation.getWorld().playSound(beamLocation, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.0f, 1.0f);
                }
            }

            // Get entities within the circular area of the beam
            List<Entity> nearbyEntities = beamLocation.getWorld().getEntities();
            for (Entity entity : nearbyEntities) {
                if (entity instanceof LivingEntity) {
                    if (entity.getLocation().distance(beamLocation) <= beamAttackRange && entity != player && HelpUtil.shouldDamageEntity((LivingEntity) entity,player)) {
                        // Apply damage and pull effect
                        ((LivingEntity) entity).damage(damage,player);
                        ((LivingEntity) entity).setNoDamageTicks(10);

                        Vector pullDirection = player.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
                        entity.setVelocity(pullDirection.multiply(pullStrength));
                    }
                }
            }
        }
    }

    // Helper method to get a vector perpendicular to the given direction vector
    private Vector getPerpendicularVector(Vector direction) {
        if (direction.getX() == 0 && direction.getZ() == 0) {
            return new Vector(1, 0, 0);
        } else {
            return new Vector(-direction.getZ(), 0, direction.getX());
        }
    }
}
