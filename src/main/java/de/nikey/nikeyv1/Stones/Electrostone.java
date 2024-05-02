package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.CylinderEffect;
import de.slikey.effectlib.effect.ShieldEffect;
import de.slikey.effectlib.effect.TornadoEffect;
import io.papermc.paper.configuration.type.fallback.FallbackValue;
import io.papermc.paper.event.entity.EntityMoveEvent;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityTeleportEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
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
        LightningStrike lightningStrike = p.getWorld().strikeLightning(location);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    entity.damage(10,lightningStrike);
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
        LightningStrike lightningStrike = p.getWorld().strikeLightning(location);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    entity.damage(10,lightningStrike);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
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
        LightningStrike lightningStrike = p.getWorld().strikeLightning(location);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    entity.damage(14,lightningStrike);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*4,2));
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
        LightningStrike lightningStrike = p.getWorld().strikeLightning(location);
        for (Entity e : location.getWorld().getNearbyEntities(location,4,4,4)){
            if (e instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) e;
                if (entity != p){
                    entity.damage(14,lightningStrike);
                    entity.addPotionEffect(new PotionEffect(PotionEffectType.SLOW,20*6,2));
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
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR) {
                if (i >= 10){
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime1 = cooldown.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*100);
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
                if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                    event.setCancelled(true);
                    p.updateInventory();
                    remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                }else {
                    if (i == 15||i == 16){
                        ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ability.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*180);

                        stunned.add(entity);
                        CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                        effect.setEntity(entity);
                        effect.duration = 6000;
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
                                stunned.add(e);
                                if (e instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) e;
                                    livingEntity.damage(8,p);
                                }
                            }
                        }
                    } else if (i == 17 || i == 18) {
                        ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ability.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*180);

                        stunned.add(entity);
                        CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                        effect.setEntity(entity);
                        effect.duration = 6000;
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
                                stunned.add(e);
                                if (e instanceof LivingEntity) {
                                    LivingEntity livingEntity = (LivingEntity) e;
                                    livingEntity.damage(8,p);
                                }
                            }
                        }
                    } else if (i >=19) {
                        ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                ability.remove(p.getUniqueId());
                                cancel();
                                return;
                            }
                        }.runTaskLater(NikeyV1.getPlugin(),20*180);

                        stunned.add(entity);
                        CylinderEffect effect = new CylinderEffect(NikeyV1.em);
                        effect.setEntity(entity);
                        effect.duration = 6000;
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
                                stunned.add(e);
                            }
                        }
                    }
                }
            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
        String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
        if (stone.equalsIgnoreCase("electric")) {
            if (level >= 7) {
                if (level == 7) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.075);
                } else if (level == 8) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.15);
                }else if (level >= 9) {
                    player.getAttribute(Attribute.GENERIC_ATTACK_SPEED).setBaseValue(4.2);
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
                if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                    p.updateInventory();
                    remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                }else {
                    cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cooldown2.remove(p.getUniqueId());
                            cancel();
                        }
                    }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
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
                                if (entity instanceof LivingEntity) {
                                    LivingEntity target = (LivingEntity) entity;
                                    if (target != p) {

                                        target.damage(3);
                                        target.setVelocity(target.getLocation().getDirection().multiply(-1.5));
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
                if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                    p.updateInventory();
                    remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                }else {
                    cooldown2.put(p.getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            cooldown2.remove(p.getUniqueId());
                            cancel();
                        }
                    }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
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
                            for (Entity entity : p.getNearbyEntities(9, 9, 9)) {
                                if (entity instanceof LivingEntity) {
                                    LivingEntity target = (LivingEntity) entity;
                                    if (target != p) {
                                        target.damage(6);
                                        target.setVelocity(target.getLocation().getDirection().multiply(-2));
                                    }
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
