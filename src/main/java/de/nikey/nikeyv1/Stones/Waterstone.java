package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.Util.Tornado;
import de.nikey.nikeyv1.api.EntityTypeDamage;
import de.slikey.effectlib.effect.EquationEffect;
import de.slikey.effectlib.effect.FountainEffect;
import io.papermc.paper.tag.EntityTags;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Waterstone implements Listener {
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;

    @EventHandler
    public void onEntityAirChange(EntityAirChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player player = (Player) entity;
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
            if (player.isInWater() && stone.equalsIgnoreCase("water")) {
                if (level == 4) {
                    Random random = new Random();
                    int i = random.nextInt(2);
                    if (i == 0) {
                        event.setCancelled(true);
                    }
                } else if (level >= 5) {
                    event.setCancelled(true);
                }
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
        String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("water") && level >= 6) {
            player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE,PotionEffect.INFINITE_DURATION,0,true,false,false));
        }else {
            PotionEffect dolphinGrace = player.getPotionEffect(PotionEffectType.DOLPHINS_GRACE);
            if (dolphinGrace != null) {
                if (dolphinGrace.getDuration() == PotionEffect.INFINITE_DURATION) {
                    player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
                }
            }
        }    
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (player.hasPotionEffect(PotionEffectType.DOLPHINS_GRACE)) {
            player.removePotionEffect(PotionEffectType.DOLPHINS_GRACE);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
            if (stone.equalsIgnoreCase("water") && level >= 3 && event.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                event.setDamage(event.getDamage() * 0.5);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Water Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Water");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction().isRightClick()) {
                if (i >= 10) {
                    if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                        //cooldown-ability
                        if (i == 10) {
                            startFountainEffect(p, 7, 8, 7, 15, 3, 2, false);
                        } else if (i == 11) {
                            startFountainEffect(p, 9, 8, 9, 15, 3, 2, false);
                        } else if (i == 12) {
                            startFountainEffect(p, 9, 8, 9, 15, 3, 4, false);
                        } else if (i == 13) {
                            startFountainEffect(p, 9, 8, 9, 15, 4, 4, false);
                        } else if (i >= 14) {
                            startFountainEffect(p, 9, 8, 9, 15, 4, 4, true);
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR){
                if (!p.isSneaking()) {
                    if (i == 15){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 30);
                            if (e != null) {
                                e.getLocation().getWorld().createExplosion(e.getLocation(),1F);
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400,0));
                                    double maxHealth = entity.getMaxHealth();
                                    double damage = maxHealth * 0.35;
                                    if (entity.getHealth()-damage >= 1) {
                                        entity.setHealth(entity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(entity.getType())){
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }
                                    entity.setVisualFire(false);
                                }
                            }
                        }
                    } else if (i == 16) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 40);
                            if (e != null) {
                                e.getLocation().getWorld().createExplosion(e.getLocation(),1F);
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400,0));
                                    double maxHealth = entity.getMaxHealth();
                                    double damage = maxHealth * 0.35;
                                    if (entity.getHealth()-damage >= 1) {
                                        entity.setHealth(entity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(entity.getType())){
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }
                                    entity.setVisualFire(false);
                                }
                            }
                        }
                    }else if (i == 17) {
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 40);
                            if (e != null) {
                                e.getLocation().getWorld().createExplosion(e.getLocation(),1F);
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400,0));
                                    double maxHealth = entity.getMaxHealth();
                                    double damage = maxHealth * 0.45;
                                    if (entity.getHealth()-damage >= 1) {
                                        entity.setHealth(entity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(entity.getType())){
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }
                                    entity.setVisualFire(false);
                                }
                            }
                        }
                    } else if (i == 18){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 40);
                            if (e != null) {
                                e.getLocation().getWorld().createExplosion(e.getLocation(),2F);
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 450,0));
                                    double maxHealth = entity.getMaxHealth();
                                    double damage = maxHealth * 0.45;
                                    if (entity.getHealth()-damage >= 1) {
                                        entity.setHealth(entity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(entity.getType())){
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }
                                    entity.setVisualFire(false);
                                }
                            }
                        }
                    }else if (i >=19){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(), System.currentTimeMillis() + (180 * 1000));
                            //Cooldown-Ability
                            EquationEffect effect = new EquationEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.BUBBLE_COLUMN_UP;
                            effect.particles = 5;
                            effect.visibleRange = 100;
                            effect.start();
                            p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                            Entity e = HelpUtil.getNearestEntityInSight(p, 50);
                            if (e != null) {
                                e.getLocation().getWorld().createExplosion(e.getLocation(),2F);
                                if (e instanceof LivingEntity){
                                    LivingEntity entity = (LivingEntity) e;
                                    entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 450,0));
                                    double maxHealth = entity.getMaxHealth();
                                    double damage = maxHealth * 0.45;
                                    if (entity.getHealth()-damage >= 1) {
                                        entity.setHealth(entity.getHealth()-damage);
                                    }else if (!EntityTags.UNDEADS.isTagged(entity.getType())){
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_DAMAGE,1,240));
                                    }else {
                                        entity.addPotionEffect(new PotionEffect(PotionEffectType.INSTANT_HEALTH,1,240));
                                    }
                                    entity.setVisualFire(false);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private void startFountainEffect(Player p, int radiusX, int radiusY, int radiusZ, int timerDuration, double healAmount, double damageAmount, boolean applyAbsorption) {
        Location location = p.getLocation();
        FountainEffect effect = new FountainEffect(NikeyV1.em);
        effect.setLocation(location);
        effect.duration = 20000;
        effect.particlesStrand = 30;
        effect.particlesSpout = 50;
        effect.start();

        new BukkitRunnable() {
            int timer = timerDuration;

            @Override
            public void run() {
                timer--;
                if (timer == 0) {
                    cancel();
                    return;
                }
                handleEntitiesAround(p, location, radiusX, radiusY, radiusZ, healAmount, damageAmount, applyAbsorption);
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 30, 30);
    }

    private void handleEntitiesAround(Player p, Location location, int radiusX, int radiusY, int radiusZ, double healAmount, double damageAmount, boolean applyAbsorption) {
        for (Entity e : location.getNearbyEntities(radiusX, radiusY, radiusZ)) {
            healOrDamageEntity(p, e, healAmount, damageAmount, applyAbsorption);
        }
    }

    private void healOrDamageEntity(Player p, Entity e, double healAmount, double damageAmount, boolean applyAbsorption) {
        if (e instanceof Player) {
            Player player = (Player) e;
            if (HelpUtil.getPlayersInSameTeam(p).contains(player) || e == p) {
                if (applyAbsorption) {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400, 0, false));
                }
                if (!player.isValid()) {
                    player.setHealth(Math.min(player.getHealth() + healAmount, 20));
                }
            }
        } else if (e instanceof LivingEntity) {
            LivingEntity entity = (LivingEntity) e;
            if (HelpUtil.shouldDamageEntity(entity, p)) {
                entity.damage(damageAmount);
            }
        }
    }



    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase("§9Water Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Water");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (p.isSneaking()) {
                if (i== 20){
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f);
                        Location BlocksAway = p.getLocation().add(p.getLocation().getDirection().multiply(4));
                        Tornado.spawnTornado(NikeyV1.getPlugin(),BlocksAway,Material.STONE,p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getData(),p.getLocation().getDirection().multiply(4),0.4,275,20*20,false,p,false);
                    }
                } else if (i == 21) {
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));

                        p.playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH, 1.0f, 1.0f);
                        Location BlocksAway = p.getLocation().add(p.getLocation().getDirection().multiply(4));
                        Tornado.spawnTornado(NikeyV1.getPlugin(),BlocksAway,Material.STONE,p.getLocation().getWorld().getHighestBlockAt(p.getLocation()).getData(),p.getLocation().getDirection().multiply(4.5),0.4,300,20*30,false,p,false);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity damager = event.getDamager();
        if (damager instanceof  Player) {
            Player player = (Player) damager;
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
            if (stone.equalsIgnoreCase("Water") && player.isInWater()) {
                double damage = event.getDamage();
                if (level == 7) {
                    event.setDamage(damage *1.08);
                }else if (level == 8) {
                    event.setDamage(damage *1.16);
                }else if (level >= 9) {
                    event.setDamage(damage *1.24);
                }
            }
        }
    }
}
