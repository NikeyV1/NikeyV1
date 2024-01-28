package de.nikey.nikeyv1.Util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@SuppressWarnings("ALL")
public class HelpUtil {
    public static Entity getNearestEntityInSight(Player player, int range) {
        ArrayList<Entity> entities = (ArrayList<Entity>) player.getNearbyEntities(range, range, range);
        ArrayList<Block> sightBlock = (ArrayList<Block>) player.getLineOfSight( (Set<Material>) null, range);
        ArrayList<Location> sight = new ArrayList<Location>();
        for (Block block : sightBlock) sight.add(block.getLocation());
        for (int i = 0;i<sight.size();i++) {
            for (int k = 0;k<entities.size();k++) {
                if (Math.abs(entities.get(k).getLocation().getX()-sight.get(i).getX())<1.3) {
                    if (Math.abs(entities.get(k).getLocation().getY()-sight.get(i).getY())<1.5) {
                        if (Math.abs(entities.get(k).getLocation().getZ()-sight.get(i).getZ())<1.3) {
                            return entities.get(k);
                        }
                    }
                }
            }
        }
        return null; //Return null/nothing if no entity was found
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

    public static void triggerEntityAggro(Monster damagedEntity, Player p) {
        // Iteriere durch alle nahegelegenen Entitäten
        for (Entity nearbySummoned : damagedEntity.getWorld().getNearbyEntities(damagedEntity.getLocation(),200,200,200)) {
            if (nearbySummoned instanceof Monster) {
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


