package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.SmokeEffect;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import static de.nikey.nikeyv1.Util.HelpUtil.getNearbyBlocks;

@SuppressWarnings("ALL")
public class Frozenstone implements Listener {
    private ArrayList<LivingEntity> entities = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static ArrayList<LivingEntity> notp = new ArrayList<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        FileConfiguration config = NikeyV1.plugin.getConfig();
        String stone = config.getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("Frozen") && config.getInt(player.getName()+".level") >4){
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
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".stone","Frozen");
            config.set(p.getName()+".level",i);
            NikeyV1.plugin.saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
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
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (i >=15){
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
                        for(Block b : getNearbyBlocks(p.getLocation(), 50)) {
                            if (b.getType().equals(Material.WATER)){
                                b.setType(Material.FROSTED_ICE);
                            }
                        }
                        for (Entity e : p.getNearbyEntities(50,50,50)){
                            if (e instanceof LivingEntity){
                                LivingEntity entity = (LivingEntity) e;
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,400,3,false));
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS,400,0,false));
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
}
