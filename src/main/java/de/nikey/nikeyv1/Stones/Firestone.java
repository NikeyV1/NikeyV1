package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.slikey.effectlib.effect.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Firestone implements Listener {
    private ArrayList<Entity> entities = new ArrayList<>();
    private ArrayList<Entity> player = new ArrayList<>();
    private int timer;
    private int time;
    private int timecooldown;
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;


    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getItem() == null)return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#e66b63")+"Fire Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            Player p = event.getPlayer();
            ItemStack item = event.getItem();
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Fire");
            config.set(p.getName()+".fire",null);
            config.set(p.getName()+".level",i);
            String stone = NikeyV1.getPlugin().getConfig().getString(p.getName() + ".stone");
            NikeyV1.getPlugin().saveConfig();
            if (event.getAction() == Action.RIGHT_CLICK_BLOCK || event.getAction() == Action.RIGHT_CLICK_AIR){
                if (i >= 10) {
                    if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                        event.setCancelled(true);
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
                        if (i == 10 || i == 11) {
                            timer = 20;
                            SphereEffect effect = new SphereEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.duration = 20000;
                            effect.particles = 100;
                            effect.particle = Particle.FLAME;
                            effect.radius = 20.0;
                            effect.start();
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(),20,20,20)){
                                        if (e instanceof LivingEntity){
                                            LivingEntity entity = (LivingEntity) e;
                                            entities.add(entity);
                                            if (entity != p){
                                                e.setVisualFire(true);
                                                entity.damage(2);
                                            }
                                            if (timer == 0){
                                                entity.setVisualFire(false);
                                                entities.forEach(entity1 -> entity1.setVisualFire(false));
                                                entities.clear();
                                                cancel();
                                                return;
                                            }
                                        }
                                    }
                                    timer--;
                                }
                            };
                            runnable.runTaskTimer(NikeyV1.getPlugin(),0,20);
                        } else if (i >= 12) {
                            timer = 20;
                            SphereEffect effect = new SphereEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.duration = 20000;
                            effect.particles = 200;
                            effect.particle = Particle.FLAME;
                            effect.radius = 30.0;
                            effect.start();
                            BukkitRunnable runnable = new BukkitRunnable() {
                                @Override
                                public void run() {
                                    for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), 30, 30, 30)) {
                                        if (e instanceof LivingEntity) {
                                            LivingEntity entity = (LivingEntity) e;
                                            entities.add(entity);
                                            if (entity != p) {
                                                e.setVisualFire(true);
                                                entity.damage(3);
                                            }
                                            if (timer == 0) {
                                                entity.setVisualFire(false);
                                                entities.forEach(entity1 -> entity1.setVisualFire(false));
                                                entities.clear();
                                                cancel();
                                                return;
                                            }
                                        }
                                    }
                                    timer--;
                                }
                            };
                            runnable.runTaskTimer(NikeyV1.getPlugin(), 0, 20);
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (!p.isSneaking()) {
                    if (i == 15||i == 16||i == 17){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*180);
                            //Cooldown-Ability
                            time = 10;
                            AnimatedBallEffect effect = new AnimatedBallEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.FLAME;
                            effect.duration = 20000;
                            effect.visibleRange = 100;
                            effect.start();
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        double health = p.getHealth();
                                        if (p.getHealth() <=18 )p.setHealth(health+1.2);
                                    }
                                    time--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),40,40);
                        }
                    }else if (i >=18){
                        if (ability.containsKey(p.getUniqueId()) && ability.get(p.getUniqueId()) > System.currentTimeMillis()){
                            event.setCancelled(true);
                            p.updateInventory();
                            remainingTime2 = ability.get(p.getUniqueId()) - System.currentTimeMillis();
                        }else {

                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    ability.remove(p.getUniqueId());
                                    cancel();
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(),20*180);
                            //Cooldown-Ability
                            time = 10;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        double health = p.getHealth();
                                        if (p.getHealth() <=18 )p.setHealth(health+1.2);
                                    }
                                    time--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),40,40);
                            AnimatedBallEffect effect = new AnimatedBallEffect(NikeyV1.em);
                            effect.setEntity(p);
                            effect.particle = Particle.FLAME;
                            effect.duration = 20000;
                            effect.visibleRange = 100;
                            effect.start();
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        Entity d = event.getDamager();
        Entity entity = event.getEntity();
        if (entity instanceof Player && d instanceof LivingEntity){
            Player p = (Player) entity;
            LivingEntity damager = (LivingEntity) d;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (ability.containsKey(p.getUniqueId())){
                if (i == 15){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.80);
                        damager.damage(damage*0.2,entity);
                    }

                }else if (i ==16 || i == 17){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.75);
                        damager.damage(damage*0.3,entity);
                    }
                }else if (i >= 18){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.70);
                        damager.damage(damage*0.4,entity);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player) {
            Player p = (Player) entity;
            int i = NikeyV1.getPlugin().getConfig().getInt(p.getName() + ".level");
            if (ability.containsKey(p.getUniqueId())){
                if (i == 15){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.75);
                        }else {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.70);
                        }
                    }
                }else if (i == 16||i == 17){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.675);
                        }else {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.6);
                        }
                    }
                }else if (i >= 18){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.6);
                        }else {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.5);
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerDropItem(PlayerDropItemEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItemDrop().getItemStack();
        if (item == null) return;
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#e66b63")+"Fire Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Fire");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (i == 20 || i==21){
                if (p.isSneaking()) {
                    if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        //Cooldown-Ability
                        openPlayerSelectionInventory(p);
                    }
                }
            }
        }
    }


    private void openPlayerSelectionInventory(Player player) {
        Inventory playerSelectionInventory = Bukkit.createInventory(null, 27, "§cSelect Player");

        List<Player> nearbyPlayers = getPlayersInRadius(player.getLocation(), 100);

        for (Player nearbyPlayer : nearbyPlayers) {
            ItemStack playerHead = getPlayerHeadItem(nearbyPlayer);
            playerSelectionInventory.addItem(playerHead);
        }

        player.openInventory(playerSelectionInventory);
    }


    private ItemStack getPlayerHeadItem(Player player) {
        ItemStack headItem = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta headMeta = (SkullMeta) headItem.getItemMeta();
        headMeta.setDisplayName("§r"+player.getName());
        headMeta.setOwningPlayer(player);
        headItem.setItemMeta(headMeta);
        return headItem;
    }


    private List<Player> getPlayersInRadius(Location location, double radius) {
        List<Player> nearbyPlayers = new ArrayList<>();

        for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
            if (onlinePlayer.getLocation().distanceSquared(location) <= Math.pow(radius, 2)) {
                nearbyPlayers.add(onlinePlayer);
            }
        }
        return nearbyPlayers;
    }


    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§cSelect Player")) {
            event.setCancelled(true);

            if (event.getCurrentItem() != null && event.getCurrentItem().getType() == Material.PLAYER_HEAD) {
                Player selectedPlayer = Bukkit.getPlayerExact(event.getCurrentItem().getItemMeta().getDisplayName());
                Player p = (Player) event.getWhoClicked();

                if (selectedPlayer != null) {
                    if (cooldown2.containsKey(p.getUniqueId()) && cooldown2.get(p.getUniqueId()) > System.currentTimeMillis()){
                        p.updateInventory();
                        remainingTime3 = cooldown2.get(p.getUniqueId()) - System.currentTimeMillis();
                    }else {
                        cooldown2.put(event.getWhoClicked().getUniqueId(), System.currentTimeMillis() + (300 * 1000));
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                cooldown2.remove(p.getUniqueId());
                                cancel();
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20 * 300);
                        triggerMegaFireAbility(selectedPlayer, (Player) event.getWhoClicked());
                    }
                }
            }
        }
    }


    private void triggerMegaFireAbility(Player selectedPlayer, Player launcherPlayer) {
        launcherPlayer.closeInventory();
        FileConfiguration config = NikeyV1.getPlugin().getConfig();
        Integer level = config.getInt(launcherPlayer.getName() + ".level");
        playDangerousWarning(selectedPlayer);
        new BukkitRunnable() {
            @Override
            public void run() {
                if (level == 20) {
                    timecooldown=38;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timecooldown--;
                            int x = (int) (selectedPlayer.getLocation().getX());
                            int z = (int) (selectedPlayer.getLocation().getZ());
                            int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                            int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                            int randomY = selectedPlayer.getWorld().getHighestBlockYAt(randomX,randomZ);
                            if (selectedPlayer.getLocation().getZ() > 50) {
                                Location location = new Location(selectedPlayer.getWorld(),randomX,randomY+20,randomZ);
                                Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
                                fireball.setShooter(launcherPlayer);
                                fireball.setVelocity(new org.bukkit.util.Vector(0, -0.8, 0)); // Adjust the fireball trajectory
                                fireball.setIsIncendiary(false);
                                fireball.setCustomName("airstrike");
                                fireball.setCustomNameVisible(false);
                            }else {
                                Location location = new Location(selectedPlayer.getWorld(),randomX,randomY+40,randomZ);
                                Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
                                fireball.setShooter(launcherPlayer);
                                fireball.setVelocity(new org.bukkit.util.Vector(0, -0.8, 0)); // Adjust the fireball trajectory
                                fireball.setCustomName("airstrike");
                                fireball.setCustomNameVisible(false);
                            }


                            if (timecooldown == 0) {
                                cancel();
                                return;
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(),0,4);
                } else if (level == 21) {
                    timecooldown = 45;
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            timecooldown--;
                            int x = (int) (selectedPlayer.getLocation().getX());
                            int z = (int) (selectedPlayer.getLocation().getZ());
                            int randomX = ThreadLocalRandom.current().nextInt(x - 6, x + 6);
                            int randomZ = ThreadLocalRandom.current().nextInt(z - 6, z + 6);
                            int randomY = selectedPlayer.getWorld().getHighestBlockYAt(randomX, randomZ);
                            if (selectedPlayer.getLocation().getZ() > 50) {
                                Location location = new Location(selectedPlayer.getWorld(), randomX, randomY + 20, randomZ);
                                Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
                                fireball.setShooter(launcherPlayer);
                                fireball.setVelocity(new org.bukkit.util.Vector(0, -0.8, 0)); // Adjust the fireball trajectory
                                fireball.setIsIncendiary(false);
                                fireball.setCustomName("strongairstrike");
                                fireball.setCustomNameVisible(false);

                            } else {
                                Location location = new Location(selectedPlayer.getWorld(), randomX, randomY + 40, randomZ);
                                Fireball fireball = (Fireball) location.getWorld().spawnEntity(location, EntityType.FIREBALL);
                                fireball.setShooter(launcherPlayer);
                                fireball.setVelocity(new org.bukkit.util.Vector(0, -0.8, 0)); // Adjust the fireball trajectory
                                fireball.setCustomName("strongairstrike");
                                fireball.setCustomNameVisible(false);
                            }


                            if (timecooldown == 0) {
                                cancel();
                                return;
                            }
                        }
                    }.runTaskTimer(NikeyV1.getPlugin(), 0, 8);
                }
            }
        }.runTaskLater(NikeyV1.getPlugin(),40);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Fireball ) {
            if (event.getEntity().getCustomName().equalsIgnoreCase("airstrike")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location location = event.getEntity().getLocation();
                        Fireball fireball = (Fireball) event.getEntity();
                        Block highestBlockAt = fireball.getWorld().getHighestBlockAt(fireball.getLocation());
                        Location exp = highestBlockAt.getLocation().add(0, 1, 0);
                        fireball.getWorld().createExplosion(exp,3F,false,false);
                        fireball.remove();
                    }
                }.runTaskLater(NikeyV1.getPlugin(),4);
            } else if (event.getEntity().getCustomName().equals("strongairstrike")) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        Location location = event.getEntity().getLocation();
                        Fireball fireball = (Fireball) event.getEntity();
                        Block highestBlockAt = fireball.getWorld().getHighestBlockAt(fireball.getLocation());
                        Location exp = highestBlockAt.getLocation().add(0, 1, 0);
                        fireball.getWorld().createExplosion(exp,3.8F,false,false);
                        fireball.getWorld().createExplosion(exp,1F,false,true);
                        fireball.remove();
                    }
                }.runTaskLater(NikeyV1.getPlugin(),4);
            }
        }
    }

    private void playDangerousWarning(Player player) {

        // Die Melodie abspielen
        for (int i=0;i<7;i++) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            try {
                // Eine kurze Pause zwischen den Tönen
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§cSelect Player")) {
            event.setCancelled(true);
        }
    }
}

