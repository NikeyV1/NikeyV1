package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.slikey.effectlib.effect.EquationEffect;
import de.slikey.effectlib.effect.FountainEffect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

@SuppressWarnings("ALL")
public class Waterstone implements Listener {
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    @EventHandler
    public void onEntityAirChange(EntityAirChangeEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player){
            Player p = (Player) entity;
            FileConfiguration config = NikeyV1.plugin.getConfig();
            if (config.getString(p.getName()+".stone").equalsIgnoreCase("Water") && config.getInt(p.getName()+".level") >=3 && p.isInWater()){
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase("§9Wasser Stein")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.plugin.getConfig();
            config.set(p.getName()+".stone","Water");
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
                        Location location = p.getLocation();
                        FountainEffect effect = new FountainEffect(NikeyV1.em);
                        effect.setLocation(location);
                        effect.duration = 20000;
                        effect.start();
                        //Ability
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                timer--;
                                if (timer == 0){
                                    cancel();
                                }else {
                                    for (Entity e : location.getNearbyEntities(10,8,10)){
                                        if (e instanceof Player && e == p){
                                            p.setHealth(p.getHealth()+2);
                                            if (p.getAbsorptionAmount() <4){
                                                p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400,0,false));
                                            }
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(), 20,20);
                    }else if (i >= 12) {
                        timer = 20;
                        Location location = p.getLocation();
                        FountainEffect effect = new FountainEffect(NikeyV1.em);
                        effect.setLocation(location);
                        effect.duration = 20000;
                        effect.start();
                        //Ability
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                timer--;
                                if (timer == 0){
                                    cancel();
                                    return;
                                }
                                for (Entity e : location.getNearbyEntities(10,8,10)){
                                    if (e == p){
                                        p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 400,0,false));
                                        p.setHealth(p.getHealth()+2);
                                    } else {
                                        if (e instanceof LivingEntity){
                                            LivingEntity entity = (LivingEntity) e;
                                            entity.damage(3);
                                        }
                                    }
                                }
                            }
                        }.runTaskTimer(NikeyV1.getPlugin(), 20,20);
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
                        EquationEffect effect = new EquationEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.particle = Particle.BUBBLE_COLUMN_UP;
                        effect.particles = 5;
                        effect.visibleRange = 100;
                        effect.start();
                        p.getLocation().getWorld().playSound(p.getLocation(), Sound.ENTITY_GENERIC_SPLASH,1,1);
                        Entity e = HelpUtil.getNearestEntityInSight(p, 40);
                        e.getLocation().getWorld().createExplosion(e.getLocation(),2F);
                        if (e instanceof LivingEntity){
                            LivingEntity entity = (LivingEntity) e;
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 400,0));
                            double armor = p.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                            if (armor >16){
                                entity.damage(28,p);
                            }else {
                                entity.damage(18,p);
                            }
                            entity.setVisualFire(false);
                        }
                    }
                }
            }
        }
    }
}
