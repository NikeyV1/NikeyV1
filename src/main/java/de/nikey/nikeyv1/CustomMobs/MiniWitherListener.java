package de.nikey.nikeyv1.CustomMobs;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import net.kyori.adventure.text.Component;
import org.apache.logging.log4j.core.tools.picocli.CommandLine;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MiniWitherListener implements Listener {
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
                    location.getWorld().createExplosion(location, 2); // Create explosion with power 2
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
            if (player.getInventory().getItemInMainHand().getType() == Material.WITHER_SKELETON_SKULL) {
                if (player.getVehicle() instanceof Wither && Objects.equals(player.getVehicle().customName(), Component.text("Mini-Wither"))) shootWitherHead(player, (Wither) player.getVehicle());
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
                        rideWither(player, wither);
                    } else {
                        player.sendMessage("Der Wither hat nicht genug Gesundheit, um geritten zu werden!");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onWitherTarget(EntityTargetEvent event) {
        if (event.getEntityType() == EntityType.WITHER) {
            Wither wither = (Wither) event.getEntity();
            if (wither.getCustomName() != null && wither.getCustomName().equals("Mini-Wither")) {
                if (!(event.getTarget() instanceof Player)) {
                    event.setCancelled(true);
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
                                setAlternateTarget(wither);
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
            if (entitys instanceof LivingEntity) {
                if (!entitys.getName().equalsIgnoreCase(wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING))) {
                    wither.setTarget((LivingEntity) entitys);
                    return;
                }
            }
        }
    }

    private void rideWither(Player player, Wither wither) {
        player.sendMessage("L");
        wither.addPassenger(player);
        new BukkitRunnable() {
            @Override
            public void run() {
                wither.setVelocity(player.getLocation().getDirection().multiply(0.6).normalize());
                if (wither.getHealth() < wither.getMaxHealth() * 0.5) {
                    // Remove player from riding Wither
                    player.leaveVehicle();
                    cancel(); // Stop the task
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);
    }

    private void shootWitherHead(Player player,Wither wither) {
        Location eyeLoc = player.getEyeLocation();
        Vector direction = eyeLoc.getDirection();
        eyeLoc.add(direction.multiply(2));

        WitherSkull witherSkull = (WitherSkull) player.getWorld().spawnEntity(eyeLoc, EntityType.WITHER_SKULL);
        witherSkull.setVelocity(direction);
        witherSkull.setYield(1.5F);
        witherSkull.setShooter(wither);

        // Make Wither hover over the player every 20 seconds
        new BukkitRunnable() {
            boolean isShootingPhase = false;

            @Override
            public void run() {
                if (isShootingPhase) {
                    for (Player target : player.getWorld().getPlayers()) {
                        if (!target.equals(player)) {
                            WitherSkull skull = wither.launchProjectile(WitherSkull.class);
                            skull.setCharged(true);
                            Vector direction = target.getLocation().toVector().subtract(wither.getLocation().toVector()).normalize();
                            skull.setDirection(direction);
                            skull.setShooter(wither);
                        }
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);
    }
}
