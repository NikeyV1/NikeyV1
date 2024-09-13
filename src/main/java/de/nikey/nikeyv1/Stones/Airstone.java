package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.DonutEffect;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
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
    public static HashMap<String , Integer> flyingtimer = new HashMap<>();
    public static HashMap<String , Integer> used = new HashMap<>();
    public static HashMap<Player, Boolean> playerBoostCooldown = new HashMap<>();
    private final HashMap<Player, Long> chargeStartTime = new HashMap<>();
    private final HashMap<Player, BossBar> bossBars = new HashMap<>();
    private final HashMap<Player, Boolean> isCharging = new HashMap<>();
    private final long MAX_CHARGE_TIME = 60 * 1000;

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
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL && !flyingtimer.containsKey(player.getName())) {
                    if (!player.isGliding() ) {
                        if (player.getInventory().getChestplate() == null) {
                            event.setCancelled(true);

                            double shockwaveDamage = 0.3;
                            if (level == 8) {
                                shockwaveDamage = 0.4;
                            }else if (level >= 9) {
                                shockwaveDamage = 0.5;
                            }

                            performShockwave(player, player.getFallDistance(),shockwaveDamage);
                        }else if (!player.getInventory().getChestplate().getType().equals(Material.ELYTRA)){
                            event.setCancelled(true);

                            double shockwaveDamage = 0.3;
                            if (level == 8) {
                                shockwaveDamage = 0.4;
                            }else if (level >= 9) {
                                shockwaveDamage = 0.5;
                            }

                            performShockwave(player, player.getFallDistance(),shockwaveDamage);
                        }
                    }
                }
            }
        }
    }

    private void performShockwave(Player player, double fallHeight, double dmgmultiplier) {
        double radius =fallHeight * 0.1;
        double damage = fallHeight * dmgmultiplier;

        player.getWorld().playSound(player.getLocation(), Sound.ITEM_MACE_SMASH_AIR, 0.3f, 1.0f);
        player.getWorld().spawnParticle(Particle.GUST_EMITTER_SMALL, player.getLocation(), 4);

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entity;

                target.damage(damage, player);

                Vector knockback = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5);
                knockback.setY(0.5);
                target.setVelocity(knockback);
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

                            flyingtimer.put(player.getName(), 20);
                        }else {
                            maxsec = 15;

                            flyingtimer.put(player.getName(), 15);
                        }
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (flyingtimer.containsKey(player.getName())) {
                                    int timeLeft = flyingtimer.get(player.getName());

                                    if (timeLeft > 0) {
                                        player.sendActionBar(ChatColor.YELLOW +""+ flyingtimer.get(player.getName()) +"/"+maxsec);

                                        flyingtimer.put(player.getName(), timeLeft - 1);
                                    } else {
                                        PlayerInventory inventory = player.getInventory();
                                        if (inventory.getChestplate() == null && !(inventory.getChestplate().getType() == Material.ELYTRA)) {
                                            player.setGliding(false);
                                        }

                                        triggerLanding(player,0);

                                        flyingtimer.remove(player.getName());

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

                        if (HelpUtil.isLookingDown(player) && level >= 17 && event.getAction() == Action.LEFT_CLICK_BLOCK) {
                            int max;
                            if (level >= 19) {
                                max = 7;
                            }else {
                                max = 5;
                            }
                            if (!playerBoostCooldown.containsKey(player)) {
                                if (used.get(player.getName()) == null) {
                                    used.put(player.getName(),1);
                                    Location clickedBlockLocation = event.getClickedBlock().getLocation();
                                    triggerBoostWithDelay(player,clickedBlockLocation);

                                }else if (used.get(player.getName()) < max){
                                    used.put(player.getName(),used.get(player.getName())+1);
                                    Location clickedBlockLocation = event.getClickedBlock().getLocation();
                                    triggerBoostWithDelay(player,clickedBlockLocation);
                                }else {
                                    used.remove(player.getName());
                                    Location clickedBlockLocation = event.getClickedBlock().getLocation();
                                    triggerBoostWithDelay(player,clickedBlockLocation);

                                    ability.put(player.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                                }
                                if (used.get(player.getName()) == null) {
                                    return;
                                }
                                player.sendActionBar(ChatColor.YELLOW +""+ used.get(player.getName())+"/"+max);
                            }
                            return;
                        }


                        ability.put(player.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                        used.remove(player.getName());

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
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (Stone.whatStone(item).equalsIgnoreCase("Air")){
            int level = Stone.getStoneLevelFromItem(item);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Air");
            config.set(p.getName()+".level",level);
            NikeyV1.getPlugin().saveConfig();
            if (level == 20 || level == 21){
                if (p.isSneaking() && Bukkit.getServer().getServerTickManager().isRunningNormally()) {
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                        if (!isCharging.getOrDefault(p, false)) {
                            startCharging(p);
                        }
                    }else if (isCharging.getOrDefault(p, false)) {
                        releaseAirSwipe(p);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPluginEnable(PluginDisableEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBar bossBar = bossBars.remove(player);
            if (bossBar != null) {
                bossBar.removeAll();
            }
            chargeStartTime.remove(player);
            isCharging.remove(player);
        }

    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        BossBar bossBar = bossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
        chargeStartTime.remove(player);
        isCharging.remove(player);
    }

    private void startCharging(Player player) {
        isCharging.put(player, true);
        chargeStartTime.put(player, System.currentTimeMillis());

        BossBar bossBar = Bukkit.createBossBar(ChatColor.AQUA + "Charging Air Swipe...", BarColor.BLUE, BarStyle.SOLID);
        bossBar.addPlayer(player);
        bossBars.put(player, bossBar);


        new BukkitRunnable() {
            @Override
            public void run() {
                if (!isCharging.getOrDefault(player, false)) {
                    cancel();
                    return;
                }

                long elapsedTime = System.currentTimeMillis() - chargeStartTime.get(player);
                if (elapsedTime >= MAX_CHARGE_TIME) {
                    elapsedTime = MAX_CHARGE_TIME;
                }

                double chargePercentage = (double) elapsedTime / MAX_CHARGE_TIME;

                bossBar.setProgress(chargePercentage);
                bossBar.setTitle(ChatColor.AQUA + "Charging Air Swipe... " + (int) (chargePercentage * 100) + "%");

                player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1.5, 0), 5);
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 10);
    }

    private void releaseAirSwipe(Player player) {
        int level = Stone.getStoneLevel(player);
        isCharging.put(player, false);
        BossBar bossBar = bossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeAll();
        }
        long elapsedTime = System.currentTimeMillis() - chargeStartTime.get(player);
        if (elapsedTime >= MAX_CHARGE_TIME) {
            elapsedTime = MAX_CHARGE_TIME;
        }

        double chargePercentage = (double) elapsedTime / MAX_CHARGE_TIME;
        double maxDamage;
        double initialRadius = 2.0;
        double maxRadius;
        double sweepAngle = 180.0;
        if (level == 21) {
            maxDamage = 35.0;
            maxRadius = 18.0;
        }else {
            maxDamage = 30.0;
            maxRadius = 15;
        }
        double damage = 5.0 + chargePercentage * maxDamage;

        player.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 1.0f);
        Location playerLocation = player.getEyeLocation();
        Vector direction = playerLocation.getDirection().normalize();

        int numberOfSwipes;
        if (level == 21) {
            numberOfSwipes = 5;
        }else {
            numberOfSwipes = 3;
        }
        int swipeInterval = 5;  // Ticks between each swipe

        new BukkitRunnable() {
            int swipeCount = 0;

            @Override
            public void run() {
                if (swipeCount >= numberOfSwipes) {
                    cancel();
                    return;
                }

                new BukkitRunnable() {
                    double currentRadius = initialRadius;
                    final double speedMultiplier = 1.5;

                    @Override
                    public void run() {
                        if (currentRadius > maxRadius) {
                            cancel();
                            return;
                        }

                        // Define the number of particles based on the radius (increase with distance)
                        int particlesPerStep = (int) (currentRadius * 10);

                        // Generate particles along a half-circle in front of the player
                        for (int i = 0; i <= particlesPerStep; i++) {
                            double angle = -sweepAngle / 2 + (sweepAngle / particlesPerStep) * i;

                            Vector particleDirection = rotateAroundAxisY(direction.clone(), Math.toRadians(angle));

                            // Calculate the location of the particle based on the player's position and the radius
                            Location particleLocation = playerLocation.clone().add(particleDirection.multiply(currentRadius));

                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 0, 0.1, 0.1, 0.1, 0.05);

                            // Damage entities within the swipe area
                            for (Entity entity : player.getWorld().getNearbyEntities(particleLocation, 1.0, 1.0, 1.0)) {
                                if (entity instanceof LivingEntity && entity != player) {
                                    LivingEntity target = (LivingEntity) entity;
                                    if (swipeCount == numberOfSwipes) {
                                        Vector knockback = particleDirection.clone().normalize().multiply(1.0).setY(0.5);
                                        target.setVelocity(knockback);
                                    }
                                    target.damage(damage, player);
                                    target.setNoDamageTicks(15);
                                    target.getWorld().playSound(target.getLocation(), Sound.ENTITY_PLAYER_HURT, 1.0f, 0.8f);
                                }
                            }
                        }

                        // Increase the radius of the swipe for the next iteration, faster than before
                        currentRadius += speedMultiplier;  // Increase the speed of the swipe
                    }
                }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);  // Run each swipe every tick

                swipeCount++;
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, swipeInterval);  // Run the next swipe after swipeInterval ticks
    }

    // Helper method to rotate a vector around the Y-axis (used to create the half-circle arc)
    private Vector rotateAroundAxisY(Vector vector, double angle) {
        double cos = Math.cos(angle);
        double sin = Math.sin(angle);
        double x = vector.getX() * cos - vector.getZ() * sin;
        double z = vector.getX() * sin + vector.getZ() * cos;
        return new Vector(x, vector.getY(), z);
    }


    private void triggerBoostWithDelay(Player player, Location location) {
        playerBoostCooldown.put(player, true);
        new BukkitRunnable() {
            @Override
            public void run() {
                Vector normalize = player.getLocation().toVector().subtract(location.toVector()).normalize();
                Vector multiplied = normalize.multiply(1.3);

                player.getWorld().spawnParticle(Particle.GUST, player.getLocation(), 0);
                player.playSound(location,Sound.ENTITY_WIND_CHARGE_WIND_BURST,1,1);
                player.setVelocity(multiplied);
                playerBoostCooldown.remove(player);
            }
        }.runTaskLater(NikeyV1.getPlugin(), 5);
    }


    @EventHandler
    public void onEntityToggleGlide(EntityToggleGlideEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Überprüfen, ob der Spieler versucht, das Gleiten zu deaktivieren, während der Timer läuft
            if (flyingtimer.containsKey(player.getName()) && !event.isGliding()) {
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
        if (flyingtimer.containsKey(player.getName()) && !player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType().isAir() && flyingtimer.get(player.getName()) < 14) {
            triggerLanding(player,0);
            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                flyingtimer.remove(player.getName());
            }, 10);
        }
    }


    @EventHandler
    public void onPlayerDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();

            // Verhindere Fallschaden, wenn der Spieler mit der Fähigkeit gelandet ist
            if ((event.getCause() == EntityDamageEvent.DamageCause.FALL ||event.getCause() == EntityDamageEvent.DamageCause.FLY_INTO_WALL) && flyingtimer.containsKey(player.getName())) {
                event.setCancelled(true);
                int stoneLevel = Stone.getStoneLevel(player);
                if (stoneLevel >= 14) {
                    triggerLanding((Player) event.getEntity(),event.getDamage()*0.5);
                }else {
                    triggerLanding((Player) event.getEntity(),event.getDamage()*0.4);
                }
                flyingtimer.remove(player.getName());
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
