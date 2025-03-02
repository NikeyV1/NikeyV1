package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.nikey.nikeyv1.api.Stone;
import de.slikey.effectlib.effect.*;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("ALL")
public class Firestone implements Listener {
    private HashMap<String, ArrayList<Entity>> entityMap = new HashMap<>();
    private int timer;
    private int time;
    private HashMap<Player , Integer> timecooldown = new HashMap<>();
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
                String damageEntityType = Stone.getAttacking(p);
                if (i >= 10) {
                    if (!(cooldown.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown.put(p.getUniqueId(),System.currentTimeMillis() + (100*1000));
                        int radius = 20;
                        int particles = 100;
                        double damage = 2.0;

                        if (i == 11 || i == 12) {
                            radius = 25;
                            particles = 200;
                        } else if (i == 13 || i >= 14) {
                            radius = 30;
                            particles = 200;
                            damage = (i >= 14) ? 3.0 : 2.5;
                        }
                        if (i == 12 || i == 13) {
                            damage = 2.5;
                        }

                        timer = 20;
                        SphereEffect effect = new SphereEffect(NikeyV1.em);
                        effect.setEntity(p);
                        effect.duration = 20000;
                        effect.particles = particles;
                        effect.particle = Particle.FLAME;
                        effect.radius = radius;
                        effect.visibleRange = 120;
                        effect.start();

                        int finalRadius = radius;
                        double finalDamage = damage;
                        entityMap.putIfAbsent(p.getName(), new ArrayList<>());
                        BukkitRunnable runnable = new BukkitRunnable() {
                            ArrayList<Entity> entities = entityMap.get(p.getName());
                            @Override
                            public void run() {
                                for (Entity e : p.getLocation().getWorld().getNearbyEntities(p.getLocation(), finalRadius, finalRadius, finalRadius)) {
                                    if (e instanceof LivingEntity) {
                                        if (HelpUtil.shouldDamageEntity((LivingEntity) e,p)) {
                                            if (e != p) {
                                                setEntityOnFire((LivingEntity) e, finalDamage);
                                                if (!entities.contains(e)) {
                                                    entities.add(e);
                                                }
                                            }
                                        }
                                    }
                                }
                                if (timer == 0) {
                                    entities.forEach(entity1 -> entity1.setVisualFire(false));
                                    entities.clear();
                                    cancel();
                                    return;
                                }
                                timer--;
                            }
                        };
                        runnable.runTaskTimer(NikeyV1.getPlugin(), 0, 20);
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (!p.isSneaking()) {
                    if (i == 15||i == 16||i == 17){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
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
                                        if (p.getHealth() <=18 && !p.isDead())p.heal(1.2, EntityRegainHealthEvent.RegainReason.MAGIC);
                                    }
                                    time--;
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),40,40);
                        }
                    }else if (i >=18){
                        if (!(ability.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                            ability.put(p.getUniqueId(),System.currentTimeMillis() + (180*1000));
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
                                        if (p.getHealth() <=18 && !p.isDead())p.setHealth(health+1.2);
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

    private void setEntityOnFire(LivingEntity entity, double damage) {
        entity.setVisualFire(true);
        entity.setFireTicks(20);
        entity.damage(damage);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Entity d = event.getDamager();
        Entity entity = event.getEntity();
        if (d instanceof Player) {
            Player player = (Player) d;
            int level = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            String stone = NikeyV1.getPlugin().getConfig().getString(player.getName() + ".stone");
            World world = player.getWorld();

            // Check if the player is in the Nether
            if (stone.equalsIgnoreCase("Fire")&&world.getEnvironment() == World.Environment.NETHER) {
                if (level == 5) {
                    double originalDamage = event.getDamage();
                    double boostedDamage = originalDamage * 1.075; // Increase damage by 7.5%
                    event.setDamage(boostedDamage);
                } else if (level == 6) {
                    double originalDamage = event.getDamage();
                    double boostedDamage = originalDamage * 1.15; // Increase damage by 7.5%
                    event.setDamage(boostedDamage);
                }else if (level >= 7) {
                    double originalDamage = event.getDamage();
                    double boostedDamage = originalDamage * 1.225; // Increase damage by 7.5%
                    event.setDamage(boostedDamage);
                }
            }
        }
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
                }else if (i ==16 ){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.7);
                        damager.damage(damage*0.2,entity);
                    }
                }else if (i == 17){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.7);
                        damager.damage(damage*0.3,entity);
                    }
                }else if (i == 18){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.6);
                        damager.damage(damage*0.4,entity);
                    }
                }else if (i >= 19){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        double damage = event.getDamage();
                        event.setDamage(damage*0.5);
                        damager.damage(damage*0.5,entity);
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
            String stone = NikeyV1.getPlugin().getConfig().getString(p.getName() + ".stone");
            if (stone.equalsIgnoreCase("Fire")) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK || event.getCause() == EntityDamageEvent.DamageCause.FIRE) {
                    if (i == 3) {
                        event.setDamage(event.getDamage() * 0.5);
                    } else if (i >= 4) {
                        event.setCancelled(true);
                    }
                } else if (event.getCause() == EntityDamageEvent.DamageCause.LAVA) {
                    if (i == 8) {
                        event.setDamage(event.getDamage()*0.8);
                    }else if (i >= 9) {
                        event.setDamage(event.getDamage()*0.6);
                    }
                }
            }
            if (ability.containsKey(p.getUniqueId())){
                if (i == 15){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.75);
                        }else if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
                            double damage = event.getDamage();
                            event.setDamage(damage*0.8);
                        }
                    }
                }else if (i == 16){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.65);
                        }else if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
                            double damage = event.getDamage();
                            event.setDamage(damage*0.7);
                        }
                    }
                }else if (i == 17){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.55);
                        }else if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
                            double damage = event.getDamage();
                            event.setDamage(damage*0.6);
                        }
                    }
                }else if (i == 18){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.45);
                        }else if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
                            double damage = event.getDamage();
                            event.setDamage(damage*0.6);
                        }
                    }
                }else if (i >= 19){
                    long remain = Firestone.ability.get(p.getUniqueId()) - System.currentTimeMillis();
                    int a = (int) (remain/1000);
                    if (a >160){
                        if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_EXPLOSION || event.getCause() == EntityDamageEvent.DamageCause.BLOCK_EXPLOSION) {
                            double damage = event.getDamage();
                            event.setDamage(damage*0.45);
                        }else if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK){
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
            if (i == 20 || i == 21){
                if (p.isSneaking()) {
                    if (cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis()){
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

        List<Player> nearbyPlayers = getPlayersInRadius(player.getLocation(), 250);

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
                    if (!(cooldown2.getOrDefault(p.getUniqueId(),0L) > System.currentTimeMillis())){
                        cooldown2.put(event.getWhoClicked().getUniqueId(), System.currentTimeMillis() + (300 * 1000));

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
                    handleAirstrike(launcherPlayer, selectedPlayer, level, 38);
                } else if (level == 21) {
                    handleAirstrike(launcherPlayer, selectedPlayer, level, 45);
                }
            }
        }.runTaskLater(NikeyV1.getPlugin(),40);
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getEntity() instanceof Fireball ) {
            if (event.getEntity().getCustomName() != null) {
                if (event.getEntity().getCustomName().equalsIgnoreCase("airstrike")) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            Location location = event.getEntity().getLocation();
                            Fireball fireball = (Fireball) event.getEntity();
                            Block highestBlockAt = fireball.getWorld().getHighestBlockAt(fireball.getLocation());
                            Location exp = highestBlockAt.getLocation().add(0, 1, 0);
                            fireball.getWorld().createExplosion(exp,3.4F,false,false);
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
                            fireball.getWorld().createExplosion(exp,4.2F,false,false);
                            fireball.getWorld().createExplosion(exp,1.3F,false,true);
                            fireball.remove();
                        }
                    }.runTaskLater(NikeyV1.getPlugin(),4);
                }
            }
        }
    }

    private void playDangerousWarning(Player player) {
        for (int i=0;i<7;i++) {
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 1.0f, 1.0f);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void handleAirstrike(final Player launcherPlayer, final Player selectedPlayer, final int level, final int cooldown) {
        timecooldown.put(launcherPlayer, cooldown);

        new BukkitRunnable() {
            @Override
            public void run() {
                timecooldown.replace(launcherPlayer, timecooldown.get(launcherPlayer) - 1);

                int x = (int) selectedPlayer.getLocation().getX();
                int z = (int) selectedPlayer.getLocation().getZ();
                int randomX = ThreadLocalRandom.current().nextInt(x - 6, x + 6);
                int randomZ = ThreadLocalRandom.current().nextInt(z - 6, z + 6);
                int yOffset = selectedPlayer.getLocation().getZ() > 50 ? 20 : 40;
                String customName = level == 20 ? "airstrike" : "strongairstrike";

                Location location = new Location(selectedPlayer.getWorld(), randomX, selectedPlayer.getLocation().getY() + yOffset, randomZ);
                Fireball fireball = (Fireball) location.getWorld().spawn(location, Fireball.class, CreatureSpawnEvent.SpawnReason.CUSTOM);
                fireball.setShooter(launcherPlayer);
                fireball.setVelocity(new Vector(0, -2.5, 0));
                fireball.setDirection(new Vector(0, -2.5, 0));
                fireball.setCustomName(customName);
                fireball.setCustomNameVisible(false);

                if (timecooldown.get(launcherPlayer) == 0) {
                    timecooldown.remove(launcherPlayer);
                    cancel();
                }
            }
        }.runTaskTimer(NikeyV1.getPlugin(), 0, 6);
    }

    @EventHandler
    public void onInventoryInteract(InventoryInteractEvent event) {
        if (event.getView().getTitle().equalsIgnoreCase("§cSelect Player")) {
            event.setCancelled(true);
        }
    }
}

