package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.api.EntityTypeDamage;
import de.slikey.effectlib.effect.FlameEffect;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
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

    double dmg = 0;

    Map<Entity, Integer> executionCountMap = new HashMap<>();

    private double damageCount;

    public static ArrayList<Entity> stunned = new ArrayList<>();

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
                                if (cooldown2.containsKey(player.getUniqueId()) && cooldown2.get(player.getUniqueId()) > System.currentTimeMillis()){
                                    event.setCancelled(true);
                                    player.updateInventory();
                                    remainingTime3 = cooldown2.get(player.getUniqueId()) - System.currentTimeMillis();
                                }else {
                                    cooldown2.put(player.getUniqueId(),System.currentTimeMillis() + (150*1000));
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            cooldown2.remove(player.getUniqueId());
                                            cancel();
                                            return;
                                        }
                                    }.runTaskLater(NikeyV1.getPlugin(),20*150);

                                    dmg = 0;
                                    damageCount = 1;
                                    new BukkitRunnable(){
                                        String damageEntityType = EntityTypeDamage.getDamageEntityType(player);
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
                                                        if (damageEntityType.equalsIgnoreCase("all")) {
                                                            LivingEntity living = (LivingEntity) e;
                                                            int executionCount = executionCountMap.getOrDefault(living, 0);
                                                            if (executionCount < 5) {

                                                                if (!stunned.contains(living)) {
                                                                    stunned.add(living);
                                                                }


                                                                if (executionCount == 0) {
                                                                    damageArmor(living);
                                                                    FlameEffect effect = new FlameEffect(NikeyV1.em);
                                                                    effect.duration = 4500;
                                                                    effect.particle = Particle.WAX_ON;
                                                                    effect.visibleRange = 60;
                                                                    effect.setLocation(living.getLocation());
                                                                    effect.start();
                                                                }

                                                                transferPositiveEffects(living,player);


                                                                if (living instanceof Player){
                                                                    double damage = getArmorStrengthMultiplier(living);
                                                                    living.damage(damage*1.5,player);
                                                                    dmg += damage*0.5;
                                                                }else if (executionCount == 0){
                                                                    double health = living.getHealth();
                                                                    health = health * 0.65;
                                                                    living.setHealth(living.getHealth() * 0.65);
                                                                    dmg += health*0.5;
                                                                }
                                                                executionCountMap.put(living, executionCount + 1);
                                                                damageCount++;
                                                            }
                                                        } else if (damageEntityType.equalsIgnoreCase("players")) {
                                                            if (e instanceof Player) {
                                                                Player living = (Player) e;
                                                                int executionCount = executionCountMap.getOrDefault(living, 0);
                                                                if (executionCount < 5) {

                                                                    if (!stunned.contains(living)) {
                                                                        stunned.add(living);
                                                                    }


                                                                    if (executionCount == 0) {
                                                                        damageArmor(living);
                                                                        FlameEffect effect = new FlameEffect(NikeyV1.em);
                                                                        effect.duration = 4500;
                                                                        effect.particle = Particle.WAX_ON;
                                                                        effect.visibleRange = 60;
                                                                        effect.setLocation(living.getLocation());
                                                                        effect.start();
                                                                    }

                                                                    transferPositiveEffects(living,player);


                                                                    if (living instanceof Player){
                                                                        double damage = getArmorStrengthMultiplier(living);
                                                                        living.damage(damage*1.5,player);
                                                                        dmg += damage*0.5;
                                                                    }
                                                                    executionCountMap.put(living, executionCount + 1);
                                                                    damageCount++;
                                                                }
                                                            }
                                                        }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                                            if (e instanceof Monster) {
                                                                Monster living = (Monster) e;
                                                                int executionCount = executionCountMap.getOrDefault(living, 0);
                                                                if (executionCount < 5) {

                                                                    if (!stunned.contains(living)) {
                                                                        stunned.add(living);
                                                                    }


                                                                    if (executionCount == 0) {
                                                                        damageArmor(living);
                                                                        FlameEffect effect = new FlameEffect(NikeyV1.em);
                                                                        effect.duration = 4500;
                                                                        effect.particle = Particle.WAX_ON;
                                                                        effect.visibleRange = 60;
                                                                        effect.setLocation(living.getLocation());
                                                                        effect.start();
                                                                    }

                                                                    transferPositiveEffects(living,player);


                                                                    if (executionCount == 0){
                                                                        double health = living.getHealth();
                                                                        health = health * 0.65;
                                                                        living.setHealth(living.getHealth() * 0.65);
                                                                        dmg += health*0.5;
                                                                    }
                                                                    executionCountMap.put(living, executionCount + 1);
                                                                    damageCount++;
                                                                }
                                                            }
                                                        }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                                            if (e instanceof Monster || e instanceof Player) {
                                                                LivingEntity living = (LivingEntity) e;
                                                                int executionCount = executionCountMap.getOrDefault(living, 0);
                                                                if (executionCount < 5) {

                                                                    if (!stunned.contains(living)) {
                                                                        stunned.add(living);
                                                                    }


                                                                    if (executionCount == 0) {
                                                                        damageArmor(living);
                                                                        FlameEffect effect = new FlameEffect(NikeyV1.em);
                                                                        effect.duration = 4500;
                                                                        effect.particle = Particle.WAX_ON;
                                                                        effect.visibleRange = 60;
                                                                        effect.setLocation(living.getLocation());
                                                                        effect.start();
                                                                    }

                                                                    transferPositiveEffects(living,player);


                                                                    if (living instanceof Player){
                                                                        double damage = getArmorStrengthMultiplier(living);
                                                                        living.damage(damage*1.5,player);
                                                                        dmg += damage*0.5;
                                                                    }else if (executionCount == 0){
                                                                        double health = living.getHealth();
                                                                        health = health * 0.65;
                                                                        living.setHealth(living.getHealth() * 0.65);
                                                                        dmg += health*0.5;
                                                                    }
                                                                    executionCountMap.put(living, executionCount + 1);
                                                                    damageCount++;
                                                                }
                                                            }
                                                        }
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
                                                        if (damageEntityType.equalsIgnoreCase("all")) {
                                                            LivingEntity living = (LivingEntity) e;
                                                            living.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*20,1));
                                                            living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*20,4));
                                                            living.playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.BLOCK,1,1));
                                                        }else if (damageEntityType.equalsIgnoreCase("players")) {
                                                            if (e instanceof Player) {
                                                                Player living = (Player) e;
                                                                living.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*20,1));
                                                                living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*20,4));
                                                                living.playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.BLOCK,1,1));
                                                            }
                                                        }else if (damageEntityType.equalsIgnoreCase("monster")) {
                                                            if (e instanceof Monster) {
                                                                Monster living = (Monster) e;
                                                                living.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*20,1));
                                                                living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*20,4));
                                                                living.playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.BLOCK,1,1));
                                                            }
                                                        }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                                            if (e instanceof Monster || e instanceof Player) {
                                                                LivingEntity living = (LivingEntity) e;
                                                                living.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS,20*20,1));
                                                                living.addPotionEffect(new PotionEffect(PotionEffectType.WITHER,20*20,4));
                                                                living.playSound(Sound.sound(Key.key("block.beacon.deactivate"), Sound.Source.BLOCK,1,1));
                                                            }
                                                        }
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
                                                dmg = dmg *0.5;
                                                if (!(player.getAbsorptionAmount() > dmg)) {
                                                    double baseValue = player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).getBaseValue();
                                                    player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).setBaseValue(dmg);
                                                    player.setAbsorptionAmount(dmg);
                                                    new BukkitRunnable() {
                                                        @Override
                                                        public void run() {
                                                            player.getAttribute(Attribute.GENERIC_MAX_ABSORPTION).setBaseValue(baseValue);
                                                        }
                                                    }.runTaskLater(NikeyV1.getPlugin(),20*120);
                                                }
                                            }else {
                                                player.setHealth(health + dmg);
                                            }
                                        }
                                    }.runTaskLater(NikeyV1.getPlugin(),20*4);
                                    executionCountMap.clear();
                                    new BukkitRunnable() {
                                        @Override
                                        public void run() {
                                            stunned.clear();
                                        }
                                    }.runTaskLater(NikeyV1.getPlugin(),20*5);
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    public void transferPositiveEffects(Entity entity, Player player) {
        if (entity instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity) entity;
            // Überprüfe, ob das Entity positive Potion-Effekte hat
            for (PotionEffect effect : livingEntity.getActivePotionEffects()) {
                // Überprüfe, ob der Effekt als "gut" betrachtet werden kann
                if (isGoodEffect(effect)) {
                    // Entferne den positiven Effekt vom Entity
                    livingEntity.removePotionEffect(effect.getType());
                    // Gib den positiven Effekt dem Spieler, wenn er ihn noch nicht hat oder der Effekt länger anhält und stärker ist
                    if (!player.hasPotionEffect(effect.getType()) || isEffectStronger(effect, player)) {
                        player.addPotionEffect(new PotionEffect(effect.getType(), effect.getDuration(), effect.getAmplifier()));
                    }
                }
            }
        }
    }

    private boolean isGoodEffect(PotionEffect effect) {
        PotionEffectType type = effect.getType();
        // Hier können Sie die Potion-Effekte definieren, die als "gut" betrachtet werden sollen
        return type.equals(PotionEffectType.INCREASE_DAMAGE) || type.equals(PotionEffectType.HEAL) || type.equals(PotionEffectType.REGENERATION)|| type.equals(PotionEffectType.INVISIBILITY) || type.equals(PotionEffectType.WATER_BREATHING) || type.equals(PotionEffectType.DAMAGE_RESISTANCE) || type.equals(PotionEffectType.SPEED)|| type.equals(PotionEffectType.HEALTH_BOOST);
    }

    private boolean isEffectStronger(PotionEffect effect, Player player) {
        PotionEffect playerEffect = player.getPotionEffect(effect.getType());
        return (playerEffect != null && playerEffect.getDuration() <= effect.getDuration() && playerEffect.getAmplifier() <= effect.getAmplifier());
    }



    private double getArmorStrengthMultiplier(LivingEntity living) {
        double armorValue = 0.0;
        for (ItemStack item : living.getEquipment().getArmorContents()) {
            if (item != null) {
                armorValue += calculateArmorValue(item);
            }
        }
        return armorValue;
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

    public static void damageArmor(LivingEntity entity) {
        EntityEquipment equipment = entity.getEquipment();


        if (equipment == null) {
            return; // Das Entity hat keine Ausrüstung
        }

        // Überprüfe jede Rüstungseinheit
        for (ItemStack armorPiece : equipment.getArmorContents()) {
            if (armorPiece == null || armorPiece.getType().isAir()) {
                continue; // Überspringe leere Slots
            }


            int maxDurability = armorPiece.getType().getMaxDurability();
            int currentDurability = armorPiece.getDurability();

            // Berechne den Schaden um 40%, berücksichtige aber die Mindesthaltbarkeit
            int damage = (int) Math.ceil(0.4 * (maxDurability - currentDurability));
            int minDurability = maxDurability / 10; // 10%
            armorPiece.setDurability((short) (currentDurability + damage));
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

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (stunned.contains(entity)){
            stunned.remove(entity);
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

}
