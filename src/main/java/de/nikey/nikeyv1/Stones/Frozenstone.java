package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.StoneHandler;
import de.slikey.effectlib.effect.SmokeEffect;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Frozenstone implements Listener {
    private ArrayList<LivingEntity> entities = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    public static ArrayList<LivingEntity> notp = new ArrayList<>();
    public static HashMap<Player, Integer> timer = new HashMap<>();
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;

    private static final String ICE_ARROW_METADATA = "isIceArrow";

    private boolean abilityActivated = false;

    public static HashMap<Player, Integer> arrowsShot = new HashMap<>();



    private boolean isColdBiome(Biome biome) {
        Biome[] coldBiomes = {Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER, Biome.COLD_OCEAN, Biome.DEEP_COLD_OCEAN, Biome.ICE_SPIKES, Biome.OCEAN,
                Biome.SNOWY_BEACH, Biome.SNOWY_PLAINS, Biome.SNOWY_SLOPES, Biome.SNOWY_TAIGA, Biome.STONY_SHORE, Biome.FROZEN_RIVER,Biome.DEEP_FROZEN_OCEAN,Biome.JAGGED_PEAKS,
                Biome.GROVE};

        for (Biome coldBiome : coldBiomes) {
            if (biome.equals(coldBiome)) {
                return true;
            }
        }
        return false;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String stone = config.getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("Frozen")) {
            if (config.getInt(player.getName()+".level") == 7) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnline())cancel();
                        if (player.isInPowderedSnow()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,22,0,true,false,true));
                        }
                    }
                }.runTaskTimer(NikeyV1.getPlugin(), 0L, 20L);
            } else if (config.getInt(player.getName() + ".level") == 8) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnline())cancel();
                        if (player.isInPowderedSnow()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,22,1,true,false,true));
                        }
                    }
                }.runTaskTimer(NikeyV1.getPlugin(), 0L, 20L);
            }else if (config.getInt(player.getName() + ".level") >= 9) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        if (!player.isOnline()) cancel();
                        if (player.isInPowderedSnow()) {
                            player.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION, 222, 1, true, false,true));
                        }
                    }
                }.runTaskTimer(NikeyV1.getPlugin(), 0L, 20L);
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        String stone = config.getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("Frozen") && player.getGameMode() == GameMode.SURVIVAL && config.getInt(player.getName()+".level") >=5){
            if (player.getLocation().getBlock().getType() == Material.POWDER_SNOW) {
                player.setAllowFlight(true);
                player.setFlying(true);
            } else {
                player.setAllowFlight(false);
                player.setFlying(false);
            }
            if (config.getInt(player.getName()+".level") >= 6) {
                if (isColdBiome(player.getWorld().getBiome(player.getLocation()))) {
                    if (player.getFlySpeed() != 0.14F) {
                        player.setFlySpeed(0.14F);
                    }
                }else {
                    if (player.getFlySpeed() == 0.14F) {
                        player.setFlySpeed(0.1F);
                    }
                }
            }
        }
    }



    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player player = (Player) entity;
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
            if (stone.equalsIgnoreCase("frozen") && event.getCause() == EntityDamageEvent.DamageCause.FREEZE) {
                if (level == 3) {
                    double damage = event.getDamage();
                    event.setDamage(damage * 0.5);
                } else if (level >= 4) {
                    event.setCancelled(true);
                }
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
                            Component textComponent = Component.text("You have shot 5 Frozen Daggers").color(NamedTextColor.RED);
                            p.sendActionBar(textComponent);
                            arrowsShot.put(p,0);
                        }
                    }
                }else {
                    if (i >= 10) {
                        if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                            //cooldown-ability
                            if (i ==10){
                                timer.put(p,4);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        timer.replace(p,timer.get(p)-1);
                                        if (timer.get(p) ==0){
                                            cancel();
                                        }else {
                                            Snowball snowball = p.launchProjectile(Snowball.class);
                                            snowball.setVelocity(p.getLocation().getDirection().multiply(2));
                                            snowball.setShooter(p);
                                            snowball.setCustomName("B");
                                        }
                                    }
                                }.runTaskTimer(NikeyV1.getPlugin(),15,15);
                            } else if (i == 11) {
                                timer.put(p,4);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        timer.replace(p,timer.get(p)-1);
                                        if (timer.get(p) ==0){
                                            cancel();
                                        }else {
                                            Snowball snowball = p.launchProjectile(Snowball.class);
                                            snowball.setVelocity(p.getLocation().getDirection().multiply(2));
                                            snowball.setShooter(p);
                                            snowball.setCustomName("B");
                                        }
                                    }
                                }.runTaskTimer(NikeyV1.getPlugin(),15,15);
                            } else if (i >= 12) {
                                timer.put(p,4);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        timer.replace(p,timer.get(p)-1);
                                        if (timer.get(p) ==0){
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
                String damageEntityType = StoneHandler.getAttacking(p);
                if (p.isSneaking() || ability.getOrDefault(p.getUniqueId(), 0L) > System.currentTimeMillis()) return;

                ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000)); // Cooldown-Ability

                int range = (i == 15) ? 40 : 50;
                int damage = (i >= 17) ? 24 : 18;
                int freezeTicks = (i >= 18) ? 700 : 600;
                int slownessLevel = (i >= 19) ? 3 : 2;
                    

                for (Entity e : p.getNearbyEntities(range, range, range)) {
                    if (e instanceof LivingEntity entity && HelpUtil.shouldDamageEntity(entity, p)) {
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 400, slownessLevel, false));
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 400, 0, false));
                        notp.add(entity);
                        entity.setNoDamageTicks(0);
                        entity.damage(damage, p);
                        entity.setFreezeTicks(freezeTicks);

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
                        }.runTaskLater(NikeyV1.getPlugin(), 400);
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
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                        activateAbility(p);
                    }
                }
            }
        }
    }

    private void shootFrozenDagger(Player player) {
        Location eyeLocation = player.getEyeLocation();
        org.bukkit.util.Vector direction = eyeLocation.getDirection().normalize();

        org.bukkit.util.Vector right = direction.clone().crossProduct(new Vector(0, 1, 0)).normalize();
        org.bukkit.util.Vector up = new Vector(0, 1, 0);
        org.bukkit.util.Vector down = up.clone().multiply(-1);

        Location spawnLocation1 = eyeLocation.clone().add(direction);
        Location spawnLocation2 = spawnLocation1.clone().add(down.multiply(0.8));
        Location spawnLocation3 = spawnLocation1.clone().add(right.multiply(0.9));
        Location spawnLocation4 = spawnLocation1.clone().add(up.multiply(0.8));
        Location spawnLocation5 = spawnLocation1.clone().add(right.multiply(-0.9));

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
            arrow.setDamage(5);
            arrow.setCritical(true);
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
            arrow.setDamage(10);
            arrow.setCritical(true);
            arrow.setPierceLevel(3);
            arrow.setGravity(false); // Disable arrow gravity
            arrow.setMetadata(ICE_ARROW_METADATA, new FixedMetadataValue(NikeyV1.getPlugin(), true)); // Set metadata to mark arrow as ice arrow for no reason
            Bukkit.getScheduler().runTaskLater(NikeyV1.getPlugin(), arrow::remove, 20 * 6L); // Remove arrow after 6 seconds
            new BukkitRunnable(){
                @Override
                public void run() {
                    if (!arrow.isDead()){
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),0,0.2,0.2,0.2);
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),0,0.2,0.2,0.2);
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),0,0.2,0.2,0.2);
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),0,0.2,0.2,0.2);
                        arrow.getWorld().spawnParticle(Particle.SNOWFLAKE,arrow.getLocation(),0,0.2,0.2,0.2);
                    }else {
                        cancel();
                        return;
                    }
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0L,3);
        }
    }

    public void activateAbility(Player player) {
        abilityActivated = true;
        arrowsShot.put(player,0);
        Component textComponent = Component.text("You can now throw Frozen Daggers for 20 seconds").color(NamedTextColor.GREEN);
        player.sendActionBar(textComponent);
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
        if ((entity instanceof Snowball) && entity.getCustomName().equalsIgnoreCase("B") && (shooter instanceof Player) && event.getHitEntity() instanceof LivingEntity){
            Player p = (Player) shooter;
            LivingEntity e = (LivingEntity) hitEntity;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            assert e != null;
            if (i == 10 || i == 11){
                e.damage(12,p);
            }else if (i == 12 || i == 13){
                e.damage(15,p);
            } else if (i >= 14) {
                e.damage(18,p);
            }
            e.setFreezeTicks(800);
            if (i == 10 || i == 11 || i == 12) {
                e.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*20,1,false));
            }else if (i >= 13) {
                e.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS,20*20,3,false));
            }
            e.setVisualFire(false);
            if (i >= 11) {
                entities.add(e);
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        entities.remove(e);
                    }
                }.runTaskLater(NikeyV1.getPlugin(),20*20);
            }
        }
        if (entity instanceof SpectralArrow arrow && entity.getCustomName() != null && entity.getCustomName().equals(ChatColor.DARK_AQUA + "FD") && shooter instanceof Player) {
            Player p = (Player) shooter;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            arrow.remove();
            if (hitEntity != null) {
                LivingEntity e = (LivingEntity) hitEntity;

                applyRandomNegativeEffect(i,e);
                e.setFreezeTicks(20*10);
            }
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
                PotionEffectType.NAUSEA,
                PotionEffectType.HUNGER,
                PotionEffectType.POISON,
                PotionEffectType.SLOWNESS,
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
