package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.EntityTypeDamage;
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

    public void lightning10(Player p) {
        int x = (int) (p.getLocation().getX());
        int z = (int) (p.getLocation().getZ());
        int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
        int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
        LightningStrike lightningStrike = p.getWorld().strikeLightningEffect(location);
        lightningStrike.setCausingPlayer(p);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                    if (damageEntityType.equalsIgnoreCase("all")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();
                                entity.damage(10,source);
                            }
                        }else {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(10,source);
                        }
                    }else if (damageEntityType.equalsIgnoreCase("players")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(10,source);
                            }
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                        if (e instanceof Monster) {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(10,source);
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(10,source);
                            }
                        }else if (e instanceof Monster){
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(10,source);
                        }
                    }
                }
            }
        }
        TornadoEffect effect = new TornadoEffect(NikeyV1.em);
        effect.setLocation(location);
        effect.maxTornadoRadius = 3F;
        effect.visibleRange = 70;
        effect.circleParticles =32;
        effect.cloudParticles =20;
        effect.tornadoParticle = Particle.ELECTRIC_SPARK;
        effect.duration = 1500;
        effect.start();
    }

    public void lightning11(Player p) {
        int x = (int) (p.getLocation().getX());
        int z = (int) (p.getLocation().getZ());
        int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
        int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
        LightningStrike lightningStrike = p.getWorld().strikeLightningEffect(location);
        lightningStrike.setCausingPlayer(p);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){

                    String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                    if (damageEntityType.equalsIgnoreCase("all")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();
                                entity.damage(10,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                            }
                        }else {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();
                            entity.damage(10,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                        }
                    }else if (damageEntityType.equalsIgnoreCase("players")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(10,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                            }
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                        if (e instanceof Monster) {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(10,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(10,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                            }
                        }else if (entity instanceof Monster){
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(10,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                        }
                    }
                }
            }
        }
        TornadoEffect effect = new TornadoEffect(NikeyV1.em);
        effect.setLocation(location);
        effect.maxTornadoRadius = 3F;
        effect.visibleRange = 70;
        effect.circleParticles =32;
        effect.cloudParticles =20;
        effect.tornadoParticle = Particle.ELECTRIC_SPARK;
        effect.duration = 1500;
        effect.start();
    }

    public void lightning13(Player p) {
        int x = (int) (p.getLocation().getX());
        int z = (int) (p.getLocation().getZ());
        int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
        int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
        LightningStrike lightningStrike = p.getWorld().strikeLightningEffect(location);
        lightningStrike.setCausingPlayer(p);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                    if (damageEntityType.equalsIgnoreCase("all")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(14,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                            }
                        }else {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(14,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                        }
                    }else if (damageEntityType.equalsIgnoreCase("players")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(14,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                            }
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                        if (e instanceof Monster) {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(14,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(14,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                            }
                        }else if (entity instanceof Monster){
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(14,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
                        }
                    }
                }
            }
        }
        TornadoEffect effect = new TornadoEffect(NikeyV1.em);
        effect.setLocation(location);
        effect.maxTornadoRadius = 3F;
        effect.visibleRange = 70;
        effect.circleParticles =32;
        effect.cloudParticles =20;
        effect.tornadoParticle = Particle.ELECTRIC_SPARK;
        effect.duration = 1500;
        effect.start();
    }

    public void lightning14(Player p) {
        int x = (int) (p.getLocation().getX());
        int z = (int) (p.getLocation().getZ());
        int randomX = ThreadLocalRandom.current().nextInt(x-10, x+10);
        int randomZ = ThreadLocalRandom.current().nextInt(z-10, z+10);
        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
        LightningStrike lightningStrike = p.getWorld().strikeLightningEffect(location);
        lightningStrike.setCausingPlayer(p);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){

                    String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                    if (damageEntityType.equalsIgnoreCase("all")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(14,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
                            }
                        }else {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(14,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
                        }
                    }else if (damageEntityType.equalsIgnoreCase("players")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(14,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
                            }
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                        if (e instanceof Monster) {
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(14,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
                        }
                    }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                        if (entity instanceof Player){
                            List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                            if (!playersInSameTeam.contains(entity)) {
                                entity.setFireTicks(20*8);
                                DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                                entity.damage(14,source);
                                entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
                            }
                        }else if (entity instanceof Monster){
                            entity.setFireTicks(20*8);
                            DamageSource source = DamageSource.builder(DamageType.LIGHTNING_BOLT).withDirectEntity(p).withCausingEntity(p).build();;
                            entity.damage(14,source);
                            entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
                        }
                    }
                }
            }
        }
        TornadoEffect effect = new TornadoEffect(NikeyV1.em);
        effect.setLocation(location);
        effect.maxTornadoRadius = 3F;
        effect.visibleRange = 70;
        effect.circleParticles =32;
        effect.cloudParticles =20;
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
                        if (i == 10 ){
                            timer = 20;
                            World world = p.getWorld();
                            BukkitRunnable runnable = new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        lightning10(p);
                                    }
                                    timer--;
                                }
                            };
                            runnable.runTaskTimer(NikeyV1.getPlugin(),20,20);


                        } else if (i == 11) {
                            timer = 20;
                            World world = p.getWorld();
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        lightning11(p);
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        } else if (i == 12) {
                            timer = 25;
                            World world = p.getWorld();
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        lightning11(p);
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }else if (i == 13) {
                            timer = 25;
                            World world = p.getWorld();
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        lightning13(p);
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }else if (i >= 14) {
                            timer = 25;
                            World world = p.getWorld();
                            new BukkitRunnable() {

                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        lightning14(p);
                                    }
                                    timer--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),20,20);
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
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
                        if (i == 15||i == 16){
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                             

                            stunned.add(entity);
                            CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                            effect.setEntity(entity);
                            effect.duration = 5000;
                            effect.particles = 30;
                            effect.particle = Particle.ELECTRIC_SPARK;
                            effect.visibleRange = 100;
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    stunned.clear();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*5);
                            for (Entity e : entity.getNearbyEntities(1,1,1)){
                                if (e != p){
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    if (e instanceof Player){
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player = (Player) e;
                                            player.damage(8,p);
                                            stunned.add(player);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity livingEntity = (LivingEntity) e;
                                        livingEntity.damage(8,p);
                                        stunned.add(livingEntity);
                                    }
                                }
                            }
                        } else if (i == 17 || i == 18) {
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));

                            stunned.add(entity);
                            CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                            effect.setEntity(entity);
                            effect.duration = 5000;
                            effect.particles = 30;
                            effect.particle = Particle.ELECTRIC_SPARK;
                            effect.visibleRange = 100;
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    stunned.clear();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*5);
                            for (Entity e : entity.getNearbyEntities(2,2,2)){
                                if (e != p){
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    if (e instanceof Player){
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player = (Player) e;
                                            player.damage(8,p);
                                            stunned.add(player);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity livingEntity = (LivingEntity) e;
                                        livingEntity.damage(8,p);
                                        stunned.add(livingEntity);
                                    }
                                }
                            }
                        } else if (i >=19) {
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));

                            stunned.add(entity);
                            CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                            effect.setEntity(entity);
                            effect.duration = 8000;
                            effect.particles = 30;
                            effect.particle = Particle.ELECTRIC_SPARK;
                            effect.visibleRange = 100;
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    stunned.clear();
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*8);
                            for (Entity e : entity.getNearbyEntities(2,2,2)){
                                if (e != p){
                                    e.getWorld().strikeLightningEffect(e.getLocation());
                                    if (e instanceof Player){
                                        List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                        if (!playersInSameTeam.contains(e)) {
                                            Player player = (Player) e;
                                            player.damage(8,p);
                                            stunned.add(player);
                                        }
                                    }else if (e instanceof LivingEntity){
                                        LivingEntity livingEntity = (LivingEntity) e;
                                        livingEntity.damage(8,p);
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
        if (stone.equalsIgnoreCase("electric")) {
            if (level >= 7) {
                if (level == 7) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.1);
                } else if (level == 8) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.2);
                }else if (level >= 9) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.3);
                }
            }else {
                player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
            }
        }else {
            player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4);
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
            if (i == 20 ){
                mtimer.put(p,40);
                if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                    cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                    //Cooldown-Ability

                    ShieldEffect effect = new ShieldEffect(NikeyV1.em);
                    effect.radius = 6;
                    effect.sphere = true;
                    effect.setEntity(p);
                    effect.duration = 20000;
                    effect.particle = Particle.SCRAPE;
                    effect.particles = 50;
                    effect.start();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mtimer.replace(p,mtimer.get(p)-1);
                            for (Entity entity : p.getNearbyEntities(5, 5, 5)) {
                                Location difference = entity.getLocation().subtract(p.getLocation());
                                Vector normalizedDifference = difference.toVector().normalize();
                                Vector multiplied = normalizedDifference.multiply(1.5);
                                entity.setVelocity(multiplied);
                                if (entity instanceof LivingEntity) {
                                    LivingEntity target = (LivingEntity) entity;
                                    if (target != p) {
                                        String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                                        if (damageEntityType.equalsIgnoreCase("all")) {
                                            if (target instanceof Player){
                                                List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                                if (!playersInSameTeam.contains(target)) {
                                                    target.damage(1);
                                                }
                                            }else {
                                                target.damage(1);
                                            }
                                        }else if (damageEntityType.equalsIgnoreCase("players")) {
                                            if (target instanceof Player){
                                                List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                                if (!playersInSameTeam.contains(target)) {
                                                    target.damage(1);
                                                }
                                            }
                                        }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                            if (target instanceof Monster) {
                                                target.damage(1);
                                            }
                                        }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                            if (target instanceof Player){
                                                List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                                if (!playersInSameTeam.contains(target)) {
                                                    target.damage(1);
                                                }
                                            }else if (target instanceof Monster){
                                                target.damage(1);
                                            }
                                        }
                                    }
                                }
                            }

                            if (mtimer.get(p) == 0) {
                                cancel();
                                return;
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,10);
                }
            } else if (i == 21) {
                mtimer.put(p,100);
                if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                    cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                    //Cooldown-Ability
                    ShieldEffect effect = new ShieldEffect(NikeyV1.em);
                    effect.radius = 10;
                    effect.sphere = true;
                    effect.setEntity(p);
                    effect.duration = 26250;
                    effect.particle = Particle.SCRAPE;
                    effect.particles = 50;
                    effect.start();
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            mtimer.replace(p,mtimer.get(p)-1);
                            for (Entity entity : p.getNearbyEntities(8, 8, 8)) {
                                Location difference = entity.getLocation().subtract(p.getLocation());
                                Vector normalizedDifference = difference.toVector().normalize();
                                Vector multiplied = normalizedDifference.multiply(2);
                                entity.setVelocity(multiplied);
                                if (entity instanceof LivingEntity) {
                                    LivingEntity target = (LivingEntity) entity;
                                    if (target != p) {

                                        String damageEntityType = EntityTypeDamage.getDamageEntityType(p);
                                        if (damageEntityType.equalsIgnoreCase("all")) {
                                            if (target instanceof Player){
                                                List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                                if (!playersInSameTeam.contains(target)) {
                                                    target.damage(2);
                                                }
                                            }else {
                                                target.damage(2);
                                                 
                                            }
                                        }else if (damageEntityType.equalsIgnoreCase("players")) {
                                            if (target instanceof Player){
                                                List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                                if (!playersInSameTeam.contains(target)) {
                                                    target.damage(2);
                                                }
                                            }
                                        }else if (damageEntityType.equalsIgnoreCase("monsters")) {
                                            if (target instanceof Monster) {
                                                target.damage(2);
                                            }
                                        }else if (damageEntityType.equalsIgnoreCase("monsters-player")) {
                                            if (target instanceof Player){
                                                List<Player> playersInSameTeam = HelpUtil.getPlayersInSameTeam(p);
                                                if (!playersInSameTeam.contains(target)) {
                                                    target.damage(2);
                                                }
                                            }else if (target instanceof Monster){
                                                target.damage(2);
                                            }
                                        }
                                    }
                                }else {
                                }
                            }

                            if (mtimer.get(p) == 0) {
                                cancel();
                                return;
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,5);
                }
            }
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
