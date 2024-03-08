package de.nikey.nikeyv1.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class HelpUtil {
    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = new ArrayList<>(player.getNearbyEntities(range, range, range));
        ArrayList<Block> sightBlock = new ArrayList<>(player.getLineOfSight((Set<Material>) null, range));
        ArrayList<Location> sight = new ArrayList<>();

        for (Block block : sightBlock) {
            // Überprüfe, ob der Block Wasser ist und ignoriere ihn in diesem Fall
            //Dosnt work :(
            if (block.getType() == Material.WATER) {
                continue;
            }
            sight.add(block.getLocation());
        }

        for (Location location : sight) {
            for (Entity entity : entities) {
                double xDiff = Math.abs(entity.getLocation().getX() - location.getX());
                double yDiff = Math.abs(entity.getLocation().getY() - location.getY());
                double zDiff = Math.abs(entity.getLocation().getZ() - location.getZ());

                // Überprüfe die Nähe unter Berücksichtigung von Wasser
                if (xDiff < 1.3 && yDiff < 1.5 && zDiff < 1.3) {
                    return entity;
                }
            }
        }

        return null; // Rückgabe von null, wenn keine Entity gefunden wurde
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
            if (nearbySummoned instanceof Monster || nearbySummoned instanceof Giant || nearbySummoned instanceof Husk) {
                Monster monster = (Monster) nearbySummoned;
                // Überprüfen, ob die Entität ein gespawntes Entity ist
                if (monster.getName().contains(p.getName()+"'s")) {
                    // Setze das Ziel des Entitys auf den Spieler, der das ursprünglichen Entity geschlagen hat
                    monster.setTarget(damagedEntity);
                }
            }
        }
    }
}


