package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.TornadoEffect;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
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

public class Elementalstone implements Listener {

    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    int timer;
    int time;

    int dddick;

    int flick;

    public static long remainingTime1;
    public static long remainingTime2;
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
                        if (cooldown.containsKey(player.getUniqueId()) && cooldown.get(player.getUniqueId()) > System.currentTimeMillis()){
                            player.updateInventory();
                            remainingTime1 = cooldown.get(player.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            cooldown.put(player.getUniqueId(), System.currentTimeMillis() + (100 * 1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    cooldown.remove(player.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 100);
                            //Ability
                            player.getWorld().setThundering(true);
                            player.getWorld().setWeatherDuration(20*180);
                            for(int i = 0; i < 600; i++) {
                                lightning(player);
                            }
                        }
                    }
                }
            }
        }
    }

    public void lightning(Player p) {
        int x = (int) (p.getLocation().getX());
        int z = (int) (p.getLocation().getZ());
        int randomX = ThreadLocalRandom.current().nextInt(x-100, x+100);
        int randomZ = ThreadLocalRandom.current().nextInt(z-100, z+100);
        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
        LightningStrike lightningStrike = p.getWorld().strikeLightning(location);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    applyRandomNegativeEffect(entity);
                    entity.damage(30,p);
                }
            }
        }
        //TornadoEffect effect = new TornadoEffect(NikeyV1.em);
        //        effect.setLocation(location);
        //        effect.maxTornadoRadius = 3F;
        //        effect.visibleRange = 70;
        //        effect.circleParticles =32;
        //        effect.cloudParticles =20;
        //        effect.tornadoParticle = Particle.ELECTRIC_SPARK;
        //        effect.duration = 1200;
        //        effect.start();
    }
    private void applyRandomNegativeEffect(LivingEntity entity) {
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
        PotionEffectType effectType = negativeEffects[random.nextInt(negativeEffects.length)];
        // Apply the effect to the player
        entity.addPotionEffect(new PotionEffect(effectType, 200, 2));
    }
}
