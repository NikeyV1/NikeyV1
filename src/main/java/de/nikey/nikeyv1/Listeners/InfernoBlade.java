package de.nikey.nikeyv1.Listeners;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.BleedEffect;
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
                                        List<Entity> nearbyEntities = player.getNearbyEntities(12, 12, 12);
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
                                            Location loc = targetLocation.clone().subtract(0, i, 0);
                                            if (loc.getBlock().getType().isSolid()) {
                                                targetLocation = loc.add(0, i + 1, 0);
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

    private void teleportAndRoot(Player player, List<LivingEntity> targets) {
        Location loc = player.getLocation();
        Collections.sort(targets, Comparator.comparingDouble(entity -> entity.getLocation().distance(player.getLocation())));

        new BukkitRunnable() {
            int teleportCount = 0;

            @Override
            public void run() {
                if (teleportCount >= 5 || targets.isEmpty()) {
                    player.teleport(loc);
                    cancel();
                    return;
                }

                LivingEntity targetEntity = targets.get(teleportCount);
                targetEntity.damage(14);
                Location playerLocation = player.getLocation();
                Location nearestPlayerLocation = targetEntity.getLocation();

                // Überprüfen, ob der Spieler weniger als 10 Blöcke entfernt ist
                double distance = playerLocation.distance(nearestPlayerLocation);
                if (distance < 10) {
                    Location teleportLocation = nearestPlayerLocation.getWorld().getHighestBlockAt(nearestPlayerLocation).getLocation();
                    player.teleport(teleportLocation);
                    player.playSound(teleportLocation, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                }
                teleportCount++;
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 20, 20);
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
                    eventTo.getWorld().createExplosion(eventTo,1.7F,false,false);
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
