package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.TornadoEffect;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Elementalstone implements Listener {

    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    private int radius = 20;
    private static final Map<UUID, Player> telekinesisTargets = new HashMap<>();

    public static long remainingTime1;
    public static long remainingTime3;
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        ItemStack item = event.getItem();

        if (item != null&& item.getType() == Material.FIREWORK_STAR && item.getItemMeta().hasLore()) {
            ItemMeta meta = item.getItemMeta();
            if (meta != null) {
                // Getting the lore of the item
                List<String> lore = meta.getLore();
                // Checking if lore contains the keyword "Combined"
                if (lore != null ) {
                    String l = String.valueOf(lore);
                    if (l.equalsIgnoreCase("[§fThe combined power of all §8stones]")) {
                        if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                            if (cooldown.containsKey(player.getUniqueId()) && cooldown.get(player.getUniqueId()) > System.currentTimeMillis()){
                                player.updateInventory();
                                remainingTime1 = cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                            }else {
                                cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (120 * 1000));
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        cooldown.remove(player.getUniqueId());
                                        cancel();
                                        return;
                                    }
                                }.runTaskLater(NikeyV1.getPlugin(), 20 * 120);
                                //Ability
                                player.getWorld().setThundering(true);
                                player.getWorld().setWeatherDuration(20*60);
                                startLightningTask(player.getWorld(),player);
                            }
                        }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                            if (!player.isSneaking()) {
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player victim = (Player) event.getEntity();
            //useForce(attacker, victim);
            //telekinesisTargets.put(victim.getUniqueId(), attacker);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player) {
            Player victim = (Player) event.getEntity();
            if (telekinesisTargets.containsKey(victim.getUniqueId())) {
                Player attacker = telekinesisTargets.get(victim.getUniqueId());
                if (attacker != null && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.CUSTOM) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public static void startDamageScheduler() {
        new BukkitRunnable() {
            @Override
            public void run() {
                for (Map.Entry<UUID, Player> entry : telekinesisTargets.entrySet()) {
                    Player victim = Bukkit.getPlayer(entry.getKey());
                    if (victim != null && victim.isOnline()) {
                        victim.damage(1.0); // Adjust the damage value as needed
                    } else {
                        telekinesisTargets.remove(entry.getKey());
                    }
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(),10,10);
    }

    private void useForce(Player attacker, Player victim) {
        Location attackerLocation = attacker.getLocation();
        Location victimLocation = victim.getLocation();

        Vector direction = attackerLocation.toVector().subtract(victimLocation.toVector()).normalize();

        double forceMultiplier = 2.0; // Adjust this value to change the force of the telekinesis

        Vector force = direction.multiply(forceMultiplier);

        //
        // victim.setVelocity(force);

        // Play sound and visual effects
        attacker.getWorld().playSound(attacker.getLocation(), "entity.enderman.teleport", 1.0f, 1.0f);
        attacker.getWorld().spawnParticle(org.bukkit.Particle.PORTAL, attacker.getLocation(), 50, 1, 1, 1);

        // Optionally, you can also lift the victim slightly off the ground
        double liftHeight = 1.5;
        victimLocation.setY(victimLocation.getY() + liftHeight);
        victim.teleport(victimLocation);
    }

    private int pg =200;


    private void startLightningTask(World world, Player p) {
        radius = 20;
        new BukkitRunnable() {
            @Override
            public void run() {
                    for (int i = 0; i < pg; i++) {
                        int x = (int) (p.getLocation().getX());
                        int z = (int) (p.getLocation().getZ());
                        int randomX = ThreadLocalRandom.current().nextInt(x-radius, x+radius);
                        int randomZ = ThreadLocalRandom.current().nextInt(z-radius, z+radius);
                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                        world.strikeLightning(location);
                        for (Entity e : location.getWorld().getNearbyEntities(location,6,6,6)){
                            if (e instanceof LivingEntity){
                                LivingEntity entity = (LivingEntity) e;
                                if (entity != p){
                                    applyRandomNegativeEffect(entity);
                                    entity.damage(45,p);
                                    entity.setFreezeTicks(200);
                                }
                            }
                        }
                    }
                pg += 110;
                radius += 25; // Erhöhe den Radius um 20 Blöcke
                if (radius >= 100)cancel();

            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 20);
    }

    private void applyRandomNegativeEffect(LivingEntity entity) {
        // List of negative potion effects
        PotionEffectType[] negativeEffects = {
                PotionEffectType.BLINDNESS,
                PotionEffectType.CONFUSION,
                PotionEffectType.HUNGER,
                PotionEffectType.WITHER,
                PotionEffectType.POISON,
                PotionEffectType.SLOW,
                PotionEffectType.WEAKNESS
        };

        Random random = new Random();
        PotionEffectType effectType = negativeEffects[random.nextInt(negativeEffects.length)];
        // Apply the effect to the player
        entity.addPotionEffect(new PotionEffect(effectType, 260, 3));
    }
}
