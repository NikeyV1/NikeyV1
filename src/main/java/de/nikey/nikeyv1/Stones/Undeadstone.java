package de.nikey.nikeyv1.Stones;

import de.nikey.nikeyv1.NikeyV1;
import de.nikey.nikeyv1.Util.HelpUtil;
import de.slikey.effectlib.effect.SmokeEffect;
import io.papermc.paper.event.entity.EntityMoveEvent;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeInstance;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

@SuppressWarnings("ALL")
public class Undeadstone implements Listener {
    public ArrayList<LivingEntity> Soul = new ArrayList<>();
    public static HashMap<UUID, Long> cooldown = new HashMap<>();
    public static HashMap<UUID, Long> ability = new HashMap<>();
    public static HashMap<UUID, Long> cooldown2 = new HashMap<>();
    private int timer;
    public static long remainingTime1;
    public static long remainingTime2;
    public static long remainingTime3;
    private Player player;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Player p = event.getPlayer();
        ItemStack item = event.getItem();
        if (item == null) return;
        if (event.getItem().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")&& event.getItem().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Undead");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (event.getAction() == Action.RIGHT_CLICK_AIR||event.getAction() == Action.RIGHT_CLICK_BLOCK){
                if (!Soul.isEmpty()){
                    EntityType type = Soul.get(0).getType();
                    if (type.equals(EntityType.WITHER)||type.equals(EntityType.ENDER_DRAGON)||type.equals(EntityType.EVOKER)||type.equals(EntityType.GIANT)){
                    }else {
                        if (i == 10){
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        } else if (i == 11) {
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,PotionEffect.INFINITE_DURATION,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,1));
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        } else if (i == 12){
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,PotionEffect.INFINITE_DURATION,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        }else if (i == 13){
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,PotionEffect.INFINITE_DURATION,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                            summoned.setAbsorptionAmount(4);
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        }else if (i >= 14){
                            LivingEntity summoned = (LivingEntity) p.getWorld().spawnEntity(p.getLocation(), type);
                            summoned.setCustomName(p.getDisplayName()+"'s "+type.getName());
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                            summoned.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,PotionEffect.INFINITE_DURATION,0));
                            summoned.setAbsorptionAmount(4);
                            Soul.clear();
                            p.getWorld().playSound(p.getLocation(),Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                            ItemStack hand = p.getInventory().getItemInMainHand();
                            ItemMeta meta = hand.getItemMeta();
                            ArrayList<String> list = new ArrayList<>();
                            list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                            list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                            meta.setLore(list);
                            hand.setItemMeta(meta);
                        }
                    }
                }
            }else if (event.getAction() == Action.LEFT_CLICK_AIR ||event.getAction() == Action.LEFT_CLICK_BLOCK){
                if (p.isSneaking()){
                    if (i == 15){
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
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 40;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(2);
                                        SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                        effect.setLocation(location);
                                        effect.particle = Particle.SOUL;
                                        effect.duration = 100;
                                        effect.particles = 3;
                                        effect.start();
                                        if (r == 0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
                        }
                    } else if (i == 16) {
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
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 40;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(3);
                                        SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                        effect.setLocation(location);
                                        effect.particle = Particle.SOUL;
                                        effect.duration = 100;
                                        effect.particles = 3;
                                        effect.start();
                                        if (r == 0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else if (r == 2){
                                            WitherSkeleton spawn = location.getWorld().spawn(location, WitherSkeleton.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,400,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
                        }
                    }else if (i == 17) {
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
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 40;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(3);
                                        SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                        effect.setLocation(location);
                                        effect.particle = Particle.SOUL;
                                        effect.duration = 100;
                                        effect.particles = 3;
                                        effect.start();
                                        if (r == 0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else if (r == 2){
                                            WitherSkeleton spawn = location.getWorld().spawn(location, WitherSkeleton.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
                        }
                    }else if (i == 18) {
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
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 60;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(3);
                                        SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                        effect.setLocation(location);
                                        effect.particle = Particle.SOUL;
                                        effect.duration = 100;
                                        effect.particles = 3;
                                        effect.start();
                                        if (r == 0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else if (r == 2){
                                            WitherSkeleton spawn = location.getWorld().spawn(location, WitherSkeleton.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
                        }
                    } else if (i >=19){
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
                                    return;
                                }
                            }.runTaskLater(NikeyV1.getPlugin(), 20 * 180);
                            //cooldown-ability
                            timer = 60;
                            player = p;
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    timer--;
                                    if (timer == 0){
                                        cancel();
                                        return;
                                    }else {
                                        int x = (int) (p.getLocation().getX());
                                        int z = (int) (p.getLocation().getZ());
                                        int randomX = ThreadLocalRandom.current().nextInt(x-6, x+6);
                                        int randomZ = ThreadLocalRandom.current().nextInt(z-6, z+6);
                                        int randomY = p.getWorld().getHighestBlockYAt(randomX,randomZ);
                                        Location location = new Location(p.getWorld(),randomX,randomY+1,randomZ);
                                        Random random = new Random();
                                        int r = random.nextInt(3);
                                        SmokeEffect effect = new SmokeEffect(NikeyV1.em);
                                        effect.setLocation(location);
                                        effect.particle = Particle.SOUL;
                                        effect.duration = 100;
                                        effect.particles = 3;
                                        effect.start();
                                        if (r == 0){
                                            Zombie zombie = location.getWorld().spawn(location, Zombie.class);
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            zombie.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            zombie.setCustomName(p.getDisplayName()+"'s "+zombie.getType().getName());
                                        }else if (r ==1){
                                            ZombieVillager spawn = location.getWorld().spawn(location, ZombieVillager.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else if (r == 2){
                                            WitherSkeleton spawn = location.getWorld().spawn(location, WitherSkeleton.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }else{
                                            Zombie spawn = location.getWorld().spawn(location, Zombie.class);
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE,400,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,400,1));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION,400,4));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE,PotionEffect.INFINITE_DURATION,0));
                                            spawn.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,400,1));
                                            spawn.setCustomName(p.getDisplayName()+"'s "+spawn.getType().getName());
                                        }
                                    }
                                }
                            }.runTaskTimer(NikeyV1.getPlugin(),10,10);
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
        if (event.getItemDrop().getItemStack().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")&& event.getItemDrop().getItemStack().getType() == Material.FIREWORK_STAR){
            String[] arr = item.getLore().get(1).split(":");
            int i = Integer.parseInt(arr[1]);
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            config.set(p.getName()+".stone","Undead");
            config.set(p.getName()+".level",i);
            NikeyV1.getPlugin().saveConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (i == 20 || i == 21){
                if (p.isSneaking()) {
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
                        spawnRidingHusk(p);
                    }
                }
            }
        }
    }


    private void spawnRidingHusk(Player player) {
        World world = getServer().getWorld("world"); // world has to be names "world"
        if (world != null) {
            Location added = player.getLocation().getWorld().getHighestBlockAt(player.getLocation()).getLocation();
            Location l = added.clone().add(0,1,0);
            Giant giant = (Giant) world.spawnEntity(l, EntityType.GIANT);
            giant.setMaxHealth(500);
            giant.setHealth(500);
            int i = NikeyV1.getPlugin().getConfig().getInt(player.getName() + ".level");
            if (i == 20) {
                giant.setCustomName(player.getName()+"'s "+giant.getType().getName());
            } else if (i == 21) {
                giant.setCustomName(player.getName()+"'s "+giant.getType().getName()+" strong");
            }
            giant.setCustomNameVisible(true);
            giant.addPotionEffect(new PotionEffect(PotionEffectType.REGENERATION,PotionEffect.INFINITE_DURATION,1,false,false));
            giant.addPotionEffect(new PotionEffect(PotionEffectType.JUMP,PotionEffect.INFINITE_DURATION,4,false,false));
            AttributeInstance attribute = giant.getAttribute(Attribute.GENERIC_KNOCKBACK_RESISTANCE);
            if (attribute != null) {
                attribute.setBaseValue(1);
            }
            if (i == 21) {
                giant.addPotionEffect(new PotionEffect(PotionEffectType.SPEED,PotionEffect.INFINITE_DURATION,1));
            }
            Husk husk = (Husk) world.spawnEntity(player.getLocation(), EntityType.HUSK);
            husk.setInvisible(true);
            husk.setMaxHealth(500);
            husk.setHealth(500);
            husk.setInvulnerable(true);
            husk.getAttribute(Attribute.GENERIC_FOLLOW_RANGE).setBaseValue(100);
            husk.setCustomName(player.getName()+"'s");
            husk.setCustomNameVisible(false);
            giant.addPassenger(husk);

            new BukkitRunnable() {
                @Override
                public void run() {
                    giant.setJumping(true);
                }
            }.runTaskTimer(NikeyV1.getPlugin(),0,100);
            new BukkitRunnable() {
                @Override
                public void run() {
                    husk.remove();
                    giant.remove();
                }
            }.runTaskLater(NikeyV1.getPlugin(),20*60*10);
        } else {
            getLogger().warning("A world called world was not found.");
        }
    }


    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.GIANT) {
            Giant giant = (Giant) entity;
            if (event.getCause() == EntityDamageEvent.DamageCause.ENTITY_ATTACK) {
                if (event.getFinalDamage() >12) {
                    event.setDamage(12);
                }
            } else if (event.getCause() == EntityDamageEvent.DamageCause.FALL && giant.getCustomName().contains("low")) {
                for (Entity nearbyEntity : giant.getNearbyEntities(30, 50, 30)) {
                    if (nearbyEntity instanceof LivingEntity) {
                        giant.getWorld().playSound(giant,Sound.ENTITY_WITHER_BREAK_BLOCK,1,1);
                        LivingEntity player = (LivingEntity) nearbyEntity;
                        Vector dir = player.getLocation().toVector().subtract(giant.getLocation().toVector()).normalize();
                        player.setVelocity(dir.multiply(-2.5F).add(new Vector(0,1.5F,0))); // Spieler zum von dem Giant schleudern
                        double health = player.getHealth();
                        player.damage(20+health,giant);
                        event.setDamage(0);
                        giant.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY,PotionEffect.INFINITE_DURATION,0));
                        giant.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING,PotionEffect.INFINITE_DURATION,0));
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityMove(EntityMoveEvent event) {
        Entity entity = event.getEntity();
        if (entity.getType() == EntityType.GIANT) {
            Entity passenger = entity.getPassenger();
            if (passenger instanceof Husk) {
                Husk husk = (Husk) passenger;
                for (Entity nearbyEntity : entity.getNearbyEntities(4, 13, 4)) {
                    if (nearbyEntity instanceof LivingEntity) {
                        LivingEntity player = (LivingEntity) nearbyEntity;
                        double distance = husk.getLocation().distance(player.getLocation());
                        if (distance <= 13 && player.getLocation().add(0,-1,0).getBlock().getType() != Material.AIR) {
                            String[] arr = entity.getCustomName().split("'");
                            int level = NikeyV1.getPlugin().getConfig().getInt(arr[0] + ".level");
                            if (!entity.getCustomName().contains("low")) {
                                if (!entity.getCustomName().contains("strong")) {
                                    if (player instanceof Player) {
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*0.15;
                                        player.damage(armor+1.5F);
                                    }else {
                                        player.damage(2.5F);
                                    }
                                }else{
                                    if (player instanceof Player) {
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*0.15;
                                        player.damage(armor+3.5F);
                                    }else {
                                        player.damage(4.5F);
                                    }
                                }
                            }else {
                                if (!entity.getCustomName().contains("strong")) {
                                    if (player instanceof Player) {
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*0.2;
                                        player.damage(armor+2.5F);
                                    }else {
                                        player.damage(3.5F);
                                    }
                                }else{
                                    if (player instanceof Player) {
                                        double armor = player.getAttribute(Attribute.GENERIC_ARMOR).getValue();
                                        armor = armor*0.2;
                                        player.damage(armor+4.5F);
                                    }else {
                                        player.damage(5.5F);
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
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (event.getEntityType() == EntityType.GIANT) {
            entity.getPassenger().remove();
        }
        if (entity.getCustomName() != null && entity.getCustomName().contains("'s")) {
            event.getDrops().clear();
        }
        if (entity.getKiller() instanceof Player && !(entity instanceof Player) && !(entity instanceof Giant)){
            Player p = (Player) entity.getKiller();
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")){
                String[] arr = p.getItemInHand().getLore().get(1).split(":");
                int i = Integer.parseInt(arr[1]);
                if (cooldown.containsKey(p.getUniqueId()) && cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
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
                    ItemStack hand = p.getInventory().getItemInMainHand();
                    ItemMeta meta = hand.getItemMeta();
                    ArrayList<String> list = new ArrayList<>();
                    list.add(ChatColor.of("#221726")+"Souls wander around in this stone");
                    list.add(ChatColor.of("#00FFAA")+"Level:"+i);
                    list.add("§e");
                    list.add("§7Absorbed Soul:§3 "+entity.getType());
                    meta.setLore(list);
                    hand.setItemMeta(meta);
                    Soul.clear();
                    Soul.add(entity);
                    p.getWorld().playSound(p.getLocation(), Sound.ENTITY_EVOKER_CAST_SPELL,1,1);
                }
            }
        }
    }

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent event) {
        if (event.getHitEntity() != null) {
            Entity hitEntity = event.getHitEntity();
            if (hitEntity.getType() == EntityType.GIANT && hitEntity.getCustomName().contains("low") && event.getEntity().getCustomName() == null) {
                event.setCancelled(true);
            }
        }
    }

    private boolean isUndead(LivingEntity entity) {
        // Check if the entity is undead (zombie, skeleton, etc.)
        EntityType type = entity.getType();
        return type == EntityType.ZOMBIE || type == EntityType.SKELETON || type == EntityType.ZOGLIN|| type == EntityType.ZOMBIFIED_PIGLIN|| type == EntityType.ZOMBIE_VILLAGER|| type == EntityType.PHANTOM || type == EntityType.DROWNED  || type == EntityType.WITHER_SKELETON || type == EntityType.STRAY || type == EntityType.HUSK;
    }

    @EventHandler(ignoreCancelled = true)
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            // Check if the damage source is an undead monster
            if (isUndead((LivingEntity) event.getDamager())) {
                double damageReductionPercentage = 7.5; // 7.5% damage reduction
                double damage = event.getDamage();
                double reducedDamage = damage - (damage * (damageReductionPercentage / 100));
                event.setDamage(reducedDamage);
            }
        }
        if (event.getDamager() instanceof Player && event.getEntity() instanceof LivingEntity) {
            String stone = NikeyV1.getPlugin().getConfig().getString(event.getDamager().getName() + ".stone");
            int level = NikeyV1.getPlugin().getConfig().getInt(event.getDamager().getName() + ".level");
            if (stone.equalsIgnoreCase("undead")) {
                HelpUtil.triggerEntityAggro((LivingEntity) event.getEntity(), (Player) event.getDamager());
                if (isUndead((LivingEntity) event.getEntity()) ) {
                    if (level == 3) {
                        event.setDamage(event.getDamage()+0.5F);
                    } else if (level == 4) {
                        event.setDamage(event.getDamage()+1);
                    }else if (level == 5) {
                        event.setDamage(event.getDamage()+1.5F);
                    }
                }
            }
        }
        if (event.getEntity().getType() == EntityType.GIANT) {
            Giant giant = (Giant) event.getEntity();
            double health = giant.getHealth();
            if (event.getFinalDamage() >12) {
                event.setDamage(12);
            }
            if (health < 100) {
                if (event.getCause() == EntityDamageEvent.DamageCause.FALL) {

                }else {
                    if (!giant.getCustomName().contains("low") && giant.getHealth() <100) {
                        Block h = giant.getWorld().getHighestBlockAt(giant.getLocation());
                        Location loc = h.getLocation().add(0, 50, 0);
                        int y = (int) giant.getLocation().getY();
                        y += 50;
                        int x = (int) giant.getLocation().getX();
                        int z = (int) giant.getLocation().getZ();
                        getServer().dispatchCommand(Bukkit.getConsoleSender(),"tp "+giant.getUniqueId()+" "+x+" " +y+" "+z);
                        giant.setCustomName(giant.getCustomName() +" low");
                        giant.setHealth(200);
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                giant.setVelocity(new Vector(0, -1, 0)); // Den Giant wieder nach unten fallen lassen
                            }
                        }.runTaskLater(NikeyV1.getPlugin(), 20L); // 40 Tick (2 Sekunden) später
                    }
                }
            }
        }
        if (event.getEntity() instanceof Player) {
            Player p = (Player) event.getEntity();
            FileConfiguration config = NikeyV1.getPlugin().getConfig();
            String stone = config.getString(p.getName() + ".stone");
            if (stone.equalsIgnoreCase("Undead")) {
                Monster damager = (Monster) event.getDamager();
                // Überprüfen, ob der Spieler von einen Entity geschlagen wurde
                if (damager != null) {
                    // Aggro-Mechanismus aktivieren
                    HelpUtil.triggerEntityAggro(damager,p);
                }
            }
        }

        if (event.getDamager() instanceof Player){
            Player p = (Player) event.getDamager();
            if (event.getEntity() instanceof LivingEntity){
                LivingEntity entity = (LivingEntity) event.getEntity();
                FileConfiguration config = NikeyV1.getPlugin().getConfig();
                String stone = config.getString(p.getName() + ".stone");

                if (stone.equalsIgnoreCase("Undead")) {
                    Monster damagedEntity = (Monster) event.getEntity();
                    // Überprüfen, ob der Spieler ein Entity schlägt
                    if (damagedEntity != null) {
                        // Aggro-Mechanismus aktivieren
                        HelpUtil.triggerEntityAggro(damagedEntity,p);
                    }
                }

                if (entity.getCategory() == EntityCategory.UNDEAD && stone.equalsIgnoreCase("Undead")&&config.getInt(p.getName()+".level")>=5){
                    event.setDamage(event.getFinalDamage()+1);
                }
            }
            if (p.getInventory().getItemInMainHand().getItemMeta() == null)return;
            if (p.getInventory().getItemInMainHand().getItemMeta().getDisplayName().equalsIgnoreCase(ChatColor.of("#100613")+"Undead Stone")){
                if (cooldown.containsKey(p.getUniqueId())&& cooldown.get(p.getUniqueId()) > System.currentTimeMillis()){
                    event.setCancelled(true);
                }else {
                    event.setDamage(4);
                    }
                }
            }
        }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event){
        if (event.getTarget() instanceof Player && event.getEntity() instanceof Husk) {
            Husk husk = (Husk) event.getEntity();
            Player player = (Player) event.getTarget();
            if (husk.getVehicle() != null && husk.getVehicle().getType() == EntityType.GIANT) {
                Entity vehicle = husk.getVehicle();
                String[] arr = vehicle.getCustomName().split("'");
                if (player.getName().equalsIgnoreCase(arr[0])) {
                    event.setCancelled(true);
                }
            }
        }

        if (event.getTarget() instanceof Player){
            if (event.getEntity() instanceof Monster || event.getEntity() instanceof IronGolem || event.getEntity() instanceof Warden || event.getEntity() instanceof Giant){
                if (event.getEntity().getCustomName() != null) {
                    if (event.getEntity().getCustomName().equalsIgnoreCase(((Player) event.getTarget()).getDisplayName()+"'s "+event.getEntityType().getName())){
                        event.setCancelled(true);
                    }
                }
            }
        }
    }
}
