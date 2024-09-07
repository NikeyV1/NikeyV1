package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.BleedEffect;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("ALL")
public class InfernoBlade implements Listener {

    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static long remainingTime2;

    public static boolean red;

    private HashMap<LivingEntity, Long> frozenPlayers = new HashMap<>();

    private final Random random = new Random();

    public static HashMap<String , List<LivingEntity>> targetsmap = new HashMap<>();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();
        if (item != null && item.getType() == Material.NETHERITE_SWORD && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                List<String> lore = meta.getLore();
                if (lore != null ) {
                    String l = String.valueOf(lore);
                    if (l.contains("§7What will you do?")) {
                        if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                            if (meta.getDisplayName().equalsIgnoreCase( ChatColor.RED +"Inferno Blade")){
                                if (player.isSneaking()){
                                    meta.setDisplayName(ChatColor.AQUA + "Inferno Blade");
                                    event.getItem().setItemMeta(meta);
                                }else {
                                    if (!(ability.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){

                                        ability.put(player.getUniqueId(),System.currentTimeMillis() + (45*1000));
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                ability.remove(player.getUniqueId());
                                                cancel();
                                                return;
                                            }
                                        }.runTaskLater(NikeyV1.getPlugin(),20*45);
                                        red = true;
                                        //Cooldown-Ability
                                        teleportAndRoot(player);
                                    }
                                }
                            } else if (meta.getDisplayName().equalsIgnoreCase( ChatColor.AQUA + "Inferno Blade")) {
                                if (player.isSneaking()){
                                    meta.setDisplayName(ChatColor.RED + "Inferno Blade");
                                    event.getItem().setItemMeta(meta);
                                }else {
                                    if (!(ability.getOrDefault(player.getUniqueId(),0L) > System.currentTimeMillis())){
                                        red = false;
                                        ability.put(player.getUniqueId(),System.currentTimeMillis() + (6*1000));
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                ability.remove(player.getUniqueId());
                                                cancel();
                                                return;
                                            }
                                        }.runTaskLater(NikeyV1.getPlugin(),20*6);
                                        Block b = player.getTargetBlock((Set)null, 8);
                                        Location loc = new Location(b.getWorld(), (double)b.getX(), (double)b.getY(), (double)b.getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
                                        for (int i = 0; i < 3; i++) {
                                            Location loca = loc.clone().subtract(0, i, 0);
                                            if (loca.getBlock().getType().isSolid()) {
                                                loc = loca.add(0, i + 1, 0);
                                                break;
                                            }
                                        }
                                        player.teleport(loc);
                                        player.playSound(loc, Sound.ENTITY_PLAYER_TELEPORT, 0.5f, 0.5f);
                                    }
                                }
                            }else if (meta.getDisplayName().equalsIgnoreCase("§dInferno Blade")){
                                meta.setDisplayName(ChatColor.RED + "Inferno Blade");
                                event.getItem().setItemMeta(meta);
                            }
                        }
                    }
                }
            }
        }
    }

    private void teleportAndRoot(Player player) {
        Location loc = player.getLocation();
        String attacking = Stone.getAttacking(player);
        List<LivingEntity> target;
        if (attacking.equalsIgnoreCase("all")) {
            target = player.getWorld().getLivingEntities().stream()
                    .filter(entity -> !entity.equals(player) && entity.getLocation().distance(player.getLocation()) <= 30)
                    .sorted(Comparator.comparingDouble(entity -> entity.getLocation().distance(player.getLocation())))
                    .limit(6)
                    .collect(Collectors.toList());
            targetsmap.put(player.getName(),target);
        }else if (attacking.equalsIgnoreCase("players")) {
            target = player.getWorld().getLivingEntities().stream()
                    .filter(entity -> entity instanceof Player && !entity.equals(player) && entity.getLocation().distance(player.getLocation()) <= 30)
                    .sorted(Comparator.comparingDouble(entity -> entity.getLocation().distance(player.getLocation())))
                    .limit(6)
                    .collect(Collectors.toList());
            targetsmap.put(player.getName(),target);
        }else if (attacking.equalsIgnoreCase("monsters")) {
            target = player.getWorld().getLivingEntities().stream()
                    .filter(entity -> entity instanceof Monster && !entity.equals(player) && entity.getLocation().distance(player.getLocation()) <= 30)
                    .sorted(Comparator.comparingDouble(entity -> entity.getLocation().distance(player.getLocation())))
                    .limit(6)
                    .collect(Collectors.toList());
            targetsmap.put(player.getName(),target);
        }else if (attacking.equalsIgnoreCase("monsters-player")) {
            target = player.getWorld().getLivingEntities().stream()
                    .filter(entity -> (entity instanceof Monster || entity instanceof Player) &&
                            !entity.equals(player) && entity.getLocation().distance(player.getLocation()) <= 30)
                    .sorted(Comparator.comparingDouble(entity -> entity.getLocation().distance(player.getLocation())))
                    .limit(6)
                    .collect(Collectors.toList());
            targetsmap.put(player.getName(),target);
        }else {
            player.sendMessage("§cError: Config.Attack wrong input");
        }

        new BukkitRunnable() {
            int teleportCount = 0;
            @Override
            public void run() {
                List<LivingEntity> targets = targetsmap.get(player.getName());
                if (teleportCount < targets.size()) {
                    LivingEntity targetEntity = targets.get(teleportCount);
                    targetEntity.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*8,0));
                    Location playerLocation = player.getLocation();
                    Location nearestPlayerLocation = targetEntity.getLocation();

                    double nX;
                    double nZ;
                    float nang = nearestPlayerLocation.getYaw() + 90;
                    if(nang < 0) nang += 360;
                    nX = Math.cos(Math.toRadians(nang));
                    nZ = Math.sin(Math.toRadians(nang));

                    Location teleportLocation = new Location(nearestPlayerLocation.getWorld(), nearestPlayerLocation.getX() - nX,
                            nearestPlayerLocation.getY(), nearestPlayerLocation.getZ() - nZ, nearestPlayerLocation.getYaw(), nearestPlayerLocation.getPitch());


                    teleportLocation.getWorld().playEffect(teleportLocation,Effect.ENDER_SIGNAL,null);
                    for (Entity entity : teleportLocation.getWorld().getNearbyEntities(teleportLocation, 2, 2, 2)) {
                        if (entity instanceof LivingEntity && entity != player) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            player.getWorld().spawnParticle(Particle.EXPLOSION, teleportLocation, 2);
                            livingEntity.damage(20,player);
                            livingEntity.setNoDamageTicks(1);
                        }
                    }
                    if (teleportLocation.getBlock().isEmpty()) {
                        player.teleport(teleportLocation);
                        player.playSound(teleportLocation, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.0f);
                    }else {
                        teleportToNearestAirBlock(player,teleportLocation);
                        player.playSound(teleportLocation, Sound.ENTITY_PLAYER_TELEPORT, 1.0f, 1.0f);
                    }

                    //Freeze
                    freezePlayer(targetEntity);
                    Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> unfreezePlayer(targetEntity), 20*8);

                    teleportCount++;
                }else {
                    loc.getWorld().playEffect(loc,Effect.ENDER_SIGNAL,null);
                    player.teleport(loc);
                    cancel();
                    return;
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),5, 20);
    }



    public static void teleportToNearestAirBlock(Player player, Location location) {
        World world = location.getWorld();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        if (world != null) {
            for (int xOffset = -1; xOffset <= 1; xOffset++) {
                for (int yOffset = -1; yOffset <= 1; yOffset++) {
                    for (int zOffset = -1; zOffset <= 1; zOffset++) {
                        Block nearbyBlock = world.getBlockAt(x + xOffset, y + yOffset, z + zOffset);
                        if (nearbyBlock.getType().equals(Material.AIR)) {
                            player.teleport(nearbyBlock.getLocation());
                            return;
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        LivingEntity player = event.getEntity();
        if (frozenPlayers.containsKey(player)) {
            if (System.currentTimeMillis() > frozenPlayers.get(player)) {
                unfreezePlayer(player);
            } else {
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        LivingEntity player = event.getPlayer();
        if (frozenPlayers.containsKey(player)) {
            if (System.currentTimeMillis() > frozenPlayers.get(player)) {
                unfreezePlayer(player);
            } else {
                event.setCancelled(true);
            }
        }
    }

    private void freezePlayer(LivingEntity entity) {
        frozenPlayers.put(entity, System.currentTimeMillis() + 8000);
    }

    private void unfreezePlayer(LivingEntity entity) {
        frozenPlayers.remove(entity);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (Stone.isBuffed(player)) {
            ItemStack itemInHand = player.getItemInHand();
            if (Stone.isInfernoBlade(itemInHand) && event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                if (!red) {
                    Location eventTo = event.getTo();
                    eventTo.getWorld().createExplosion(eventTo.add(0,0.3F,0),1.8F,false,false);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getPlayer();
        if (player.getKiller() != null ) {
            Player killer = player.getKiller();
            if (killer.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD && killer.getInventory().getItemInMainHand().getItemMeta().hasLore()) {
                BleedEffect effect = new BleedEffect(NikeyV1.em);
                effect.setLocation(player.getLocation());
                effect.iterations = 0;
                effect.start();
                player.getInventory().clear();
                player.ban("§cYour are banned by "+ killer.getName()+ " using the Inferno Blade!", (Date) null,"Inferno Blade",true);
                //player.kickPlayer("§cYour are banned by "+killer.getName()+" using the Inferno Blade");
                //                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "§cYour stone is out of strength!",null,"Game");
            }
        }
    }
}
