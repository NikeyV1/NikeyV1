package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.effect.TornadoEffect;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.damage.DamageSource;
import org.bukkit.damage.DamageType;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Electrostone implements Listener {
    public static ArrayList<Entity> stunned = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    private int timer;

    public static HashMap<Player, Integer> mtimer = new HashMap<>();
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;

    public void createLightningEffect(Player p, int damage, int slowDuration, int slowAmplifier) {
        int x = (int) p.getLocation().getX();
        int z = (int) p.getLocation().getZ();
        int randomX = ThreadLocalRandom.current().nextInt(x - 10, x + 10);
        int randomZ = ThreadLocalRandom.current().nextInt(z - 10, z + 10);
        World world = p.getWorld();
        int randomY = world.getHighestBlockYAt(randomX, randomZ);
        Location location = new Location(world, randomX, randomY + 1, randomZ);

        LightningStrike lightningStrike = world.strikeLightningEffect(location);
        lightningStrike.setCausingPlayer(p);

        for (Entity e : location.getWorld().getNearbyEntities(location, 4, 4, 4)) {
            if (e instanceof LivingEntity) {
                LivingEntity entity = (LivingEntity) e;
                if (entity != p) {
                    if (HelpUtil.shouldDamageEntity(entity, p)) {
                        entity.setFireTicks(20 * 8);
                        DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();
                        entity.damage(damage, source);
                        entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOWNESS, 20 * slowDuration, slowAmplifier));
                    }
                }
            }
        }

        TornadoEffect effect = new TornadoEffect(NikeyV1.em);
        effect.setLocation(location);
        effect.maxTornadoRadius = 3F;
        effect.visibleRange = 100;
        effect.circleParticles = 32;
        effect.cloudParticles = 20;
        effect.tornadoParticle = Particle.ELECTRIC_SPARK;
        effect.duration = 1500;
        effect.start();
    }


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§eElectric Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Electric");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i >= 10){
                    if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));

                        int timer;
                        Runnable lightningTask;

                        switch (i) {
                            case 10:
                                timer = 20;
                                lightningTask = () -> createLightningEffect(p, 10, 0, 0);
                                break;
                            case 11:
                                timer = 20;
                                lightningTask = () -> createLightningEffect(p, 10, 4, 2);
                                break;
                            case 12:
                                timer = 25;
                                lightningTask = () -> createLightningEffect(p, 10, 4, 2);
                                break;
                            case 13:
                                timer = 25;
                                lightningTask = () -> createLightningEffect(p, 14, 4, 2);
                                break;
                            case 14:
                            default:
                                timer = 25;
                                lightningTask = () -> createLightningEffect(p, 14, 6, 2);
                                break;
                        }

                        new BukkitRunnable() {
                            int countdown = timer;

                            @Override
                            public void run() {
                                if (countdown == 0) {
                                    cancel();
                                } else {
                                    lightningTask.run();
                                    countdown--;
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(), 20, 20);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            int level = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            String stone = NikeyV1.getPlugin().getConfig().getString(p.getName() + ".stone");
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase("§eElectric Stone")){
                Entity entity = event.getEntity();
                String[] arr = p.getInventory().getItemInMainHand().getLore().get(1).split(":");
                int i = Integer.parseInt(arr[1]);
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){

                    if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION ||event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK ||event.getCause() == EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK ) {
                        if (i >= 15) {
                            int duration;
                            int radius;

                            if (i == 15 || i == 16) {
                                duration = 5000;
                                radius = 1;
                            } else if (i == 17 || i == 18) {
                                duration = 5000;
                                radius = 2;
                            } else if (i >= 19) {
                                duration = 8000;
                                radius = 2;
                            } else {
                                return;
                            }

                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            stunned.add(entity);

                            CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                            effect.setEntity(entity);
                            effect.duration = duration;
                            effect.particles = 30;
                            effect.particle = Particle.ELECTRIC_SPARK;
                            effect.visibleRange = 100;
                            effect.start();

                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    stunned.clear();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * (duration / 1000));

                            for (Entity e : entity.getNearbyEntities(radius, radius, radius)) {
                                if (e != p) {
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    if (e instanceof Player player) {
                                        if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                            stunned.add(player);
                                        }
                                    } else if (e instanceof LivingEntity livingEntity) {
                                        stunned.add(livingEntity);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
        String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("Electric")) {
            if (level == 3){
                player.setWalkSpeed(0.21F);
            } else if (level == 4) {
                player.setWalkSpeed(0.22F);
            }else if (level == 5) {
                player.setWalkSpeed(0.23F);
            }else if (level >= 6){
                player.setWalkSpeed(0.24F);
            }else {
                player.setWalkSpeed(0.2F);
            }
        }
        if (stone.equalsIgnoreCase("electric")) {
            if (level >= 7) {
                if (level == 7) {
                    player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4.1);
                } else if (level == 8) {
                    player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4.2);
                }else if (level >= 9) {
                    player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4.3);
                }
            }else {
                player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4);
            }
        }else {
            player.getAttribute(Attribute.ATTACK_SPEED).setBaseValue(4);
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§eElectric Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Electric");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (i == 20 || i == 21) {
                applyShieldEffect(p, i);
            }
        }
    }


    @EventHandler
    public void onStunnedDamage(EntityDamageByEntityEvent event) {
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        Entity damager = event.getDamager();
        Entity entity = event.getEntity();
        if (Electrostone.stunned.contains(entity)){
            Integer i = config.getInt(damager.getName() + ".level");
            if (i == 15) {
                float damg = (float) ((float) event.getDamage()*1.3);
                event.setDamage(damg);
            } else if (i == 16 || i == 17) {
                float damg = (float) ((float) event.getDamage()*1.4);
                event.setDamage(damg);
            }else if (i >= 18) {
                float damg = (float) ((float) event.getDamage()*1.5);
                event.setDamage(damg);
            }
        }
    }



    private void applyShieldEffect(Player p, int i) {
        long cooldown = 300 * 1000;
        int timerDuration = (i == 20) ? 50 : 125;
        int radius = (i == 20) ? 6 : 10;
        int duration = (i == 20) ? 20000 : 25000;
        int damage = (i == 20) ? 1 : 2;
        double velocityMultiplier = (i == 20) ? 0.4 : 0.6;
        int interval = (i == 20) ? 8 : 4;
        int entityRadius = (i == 20) ? 5 : 8;

        mtimer.put(p, timerDuration);
        if (!(cooldown2.getOrDefault(p.getUniqueId(), 0L) > System.currentTimeMillis())) {
            cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + cooldown);

            // Create and start the shield effect
            ShieldEffect effect = new ShieldEffect(NikeyV1.em);
            effect.radius = radius;
            effect.sphere = true;
            effect.setEntity(p);
            effect.duration = duration;
            effect.particle = Particle.SCRAPE;
            effect.particles = 80;
            effect.visibleRange = 100;
            effect.start();

            new BukkitRunnable() {
                @Override
                public void run() {
                    mtimer.replace(p, mtimer.get(p) - 1);
                    for (Entity entity : p.getNearbyEntities(entityRadius, entityRadius, entityRadius)) {
                        Location difference = entity.getLocation().subtract(p.getLocation());
                        Vector normalizedDifference = difference.toVector().normalize();
                        Vector multiplied = normalizedDifference.multiply(velocityMultiplier);

                        if (entity instanceof LivingEntity target) {
                            if (target != p && HelpUtil.shouldDamageEntity(target, p)) {
                                target.setVelocity(multiplied);
                                target.damage(damage);
                            }
                        }else {
                            entity.setVelocity(multiplied);
                        }
                    }
                    if (mtimer.get(p) == 0) {
                        cancel();
                    }
                }
            }.runTaskTimer(NikeyV1.getPlugin(), 0, interval);
        }
    }



    
    @EventHandler(ignoreCancelled = true)
    public void onEntityMove(EntityMoveEvent event) {
        LivingEntity entity = event.getEntity();
        if (stunned.contains(entity)){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        Player entity = event.getPlayer();
        if (stunned.contains(entity)){
            event.setCancelled(true);
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityTeleport(EntityTeleportEvent event) {
        if (stunned.contains(event.getEntity())){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        if (stunned.contains(event.getPlayer())){
            event.setCancelled(true);
        }
    }
}
