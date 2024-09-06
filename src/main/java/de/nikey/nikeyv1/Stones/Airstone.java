package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.DonutEffect;
import de.slikey.effectlib.effect.SmokeEffect;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;
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
    public static HashMap<Player, Integer> flyingtimer = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int level = Stone.getStoneLevel(player);
        String stone = Stone.getStoneName(player);
        if (stone.equalsIgnoreCase("air")) {
            if (level >= 3) {
                if (level == 3) {
                    player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(4);
                } else if (level == 4) {
                    player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(5);
                }else {
                    player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(6);
                }
            }else {
                player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(3);
            }
        }else {
            player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE).setBaseValue(3);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            if (Stone.getStoneName(player).equalsIgnoreCase("air") && Stone.getStoneLevel(player) >= 6) {
                if (event.getDamage() > 55) {
                    event.setDamage(55);
                    player.playEffect(player.getLocation(),Effect.SPONGE_DRY,0);
                }
            }

            int level = Stone.getStoneLevel(player);
            if (Stone.getStoneName(player).equalsIgnoreCase("air") && level >= 7) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {
                    double originalDamage = event.getDamage();

                    // Reduce fall damage by 50%
                    double reducedDamage = originalDamage * 0.5;
                    event.setDamage(reducedDamage);

                    // Calculate shockwave damage (30% of the reduced fall damage)
                    double shockwaveDamage = 0.1;
                    if (level == 8) {
                        shockwaveDamage = originalDamage * 0.2;
                    }else if (level >= 9) {
                        shockwaveDamage = originalDamage * 0.3;
                    }

                    createShockwave(player.getLocation(), shockwaveDamage, player);
                }
            }
        }
    }

    private void createShockwave(Location location, double damage, Player player) {
        World world = location.getWorld();

        // Create a visual effect for the shockwave
        if (world != null) {
            world.spawnParticle(Particle.EXPLOSION_EMITTER, location, 1);
            world.playSound(location, Sound.ENTITY_GENERIC_EXPLODE, 1.0f, 1.0f);

            // Damage nearby entities (excluding the player)
            double radius = 3.0; // Shockwave radius
            for (Entity entity : world.getNearbyEntities(location, radius, radius, radius)) {
                if (entity instanceof LivingEntity) {
                    if (entity != player && HelpUtil.shouldDamageEntity((LivingEntity) entity,player)) {
                        ((LivingEntity) entity).damage(damage);
                    }
                }
            }
        }
    }


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
                    if (!(cooldown.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (100 * 1000));

                        DonutEffect effect = new DonutEffect(NikeyV1.em);
                        effect.setEntity(player);
                        effect.particle = Particle.SWEEP_ATTACK;
                        effect.duration = 200;
                        effect.start();

                        Vector direction = player.getLocation().getDirection();

                        direction.multiply(2);
                        direction.setY(direction.getY() + 1);

                        player.setGliding(true);
                        player.setVelocity(direction);

                        int maxsec;
                        int stoneLevel = Stone.getStoneLevel(player);
                        if (stoneLevel >= 13) {
                            maxsec = 20;

                            flyingtimer.put(player, 20);
                        }else {
                            maxsec = 15;

                            flyingtimer.put(player, 15);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (flyingtimer.containsKey(player)) {
                                    int timeLeft = flyingtimer.get(player);

                                    if (timeLeft > 0) {
                                        // Zeige die verbleibende Zeit über der Hotbar an
                                        player.sendActionBar(ChatColor.YELLOW +""+ flyingtimer.get(player) +"/"+maxsec);

                                        flyingtimer.put(player, timeLeft - 1);
                                    } else {
                                        player.setGliding(false);

                                        triggerLanding(player,0);

                                        flyingtimer.remove(player);

                                        cancel();
                                    }
                                } else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(), 0L, 20L);
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_BLOCK || event.getAction() == Action.LEFT_CLICK_AIR) {
                if (!player.isSneaking() && level >= 15) {
                    if (!(ability.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                        ability.put(player.getUniqueId(), System.currentTimeMillis() + (180 * 1000));

                        if (level == 15) {
                            timer.put(player,8);
                        }else if (level == 16 || level == 17 || level == 18) {
                            timer.put(player,10);
                        }else {
                            timer.put(player,12);
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
                        }.runTaskTimer(NikeyV1.getPlugin(),0,5);
                    }
                }
            }
        }
    }
    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Überprüfen, ob der Spieler versucht, das Gleiten zu deaktivieren, während der Timer läuft
            if (flyingtimer.containsKey(player) && !event.isGliding()) {
                event.setCancelled(true);
            }
        }
    }

    private void triggerLanding(Player player, double dmg) {
        // Erzeuge eine Explosion ohne Schaden am Spieler, aber mit Partikeln und Schaden an anderen Entitäten
        Location location = player.getLocation();

        // Erzeuge Partikel für die Explosion
        player.getWorld().spawnParticle(Particle.EXPLOSION, location, 1);
        player.getWorld().playSound(player.getLocation(),Sound.ENTITY_GENERIC_EXPLODE,1,1);


        double radius = 3.0;
        int stoneLevel = Stone.getStoneLevel(player);
        if (stoneLevel >= 11) {
            radius = 4.0;
        }
        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                if (entity instanceof Player && entity.equals(player)) {
                    continue;
                }else if (!HelpUtil.shouldDamageEntity((LivingEntity) entity,player)) {
                    continue;
                }

                if (stoneLevel >= 12) {
                    ((LivingEntity)entity).damage(10+dmg, player);
                }else {
                    ((LivingEntity)entity).damage(5+dmg, player);
                }
            }
        }

        HelpUtil.spawnParticles(player.getLocation(), (int) radius,0,0,0,Particle.TRIAL_SPAWNER_DETECTION);
    }

    @EventHandler
    public void onPlayerLand(PlayerMoveEvent event) {
        Player player = event.getPlayer();

        // Überprüfen, ob der Spieler mit der Fähigkeit geglitten ist und nun den Boden berührt
        if (flyingtimer.containsKey(player) && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir() && flyingtimer.get(player) < 14) {
            triggerLanding(player,0);
            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                flyingtimer.remove(player);
            }, 10);
        }
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Verhindere Fallschaden, wenn der Spieler mit der Fähigkeit gelandet ist
            if ((event.getCause() == EntityDamageEvent.DamageCause.FALL ||event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) && flyingtimer.containsKey(player)) {
                event.setCancelled(true);
                int stoneLevel = Stone.getStoneLevel(player);
                if (stoneLevel >= 14) {
                    triggerLanding((Player) event.getEntity(),event.getDamage()*0.5);
                }else {
                    triggerLanding((Player) event.getEntity(),event.getDamage()*0.4);
                }
                flyingtimer.remove(player);
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
            damage = 5;
        }else {
            damage = 3.5;
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
                        LivingEntity living = (LivingEntity) entity;
                        living.damage(damage,player);
                        living.setNoDamageTicks(0);

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
