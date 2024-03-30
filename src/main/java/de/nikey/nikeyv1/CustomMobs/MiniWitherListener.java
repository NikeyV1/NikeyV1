package de.nikey.nikeyv1.CustomMobs;

import com.destroystokyo.paper.entity.ai.PaperCustomGoal;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import net.kyori.adventure.text.Component;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.MoveTowardsRestrictionGoal;
import net.minecraft.world.entity.ai.navigation.FlyingPathNavigation;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.PathNavigationRegion;
import net.minecraft.world.level.pathfinder.PathFinder;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_20_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R3.entity.CraftWither;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.vehicle.VehicleEnterEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.util.Vector;

import java.util.*;

public class MiniWitherListener implements Listener {

    public static BukkitTask task;

    private final HashMap<Player, Long> cooldowns = new HashMap<>();
    private final long cooldownTimeMillis = 400; // Halbe Sekunde in Millisekunden

    private final int RADIUS = 25;
    private final int FLAG_DURATION_SECONDS = 20;
    private Location flagLocation;
    private Set<LivingEntity> restrictedPlayers = new HashSet<>();


    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof WitherSkull) {
            WitherSkull skull = (WitherSkull) event.getDamager();
            if (skull.customName() != null && skull.customName().equals("CustomWitherSkull")) {
                if (event.getEntity() instanceof Player) {
                    Location location = event.getEntity().getLocation();
                    LivingEntity entity = (LivingEntity) event.getEntity();
                    entity.damage(8);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction().toString().contains("RIGHT_CLICK")) {
            Player player = event.getPlayer();
            if (player.getInventory().getItemInMainHand().getType().isAir()) {
                if (player.getVehicle() instanceof Wither && Objects.equals(player.getVehicle().getCustomName(), "Mini-Wither")) {
                    if (checkCooldown(player)) {
                        shootWitherHead(player, (Wither) player.getVehicle());
                        cooldowns.put(player, System.currentTimeMillis());
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.WITHER) {
            Player player = event.getPlayer();
            Wither wither = (Wither) event.getRightClicked();
            if (wither.getCustomName() != null && wither.getCustomName().equals("Mini-Wither")) {
                String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                assert spawnerName != null;
                if (spawnerName.equals(player.getName())) {
                    if (!wither.isCharged()) {
                        wither.addPassenger(player);
                        startVelocityUpdateTask(wither,player);
                    }
                }
            }
        }
    }
    
    int timer;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();

        for (Entity entity : playerLocation.getWorld().getNearbyEntities(playerLocation, DISTANCE_THRESHOLD, DISTANCE_THRESHOLD, DISTANCE_THRESHOLD)) {
            if (entity.getType() == EntityType.WITHER && entity.getCustomName().equalsIgnoreCase("Mini-Wither")) {
                Location witherLocation = entity.getLocation();
                if (witherLocation.distance(playerLocation) >= DISTANCE_THRESHOLD) {
                    Location loc = playerLocation.add(0, 2, 0);
                    entity.teleport(loc);
                }
            }
        }

        if (flagLocation != null && event.getFrom().distance(flagLocation) > RADIUS && event.getTo().distance(flagLocation) <= RADIUS) {
            restrictedPlayers.add(event.getPlayer());
        }

        if (flagLocation != null && restrictedPlayers.contains(event.getPlayer())) {
            Location to = event.getTo();
            Location from = event.getFrom();
            if (to.getWorld() == flagLocation.getWorld() && from.distanceSquared(flagLocation) > RADIUS * RADIUS) {
                double angle = Math.atan2(from.getZ() - flagLocation.getZ(), from.getX() - flagLocation.getX());
                double offsetX = Math.cos(angle) * (RADIUS - 1);
                double offsetZ = Math.sin(angle) * (RADIUS - 1);
                Location newLocation = flagLocation.clone().add(offsetX, 0, offsetZ);
                event.setTo(newLocation);
                event.getPlayer().sendMessage(ChatColor.RED + "You cannot leave the radius of the flag!");
            }
        }
    }
    int op;

    //@EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Wither && entity.getCustomName() != null) {
            if (entity.getCustomName().equals("Mini-Wither")) {
                Wither wither = (Wither) entity;
                String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                assert spawnerName != null;
                Player player = Bukkit.getPlayer(spawnerName);
                assert player != null;
                if (wither.getHealth() - event.getFinalDamage() < 150) {
                    wither.enterInvulnerabilityPhase();
                    wither.setAI(false);
                    //cage
                    flagLocation = wither.getLocation().clone().add(0,10,0);
                    Bukkit.broadcastMessage(ChatColor.GREEN + "The Mini-Wither locked you in a cage");

                    op = 20;
                    List<Chunk> surroundingChunks = getChunksInRadius(wither.getWorld(), flagLocation.getBlockX(), flagLocation.getBlockZ(), 50);
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            // Speichere alle lebenden Entitäten in den umliegenden Chunks
                            List<LivingEntity> livingEntities = getLivingEntitiesInChunks(surroundingChunks);
                            restrictedPlayers.add((Player) livingEntities);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                    Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                        flagLocation = null;
                        restrictedPlayers.clear();
                        Bukkit.broadcastMessage(ChatColor.GREEN + "The Cage broke!");
                    }, FLAG_DURATION_SECONDS * 20L); // Convert seconds to ticks

                    timer = 60;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (wither.isDead()) cancel();
                            if (timer == 0 ) {
                                wither.setAI(true);
                                cancel();
                            }else {
                                Location eyeLocation = wither.getEyeLocation();
                                Location spawnLocation1 = eyeLocation.add(eyeLocation.getDirection().normalize());
                                Location spawnLocation3 = spawnLocation1.clone().add(0.8D, 0, 0);
                                Location spawnLocation5 = spawnLocation1.clone().add(-0.8D, 0, 0);
                                shootSkull(spawnLocation3,wither);
                                shootSkull(spawnLocation5,wither);
                                wither.setVelocity(new Vector(0, 0, 0));
                            }
                            timer--;
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(), 0,5);
                }
            }
        }
    }

    private List<Chunk> getChunksInRadius(World world, int centerX, int centerZ, int radius) {
        List<Chunk> chunksInRadius = new ArrayList<>();
        int chunkRadius = radius >> 4;

        for (int x = -chunkRadius; x <= chunkRadius; x++) {
            for (int z = -chunkRadius; z <= chunkRadius; z++) {
                Chunk chunk = world.getChunkAt(centerX + x, centerZ + z);
                chunksInRadius.add(chunk);
            }
        }

        return chunksInRadius;
    }

    // Methode zum Sammeln aller lebenden Entitäten in den Chunks
    private List<LivingEntity> getLivingEntitiesInChunks(List<Chunk> chunks) {
        List<LivingEntity> livingEntities = new ArrayList<>();
        for (Chunk chunk : chunks) {
            for (Entity entity : chunk.getEntities()) {
                if (entity instanceof LivingEntity) {
                    livingEntities.add((LivingEntity) entity);
                }
            }
        }
        return livingEntities;
    }


    private void shootSkull( Location spawnLocation, Wither wither) {
        // Finde die zwei nächsten Spieler in der Nähe des Spawnstandorts
        Location[] nearestPlayersLocations = findNearestPlayersLocations(spawnLocation, 2);
        int i = 0;
        for (Location targetLocation : nearestPlayersLocations) {
            if (i <= 2) { // Stelle sicher, dass nur die ersten beiden Spieler betrachtet werden
                Vector direction = targetLocation.toVector().subtract(wither.getLocation().toVector()).normalize();
                WitherSkull skull = wither.getWorld().spawn(wither.getLocation(), WitherSkull.class, CreatureSpawnEvent.SpawnReason.CUSTOM);
                skull.setShooter(wither);
                skull.setVelocity(direction); // Hier kannst du die Geschwindigkeit anpassen
                skull.setYield(5);
                skull.setCharged(true);
                i++;
            } else {
                break;
            }
        }
    }


    private Location[] findNearestPlayersLocations(Location location, int count) {
        return Bukkit.getOnlinePlayers().stream()
                .sorted(Comparator.comparingDouble(p -> p.getLocation().distanceSquared(location)))
                .limit(count)
                .map(Player::getLocation)
                .toArray(Location[]::new);
    }

    @EventHandler
    public void onWitherTarget(EntityTargetEvent event) {
        if (event.getEntityType() == EntityType.WITHER) {
            Wither wither = (Wither) event.getEntity();
            if (wither.getCustomName() != null && wither.getCustomName().equals("Mini-Wither")) {
                if(wither.getPassenger() != null){
                    event.setCancelled(true);
                }

                if (!(event.getTarget() instanceof Player)) {
                    // Das Event abbrechen, damit der Wither nicht auf ein Ziel zielen kann
                    event.setCancelled(true);
                    boolean playerNearby = false;
                    // Überprüfen, ob ein Spieler in der Nähe des Withers ist
                    for (Entity entity : wither.getNearbyEntities(60, 30, 60)) { // Ändere die Zahlen entsprechend dem Abstand, den du berücksichtigen möchtest
                        // Wenn ein Spieler in der Nähe ist
                        if (entity instanceof Player && ((Player) entity).getGameMode() == GameMode.SURVIVAL) {
                            // Das Ziel des Events auf den Spieler setzen
                            event.setTarget(entity);
                            // Variable setzen, um anzuzeigen, dass ein Spieler in der Nähe gefunden wurde
                            playerNearby = true;
                            // Schleife abbrechen, da nur der nächste Spieler ins Visier genommen werden soll
                            break;
                        }
                    }
                    // Wenn kein Spieler in der Nähe gefunden wurde
                    if (!playerNearby) {
                        event.setCancelled(false);
                    }
                } else {
                    String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                    if (spawnerName != null && spawnerName.equals((event.getTarget()).getName())) {
                        event.setCancelled(true); // Cancel targeting if the target is the spawner player
                        // Let the Wither target another player
                        for (Player player : wither.getWorld().getPlayers()) {
                            if (!player.getName().equals(spawnerName) ) {
                                if (player.getGameMode() == GameMode.SURVIVAL) {
                                    wither.setTarget(player);
                                    break;
                                }
                            }else {
                                event.setCancelled(true);
                                boolean playerNearby = false;
                                // Überprüfen, ob ein Spieler in der Nähe des Withers ist
                                for (Entity entity : wither.getNearbyEntities(60, 30, 60)) { // Ändere die Zahlen entsprechend dem Abstand, den du berücksichtigen möchtest
                                    // Wenn ein Spieler in der Nähe ist
                                    if (entity instanceof Player && player.getGameMode() == GameMode.SURVIVAL) {
                                        // Das Ziel des Events auf den Spieler setzen
                                        event.setTarget(entity);
                                        // Variable setzen, um anzuzeigen, dass ein Spieler in der Nähe gefunden wurde
                                        playerNearby = true;
                                        // Schleife abbrechen, da nur der nächste Spieler ins Visier genommen werden soll
                                        break;
                                    }
                                }
                                // Wenn kein Spieler in der Nähe gefunden wurde
                                if (!playerNearby) {
                                    event.setCancelled(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static final int DISTANCE_THRESHOLD = 35;

    private void startVelocityUpdateTask(Wither wither, Player player) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                if (!wither.isDead()) {
                    String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                    if (spawnerName.equals(player.getName()) && player.getVehicle() instanceof Wither) {
                        if (!wither.isCharged()) {
                            Vector direction = player.getLocation().getDirection();
                            Vector velocity = direction.normalize().multiply(0.365); // Adjust the multiplier as needed
                            wither.setVelocity(velocity);
                            wither.teleport(wither.getLocation().setDirection(direction));
                        }else {
                            player.leaveVehicle();
                            player.setVelocity(new Vector(0, 1, 0));
                        }
                    }else {
                        cancel();
                    }
                }else {
                    cancel();
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0L, 3); // Run the task every tick
    }

    @EventHandler
    public void onEntityDismount(EntityDismountEvent event) {
        if (event.getDismounted() instanceof Wither && event.getEntity() instanceof Player) {
            if (event.getDismounted().getCustomName().equalsIgnoreCase("Mini-Wither")) {
                Wither vehicle = (Wither) event.getDismounted();
                cancelVelocityUpdateTask();
            }
        }
    }

    private void cancelVelocityUpdateTask() {
        if (task != null) {
            task.cancel();
        }
    }

    private boolean checkCooldown(Player player) {
        if (!cooldowns.containsKey(player)) return true;
        long lastShootTime = cooldowns.get(player);
        return System.currentTimeMillis() - lastShootTime >= cooldownTimeMillis;
    }

    private void shootWitherHead(Player player, Wither wither) {
        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        eyeLoc.add(direction);

        WitherSkull witherSkull = wither.launchProjectile(WitherSkull.class);
        witherSkull.setDirection(direction);
        witherSkull.setYield(3.7F);
        witherSkull.setShooter(wither);
    }
}
