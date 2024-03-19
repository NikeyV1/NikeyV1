package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
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
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@SuppressWarnings("ALL")
public class InfernoBlade implements Listener {

    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static long remainingTime2;

    public static boolean red;

    private HashMap<LivingEntity, Long> frozenPlayers = new HashMap<>();

    private final Random random = new Random();

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();

        if (item.getType() == Material.FIREWORK_STAR && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // Getting the lore of the item
                List<String> lore = meta.getLore();
                // Checking if lore contains the keyword "Combined"
                if (lore != null ) {
                    String l = String.valueOf(lore);
                    if (l.equalsIgnoreCase("[§fThe combined power of all §8stones]")) {
                        ChatColor currentColor = ChatColor.getByChar(meta.getDisplayName().charAt(1));
                        ChatColor newColor = getRandomColor(currentColor);
                        meta.setDisplayName(newColor + "Elemental Stone");
                        item.setItemMeta(meta);
                    }
                }
            }
        }
    }

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
                                    if (ability.containsKey(player.getUniqueId()) && ability.get(player.getUniqueId()) > System.currentTimeMillis()){
                                        event.setCancelled(true);
                                        player.updateInventory();
                                        remainingTime2 = ability.get(player.getUniqueId()) - System.currentTimeMillis();
                                    }else {
                                        ability.put(player.getUniqueId(),System.currentTimeMillis() + (80*1000));
                                        new BukkitRunnable() {
                                            @Override
                                            public void run() {
                                                ability.remove(player.getUniqueId());
                                                cancel();
                                                return;
                                            }
                                        }.runTaskLater(NikeyV1.getPlugin(),20*80);
                                        red = true;
                                        //Cooldown-Ability
                                        List<Entity> nearbyEntities = player.getNearbyEntities(20, 15, 20);
                                        List<LivingEntity> targets = new ArrayList<>();
                                        for (Entity entity : nearbyEntities) {
                                            if ((entity instanceof Player || entity instanceof LivingEntity) && !entity.equals(player)) {
                                                targets.add((LivingEntity) entity);
                                            }
                                        }
                                        if (!targets.isEmpty()) {
                                            teleportAndRoot(player, targets);
                                        }
                                    }
                                }
                            } else if (meta.getDisplayName().equalsIgnoreCase( ChatColor.AQUA + "Inferno Blade")) {
                                if (player.isSneaking()){
                                    meta.setDisplayName(ChatColor.RED + "Inferno Blade");
                                    event.getItem().setItemMeta(meta);
                                }else {
                                    if (ability.containsKey(player.getUniqueId()) && ability.get(player.getUniqueId()) > System.currentTimeMillis()){
                                        event.setCancelled(true);
                                        player.updateInventory();
                                        remainingTime2 = ability.get(player.getUniqueId()) - System.currentTimeMillis();
                                    }else {
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
                                        player.playSound(loc, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 0.5f);
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

    int teleportCount = 0;
    private void teleportAndRoot(Player player, List<LivingEntity> targets) {
        Location loc = player.getLocation();
        Collections.sort(targets, Comparator.comparingDouble(entity -> entity.getLocation().distance(player.getLocation())));

        new BukkitRunnable() {

            @Override
            public void run() {
                if (teleportCount >= 5 || targets.isEmpty()) {
                    player.teleport(loc);
                    cancel();
                    return;
                }

                LivingEntity targetEntity = targets.get(teleportCount);
                targetEntity.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*8,0));
                Location playerLocation = player.getLocation();
                Location nearestPlayerLocation = targetEntity.getLocation();

                Location teleportLocation = nearestPlayerLocation.clone().subtract(nearestPlayerLocation.getDirection().multiply(2));
                teleportLocation.getWorld().playEffect(teleportLocation,Effect.ENDER_SIGNAL,null);
                for (Entity entity : teleportLocation.getWorld().getNearbyEntities(teleportLocation, 2, 2, 2)) {
                    if (entity instanceof LivingEntity) {
                        Vector direction = playerLocation.toVector().subtract(entity.getLocation().toVector()).normalize();
                        entity.setVelocity(direction.multiply(2)); // Adjust the multiplier for the strength of attraction
                        entity.teleport(teleportLocation);
                        LivingEntity livingEntity = (LivingEntity) entity;
                        player.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, teleportLocation, 2);
                        livingEntity.damage(20,player);
                    }
                }
                if (teleportLocation.getBlock().isEmpty()) {
                    player.teleport(teleportLocation);
                    player.playSound(teleportLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }else {
                    teleportToNearestAirBlock(player,teleportLocation);
                    player.playSound(teleportLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }
                freezePlayer(targetEntity);
                Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> unfreezePlayer(targetEntity), 50L);
                teleportCount++;
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 20, 20);
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
    public void onPlayerMove(EntityMoveEvent event) {
        LivingEntity player = event.getEntity();
        // Überprüfen, ob der Spieler eingefroren ist
        if (frozenPlayers.containsKey(player)) {
            // Überprüfen, ob die Einfrierzeit abgelaufen ist
            if (System.currentTimeMillis() > frozenPlayers.get(player)) {
                // Entfrieren des Spielers, wenn die Einfrierzeit abgelaufen ist
                unfreezePlayer(player);
            } else {
                // Verhindern, dass sich der Spieler bewegt, während er eingefroren ist
                event.setCancelled(true);
            }
        }
    }

    // Spieler einfrieren
    private void freezePlayer(LivingEntity entity) {
        frozenPlayers.put(entity, System.currentTimeMillis() + 2500);
    }

    // Spieler entfrieren
    private void unfreezePlayer(LivingEntity entity) {
        frozenPlayers.remove(entity);
    }



    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        boolean buffed = NikeyV1.getPlugin().getConfig().getBoolean(player.getName() + ".buffed");
        if (buffed) {
            ItemStack itemInHand = player.getItemInHand();
            if (itemInHand.getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.AQUA + "Inferno Blade") && event.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                if (!red) {
                    Location eventTo = event.getTo();
                    eventTo.getWorld().createExplosion(eventTo.add(0,0.3F,0),1.7F,false,false);
                }
            }
        }
    }

    private ChatColor getRandomColor(ChatColor currentColor) {
        ChatColor[] colors = {ChatColor.RED, ChatColor.GREEN, ChatColor.BLUE, ChatColor.YELLOW, ChatColor.AQUA, ChatColor.LIGHT_PURPLE};
        Random random = new Random();
        ChatColor newColor = colors[random.nextInt(colors.length)];
        // Ensure the new color is different from the current color
        while (newColor == currentColor) {
            newColor = colors[random.nextInt(colors.length)];
        }
        return newColor;
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        Entity d = event.getDamager();
        if (d instanceof Player) {
            Player damager = (Player) d;
            if (damager.getInventory().getItemInMainHand().getType() == Material.NETHERITE_SWORD && damager.getInventory().getItemInMainHand().getItemMeta().hasLore()) {

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
                effect.iterations =0;
                effect.start();
                player.kickPlayer("§cYour are banned by "+killer.getName()+" useing the Inferno Blade");
                Bukkit.getBanList(BanList.Type.NAME).addBan(player.getName(), "§cYour stone is out of strength!",null,"Game");
            }
        }
    }
}
