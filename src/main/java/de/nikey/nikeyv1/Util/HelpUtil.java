package de.nikey.nikeyv1.Util;

import de.nikey.nikeyv1.NikeyV1;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class HelpUtil {
    public static Entity getNearestEntityInSight(Player player, int range) {
        final Entity[] e = {null};
        new BukkitRunnable(){
            double t = 0;
            Location loc = player.getEyeLocation();
            Vector direction = loc.getDirection().normalize();

            public void run(){
                t += 1;
                double x = direction.getX() * t;
                double y = direction.getY();
                double z = direction.getZ() * t;
                loc.add(x,y,z);
                for (Entity entity : loc.getNearbyEntities(1,1,1)) {
                    if (entity != player) {
                        e[0] = entity;
                        cancel();
                    }
                }
                //loc.subtract(x,y,z);

                loc.getWorld().spawnParticle(Particle.FIREWORKS_SPARK,loc,1);
                if (t > range){
                    this.cancel();
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);
        return e[0];
    }
    public static List<Block> getNearbyBlocks(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++) {
                for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                    blocks.add(location.getWorld().getBlockAt(x, y, z));
                }
            }
        }
        return blocks;
    }

    public static List<Block> getNearbyBlocksNoY(Location location, int radius) {
        List<Block> blocks = new ArrayList<Block>();
        for(int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++) {
            for(int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++) {
                blocks.add(location.getWorld().getHighestBlockAt(x, z).getLocation().subtract(0,1,0).getBlock());
            }
        }
        return blocks;
    }

    public static void triggerEntityAggro(LivingEntity damagedEntity, Player p) {
        // Iteriere durch alle nahegelegenen Entitäten
        for (Entity nearbySummoned : damagedEntity.getWorld().getNearbyEntities(damagedEntity.getLocation(),200,200,200)) {
            if (nearbySummoned instanceof Monster || nearbySummoned instanceof Giant || nearbySummoned instanceof Husk ) {
                Monster monster = (Monster) nearbySummoned;
                // Überprüfen, ob die Entität ein gespawntes Entity ist
                if (monster.getName().contains(p.getName()+"'s")) {
                    // Setze das Ziel des Entitys auf den Spieler, der das ursprünglichen Entity geschlagen hat
                    monster.setTarget(damagedEntity);
                }
            }
        }
    }

    public static Player getNearestPlayer(Player player) {
        Player nearestPlayer = null;
        double nearestDistanceSquared = Double.MAX_VALUE;

        for (Player otherPlayer : Bukkit.getServer().getOnlinePlayers()) {
            if (otherPlayer.equals(player)) continue;

            double distanceSquared = player.getLocation().distanceSquared(otherPlayer.getLocation());
            if (distanceSquared < nearestDistanceSquared && distanceSquared <= 50 * 50) {
                nearestPlayer = otherPlayer;
                nearestDistanceSquared = distanceSquared;
            }
        }

        return nearestPlayer;
    }

    public static void boostEntityTowards(Entity entity, Entity targetEntity, double boostStrength) {
        Vector direction = targetEntity.getLocation().toVector().subtract(entity.getLocation().toVector()).normalize();
        Vector boost = direction.multiply(boostStrength);
        entity.setVelocity(boost);
    }
}


