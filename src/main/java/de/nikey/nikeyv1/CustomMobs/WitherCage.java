package de.nikey.nikeyv1.CustomMobs;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Vector;

import java.util.HashSet;
import java.util.Set;

import static org.bukkit.Bukkit.getServer;

public class WitherCage implements Listener {
    public static Set<Chunk> lockedChunks = new HashSet<>();
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Chunk fromChunk = event.getFrom().getChunk();
        Chunk toChunk = event.getTo().getChunk();

        if (lockedChunks.contains(fromChunk) && !lockedChunks.contains(toChunk)) {
            Location fromLocation = event.getFrom();
            Location toLocation = event.getTo();

            // Calculate the direction vector from toLocation to fromLocation
            Vector direction = toLocation.toVector().subtract(fromLocation.toVector()).normalize();

            // Multiply the direction vector by -1 to move the player back
            Vector backDirection = direction.clone().multiply(-1);

            // Apply the back direction vector to the player's velocity
            player.setVelocity(backDirection);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Chunk toChunk = event.getTo().getChunk();

        if (lockedChunks.contains(toChunk)) {
            event.setCancelled(true);
            player.sendMessage("§cYou are locked in a cage");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Wither && entity.customName() != null) {
            if (entity.getCustomName().equals("Mini-Wither")) {
                Wither wither = (Wither) entity;
                String spawnerName = wither.getPersistentDataContainer().get(new NamespacedKey(NikeyV1.getPlugin(), "Spawner"), PersistentDataType.STRING);
                assert spawnerName != null;
                Player player = Bukkit.getPlayer(spawnerName);
                double maxHealth = wither.getMaxHealth();
                double currentHealth = wither.getHealth();
                double halfHealth = maxHealth / 2.0;
                if (currentHealth <= halfHealth && currentHealth - event.getFinalDamage() > halfHealth) {
                    lockChunksAroundLocation(wither.getChunk());
                }
            }
        }
    }

    public void lockChunksAroundLocation(Chunk centerChunk) {
        lockedChunks.clear(); // clear previous locked chunks
        int radius = 2; // adjust this to set the radius of affected chunks

        for (int x = centerChunk.getX() - radius; x <= centerChunk.getX() + radius; x++) {
            for (int z = centerChunk.getZ() - radius; z <= centerChunk.getZ() + radius; z++) {
                Chunk chunk = centerChunk.getWorld().getChunkAt(x, z);
                lockedChunks.add(chunk);
            }
        }

        // Schedule unlocking after 30 seconds
        getServer().getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
            lockedChunks.clear(); // unlock chunks
        }, 30 * 20); // 30 seconds, 20 ticks per second
    }
}
