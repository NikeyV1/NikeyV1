package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.WarpEffect;
import io.papermc.paper.tag.EntityTags;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
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
    private final Set<UUID> teleportedPlayers = new HashSet<>();
    private HashMap<String, Integer> particletimes = new HashMap<>();
    private final Set<UUID> damageReductionPlayers = new HashSet<>();

    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    public static long remainingTime1;
    public static long remainingTime3;

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
    if (!(event.getEntity() instanceof Player)) return;

    Player player = (Player) event.getEntity();
    String name = Stone.getStoneName(player);
    int level = Stone.getStoneLevel(player);

    if (name.equalsIgnoreCase("Ghost") && level >= 7) {
        int hitCount = playerHitCount.getOrDefault(player.getUniqueId(), 0);
        hitCount++;
        if (level == 7) {
            if (hitCount % 15 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),8,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        } else if (level == 8) {
            if (hitCount % 12 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),8,0.1,0.1,0.1);
                event.setCancelled(true);
                playerHitCount.remove(player.getUniqueId());
            }
        }else if (level >= 9) {
            if (hitCount % 9 == 0) {
                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE,player.getLocation().add(0,1,0),8,0.1,0.1,0.1);
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
        String stoneName = Stone.getStoneName(shooter);
        int stoneLevel = Stone.getStoneLevel(shooter);
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
                String stone = Stone.getStoneName(event.getPlayer());
                if (stone.equalsIgnoreCase("Ghost")) {
                    Player player = event.getPlayer();
                    int level = Stone.getStoneLevel(player);
                    String attacking = Stone.getAttacking(player);
                    int range;
                    if (level == 3) {
                        range = 4;
                    } else if (level == 4) {
                        range = 7;
                    } else if (level >= 5) {
                        range = 10;
                    } else {
                        return;
                    }

                    for (Entity e : player.getNearbyEntities(range, range, range)) {
                        if (e instanceof LivingEntity && e != player) {
                            LivingEntity entity = (LivingEntity) e;
                            if (HelpUtil.shouldDamageEntity(entity, player)) {
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * 3, 1, true, false, false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.MINING_FATIGUE, 20 * 3, 0, true, false, false));
                                entity.getWorld().spawnParticle(Particle.SNOWFLAKE, entity.getLocation().add(0, 0.5F, 0), 0, 0, 0, 0);
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
        if (Stone.whatStone(item).equalsIgnoreCase("Ghost")) {
            int level = Stone.getStoneLevelFromItem(item);
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
                                        p.sendMessage("§3You are now visible!");
                                    }
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*20);
                        }else if (level >= 12) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (playerHitted.containsKey(p.getName())) {
                                        makePlayerVisible(p);
                                        p.sendMessage("§3You are now visible!");
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
        if (Stone.whatStone(item).equalsIgnoreCase("Ghost")){
            int level = Stone.getStoneLevelFromItem(item);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Ghost");
            config.set(p.getName()+".level",level);
            NikeyV1.getPlugin().saveConfig();
            if (level == 20 || level == 21){
                if (p.isSneaking()) {
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                       Vector up = new Vector(0, 3, 0);
                        p.setVelocity(up);
                        teleportedPlayers.add(p.getUniqueId());
                        damageReductionPlayers.add(p.getUniqueId());

                        Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> teleportedPlayers.remove(p.getUniqueId()), 120L);
                        Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> damageReductionPlayers.remove(p.getUniqueId()), 300L);

                        Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
                            Vector down = new Vector(0, -3, 0); // Boost nach unten
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
            String attacking = Stone.getAttacking(player);
            if (event.getCause() == EntityDamageEvent.DamageCause.FALL && teleportedPlayers.contains(player.getUniqueId())) {
                event.setCancelled(true); // Prevent fall damage for the player

                int level = Stone.getStoneLevel(player);
                if (level == 20) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(3);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED ,20*15,4,false,false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,20*15,3,false,false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*15,3,false,false));

                    // Create smoke particles around the player
                    particletimes.put(player.getName(),8);
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            if (particletimes.get(player.getName()) == 0) {
                                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                                particletimes.remove(player.getName());
                                cancel();
                                return;
                            }

                            for (int i = 0; i < 3500; i++) {
                                Location smokeLocation = player.getLocation().add((Math.random() - 0.5) * 30, (Math.random() - 0.5) * 10, (Math.random() - 0.5) * 30);
                                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, 0, 0, 0, 0.1);
                            }

                            particletimes.replace(player.getName(),particletimes.get(player.getName())-1);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,40);
                    
                    for (Entity entity : player.getNearbyEntities(30, 30, 30)) {
                        if (entity instanceof LivingEntity && entity != player) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (HelpUtil.shouldDamageEntity(livingEntity,player)) {
                                if (entity instanceof Player) {
                                    List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                                    if (!playersInSameTeam.contains(entity)) {
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*15,3,true));
                                        double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                                        double damage = maxHealth * 0.3;
                                        livingEntity.playHurtAnimation(0);
                                        if (livingEntity.getHealth()-damage >= 1) {
                                            livingEntity.setHealth(livingEntity.getHealth()-damage);
                                        }else if (!EntityTags.UNDEADS.isTagged(livingEntity.getType())){
                                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                        }else {
                                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                        }

                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20*15, 1));
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*15, 1));
                                    }
                                }else {
                                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*15,3,true));
                                    double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                                    double damage = maxHealth * 0.3;
                                    livingEntity.playHurtAnimation(0);
                                    if (livingEntity.getHealth()-damage >= 1) {
                                        livingEntity.setHealth(livingEntity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(livingEntity.getType())){
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }

                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20*15, 1));
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*15, 1));
                                }
                            }
                        }
                    }

                    teleportedPlayers.remove(player.getUniqueId()); // Remove player from the teleported set

                    // Schedule task to slow down the server after 2 seconds
                    Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick rate 10"), 1L); // 2 seconds later
                    // Schedule task to reset the tick rate after 10 seconds
                    Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tick rate 20"), 300L);
                }else if (level == 21) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(5);
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED ,20*30,4,false,false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.HASTE,20*30,3,false,false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,20*30,3,false,false));

                    // Create smoke particles around the player
                    particletimes.put(player.getName(),16);
                    new BukkitRunnable() {
                        @Override
                        public void run() {

                            if (particletimes.get(player.getName()) == 0) {
                                player.getAttribute(Attribute.GENERIC_ATTACK_DAMAGE).setBaseValue(1);
                                particletimes.remove(player.getName());
                                cancel();
                                return;
                            }

                            for (int i = 0; i < 3500; i++) {
                                Location smokeLocation = player.getLocation().add((Math.random() - 0.5) * 30, (Math.random() - 0.5) * 10, (Math.random() - 0.5) * 30);
                                player.getWorld().spawnParticle(Particle.CAMPFIRE_COSY_SMOKE, smokeLocation, 0, 0, 0, 0, 0.1);
                            }

                            particletimes.replace(player.getName(),particletimes.get(player.getName())-1);
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,40);

                    // Give blindness effect to nearby players
                    for (Entity entity : player.getNearbyEntities(30, 30, 30)) {
                        if (entity instanceof LivingEntity && entity != player) {
                            LivingEntity livingEntity = (LivingEntity) entity;
                            if (HelpUtil.shouldDamageEntity(livingEntity,player)) {
                                if (entity instanceof Player) {
                                    List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(player);
                                    if (!playersInSameTeam.contains(entity)) {
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*30,3,true));
                                        double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                                        double damage = maxHealth * 0.4;
                                        livingEntity.playHurtAnimation(0);
                                        if (livingEntity.getHealth()-damage >= 1) {
                                            livingEntity.setHealth(livingEntity.getHealth()-damage);
                                        }else {
                                            livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                        }

                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20*30, 1));
                                        ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*30, 1));
                                    }
                                }else {
                                    livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*30,3,true));
                                    double maxHealth = livingEntity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                                    double damage = maxHealth * 0.4;
                                    livingEntity.playHurtAnimation(0);
                                    if (livingEntity.getHealth()-damage >= 1) {
                                        livingEntity.setHealth(livingEntity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(livingEntity.getType())){
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        livingEntity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }

                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 20*30, 1));
                                    ((LivingEntity) entity).addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20*30, 1));
                                }
                            }
                        }
                    }

                    teleportedPlayers.remove(player.getUniqueId());

                    // Schedule task to slow down the server after 2 seconds
                    Bukkit.getServerTickManager().setTickRate(10);
                    Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> Bukkit.getServerTickManager().setTickRate(10), 1L);
                    // Schedule task to reset the tick rate after 30 seconds
                    Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> Bukkit.getServerTickManager().setTickRate(20), 600L);
                }
            }else if (damageReductionPlayers.contains(player.getUniqueId())) {
                Random r = new Random();
                int choice = r.nextInt(2);
                if (choice == 0) {
                    event.setCancelled(true);
                }
            }
        }
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

        if (playerHitted.containsKey(player.getName())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerHit(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player damager = (Player) event.getDamager();
            if (Stone.getStoneName(damager).equalsIgnoreCase("ghost")) {
                if (playerHitted.containsKey(damager.getName())) {
                    int hits = playerHitted.get(damager.getName()) + 1;
                    int level = Stone.getStoneLevel(damager);
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