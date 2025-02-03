package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.DonutEffect;
import de.slikey.effectlib.effect.SphereEffect;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.BlockFace;
import org.bukkit.boss.BarColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
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

import java.util.*;

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
    private Map<UUID, BukkitRunnable> activeTasks = new HashMap<>();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int level = Stone.getLevel(player);
        String stone = Stone.getName(player);
        if (stone.equalsIgnoreCase("air")) {
            if (level >= 3) {
                if (level == 3) {
                    player.getAttribute(Attribute.MOVEMENT_EFFICIENCY).setBaseValue(0.3);
                } else if (level == 4) {
                    player.getAttribute(Attribute.MOVEMENT_EFFICIENCY).setBaseValue(0.6);
                }else {
                    player.getAttribute(Attribute.MOVEMENT_EFFICIENCY).setBaseValue(1);
                }
            }else {
                player.getAttribute(Attribute.MOVEMENT_EFFICIENCY).setBaseValue(0);
            }
        }else {
            player.getAttribute(Attribute.MOVEMENT_EFFICIENCY).setBaseValue(0);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (Stone.getName(player).equalsIgnoreCase("air") && Stone.getLevel(player) >= 6) {
                if (event.getDamage() > 50) {
                    event.setDamage(50);
                    player.playEffect(player.getLocation(),Effect.SPONGE_DRY,null);
                }
            }

            int level = Stone.getLevel(player);
            if (Stone.getName(player).equalsIgnoreCase("air") && level >= 7) {
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
        double radius =fallHeight * 0.05;
        double damage = fallHeight * dmgmultiplier;

        player.getWorld().playSound(player.getLocation(), Sound.ITEM_MACE_SMASH_AIR, 0.3f, 1.0f);

        if (radius>= 3.5) {
            SphereEffect effect = new SphereEffect(NikeyV1.em);
            effect.setLocation(player.getLocation());
            effect.duration = 500;
            effect.particle = Particle.GUST_EMITTER_SMALL;
            effect.particles = (int) (fallHeight*0.43+1);
            effect.radius = radius;
            effect.visibleRange = 120;
            effect.start();
        }else {
            player.getWorld().spawnParticle(Particle.GUST_EMITTER_SMALL, player.getLocation(), 4);
        }

        for (Entity entity : player.getNearbyEntities(radius, radius, radius)) {
            if (entity instanceof LivingEntity) {
                LivingEntity target = (LivingEntity) entity;
                if (HelpUtil.shouldDamageEntity(target,player)) {
                    target.damage(damage, player);

                    Vector knockback = target.getLocation().toVector().subtract(player.getLocation().toVector()).normalize().multiply(1.5);
                    knockback.setY(0.5);
                    target.setVelocity(knockback);
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
            int level = Stone.getLevelFromItem(item);
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
                        int stoneLevel = Stone.getLevel(player);
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
                                        player.sendActionBar(Component.text(flyingtimer.get(player.getName()) + "/" + maxsec).color(NamedTextColor.YELLOW));

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
                            int max = 5;
                            if (level >= 19) {
                                max = 7;
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
                                player.sendActionBar(Component.text(used.get(player.getName()) + "/" + max)
                                        .color(NamedTextColor.YELLOW));
                            }
                            return;
                        }


                        ability.put(player.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                        used.remove(player.getName());


                        castKillerWail(player);
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
            int level = Stone.getLevelFromItem(item);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Air");
            config.set(p.getName()+".level",level);
            NikeyV1.getPlugin().saveConfig();
            if (level == 20 || level == 21){
                if (p.isSneaking()) {
                    if (isCharging.getOrDefault(p,false)) {
                        releaseAirSwipe(p);
                        return;
                    }
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                        startCharging(p);
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPluginDisable(PluginDisableEvent event) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            BossBar bossBar = bossBars.remove(player);
            if (bossBar != null) {
                bossBar.removeViewer(player);
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
            bossBar.removeViewer(player);
        }
        chargeStartTime.remove(player);
        isCharging.remove(player);
    }

    private void startCharging(Player player) {
        summonWindEffect(player);
        isCharging.put(player, true);
        chargeStartTime.put(player, System.currentTimeMillis());


        BossBar bossBar = net.kyori.adventure.bossbar.BossBar.bossBar(Component.text("Charging Air Swipe..."), 0.0f, net.kyori.adventure.bossbar.BossBar.Color.BLUE, net.kyori.adventure.bossbar.BossBar.Overlay.PROGRESS);
        bossBar.addViewer(player);
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

                float chargePercentage = (float) elapsedTime / MAX_CHARGE_TIME;

                bossBar.progress(chargePercentage);
                bossBar.name(Component.text("Charging Air Swipe.. " + (int) (chargePercentage * 100) + "%")
                        .color(NamedTextColor.AQUA));

                if (chargePercentage == 1) {
                    player.getWorld().spawnParticle(Particle.CRIT, player.getLocation().add(0, 1.5, 0), 7);
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 10);
    }

    private void releaseAirSwipe(Player player) {
        cancelWindChargeTask(player);
        int level = Stone.getLevel(player);
        isCharging.put(player, false);
        BossBar bossBar = bossBars.remove(player);
        if (bossBar != null) {
            bossBar.removeViewer(player);
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
            maxDamage = 27.5;
            maxRadius = 18.0;
        }else {
            maxDamage = 32.5;
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
        int swipeInterval = 8;

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

                        int particlesPerStep = (int) (currentRadius * 10);

                        for (int i = 0; i <= particlesPerStep; i++) {
                            double angle = -sweepAngle / 2 + (sweepAngle / particlesPerStep) * i;

                            Vector particleDirection = rotateAroundAxisY(direction.clone(), Math.toRadians(angle));

                            Location particleLocation = playerLocation.clone().add(particleDirection.multiply(currentRadius));

                            player.getWorld().spawnParticle(Particle.SWEEP_ATTACK, particleLocation, 0, 0.1, 0.1, 0.1, 0.05);

                            for (Player players : player.getWorld().getNearbyPlayers(particleLocation, 1.0)) {
                                if (HelpUtil.shouldDamageEntity(players,player)) {
                                    if (swipeCount == numberOfSwipes) {
                                        Vector knockback = particleDirection.clone().normalize().multiply(1.0).setY(0.5);
                                        players.setVelocity(knockback);
                                    }
                                    players.damage(damage, player);
                                    players.setNoDamageTicks(7);
                                    players.getWorld().playSound(player.getLocation(), Sound.ENTITY_PLAYER_ATTACK_SWEEP, 1.0f, 0.8f);
                                }
                            }
                        }

                        currentRadius += speedMultiplier;
                    }
                }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);

                swipeCount++;
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, swipeInterval);
    }

    private void summonWindEffect(Player player) {
        if (activeTasks.containsKey(player.getUniqueId())) {
            player.sendMessage("§cError: windeffect already active");
            return;
        }

        BukkitRunnable task = new BukkitRunnable() {
            double t = 0;

            @Override
            public void run() {
                if (!player.isOnline()) {
                    cancel(); // Beendet den Task
                    activeTasks.remove(player.getUniqueId());
                    return;
                }

                t += Math.PI / 16;
                double x = Math.cos(t) * 1.5;
                double z = Math.sin(t) * 1.5;
                double yOffset = Math.sin(t) * 0.5;

                Location playerLoc = player.getLocation();
                Location windLoc = playerLoc.clone().add(x, yOffset+1, z);


                windLoc.getWorld().spawnParticle(Particle.CLOUD, windLoc, 3, 0.1, 0.1, 0.1, 0.01);
            }
        };

        // Starte den Task und speichere ihn
        task.runTaskTimer(NikeyV1.getPlugin(), 0, 1);
        activeTasks.put(player.getUniqueId(), task); // Task speichern

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
        int stoneLevel = Stone.getLevel(player);
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

        HelpUtil.spawnParticles(player.getLocation(), (int) radius,0,0,0,Particle.TRIAL_SPAWNER_DETECTION,0);
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
                int stoneLevel = Stone.getLevel(player);
                if (stoneLevel >= 14) {
                    triggerLanding((Player) event.getEntity(),event.getDamage()*0.6);
                }else {
                    triggerLanding((Player) event.getEntity(),event.getDamage()*0.5);
                }
                flyingtimer.remove(player.getName());
            }
        }
    }


    private void castKillerWail(Player player) {
        Vector direction = player.getEyeLocation().getDirection().normalize();
        Location startLocation = player.getEyeLocation().clone().add(direction.multiply(1.5));

        int beamLength = 20;
        if (Stone.getLevel(player) >= 18) {
            beamLength = 30;
        }
        double beamRadius = 1.5;
        double pullStrength = 2.5;
        double damage = 40;
        if (Stone.getLevel(player) >= 17) {
            damage = 50;
        }
        int particlesPerCircle = 20;
        double beamAttackRange = 3.5;

        Vector perpVector1 = getPerpendicularVector(direction).normalize().multiply(beamRadius);
        Vector perpVector2 = perpVector1.clone().crossProduct(direction).normalize().multiply(beamRadius);

        for (int i = 0; i < beamLength; i++) {
            Location beamLocation = startLocation.clone().add(direction.clone().multiply(i));
            
            for (int j = 0; j < particlesPerCircle; j++) {
                double angle = 2 * Math.PI * j / particlesPerCircle;
                double xOffset = perpVector1.getX() * Math.cos(angle) + perpVector2.getX() * Math.sin(angle);
                double yOffset = perpVector1.getY() * Math.cos(angle) + perpVector2.getY() * Math.sin(angle);
                double zOffset = perpVector1.getZ() * Math.cos(angle) + perpVector2.getZ() * Math.sin(angle);

                Location particleLocation = beamLocation.clone().add(xOffset, yOffset, zOffset);
                particleLocation.getWorld().spawnParticle(Particle.SMALL_GUST, particleLocation, 1, 0, 0, 0, 0);

                if (j == 0) {
                    beamLocation.getWorld().playSound(beamLocation, Sound.ENTITY_ELDER_GUARDIAN_HURT, 1.0f, 1.0f);
                }
            }

            List<Player> playerList = beamLocation.getWorld().getPlayers();
            for (Player p : playerList) {
                if (p.getLocation().distance(beamLocation) <= beamAttackRange && p != player && HelpUtil.shouldDamageEntity(p,player)) {
                    p.damage(damage, player);

                    Vector pullDirection = player.getLocation().toVector().subtract(p.getLocation().toVector()).normalize();
                    p.setVelocity(pullDirection.multiply(pullStrength));
                }
            }
        }
    }

    private Vector getPerpendicularVector(Vector direction) {
        if (direction.getX() == 0 && direction.getZ() == 0) {
            return new Vector(1, 0, 0);
        } else {
            return new Vector(-direction.getZ(), 0, direction.getX());
        }
    }
    public void cancelWindChargeTask(Player player) {
        BukkitRunnable task = activeTasks.get(player.getUniqueId());
        if (task != null) {
            task.cancel();
            activeTasks.remove(player.getUniqueId());
        }
    }
}
