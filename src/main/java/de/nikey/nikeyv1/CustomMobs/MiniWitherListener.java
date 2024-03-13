package de.nikey.nikeyv1.CustomMobs;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
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

import static org.bukkit.Bukkit.getLogger;

public class MiniWitherListener implements Listener {

    public static BukkitTask task;

    private final HashMap<Player, Long> cooldowns = new HashMap<>();
    private final long cooldownTimeMillis = 500; // Halbe Sekunde in Millisekunden

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof MiniWither) {
            Location location = event.getEntity().getLocation();
            Entity crystal = event.getEntity().getWorld().spawnEntity(location.add(0, 2, 0), EntityType.ENDER_CRYSTAL);

            new BukkitRunnable() {

                int count = 0;
                Vector movement = null;
                List<Block> blocks = HelpUtil.getNearbyBlocksNoY(location,20);
                List<FallingBlock> fblocks = new ArrayList<>();
                @Override
                public void run() {
                    if (count == 300) {
                        cancel();
                        crystal.remove();
                        TNTPrimed tnt = crystal.getWorld().spawn(crystal.getLocation(),TNTPrimed.class);
                        tnt.setFuseTicks(0);
                        for (FallingBlock b : fblocks) {
                            b.setGravity(true);
                        }
                        return;
                    }
                    Random random = new Random();
                    Block block = blocks.get(random.nextInt(blocks.size() - 0));
                    FallingBlock fblock = location.getWorld().spawnFallingBlock(block.getLocation(),block.getBlockData());
                    fblock.setVelocity((fblock.getLocation().toVector().subtract(crystal.getLocation().toVector()).multiply(-10).normalize()));
                    fblock.setGravity(false);
                    fblock.setHurtEntities(true);

                    fblocks.add(fblock);

                    count++;
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0,0);
        }
    }


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
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Wither && entity.getCustomName() != null) {
            if (entity.getCustomName().equals("Mini-Wither")) {
                Wither wither = (Wither) entity;
                double damage = event.getDamage();
                double health = wither.getHealth() + damage;
                String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                assert spawnerName != null;
                Player player = Bukkit.getPlayer(spawnerName);
                assert player != null;
                player.sendMessage(String.valueOf(health));
                if (health > 150 && wither.getHealth() < 150) {
                    wither.enterInvulnerabilityPhase();

                    // Teleport the wither 10 blocks above the player
                    wither.teleport(player.getLocation().add(0, 10, 0));
                    timer = 60;

                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (wither.isDead()) cancel();
                            if (timer == 0 ) {
                                cancel();
                            }else {
                                Location eyeLocation = wither.getEyeLocation();
                                Location spawnLocation1 = eyeLocation.add(eyeLocation.getDirection().normalize());
                                Location spawnLocation3 = spawnLocation1.clone().add(0.8D, 0, 0);
                                Location spawnLocation5 = spawnLocation1.clone().add(-0.8D, 0, 0);
                                shootSkull(player,spawnLocation3,wither);
                                shootSkull(player,spawnLocation5,wither);
                                wither.setVelocity(new Vector(0, 0, 0));
                            }
                            timer--;
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(), 0,5);
                }
            }
        }
    }

    private void shootSkull(Player player, Location spawnLocation, Wither wither) {
        // Finde die zwei nächsten Spieler in der Nähe des Spawnstandorts
        Location[] nearestPlayersLocations = findNearestPlayersLocations(spawnLocation, 2);
        int i = 0;
        for (Location targetLocation : nearestPlayersLocations) {
            if (i <= 2) { // Stelle sicher, dass nur die ersten beiden Spieler betrachtet werden
                Vector direction = targetLocation.toVector().subtract(wither.getLocation().toVector()).normalize();
                WitherSkull skull = wither.launchProjectile(WitherSkull.class);
                skull.setShooter(wither);
                skull.setVelocity(direction); // Hier kannst du die Geschwindigkeit anpassen
                skull.setYield(4);
                skull.setCharged(true);
                i++;
            } else {
                break;
            }
        }
    }

    private boolean isPlayerWithinRadius(Location location, double radius) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            if (player.getLocation().distanceSquared(location) <= radius * radius) {
                return true;
            }
        }
        return false;
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
                        if (entity instanceof Player) {
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
                            if (!player.getName().equals(spawnerName)) {
                                wither.setTarget(player);
                                break;
                            }else {
                                event.setCancelled(true);
                                boolean playerNearby = false;
                                // Überprüfen, ob ein Spieler in der Nähe des Withers ist
                                for (Entity entity : wither.getNearbyEntities(60, 30, 60)) { // Ändere die Zahlen entsprechend dem Abstand, den du berücksichtigen möchtest
                                    // Wenn ein Spieler in der Nähe ist
                                    if (entity instanceof Player) {
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

    private void setAlternateTarget(Wither wither) {
        // Set alternate target to another player

        for (Entity entitys : wither.getNearbyEntities(100,50,100)) {
            if (entitys instanceof Player) {
                if (!entitys.getName().equalsIgnoreCase(wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING))) {
                    wither.setTarget((LivingEntity) entitys);
                    return;
                }
            }
        }
    }

    private static final int DISTANCE_THRESHOLD = 35;

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
    }

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
