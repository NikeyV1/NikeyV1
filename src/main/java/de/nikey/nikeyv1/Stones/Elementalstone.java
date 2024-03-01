package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.TornadoEffect;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Elementalstone implements Listener {

    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    private int radius = 20;

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
                                    }
                                }.runTaskLater(NikeyV1.getPlugin(), 20 * 120);
                                //Ability
                                player.getWorld().setStorm(true);
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
                                    entity.damage(52,p);
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
        entity.addPotionEffect(new PotionEffect(effectType, 200, 3));
    }
}
