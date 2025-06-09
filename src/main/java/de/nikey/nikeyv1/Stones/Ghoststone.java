package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.StoneHandler;
import de.slikey.effectlib.effect.WarpEffect;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

@SuppressWarnings("ALL")
public class Ghoststone implements Listener {

    private HashMap<UUID, Integer> playerHitCount = new HashMap<>();
    private HashMap<String, Integer> playerHitted = new HashMap<>();
    private ArrayList<Entity> ghost = new ArrayList<>();
    private HashSet<UUID> ghostPlayers = new HashSet<>();
    private HashMap<UUID, HashSet<Block>> removedBlocks = new HashMap<>();

    //Master Ability
    private final Set<UUID> teleportedPlayers = new HashSet<>();
    private Long storedWorldTime = null;
    private final Map<UUID, Integer> activePlayers = new HashMap<>();
    private boolean isDarkNightActive = false;


    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    public static long remainingTime1;
    public static long remainingTime3;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player)) return;

    Player player = (Player) event.getEntity();
    String name = StoneHandler.getStoneName(player);
    int level = StoneHandler.getStoneLevel(player);

    if (name.equalsIgnoreCase("Ghost") && level >= 7) {
        int hitCount = playerHitCount.getOrDefault(player.getUniqueId(), 0);
        hitCount++;
        if (level == 7) {
            if (hitCount % 16 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),6,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        } else if (level == 8) {
            if (hitCount % 14 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),6,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        }else if (level >= 9) {
            if (hitCount % 12 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),6,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        }
        playerHitCount.put(player.getUniqueId(), hitCount);
    }



    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        if (!(event.getEntity().getShooter() instanceof Player)) return;

        Player shooter = (Player) event.getEntity().getShooter();
        String stoneName = StoneHandler.getStoneName(shooter);
        int stoneLevel = StoneHandler.getStoneLevel(shooter);
        if (stoneName.equalsIgnoreCase("Ghost") && stoneLevel >= 6) {
            if (entity instanceof Arrow) {
                Arrow arrow = (Arrow) entity;
                if (!arrow.isCritical() && arrow.canHitEntity(shooter)) {
                    shooter.getWorld().spawnParticle(Particle.DUST_PLUME,shooter.getLocation().add(0,1,0),10,0.1,0.1,0.1);
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        new BukkitRunnable() {
            @Override
            public void run() {
                String stone = StoneHandler.getStoneName(event.getPlayer());
                if (stone.equalsIgnoreCase("Ghost")) {
                    Player player = event.getPlayer();
                    int level = StoneHandler.getStoneLevel(player);
                    String attacking = StoneHandler.getAttacking(player);
                    int range;
                    if (level == 3) {
                        range = 3;
                    } else if (level == 4) {
                        range = 5;
                    } else if (level >= 5) {
                        range = 8;
                    } else {
                        return;
                    }

                    for (Entity e : player.getNearbyEntities(range, range, range)) {
                        if (e instanceof LivingEntity entity && e != player) {
                            if (HelpUtil.shouldDamageEntity(entity, player)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 3, 0, true, false, false));

                                AttributeInstance attr = entity.getAttribute(Attribute.BLOCK_BREAK_SPEED);
                                if (attr != null) {
                                    double defaultValue = attr.getDefaultValue();
                                    double reduction = defaultValue * 0.33;
                                    attr.setBaseValue(defaultValue - reduction);

                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            if (!entity.isValid()) {
                                                attr.setBaseValue(defaultValue);
                                                cancel();
                                            }else if (entity.getWorld() != player.getWorld() || entity.getLocation().distanceSquared(player.getLocation()) > range * range) {
                                                attr.setBaseValue(defaultValue);
                                                cancel();
                                            }
                                        }
                                    }.runTaskLater(NikeyV1.getPlugin(), 10);
                                }

                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE, entity.getLocation().add(0, 0.3F, 0), 0, 0, 0, 0);
                            }
                        }
                    }

                    if (!player.isOnline()) {
                        cancel();
                        return;
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),5,2);
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (StoneHandler.whatStone(item).equalsIgnoreCase("Ghost")) {
            int level = StoneHandler.getStoneLevelFromItem(item);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Ghost");
            config.set(p.getName()+".level",level);
            NikeyV1.getPlugin().saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (level >= 10){
                    if (!(cooldown.getOrDefault(p.getUniqueId(), 0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));

                        WarpEffect effect = new WarpEffect(NikeyV1.em);
                        effect.setLocation(p.getLocation());
                        effect.particles = 35;
                        effect.particle = Particle.CAMPFIRE_COSY_SMOKE;
                        effect.grow = 0.1F;
                        effect.start();
                        makePlayerInvisible(p);

                        if (level >= 13) {
                            p.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,20*30,1));
                        }

                        if (level == 10 || level == 11) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (playerHitted.containsKey(p.getName())) {
                                        makePlayerVisible(p);
                                        p.sendActionBar(Component.text("You are now visible!").color(NamedTextColor.DARK_AQUA));
                                    }
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*20);
                        }else if (level >= 12) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (playerHitted.containsKey(p.getName())) {
                                        makePlayerVisible(p);
                                        p.sendActionBar(Component.text("You are now visible!").color(NamedTextColor.DARK_AQUA));
                                    }
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*30);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (StoneHandler.whatStone(item).equalsIgnoreCase("Ghost")){
            int level = StoneHandler.getStoneLevelFromItem(item);
            if (level == 20 || level == 21){
                if (p.isSneaking() && Bukkit.getServer().getServerTickManager().isRunningNormally()) {
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                       Vector up = new Vector(0, 3, 0);
                        p.setVelocity(up);
                        teleportedPlayers.add(p.getUniqueId());

                        Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> teleportedPlayers.remove(p.getUniqueId()), 120L);

                        Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                            Vector down = new Vector(0, -3, 0);
                            p.setVelocity(down);
                        }, 20L);
                    }
                }
            }
        }
    }



    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            String attacking = StoneHandler.getAttacking(player);
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && teleportedPlayers.contains(player.getUniqueId())) {
                player.getWorld().playSound(player.getLocation(),Sound.ENTITY_ITEM_BREAK,1,1);
                event.setCancelled(true);

                int level = StoneHandler.getStoneLevel(player);
                if (level == 20) {
                    activateDarkNight(player, 15);
                }else if (level == 21) {
                    activateDarkNight(player, 30);
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onDamageDealt(EntityDamageByEntityEvent event) {
        if (!(event.getEntity() instanceof Player player)) return;
        if (!activePlayers.containsKey(player.getUniqueId())) return;

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.hidePlayer(NikeyV1.getPlugin(), player);
        }
        player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20,0,false,false));

        new BukkitRunnable() {
            @Override
            public void run() {
                for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                    onlinePlayer.showPlayer(NikeyV1.getPlugin(), player);
                }
            }
        }.runTaskLater(NikeyV1.getPlugin(), 20);
    }


    public void activateDarkNight(Player player, int durationSeconds) {
        if (activePlayers.containsKey(player.getUniqueId())) return;

        World world = player.getWorld();
        if (storedWorldTime == null) {
            storedWorldTime = world.getTime();
        }
        activePlayers.put(player.getUniqueId(), durationSeconds);

        Location loc = player.getLocation();

        for (int i = 0; i < 8; i++) {
            double angle = i * (Math.PI / 4);
            double x = loc.getX() + Math.cos(angle) * 1.5;
            double z = loc.getZ() + Math.sin(angle) * 1.5;
            Location particleLoc = new Location(world, x, loc.getY() + 1.0, z);

            world.spawnParticle(Particle.LARGE_SMOKE, particleLoc, 1, 0, 0, 0, 0);
            world.spawnParticle(Particle.SOUL, particleLoc, 1, 0, 0, 0, 0);
            world.spawnParticle(Particle.ASH, particleLoc, 1, 0, 0, 0, 0);
        }

        world.spawnParticle(Particle.END_ROD, loc.clone().add(0, 1, 0), 10, 0.5, 0.5, 0.5, 0.05);

        world.playSound(net.kyori.adventure.sound.Sound.sound(Key.key("entity.vex.charge"), net.kyori.adventure.sound.Sound.Source.AMBIENT,2,0.8f));

        double attackSpeedBase = player.getAttribute(Attribute.ATTACK_SPEED).getBaseValue();
        double breakSpeedBase = player.getAttribute(Attribute.BLOCK_BREAK_SPEED).getBaseValue();
        double movementSpeedBase = player.getAttribute(Attribute.MOVEMENT_SPEED).getBaseValue();

        player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(attackSpeedBase*1.25);
        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(breakSpeedBase*1.25);
        player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(movementSpeedBase*1.25);

        if (!isDarkNightActive) {
            startDarkNight(world);
        }

        new BukkitRunnable() {
            int timer = durationSeconds;

            @Override
            public void run() {
                if (!activePlayers.containsKey(player.getUniqueId())) {
                    cancel();
                    return;
                }

                if (timer <= 0) {
                    deactivateDarkNight(player);
                    cancel();
                    return;
                }

                for (Entity entity : player.getNearbyEntities(50, 50, 50)) {
                    if (entity instanceof Player target) {
                        if (!HelpUtil.shouldDamageEntity(target,player))continue;
                        target.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 100, 0, false, false));
                        target.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 100, 0, false, false));
                    }
                }

                activePlayers.put(player.getUniqueId(), timer--);
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0L, 20L);
    }

    private void startDarkNight(World world) {
        world.setTime(18000);
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);

        Bukkit.getServerTickManager().setTickRate(15);

        isDarkNightActive = true;
    }

    private void deactivateDarkNight(Player player) {
        activePlayers.remove(player.getUniqueId());
        player.playSound(player.getLocation(),Sound.BLOCK_ENDER_CHEST_OPEN,0.5f,0.9f);
        player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4.0);
        player.getAttribute(Attribute.BLOCK_BREAK_SPEED).setBaseValue(1.0);
        player.getAttribute(Attribute.MOVEMENT_SPEED).setBaseValue(0.1);
        if (activePlayers.isEmpty()) {
            resetWorldTime(player.getWorld());
        }
    }

    private void resetWorldTime(World world) {
        world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
        world.setTime(storedWorldTime);
        storedWorldTime = null;

        Bukkit.getServerTickManager().setTickRate(20);
        isDarkNightActive = false;
    }

    public void makePlayerInvisible(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.hidePlayer(NikeyV1.getPlugin(), player);
            }
        }
        playerHitted.put(player.getName(), 0);
    }

    public void makePlayerVisible(Player player) {
        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            onlinePlayer.showPlayer(NikeyV1.getPlugin(), player);
        }
        playerHitted.remove(player.getName());
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();
        if (!(target instanceof Player)) return;

        Player player = (Player) target;

        if (playerHitted.containsKey(player.getName()) || Holystone.vanishedPlayers.contains(player.getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (StoneHandler.getStoneName(damager).equalsIgnoreCase("ghost")) {
                if (playerHitted.containsKey(damager.getName())) {
                    int hits = playerHitted.get(damager.getName()) + 1;
                    int level = StoneHandler.getStoneLevel(damager);
                    double damageMultiplier = 1;
                    if (level == 10) {
                        damageMultiplier = 1.0 + (hits * 0.1);
                    }else if (level == 11 || level == 12 || level == 13) {
                        damageMultiplier = 1.0 + (hits * 0.15);
                    }else if (level >= 14) {
                        damageMultiplier = 1.0 + (hits * 0.2);
                    }
                    double damage = event.getDamage() * damageMultiplier;
                    event.setDamage(damage);
                    damager.playSound(damager.getLocation(), Sound.ENTITY_PLAYER_BIG_FALL, 1.2F, 1.2F);
                    if (hits >= 5) {
                        makePlayerVisible(damager);
                        damager.sendMessage("§3You have hit other players 5 times, you are now visible!");
                    } else {
                        playerHitted.put(damager.getName(), hits);
                        damager.sendMessage("§7You hit another player " + hits + " times for " + damage + " hearts of damage");
                    }
                }
            }
        }
    }
}
