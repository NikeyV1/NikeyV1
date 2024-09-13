package de.nikey.nikeyv1.Util;

import de.nikey.nikeyv1.api.Stone;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;

import java.util.*;

@SuppressWarnings("ALL")
public class HelpUtil {
    public static Entity getNearestEntityInSight(Player player, int range) {

        Set<Material> durchlauffaehigeMaterialien = new HashSet<>();

        // Hinzufügen aller Materialien, durch die ein Spieler durchlaufen kann
        durchlauffaehigeMaterialien.add(Material.AIR);
        durchlauffaehigeMaterialien.add(Material.WATER);
        durchlauffaehigeMaterialien.add(Material.LAVA);
        durchlauffaehigeMaterialien.add(Material.TALL_GRASS);
        durchlauffaehigeMaterialien.add(Material.SHORT_GRASS);
        durchlauffaehigeMaterialien.add(Material.DANDELION);
        durchlauffaehigeMaterialien.add(Material.POPPY);
        durchlauffaehigeMaterialien.add(Material.BLUE_ORCHID);
        durchlauffaehigeMaterialien.add(Material.ALLIUM);
        durchlauffaehigeMaterialien.add(Material.AZURE_BLUET);
        durchlauffaehigeMaterialien.add(Material.RED_TULIP);
        durchlauffaehigeMaterialien.add(Material.ORANGE_TULIP);
        durchlauffaehigeMaterialien.add(Material.WHITE_TULIP);
        durchlauffaehigeMaterialien.add(Material.PINK_TULIP);
        durchlauffaehigeMaterialien.add(Material.OXEYE_DAISY);
        durchlauffaehigeMaterialien.add(Material.SUNFLOWER);
        durchlauffaehigeMaterialien.add(Material.LILAC);
        durchlauffaehigeMaterialien.add(Material.ROSE_BUSH);
        durchlauffaehigeMaterialien.add(Material.PEONY);
        durchlauffaehigeMaterialien.add(Material.LIGHT);

        ArrayList<Entity> entities = new ArrayList<>(player.getNearbyEntities(range, range, range));
        ArrayList<Block> sightBlock = new ArrayList<>(player.getLineOfSight(durchlauffaehigeMaterialien, range));
        ArrayList<Location> sight = new ArrayList<>();

        for (Block block : sightBlock) {
            sight.add(block.getLocation());
        }

        for (Location location : sight) {
            for (Entity entity : entities) {
                double xDiff = Math.abs(entity.getLocation().getX() - location.getX());
                double yDiff = Math.abs(entity.getLocation().getY() - location.getY());
                double zDiff = Math.abs(entity.getLocation().getZ() - location.getZ());

                // Überprüfe die Nähe unter Berücksichtigung von Wasser
                if (xDiff < 1.8 && yDiff < 2 && zDiff < 1.8) {
                    return entity;
                }
            }
        }

        return null;
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


public static boolean shouldDamageEntity(LivingEntity entity, Player p) {
        String damageEntityType = Stone.getAttacking(p);
        switch (damageEntityType.toLowerCase()) {
            case "all":
                if ((entity instanceof Player && !HelpUtil.getPlayersInSameTeam(p).contains(entity)) && p.canSee(entity)) {
                    return true;
                }else if (!(entity instanceof Player)){
                    return true;
                }
            case "players":
                return (entity instanceof Player && !HelpUtil.getPlayersInSameTeam(p).contains(entity)) && p.canSee(entity);
            case "monsters":
                return entity instanceof Monster && p.canSee(entity);
            case "monsters-player":
                if (entity instanceof Player && !HelpUtil.getPlayersInSameTeam(p).contains(entity) && p.canSee(entity)) {
                    return true;
                }else if (entity instanceof Monster && p.canSee(entity)) {
                    return true;
                }
            default:
                return false;
        }
    }

    public static void triggerEntityAggro(LivingEntity damagedEntity, Player p) {
        // Iteriere durch alle nahegelegenen Entitäten
        for (Entity nearbySummoned : damagedEntity.getWorld().getNearbyEntities(damagedEntity.getLocation(),200,200,200)) {
            if (nearbySummoned instanceof Monster ) {
                Monster monster = (Monster) nearbySummoned;
                if (monster.getName().contains(p.getName()+"'s")) {
                    monster.setTarget(damagedEntity);
                }
            }else if (nearbySummoned instanceof Warden) {
                Warden entity = (Warden) nearbySummoned;
                if (entity.getName().contains(p.getName()+"'s")) {
                    entity.setTarget(damagedEntity);
                    entity.clearAnger(damagedEntity);
                    entity.setAnger(damagedEntity,80);
                }
            }else if (nearbySummoned instanceof IronGolem) {
                IronGolem entity = (IronGolem) nearbySummoned;
                if (entity.getName().contains(p.getName()+"'s")) {
                    entity.setTarget(damagedEntity);
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

    public static List<Player> getPlayersInSameTeam(Player player) {
        List<Player> playersInSameTeam = new ArrayList<>();
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
        Team playerTeam = scoreboard.getEntryTeam(player.getName());

        if (playerTeam != null) {
            for (String entry : playerTeam.getEntries()) {
                Player teamPlayer = Bukkit.getPlayer(entry);
                if (teamPlayer != null) {
                    playersInSameTeam.add(teamPlayer);
                }
            }
        }

        return playersInSameTeam;
    }

    public static void spawnParticles(Location center, int radius, int offsetX, int offsetY, int offsetZ, Particle particle) {
        World world = center.getWorld();

        for (int x = -radius; x <= radius; x++) {
            for (int y = -radius; y <= radius; y++) {
                for (int z = -radius; z <= radius; z++) {
                    Location loc = center.clone().add(x + offsetX, y + offsetY, z + offsetZ);
                    if (center.distance(loc) <= radius) {
                        world.spawnParticle(particle, loc, 0 ,0,0,0);
                    }
                }
            }
        }
    }

    /**
     * Überprüft, ob ein Spieler nach unten schaut.
     *
     * @param player Der Spieler, dessen Blickrichtung überprüft wird
     * @return true, wenn der Spieler nach unten schaut, false sonst
     */
    public static boolean isLookingDown(Player player) {
        float pitch = player.getLocation().getPitch();

        return pitch >= 60.0f;
    }

    public static Location getLocationInFrontOfPlayer(Player player, int blocksAhead) {
        Location playerLocation = player.getLocation();
        Vector direction = playerLocation.getDirection().normalize(); // Blickrichtung normalisieren
        Location locationInFront = playerLocation.add(direction.multiply(blocksAhead));
        return locationInFront;
    }
}