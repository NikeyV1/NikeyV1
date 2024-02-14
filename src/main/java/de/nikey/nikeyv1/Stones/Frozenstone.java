package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.Tornado;
import de.slikey.effectlib.effect.SmokeEffect;
import io.papermc.paper.configuration.type.fallback.FallbackValue;
import net.kyori.adventure.text.ComponentLike;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

import static de.nikey.nikeyv1.Util.HelpUtil.getNearbyBlocks;
import static de.nikey.nikeyv1.Util.HelpUtil.triggerEntityAggro;

@SuppressWarnings("ALL")
public class Frozenstone implements Listener {
    private ArrayList<LivingEntity> entities = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    public static ArrayList<LivingEntity> notp = new ArrayList<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;

    private static final String ICE_ARROW_METADATA = "isIceArrow";

    private boolean abilityActivated = false;

    public static HashMap<Player, Integer> arrowsShot = new HashMap<>();


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String stone = config.getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("Frozen")&&player.getGameMode() == GameMode.SURVIVAL && config.getInt(player.getName()+".level") >4){
            if (player.getLocation().getBlock().getType() == Material.POWDER_SNOW) {
                player.setAllowFlight(true);
                player.setFlying(true);
            } else {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Frozen Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Frozen");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (abilityActivated) {
                    if (abilityActivated && arrowsShot.containsKey(p) && arrowsShot.get(p) < 5) {
                        shootFrozenDagger(p);
                        Integer shot = arrowsShot.get(p);
                        arrowsShot.replace(p,shot+1);
                        if (arrowsShot.get(p) == 5) {
                            abilityActivated = false;
                            p.sendMessage(ChatColor.RED + "You have shot 5 Frozen Daggers. Ability deactivated!");
                            arrowsShot.put(p,0);
                        }
                    }
                }else {
                    if (i >= 10) {
                        if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    cooldown.remove(p.getUniqueId());
                                    cancel();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 100);
                            //cooldown-ability
                            if (i ==10 || i == 11){
                                timer = 4;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        timer--;
                                        p.sendMessage(String.valueOf(timer));
                                        if (timer ==0){
                                            cancel();
                                        }else {
                                            Snowball snowball = p.launchProjectile(Snowball.class);
                                            snowball.setVelocity(p.getLocation().getDirection().multiply(2));
                                            snowball.setShooter(p);
                                            snowball.setCustomName("B");
                                        }
                                    }
                                }.runTaskTimer(NikeyV1.getPlugin(),15,15);
                            }else if (i >= 12) {
                                timer = 4;
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        timer--;
                                        if (timer ==0){
                                            cancel();
                                        }else {
                                            Snowball snowball = p.launchProjectile(Snowball.class);
                                            snowball.setVelocity(p.getLocation().getDirection().multiply(2));
                                            snowball.setShooter(p);
                                            snowball.setCustomName("B");
                                        }
                                    }
                                }.runTaskTimer(NikeyV1.getPlugin(),15,15);
                            }
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (i == 15||i == 16||i == 17){
                    if (!p.isSneaking()) {
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //Cooldown-Ability

                            for (Entity e : p.getNearbyEntities(50,50,50)){
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,400,3,false));
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,0,false));
                                    notp.add(entity);
                                    entity.damage(16,p);
                                    entity.setFreezeTicks(600);
                                    SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                    effect.setEntity(entity);
                                    effect.particle = Particle.SNOWFLAKE;
                                    effect.duration = 20000;
                                    effect.particles = 2;
                                    effect.start();
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            notp.remove(entity);
                                        }
                                    }.runTaskLater(NikeyV1.getPlugin(),400);
                                }
                            }
                        }
                    }
                }else if (i >=18){
                    if (!p.isSneaking()) {
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //Cooldown-Ability

                            for (Entity e : p.getNearbyEntities(50,50,50)){
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,400,3,false));
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,3,false));
                                    notp.add(entity);
                                    entity.damage(18,p);
                                    entity.setFreezeTicks(700);
                                    SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                    effect.setEntity(entity);
                                    effect.particle = Particle.SNOWFLAKE;
                                    effect.duration = 20000;
                                    effect.particles = 2;
                                    effect.start();
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            notp.remove(entity);
                                        }
                                    }.runTaskLater(NikeyV1.getPlugin(),400);
                                }
                            }
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
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§3Frozen Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Frozen");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (p.isSneaking()) {
                if (i == 20 || i == 21) {
                    if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown2.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
                        //Cooldown-Ability
                        activateAbility(p);
                    }
                }
            }
        }
    }

    private void shootFrozenDagger(Player player) {
        Location eyeLocation = player.getEyeLocation();
        Location spawnLocation1 = eyeLocation.add(eyeLocation.getDirection().normalize());
        Location spawnLocation2 = spawnLocation1.clone().add(0, -0.8D, 0);
        Location spawnLocation3 = spawnLocation1.clone().add(0.8D, 0, 0);
        Location spawnLocation4 = spawnLocation1.clone().add(0, 0.8D, 0);
        Location spawnLocation5 = spawnLocation1.clone().add(-0.8D, 0, 0);

        shootArrow(player, spawnLocation2);
        shootArrow(player, spawnLocation3);
        shootArrow(player, spawnLocation4);
        shootArrow(player, spawnLocation5);
    }
    private void shootArrow(Player player, Location spawnLocation) {
        int i = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
        if (i == 20) {
            SpectralArrow arrow = (SpectralArrow) player.getWorld().spawnEntity(spawnLocation, EntityType.SPECTRAL_ARROW);
            arrow.setShooter(player);
            arrow.setVelocity(player.getEyeLocation().getDirection().multiply(2.4));
            arrow.setCustomName(ChatColor.DARK_AQUA + "FD");
            arrow.setCustomNameVisible(false);
            arrow.setDamage(9);
            arrow.setGravity(false); // Disable arrow gravity
            arrow.setMetadata(ICE_ARROW_METADATA, new FixedMetadataValue(NikeyV1.getPlugin(), true)); // Set metadata to mark arrow as ice arrow for no reason
            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), arrow::remove, 20 * 6L); // Remove arrow after 6 seconds
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()){
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),2);
                    }else {
                        cancel();
                        return;
                    }
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0L,3L);
        } else if (i == 21) {
            SpectralArrow arrow = (SpectralArrow) player.getWorld().spawnEntity(spawnLocation, EntityType.SPECTRAL_ARROW);
            arrow.setShooter(player);
            arrow.setVelocity(player.getEyeLocation().getDirection().multiply(2.6));
            arrow.setCustomName(ChatColor.DARK_AQUA + "FD");
            arrow.setCustomNameVisible(false);
            arrow.setDamage(14);
            arrow.setPierceLevel(2);
            arrow.setGravity(false); // Disable arrow gravity
            arrow.setMetadata(ICE_ARROW_METADATA, new FixedMetadataValue(NikeyV1.getPlugin(), true)); // Set metadata to mark arrow as ice arrow for no reason
            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), arrow::remove, 20 * 6L); // Remove arrow after 6 seconds
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()){
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),2);
                    }else {
                        cancel();
                        return;
                    }
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0L,3L);
        }
    }

    public void activateAbility(Player player) {
        abilityActivated = true;
        arrowsShot.put(player,0);
        player.sendMessage(ChatColor.GREEN + "Ability activated! You can now throw Frozen Daggers for 20 seconds.");
        Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), () -> {
            abilityActivated = false;
            arrowsShot.remove(player);
        }, 20 * 20L);
    }

    @EventHandler
    public void onEntityTeleport(EntityTeleportEvent event) {
        Entity entity = event.getEntity();
        if (notp.contains(entity)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        if (notp.contains(player)) event.setCancelled(true);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        Entity hitEntity = event.getHitEntity();
        ProjectileSource shooter = entity.getShooter();
        if ((entity instanceof Snowball) && entity.getCustomName().equalsIgnoreCase("B") && (shooter instanceof Player)){
            Player p = (Player) shooter;
            LivingEntity e = (LivingEntity) hitEntity;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            assert e != null;
            if (i >=12){
                e.damage(18,p);
            }else {
                e.damage(8,p);
            }
            e.setFreezeTicks(800);
            e.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*20,3,false));
            e.setVisualFire(false);
            entities.add(e);
            new BukkitRunnable() {
                @Override
                public void run() {
                    entities.remove(e);
                }
            }.runTaskLater(NikeyV1.getPlugin(),20*20);
        }
        if (entity instanceof SpectralArrow && entity.getCustomName() != null && entity.getCustomName().equals(ChatColor.DARK_AQUA + "FD") && shooter instanceof Player) {
            Player p = (Player) shooter;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (hitEntity != null) {
                LivingEntity e = (LivingEntity) hitEntity;

                applyRandomNegativeEffect(i,e);
                e.setFreezeTicks(20*6);
            }
            entity.remove();
        }
    }

    @EventHandler
    public void onEntityJump(EntityJumpEvent event) {
        LivingEntity entity = event.getEntity();
        if (entities.contains(entity)) event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        if (entities.contains(player)) event.setCancelled(true);
    }

    private void applyRandomNegativeEffect(Integer level, LivingEntity entity) {
        // List of negative potion effects
        PotionEffectType[] negativeEffects = {
                PotionEffectType.BLINDNESS,
                PotionEffectType.CONFUSION,
                PotionEffectType.HUNGER,
                PotionEffectType.POISON,
                PotionEffectType.SLOW,
                PotionEffectType.WEAKNESS
        };

        Random random = new Random();
        Random r = new Random();
        int i = r.nextInt(2);
        if (i == 1) {
            if (level == 20) {
                PotionEffectType effectType = negativeEffects[random.nextInt(negativeEffects.length)];
                // Apply the effect to the player
                entity.addPotionEffect(new PotionEffect(effectType, 200, 1));
                entity.getWorld().playEffect(entity.getLocation(), Effect.HUSK_CONVERTED_TO_ZOMBIE, 0);
            } else if (level == 21) {
                PotionEffectType effectType = negativeEffects[random.nextInt(negativeEffects.length)];
                // Apply the effect to the player
                entity.addPotionEffect(new PotionEffect(effectType, 200, 2));
                entity.getWorld().playEffect(entity.getLocation(), Effect.HUSK_CONVERTED_TO_ZOMBIE, 0);
            }
        }
    }
}
