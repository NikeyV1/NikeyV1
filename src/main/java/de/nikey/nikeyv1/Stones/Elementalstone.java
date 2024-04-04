package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static java.lang.Math.*;

@SuppressWarnings("ALL")
public class Elementalstone implements Listener {

    public static HashMap<UUID, Long> cooldown = new HashMap<>();

    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();

    private int radius = 20;
    private static final Map<UUID, Player> telekinesisTargets = new HashMap<>();

    public static long remainingTime1;
    public static long remainingTime3;

    public static ArrayList<LivingEntity> livingEntitiesList= new ArrayList<>();
    double dmg = 0;
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
                                player.getWorld().setStorm(true);
                                player.getWorld().setWeatherDuration(20*60);
                                startLightningTask(player.getWorld(),player);
                            }
                        }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                            if (!player.isSneaking()) {
                                livingEntitiesList.clear();
                                dmg = 0;
                                new BukkitRunnable(){
                                    double t = PI/4;
                                    final Location loc = player.getLocation();
                                    public void run(){
                                        t = t + 0.1* PI;
                                        for (double theta = 0; theta <= 2* PI; theta = theta + PI/32){
                                            double x = t*cos(theta);
                                            double y = 1.5* exp(-0.1*t) * sin(t) + 1.5;
                                            double z = t*sin(theta);
                                            loc.add(x,y,z);
                                            Particle.DustOptions dust = new Particle.DustOptions(Color.BLACK, 1);
                                            player.spawnParticle(Particle.REDSTONE, loc, 0, 0, 0, 0,dust);
                                            for (Entity e : loc.getNearbyEntities(2,2,2)) {
                                                if (e instanceof LivingEntity && e != player) {
                                                    LivingEntity living = (LivingEntity) e;
                                                    double damageMultiplier = getArmorStrengthMultiplier(living);
                                                    if (living instanceof Player){
                                                        living.damage(damageMultiplier,player);
                                                        dmg += damageMultiplier*0.5;
                                                    }else {
                                                        double health = living.getHealth();
                                                        health = health * 0.625;
                                                        living.setHealth(health);
                                                        dmg += health*0.5;
                                                    }
                                                    reduceArmorDurability(living);
                                                    livingEntitiesList.add(living);
                                                    break;
                                                }
                                                break;
                                            }
                                            loc.subtract(x,y,z);

                                            theta = theta + PI/64;

                                            x = t*cos(theta);
                                            y = 1.5* exp(-0.1*t) * sin(t) + 1.5;
                                            z = t*sin(theta);
                                            loc.add(x,y,z);
                                            Particle.DustTransition transition= new Particle.DustTransition(Color.AQUA, Color.BLACK,1);
                                            loc.getWorld().spawnParticle(Particle.REDSTONE,loc,0,0,0,0,transition);

                                            for (Entity e : loc.getNearbyEntities(2,2,2)) {
                                                if (e instanceof LivingEntity && e != player) {
                                                    LivingEntity living = (LivingEntity) e;
                                                    living.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*20,1));
                                                    living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*20,4));
                                                }
                                                break;
                                            }
                                            loc.subtract(x,y,z);

                                            theta = theta + PI/64;

                                            x = t*cos(theta);
                                            y = 1.5* exp(-0.1*t) * sin(t) + 1.5;
                                            z = t*sin(theta);
                                            loc.add(x,y,z);
                                            Particle.DustOptions orp= new Particle.DustOptions(Color.BLACK,1);
                                            loc.getWorld().spawnParticle(Particle.REDSTONE,loc,0,0,0,0,orp);
                                            loc.subtract(x,y,z);
                                        }
                                        if (t > 20){
                                            this.cancel();
                                        }
                                    }

                                }.runTaskTimer(NikeyV1.getPlugin(), 0, 1);
                                new BukkitRunnable() {
                                    @Override
                                    public void run() {
                                        double health = player.getHealth();
                                        double maxHealth = player.getMaxHealth();
                                        double missinghealth = maxHealth-health;
                                        if (dmg > missinghealth) {
                                            dmg -= missinghealth;
                                            player.setHealth(player.getMaxHealth());
                                            player.sendMessage(String.valueOf(dmg));
                                            double baseValue = player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).getBaseValue();
                                            player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).setBaseValue(dmg);
                                            player.setAbsorptionAmount(dmg);
                                            new BukkitRunnable() {
                                                @Override
                                                public void run() {
                                                    player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).setBaseValue(baseValue);
                                                }
                                            }.runTaskLater(NikeyV1.getPlugin(),20*240);
                                        }else {
                                            player.setHealth(health + dmg);
                                        }
                                    }
                                }.runTaskLater(NikeyV1.getPlugin(),20*3);
                            }
                        }
                    }
                }
            }
        }
    }

    private double getArmorStrengthMultiplier(final LivingEntity living) {
        double armorValue = 0.0;
        for (ItemStack item : living.getEquipment().getArmorContents()) {
            if (item != null) {
                armorValue += calculateArmorValue(item);
            }
        }
        return Math.max(10.0, armorValue);
    }

    private double calculateArmorValue(ItemStack armorPiece) {
        switch (armorPiece.getType()) {
            case NETHERITE_HELMET:
            case NETHERITE_CHESTPLATE:
            case NETHERITE_LEGGINGS:
            case NETHERITE_BOOTS: {
                return 7.0;
            }
            case DIAMOND_HELMET:
            case DIAMOND_CHESTPLATE:
            case DIAMOND_LEGGINGS:
            case DIAMOND_BOOTS: {
                return 6.0;
            }
            case IRON_HELMET:
            case IRON_CHESTPLATE:
            case IRON_LEGGINGS:
            case IRON_BOOTS: {
                return 5.0;
            }
            case GOLDEN_HELMET:
            case GOLDEN_CHESTPLATE:
            case GOLDEN_LEGGINGS:
            case GOLDEN_BOOTS: {
                return 4.5;
            }
            case CHAINMAIL_HELMET:
            case CHAINMAIL_CHESTPLATE:
            case CHAINMAIL_LEGGINGS:
            case CHAINMAIL_BOOTS: {
                return 5.0;
            }
            case LEATHER_HELMET:
            case LEATHER_CHESTPLATE:
            case LEATHER_LEGGINGS:
            case LEATHER_BOOTS: {
                return 3.5;
            }
            default: {
                return 0.0;
            }
        }
    }

    private void reduceArmorDurability(LivingEntity entity) {
        for (final ItemStack armorPiece : entity.getEquipment().getArmorContents()) {
            if (armorPiece != null && armorPiece.getDurability() < armorPiece.getType().getMaxDurability()) {
                if (!livingEntitiesList.contains(entity)) {
                    Damageable armordamage = (Damageable) armorPiece;
                    int damage = armordamage.getDamage();
                    final double maxDurability = armorPiece.getType().getMaxDurability();
                    final double currentDurability = maxDurability - armorPiece.getDurability();
                    final double remove = currentDurability - armorPiece.getDurability() + (int)(0.4 * maxDurability);
                    final double durabilityPercentage = currentDurability / maxDurability;
                    entity.sendMessage(String.valueOf(damage));
                    entity.sendMessage("after remove: "+String.valueOf(remove));
                    entity.sendMessage("current: "+String.valueOf(currentDurability));
                    if (currentDurability > 20) {
                        if (remove < 20.0) {
                            armorPiece.setDurability((short)(maxDurability - 20.0));
                        }
                        else {
                            armorPiece.setDurability((short)(armorPiece.getDurability() + (int)(0.4 * maxDurability)));
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
