package de.nikey.nikeyv1.Stones;

import com.destroystokyo.paper.event.entity.EntityJumpEvent;
import com.destroystokyo.paper.event.player.PlayerJumpEvent;
import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.FountainEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.projectiles.ProjectileSource;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class Frozenstone implements Listener {
    private ArrayList<LivingEntity> entities = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§3Eis Stein")&& event.getItem().getType() == Material.FIREWORK_STAR){
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
                        timer = 20;
                        float accuracy = 0.2F;
                        Snowball snowball = p.launchProjectile(Snowball.class);
                        snowball.setVelocity(p.getLocation().getDirection().multiply(1.6));
                        snowball.setShooter(p);
                        snowball.setCustomName("B");
                        Snowball snowball1 = p.launchProjectile(Snowball.class);
                        snowball1.setCustomName("B");
                        snowball1.setVelocity(p.getLocation().getDirection().multiply(1.6));
                        snowball1.setShooter(p);
                        Snowball snowball2 = p.launchProjectile(Snowball.class);
                        snowball2.setVelocity(p.getLocation().getDirection().multiply(1.6));
                        snowball2.setShooter(p);
                        snowball2.setCustomName("B");
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (!snowball.isDead()){
                                    snowball.getWorld().spawnParticle(Particle.SNOWFLAKE,snowball.getLocation(),3);
                                }else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0L,2L);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (!snowball2.isDead()){
                                    snowball2.getWorld().spawnParticle(Particle.SNOWFLAKE,snowball2.getLocation(),3);
                                }else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0L,2L);
                        new BukkitRunnable(){
                            @Override
                            public void run() {
                                if (!snowball1.isDead()){
                                    snowball1.getWorld().spawnParticle(Particle.SNOWFLAKE,snowball1.getLocation(),3);
                                }else {
                                    cancel();
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(),0L,2L);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        Projectile entity = event.getEntity();
        Entity hitEntity = event.getHitEntity();
        ProjectileSource shooter = entity.getShooter();
        if (entity instanceof Snowball && entity.getCustomName().equalsIgnoreCase("B") && shooter instanceof Player){
            Player p = (Player) shooter;
            p.sendMessage("H");
            LivingEntity e = (LivingEntity) hitEntity;
            assert e != null;
            e.damage(8,p);
            e.setFreezeTicks(700);
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
        if (entities.contains(entity)){
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJump(PlayerJumpEvent event) {
        Player player = event.getPlayer();
        if (entities.contains(player)){
            event.setCancelled(true);
        }
    }
}
